package com.bunbeauty.domain.feature.menu.common.category

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.model.Category
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
        val categoryList = categoryRepo.getCategoryList(token = token, companyUuid = companyUuid)

        val oldCategory = categoryList.find { category -> category.uuid == categoryUuid }
            ?: throw NotFindCategoryException()

        when {
            updateCategory.name.isBlank() -> throw CategoryNameException()
            isNameUnchanged(oldName = oldCategory.name, newName = updateCategory.name) -> return
            getHasSameName(
                categoryList = categoryList,
                name = updateCategory.name
            ) -> throw DuplicateCategoryNameException()
        }

        categoryRepo.updateCategory(
            categoryUuid = categoryUuid,
            updateCategory = updateCategory,
            token = token
        )
    }

    private fun getHasSameName(categoryList: List<Category>, name: String): Boolean {
        return categoryList.any { categoryName -> categoryName.name == name }
    }

    private fun isNameUnchanged(oldName: String, newName: String): Boolean {
        return oldName == newName
    }
}
