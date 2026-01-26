package com.lininglink.mechanicalpencils.data.repository

import com.lininglink.mechanicalpencils.data.api.ApiService
import com.lininglink.mechanicalpencils.data.model.CollectionItem
import com.lininglink.mechanicalpencils.data.model.CollectionResponse
import com.lininglink.mechanicalpencils.data.model.Ownership

class CollectionRepository(private val apiService: ApiService) {

    suspend fun getCollection(): Result<CollectionResponse> {
        return try {
            val response = apiService.getCollection()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadProof(ownershipId: Int, imageBytes: ByteArray, fileName: String): Result<Ownership> {
        return try {
            val response = apiService.uploadProof(ownershipId, imageBytes, fileName)
            Result.success(response.ownership)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
