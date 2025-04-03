package com.bunbeauty.domain.feature.menu.common.category

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.feature.menu.common.model.CreateCategory
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class CreateCategoryUseCase(
    private val categoryRepo: CategoryRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(categoryName: String) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()

        val categoryList = categoryRepo.getCategoryList(token = token, companyUuid = companyUuid)

        when {
            categoryName.isBlank() -> throw CategoryNameException()
            getHasSameName(categoryList, categoryName) -> throw CreateCategoryNameException()
        }

        categoryRepo.postCategory(
            token = token,
            createCategory = CreateCategory(
                name = categoryName,
                priority = categoryList.count() + 1
            )
        )
    }
    private fun getHasSameName(categoryList: List<Category>, name: String): Boolean {
        return categoryList.any { categoryName -> categoryName.name == name }
    }
}
