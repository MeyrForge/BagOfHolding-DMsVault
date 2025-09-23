package com.meyrforge.bagofholdingdmsvault.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.meyrforge.bagofholdingdmsvault.common.Converters
import com.meyrforge.bagofholdingdmsvault.common.data.entities.ItemEntity

@Database(entities = [ItemEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class BagOfHoldingDatabase : RoomDatabase(){

}