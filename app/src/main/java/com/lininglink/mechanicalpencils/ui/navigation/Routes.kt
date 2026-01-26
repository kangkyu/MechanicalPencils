package com.lininglink.mechanicalpencils.ui.navigation

import com.lininglink.mechanicalpencils.data.model.CollectionItem
import kotlinx.serialization.Serializable

// Auth routes
@Serializable
object Login

@Serializable
object Register

// Main tabs
@Serializable
object MainTabs

// Browse tab routes
@Serializable
object Browse

@Serializable
data class ItemDetail(val itemId: Int)

// Groups tab routes
@Serializable
object Groups

@Serializable
data class GroupDetail(val groupId: Int)

// Collection tab routes
@Serializable
object Collection

@Serializable
data class ProofUpload(
    val itemId: Int,
    val title: String,
    val maker: String?,
    val imageUrl: String?,
    val ownershipId: Int,
    val hasProof: Boolean,
    val proofUrl: String?
)

// Profile route
@Serializable
data class UserProfile(val userId: Int)

// Settings
@Serializable
object Settings
