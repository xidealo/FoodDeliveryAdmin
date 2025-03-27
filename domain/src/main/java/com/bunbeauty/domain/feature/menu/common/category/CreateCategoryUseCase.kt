package com.bunbeauty.domain.feature.menu.common.category

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.model.CategoryPost
import com.bunbeauty.domain.feature.menu.common.model.CreateCategory
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class CreateCategoryUseCase(
    private val categoryRepo: CategoryRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(createCategory: CreateCategory) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()

        val categoryList = categoryRepo.getCategoryList(token = token, companyUuid = companyUuid)
            .sortedBy { it.priority }

        if (categoryList.any { it.name == createCategory.name }) {
            throw CreateCategoryNameException()
        }
        when {
            createCategory.name.isBlank() -> throw CategoryNameException()
        }

        categoryRepo.postCategory(
            token = token,
            categoryPost = CategoryPost(
                name = createCategory.name,
                priority = categoryList.count() + 1
            )
        )
    }
}
