package com.example.usermanager.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil3.load
import com.example.usermanager.R
import com.example.usermanager.api.SessionManager
import com.example.usermanager.api.UserViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText

class DashboardActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var userAvatarImageView: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboard)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionManager = SessionManager(this)

        userNameTextView = findViewById(R.id.tv_user_name)
        userEmailTextView = findViewById(R.id.tv_user_email)
        userAvatarImageView = findViewById(R.id.iv_user_avatar)

        val logoutButton = findViewById<Button>(R.id.btn_logout)
        val editButton = findViewById<MaterialButton>(R.id.btn_edit)
        val deleteButton = findViewById<MaterialButton>(R.id.btn_delete)

        // Fetch user details
        val userId = sessionManager.getUserId()
        if (userId != -1) {
            viewModel.getUserDetails(userId)
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
            logout()
        }

        logoutButton.setOnClickListener {
            logout()
        }

        editButton.setOnClickListener {
            showEditUserDialog()
        }

        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.userDetails.observe(this) { result ->
            result.onSuccess { user ->
                userNameTextView.text = "${user.first_name} ${user.last_name}"
                userEmailTextView.text = user.email


                userAvatarImageView.load(user.avatar)
            }.onFailure { exception ->
                Toast.makeText(this, "Failed to load user: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.updateResult.observe(this) { result ->
            result.onSuccess { updateResponse ->
                Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show()

                // Refresh user details after update
                val userId = sessionManager.getUserId()
                if (userId != -1) {
                    viewModel.getUserDetails(userId)
                }
            }.onFailure { exception ->
                Toast.makeText(this, "Failed to update user: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.deleteResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show()
                logout()
            }.onFailure { exception ->
                Toast.makeText(this, "Failed to delete user: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEditUserDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_user, null)

        val nameEditText = dialogView.findViewById<TextInputEditText>(R.id.et_edit_name)
        val jobEditText = dialogView.findViewById<TextInputEditText>(R.id.et_edit_job)

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Edit Profile")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = nameEditText.text.toString().trim()
                val job = jobEditText.text.toString().trim()

                if (name.isEmpty() || job.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val userId = sessionManager.getUserId()
                if (userId != -1) {
                    viewModel.updateUser(userId, name, job)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    private fun showDeleteConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                val userId = sessionManager.getUserId()
                if (userId != -1) {
                    viewModel.deleteUser(userId)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        sessionManager.clearSession()
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}