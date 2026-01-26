package com.lininglink.mechanicalpencils.data.repository

import com.lininglink.mechanicalpencils.data.api.ApiService
import com.lininglink.mechanicalpencils.data.local.TokenManager
import com.lininglink.mechanicalpencils.data.model.AuthResponse
import com.lininglink.mechanicalpencils.data.model.LoginRequest
import com.lininglink.mechanicalpencils.data.model.RegisterRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

    val isLoggedIn: Flow<Boolean> = tokenManager.tokenFlow.map { token -> token != null }

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val request = LoginRequest(email, password)
            val authResponse = apiService.login(request)
            tokenManager.saveToken(authResponse.token)
            Result.success(authResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        email: String,
        password: String,
        passwordConfirmation: String
    ): Result<AuthResponse> {
        return try {
            val request = RegisterRequest(email, password, passwordConfirmation)
            val authResponse = apiService.register(request)
            tokenManager.saveToken(authResponse.token)
            Result.success(authResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            apiService.logout()
            tokenManager.clearToken()
            Result.success(Unit)
        } catch (e: Exception) {
            tokenManager.clearToken()
            Result.success(Unit)
        }
    }

    suspend fun isAuthenticated(): Boolean {
        return tokenManager.getToken() != null
    }
}
