package com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.models

import com.meyrforge.bagofholdingdmsvault.common.Category

data class Item(
    val id: Int?,
    val uid: String,
    val name: String,
    val description: String,
    val category: Category,
    val imgUrl: String,
    val isFavorite: Boolean = false,
    val amount: Int = 1,
    val additionDate: Long = System.currentTimeMillis(),
    val tags: List<String> = emptyList()
)
