package com.lininglink.mechanicalpencils.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: Int,
    val email: String,
    @SerialName("items_count")
    val itemsCount: Int = 0,
    val items: List<UserProfileItem>? = null
)

@Serializable
data class UserProfileItem(
    val id: Int,
    val title: String,
    val maker: String? = null,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String? = null,
    @SerialName("has_proof")
    val hasProof: Boolean = false,
    @SerialName("proof_url")
    val proofUrl: String? = null
)

@Serializable
data class UserProfileResponse(
    val user: UserProfile
)
