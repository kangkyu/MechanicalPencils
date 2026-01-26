package com.lininglink.mechanicalpencils.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Int,
    val title: String,
    val maker: String? = null,
    @SerialName("model_number")
    val modelNumber: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    val owned: Boolean = false
)

@Serializable
data class ItemDetail(
    val id: Int,
    val title: String,
    val description: String? = null,
    val maker: Maker? = null,
    @SerialName("model_number")
    val modelNumber: String? = null,
    @SerialName("tip_retractable")
    val tipRetractable: String? = null,
    @SerialName("eraser_attached")
    val eraserAttached: String? = null,
    @SerialName("jetpens_url")
    val jetpensUrl: String? = null,
    @SerialName("blick_url")
    val blickUrl: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String? = null,
    val owned: Boolean = false,
    @SerialName("has_proof")
    val hasProof: Boolean = false,
    @SerialName("ownership_id")
    val ownershipId: Int? = null,
    val proofs: List<ItemProof>? = null,
    @SerialName("owners_count")
    val ownersCount: Int? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class Maker(
    val id: Int,
    val title: String
)

@Serializable
data class ItemProof(
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("user_email")
    val userEmail: String,
    @SerialName("proof_url")
    val proofUrl: String,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class ItemsResponse(
    val items: List<Item>,
    val pagination: Pagination
)

@Serializable
data class ItemDetailResponse(
    val item: ItemDetail,
    val message: String? = null
)

@Serializable
data class Pagination(
    @SerialName("current_page")
    val currentPage: Int,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("per_page")
    val perPage: Int
)

