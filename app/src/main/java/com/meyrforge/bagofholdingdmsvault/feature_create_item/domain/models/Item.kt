package com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.models

import com.meyrforge.bagofholdingdmsvault.common.Category

data class Item(
    val id: Int?,
    val name: String,
    val description: String,
    val category: Category,
    val imgUrl: String
)
