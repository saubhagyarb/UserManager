package com.example.usermanager.api

import com.example.usermanager.data.LoginRequest
import com.example.usermanager.data.LoginResponse
import com.example.usermanager.data.RegisterResponse
import com.example.usermanager.data.UpdateUserRequest
import com.example.usermanager.data.UpdateUserResponse
import com.example.usermanager.data.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("register")
    suspend fun registerUser(@Body registerRequest: LoginRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): Response<UserResponse>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") userId: Int,
        @Body updateRequest: UpdateUserRequest
    ): Response<UpdateUserResponse>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: Int): Response<Unit>
}