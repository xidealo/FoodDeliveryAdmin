package com.bunbeauty.domain.repo

import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.feature.menu.common.model.CreateCategory
import com.bunbeauty.domain.feature.menu.common.model.UpdateCategory

interface CategoryRepo {
    suspend fun fetchCategories(token: String, companyUuid: String): List<Category>

    suspend fun getCategoryList(
        token: String,
        companyUuid: String,
        refreshing: Boolean = false
    ): List<Category>

    suspend fun postCategory(token: String, createCategory: CreateCategory): Category

    suspend fun getCategory(companyUuid: String, categoryUuid: String, token: String): Category?

    suspend fun updateCategory(
        categoryUuid: String,
        updateCategory: UpdateCategory,
        token: String
    )

    suspend fun saveCategoryPriority(token: String, category: List<Category>)

    /*DELETE*/
    fun clearCache()
}
