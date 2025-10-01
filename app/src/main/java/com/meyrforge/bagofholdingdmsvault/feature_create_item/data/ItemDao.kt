package com.meyrforge.bagofholdingdmsvault.feature_create_item.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.meyrforge.bagofholdingdmsvault.feature_create_item.data.entities.ItemEntity

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity): Long

    @Query("SELECT * FROM item_table")
    suspend fun getAllItems(): List<ItemEntity>

    @Delete
    suspend fun deleteItem(item: ItemEntity): Int

    @Query("SELECT * FROM item_table WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): ItemEntity?

    @Update
    suspend fun updateItem(item: ItemEntity): Int

    @Query("SELECT * FROM item_table WHERE item_category = :category")
    suspend fun getItemsByCategory(category: String): List<ItemEntity>

}