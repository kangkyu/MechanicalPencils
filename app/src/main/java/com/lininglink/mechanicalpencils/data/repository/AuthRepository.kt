package com.lininglink.mechanicalpencils.data.repository

import com.lininglink.mechanicalpencils.data.api.ApiService
import com.lininglink.mechanicalpencils.data.local.TokenManager
import com.lininglink.mechanicalpencils.data.model.AuthResponse
import com.lininglink.mechanicalpencils.data.model.ErrorResponse
import com.lininglink.mechanicalpencils.data.model.LoginRequest
import com.lininglink.mechanicalpencils.data.model.RegisterRequest
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) {

    val isLoggedIn: Flow<Boolean> = tokenManager.tokenFlow.map { token -> token != null }

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return runCatching {
            val request = LoginRequest(email, password)
            val response = apiService.login(request)

            if (response.status == HttpStatusCode.Created) {
                val authResponse: AuthResponse = response.body()
                tokenManager.saveToken(authResponse.token)
                authResponse
            } else {
                val errRes: ErrorResponse = response.body()
                throw Exception(errRes.error)
            }
        }
    }

    suspend fun register(
        email: String,
        password: String,
        passwordConfirmation: String
    ): Result<AuthResponse> {
        return runCatching {
            val request = RegisterRequest(email, password, passwordConfirmation)
            val response = apiService.register(request)

            if (response.status == HttpStatusCode.Created) {
                val authResponse: AuthResponse = response.body()
                tokenManager.saveToken(authResponse.token)
                authResponse
            } else {
                val errRes: ErrorResponse = response.body()
                throw Exception(errRes.error)
            }
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
