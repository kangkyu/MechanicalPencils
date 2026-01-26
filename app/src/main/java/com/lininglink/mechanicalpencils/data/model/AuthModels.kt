package com.lininglink.mechanicalpencils.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    @SerialName("password_confirmation")
    val passwordConfirmation: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val user: User
)
