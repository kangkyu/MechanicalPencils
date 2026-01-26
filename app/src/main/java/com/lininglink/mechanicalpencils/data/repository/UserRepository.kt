package com.lininglink.mechanicalpencils.data.repository

import com.lininglink.mechanicalpencils.data.api.ApiService
import com.lininglink.mechanicalpencils.data.model.UserProfile

class UserRepository(private val apiService: ApiService) {

    suspend fun getUserProfile(userId: Int): Result<UserProfile> {
        return try {
            val response = apiService.getUserProfile(userId)
            Result.success(response.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
