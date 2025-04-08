package com.example.usermanager.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usermanager.data.LoginRequest
import com.example.usermanager.data.LoginResponse
import com.example.usermanager.data.RegisterResponse
import com.example.usermanager.data.UpdateUserRequest
import com.example.usermanager.data.UpdateUserResponse
import com.example.usermanager.data.User
import kotlinx.coroutines.launch

// UserViewModel.kt
class UserViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> get() = _loginResult

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> get() = _registerResult

    private val _userDetails = MutableLiveData<Result<User>>()
    val userDetails: LiveData<Result<User>> get() = _userDetails

    private val _updateResult = MutableLiveData<Result<UpdateUserResponse>>()
    val updateResult: LiveData<Result<UpdateUserResponse>> get() = _updateResult

    private val _deleteResult = MutableLiveData<Result<Boolean>>()
    val deleteResult: LiveData<Result<Boolean>> get() = _deleteResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.api.loginUser(LoginRequest(email, password))
                if (response.isSuccessful) {
                    _loginResult.value = Result.success(response.body()!!)
                } else {
                    _loginResult.value = Result.failure(Exception("Login failed: ${response.code()}"))
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.api.registerUser(LoginRequest(email, password))
                if (response.isSuccessful) {
                    _registerResult.value = Result.success(response.body()!!)
                } else {
                    _registerResult.value = Result.failure(Exception("Registration failed: ${response.code()}"))
                }
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }

    fun getUserDetails(userId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.api.getUserById(userId)
                if (response.isSuccessful) {
                    _userDetails.value = Result.success(response.body()!!.data)
                } else {
                    _userDetails.value = Result.failure(Exception("Failed to get user: ${response.code()}"))
                }
            } catch (e: Exception) {
                _userDetails.value = Result.failure(e)
            }
        }
    }

    fun updateUser(userId: Int, name: String, job: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.api.updateUser(
                    userId, UpdateUserRequest(name, job)
                )
                if (response.isSuccessful) {
                    _updateResult.value = Result.success(response.body()!!)
                } else {
                    _updateResult.value = Result.failure(Exception("Update failed: ${response.code()}"))
                }
            } catch (e: Exception) {
                _updateResult.value = Result.failure(e)
            }
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.api.deleteUser(userId)
                if (response.isSuccessful) {
                    _deleteResult.value = Result.success(true)
                } else {
                    _deleteResult.value = Result.failure(Exception("Delete failed: ${response.code()}"))
                }
            } catch (e: Exception) {
                _deleteResult.value = Result.failure(e)
            }
        }
    }
}