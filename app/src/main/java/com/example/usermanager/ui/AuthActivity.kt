package com.example.usermanager.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.usermanager.R
import com.example.usermanager.api.SessionManager
import com.example.usermanager.api.UserViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AuthActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.auth)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionManager = SessionManager(this)

        val loginEmailEt = findViewById<TextInputEditText>(R.id.et_login_email)
        val loginPasswordEt = findViewById<TextInputEditText>(R.id.et_login_password)
        val loginBtn = findViewById<MaterialButton>(R.id.btn_login)

        val registerEmailEt = findViewById<TextInputEditText>(R.id.et_register_email)
        val registerPasswordEt = findViewById<TextInputEditText>(R.id.et_register_password)
        val registerBtn = findViewById<MaterialButton>(R.id.btn_register)

        loginBtn.setOnClickListener {
            val email = loginEmailEt.text.toString().trim()
            val password = loginPasswordEt.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }

        registerBtn.setOnClickListener {
            val email = registerEmailEt.text.toString().trim()
            val password = registerPasswordEt.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.register(email, password)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this) { result ->
            result.onSuccess { loginResponse ->
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                sessionManager.saveAuthToken(loginResponse.token)
                Log.d("AuthActivity", "observeViewModel: loginResponse.token = ${loginResponse.token}")
                sessionManager.setLoggedIn(true)

                navigateToDashboard()
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, "Login failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.registerResult.observe(this) { result ->
            result.onSuccess { registerResponse ->
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                sessionManager.saveAuthToken(registerResponse.token)
                sessionManager.saveUserId(registerResponse.id)
                sessionManager.setLoggedIn(true)
                navigateToDashboard()
            }.onFailure { exception ->
                Toast.makeText(this, "Registration failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}