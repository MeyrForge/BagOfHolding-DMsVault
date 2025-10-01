package com.meyrforge.bagofholdingdmsvault.feature_create_item.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.meyrforge.bagofholdingdmsvault.common.Category
import com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.models.Item

@Entity(tableName = "item_table")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "uid") val uid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "picture") val picture: String,
    @ColumnInfo(name = "item_category", defaultValue = "OTHER") val category: Category,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "addition_date") val additionDate: Long,
    @ColumnInfo(name = "tags") val tags: List<String>
)

fun ItemEntity.toDomain(): Item {
    return Item(
        id = id,
        uid = uid,
        name = name,
        description = description,
        category = category,
        imgUrl = picture,
        isFavorite = isFavorite,
        amount = amount,
        additionDate = additionDate,
        tags = tags
    )
}

fun Item.toEntity(): ItemEntity {
    return if(id != null) {
        ItemEntity(
            id = id,
            uid = uid,
            name = name,
            description = description,
            category = category,
            picture = imgUrl,
            isFavorite = isFavorite,
            amount = amount,
            additionDate = additionDate,
            tags = tags
        )
    } else {
        ItemEntity(
            uid = uid,
            name = name,
            description = description,
            category = category,
            picture = imgUrl,
            isFavorite = isFavorite,
            amount = amount,
            additionDate = additionDate,
            tags = tags
        )
    }
}
