package com.bunbeauty.domain.feature.menu.common.category

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.model.UpdateCategory
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class EditCategoryUseCase(
    private val categoryRepo: CategoryRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(
        categoryUuid: String,
        updateCategory: UpdateCategory
    ) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()

        when {
            updateCategory.name.isBlank() -> throw CategoryNameException()
        }

        categoryRepo.updateCategory(
            categoryUuid = categoryUuid,
            companyUuid = companyUuid,
            updateCategory = updateCategory,
            token = token
        )
    }
}
