package com.example.usermanager.data

sealed class LoginData()
{
    data class LoginRequest(
        val email: String,
        val password: String
    ) : LoginData()

    data class LoginResponse(
        val token: String
    ) : LoginData()
}

data class RegisterResponse(
    val id: Int,
    val token: String
) : UserData()

sealed class UpdateData()
{
    data class UpdateUserRequest(
        val name: String,
        val job: String
    ) : UpdateData()

    data class UpdateUserResponse(
        val name: String,
        val job: String,
        val updatedAt: String
    ) : UpdateData()
}

sealed  class UserData()
{
    data class User(
        val id: Int,
        val email: String,
        val first_name: String?,
        val last_name: String?,
        val avatar: String?
    ) : UserData()

    data class UserResponse(
        val data: User
    ) : UserData()
}