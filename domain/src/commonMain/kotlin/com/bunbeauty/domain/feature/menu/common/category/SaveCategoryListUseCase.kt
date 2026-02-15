package com.bunbeauty.domain.feature.menu.common.category

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.DataStoreRepo

class SaveCategoryListUseCase(
    private val categoryRepo: CategoryRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(categoryList: List<Category>) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        categoryRepo.saveCategoryPriority(
            token = token,
            category = categoryList,
        )
    }
}
