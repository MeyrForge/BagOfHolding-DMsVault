package com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.usecases

import com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.ItemRepository
import com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.models.Item
import com.meyrforge.bagofholdingdmsvault.common.Result
import javax.inject.Inject

class CreateItemUseCase @Inject constructor(private val itemRepository: ItemRepository) {
    suspend operator fun invoke (item: Item): Boolean{
        return when (val result = itemRepository.insertItem(item)){
            is Result.Success -> true
            is Result.Error -> false
        }
    }
}