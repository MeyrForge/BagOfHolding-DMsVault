package com.meyrforge.bagofholdingdmsvault.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.meyrforge.bagofholdingdmsvault.feature_create_item.data.Converters
import com.meyrforge.bagofholdingdmsvault.feature_create_item.data.ItemDao
import com.meyrforge.bagofholdingdmsvault.feature_create_item.data.entities.ItemEntity

@Database(entities = [ItemEntity::class], version = 3)
@TypeConverters(Converters::class)
abstract class BagOfHoldingDatabase : RoomDatabase(){
    abstract fun itemDao(): ItemDao
}