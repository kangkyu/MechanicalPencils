package com.lininglink.mechanicalpencils.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionItem(
    val id: Int,
    val title: String,
    val maker: String? = null,
    @SerialName("model_number")
    val modelNumber: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("ownership_id")
    val ownershipId: Int,
    @SerialName("has_proof")
    val hasProof: Boolean = false,
    @SerialName("proof_url")
    val proofUrl: String? = null,
    @SerialName("owned_at")
    val ownedAt: String? = null
)

@Serializable
data class CollectionResponse(
    val items: List<CollectionItem>,
    @SerialName("total_count")
    val totalCount: Int
)

@Serializable
data class ProofUploadResponse(
    val ownership: Ownership,
    val message: String? = null
)

@Serializable
data class Ownership(
    val id: Int,
    @SerialName("item_id")
    val itemId: Int,
    @SerialName("has_proof")
    val hasProof: Boolean = false,
    @SerialName("proof_url")
    val proofUrl: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)
