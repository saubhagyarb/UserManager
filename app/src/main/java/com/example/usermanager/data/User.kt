package com.example.usermanager.data

// User.kt
data class User(
    val id: Int,
    val email: String,
    val first_name: String?,
    val last_name: String?,
    val avatar: String?
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String
)

data class RegisterResponse(
    val id: Int,
    val token: String
)

data class UpdateUserRequest(
    val name: String,
    val job: String
)

data class UpdateUserResponse(
    val name: String,
    val job: String,
    val updatedAt: String
)

data class UserResponse(
    val data: User
)
