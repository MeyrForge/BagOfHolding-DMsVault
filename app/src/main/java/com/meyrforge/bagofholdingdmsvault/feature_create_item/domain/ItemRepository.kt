package com.meyrforge.bagofholdingdmsvault.feature_create_item.domain

import com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.models.Item
import com.meyrforge.bagofholdingdmsvault.common.Result

interface ItemRepository {
    suspend fun insertItem(item: Item): Result<Long, Exception>
    suspend fun updateItem(item: Item): Result<Int, Exception>
    suspend fun deleteItem(item: Item): Result<Int, Exception>
    suspend fun getAllItems(): Result<List<Item>, Exception>
    suspend fun getItemById(itemId: Int): Result<Item?, Exception>
    suspend fun getItemsByCategory(category: String): Result<List<Item>, Exception>
}