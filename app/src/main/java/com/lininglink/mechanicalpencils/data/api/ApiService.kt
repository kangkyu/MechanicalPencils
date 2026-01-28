package com.lininglink.mechanicalpencils.data.api

import com.lininglink.mechanicalpencils.data.model.AuthResponse
import com.lininglink.mechanicalpencils.data.model.CollectionResponse
import com.lininglink.mechanicalpencils.data.model.ItemDetailResponse
import com.lininglink.mechanicalpencils.data.model.ItemGroupDetailResponse
import com.lininglink.mechanicalpencils.data.model.ItemGroupsResponse
import com.lininglink.mechanicalpencils.data.model.ItemsResponse
import com.lininglink.mechanicalpencils.data.model.LoginRequest
import com.lininglink.mechanicalpencils.data.model.ProofUploadResponse
import com.lininglink.mechanicalpencils.data.model.RegisterRequest
import com.lininglink.mechanicalpencils.data.model.UserProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class ApiService(private val client: HttpClient) {

    companion object {
        const val BASE_URL = "https://mechanical-pencils-bab056ce6286.herokuapp.com"
    }

    // Auth endpoints
    suspend fun login(request: LoginRequest): HttpResponse {
        return client.post("$BASE_URL/api/v1/session") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun logout(): HttpResponse {
        return client.delete("$BASE_URL/api/v1/session")
    }

    suspend fun register(request: RegisterRequest): HttpResponse {
        return client.post("$BASE_URL/api/v1/registration") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    // Items endpoints
    suspend fun getItems(page: Int = 1, query: String? = null): ItemsResponse {
        return client.get("$BASE_URL/api/v1/items") {
            parameter("page", page)
            query?.let { parameter("search", it) }
        }.body()
    }

    suspend fun getItem(id: Int): ItemDetailResponse {
        return client.get("$BASE_URL/api/v1/items/$id").body()
    }

    suspend fun ownItem(id: Int): ItemDetailResponse {
        return client.post("$BASE_URL/api/v1/items/$id/own").body()
    }

    suspend fun unownItem(id: Int): ItemDetailResponse {
        return client.delete("$BASE_URL/api/v1/items/$id/unown").body()
    }

    // Item Groups endpoints
    suspend fun getItemGroups(): ItemGroupsResponse {
        return client.get("$BASE_URL/api/v1/item_groups").body()
    }

    suspend fun getItemGroup(id: Int): ItemGroupDetailResponse {
        return client.get("$BASE_URL/api/v1/item_groups/$id").body()
    }

    // Collection endpoints
    suspend fun getCollection(): CollectionResponse {
        return client.get("$BASE_URL/api/v1/collection").body()
    }

    suspend fun uploadProof(ownershipId: Int, imageBytes: ByteArray, fileName: String): ProofUploadResponse {
        return client.submitFormWithBinaryData(
            url = "$BASE_URL/api/v1/ownerships/$ownershipId",
            formData = formData {
                append("proof", imageBytes, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
                append("_method", "patch")
            }
        ).body()
    }

    // User Profile endpoint
    suspend fun getUserProfile(userId: Int): UserProfileResponse {
        return client.get("$BASE_URL/api/v1/users/$userId").body()
    }
}
