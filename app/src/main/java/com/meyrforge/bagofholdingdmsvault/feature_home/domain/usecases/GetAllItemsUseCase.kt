package com.meyrforge.bagofholdingdmsvault.feature_home.domain.usecases

import com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.ItemRepository
import com.meyrforge.bagofholdingdmsvault.feature_create_item.domain.models.Item
import com.meyrforge.bagofholdingdmsvault.common.Result
import javax.inject.Inject

class GetAllItemsUseCase @Inject constructor(private val itemRepository: ItemRepository) {
    suspend operator fun invoke(): List<Item>{
        return when (val result = itemRepository.getAllItems()) {
            is Result.Success -> result.data
            is Result.Error -> emptyList()
        }
    }
}