package com.lininglink.mechanicalpencils.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val email: String,
    @SerialName("created_at")
    val createdAt: String? = null
)
