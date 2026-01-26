package com.lininglink.mechanicalpencils.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemGroup(
    val id: Int,
    val title: String,
    val link: String? = null,
    @SerialName("items_count")
    val itemsCount: Int = 0,
    val items: List<Item>? = null
)

@Serializable
data class ItemGroupsResponse(
    @SerialName("item_groups")
    val itemGroups: List<ItemGroup>
)

@Serializable
data class ItemGroupDetailResponse(
    @SerialName("item_group")
    val itemGroup: ItemGroup
)
