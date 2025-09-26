package com.meyrforge.bagofholdingdmsvault.feature_create_item.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.meyrforge.bagofholdingdmsvault.common.Category
import com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.models.Item

@Entity(tableName = "item_table")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "picture") val picture: String,
    @ColumnInfo(name = "item_category", defaultValue = "OTHER") val category: Category
)

fun ItemEntity.toDomain(): Item {
    return Item(
        id = id,
        name = name,
        description = description,
        category = category,
        imgUrl = picture
    )
}

fun Item.toEntity(): ItemEntity {
    return if(id != null) {
        ItemEntity(
            id = id,
            name = name,
            description = description,
            category = category,
            picture = imgUrl
        )
    } else {
        ItemEntity(
            name = name,
            description = description,
            category = category,
            picture = imgUrl
        )
    }
}
