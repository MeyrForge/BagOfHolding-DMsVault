package com.meyrforge.bagofholdingdmsvault.feature_create_item.data

import com.meyrforge.bagofholdingdmsvault.common.Result
import com.meyrforge.bagofholdingdmsvault.feature_create_item.data.entities.toDomain
import com.meyrforge.bagofholdingdmsvault.feature_create_item.data.entities.toEntity
import com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.ItemRepository
import com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.models.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(private val itemDao: ItemDao) : ItemRepository {
    private suspend fun <T> safeDaoCall(daoCall: suspend () -> T): Result<T, Exception> {
        return try {
            Result.Success(withContext(Dispatchers.IO) { daoCall() })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    override suspend fun insertItem(item: Item): Result<Long, Exception> {
        return safeDaoCall { itemDao.insertItem(item.toEntity()) }
    }

    override suspend fun updateItem(item: Item): Result<Int, Exception> {
        return safeDaoCall { itemDao.updateItem(item.toEntity()) }
    }

    override suspend fun deleteItem(item: Item): Result<Int, Exception> {
        return safeDaoCall { itemDao.deleteItem(item.toEntity()) }
    }

    override suspend fun getAllItems(): Result<List<Item>, Exception> {
        return safeDaoCall { itemDao.getAllItems().map { it.toDomain() } }
    }

    override suspend fun getItemById(itemId: Int): Result<Item?, Exception> {
        return safeDaoCall { itemDao.getItemById(itemId)?.toDomain() }
    }

    override suspend fun getItemsByCategory(category: String): Result<List<Item>, Exception> {
        return safeDaoCall { itemDao.getItemsByCategory(category).map { it.toDomain() } }
    }
}