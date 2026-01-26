package com.lininglink.mechanicalpencils.data.repository

import com.lininglink.mechanicalpencils.data.api.ApiService
import com.lininglink.mechanicalpencils.data.model.ItemDetail
import com.lininglink.mechanicalpencils.data.model.ItemsResponse

class ItemRepository(private val apiService: ApiService) {

    suspend fun getItems(page: Int = 1, query: String? = null): Result<ItemsResponse> {
        return try {
            val response = apiService.getItems(page, query)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getItem(id: Int): Result<ItemDetail> {
        return try {
            val response = apiService.getItem(id)
            Result.success(response.item)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun ownItem(id: Int): Result<ItemDetail> {
        return try {
            val response = apiService.ownItem(id)
            Result.success(response.item)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unownItem(id: Int): Result<ItemDetail> {
        return try {
            val response = apiService.unownItem(id)
            Result.success(response.item)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
