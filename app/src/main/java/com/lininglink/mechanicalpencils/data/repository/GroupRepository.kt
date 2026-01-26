package com.lininglink.mechanicalpencils.data.repository

import com.lininglink.mechanicalpencils.data.api.ApiService
import com.lininglink.mechanicalpencils.data.model.ItemGroup

class GroupRepository(private val apiService: ApiService) {

    suspend fun getGroups(): Result<List<ItemGroup>> {
        return try {
            val response = apiService.getItemGroups()
            Result.success(response.itemGroups)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGroup(id: Int): Result<ItemGroup> {
        return try {
            val response = apiService.getItemGroup(id)
            Result.success(response.itemGroup)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
