package com.meyrforge.bagofholdingdmsvault.feature_create_item.data

import androidx.room.TypeConverter
import com.meyrforge.bagofholdingdmsvault.common.Category

class Converters {
    @TypeConverter
    fun fromCategory(category: Category): String {
        return category.name // Stores the enum name as a String (e.g., "MINIS")
    }

    @TypeConverter
    fun toCategory(value: String): Category {
        return try {
            enumValueOf<Category>(value)
        } catch (e: IllegalArgumentException) {
            // Handle cases where the string in the DB might not match an enum constant
            // This could happen if you manually changed data or if there's old data.
            // Default to OTHER or throw an error, depending on your app's needs.
            Category.OTRO
        }
    }

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(',')?.map { it.trim() }?.filter { it.isNotEmpty() }
    }

    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.joinToString(separator = ",")
    }
}