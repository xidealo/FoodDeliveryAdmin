package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.extension.onSuccess
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.data.mapper.category.CategoryMapper
import com.bunbeauty.data.model.server.category.CategoryPatchServer
import com.bunbeauty.data.model.server.category.CategoryServer
import com.bunbeauty.data.model.server.category.CreateCategoryPostServer
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.feature.menu.common.model.CreateCategory
import com.bunbeauty.domain.feature.menu.common.model.UpdateCategory
import com.bunbeauty.domain.repo.CategoryRepo

class CategoryRepository(
    private val networkConnector: FoodDeliveryApi,
    private val categoryMapper: CategoryMapper
) : CategoryRepo {

    private var categoryCache: List<Category>? = null

    override suspend fun fetchCategories(token: String, companyUuid: String): List<Category> {
        return when (val result = networkConnector.getCategoriesByCompanyUuid(token, companyUuid)) {
            is ApiResult.Success -> {
                val categoryList = result.data.results.map(categoryMapper::toModel)
                categoryCache = categoryList
                categoryList
            }

            is ApiResult.Error -> {
                throw Exception("categories load error")
            }
        }
    }

    override suspend fun getCategoryList(
        token: String,
        companyUuid: String,
        refreshing: Boolean
    ): List<Category> {
        return if (refreshing) {
            fetchCategories(token = token, companyUuid = companyUuid)
        } else {
            categoryCache ?: fetchCategories(token = token, companyUuid = companyUuid)
        }
    }

    override suspend fun postCategory(token: String, createCategory: CreateCategory): Category {
        return networkConnector.postCategory(
            token = token,
            categoryServerPost = createCategory.toCreateCategoryServer()
        ).dataOrNull()?.let { createCategoryServer ->
            val categoryCreate = categoryMapper.toModel(createCategoryServer)
            categoryCache?.let { cache ->
                categoryCache = cache + categoryCreate
            }
            categoryCreate
        } ?: throw Exception("categories create error")
    }

    val toCreateCategoryServer: CreateCategory.() -> CreateCategoryPostServer = {
        CreateCategoryPostServer(
            name = name,
            priority = priority
        )
    }

    override suspend fun getCategory(
        companyUuid: String,
        categoryUuid: String,
        token: String
    ): Category? {
        val category = categoryCache?.find { category ->
            category.uuid == categoryUuid
        }
        return category ?: fetchCategories(
            companyUuid = companyUuid,
            token = token

        ).find { foundAddition ->
            foundAddition.uuid == categoryUuid
        }
    }

    override suspend fun updateCategory(
        categoryUuid: String,
        updateCategory: UpdateCategory,
        token: String
    ) {
        networkConnector.patchCategory(
            uuid = categoryUuid,
            token = token,
            patchCategory = categoryMapper.toPatchServer(updateCategory)
        ).onSuccess { categoryServer ->
            updateLocalCache(
                uuid = categoryUuid,
                categoryServer = categoryServer
            )
        }
    }

    override suspend fun saveCategoryPriority(token: String, category: List<Category>) {
        networkConnector.patchCategoryPriority(
            token = token,
            patchCategoryPriorityItem = categoryMapper.toPatchCategoryList(category)
        ).onSuccess {
            category.forEach { category ->
                updateLocalCache(
                    uuid = category.uuid,
                    categoryServer = categoryMapper.categoryMapper(category)
                )
            }
        }
    }

    private fun updateLocalCache(
        uuid: String,
        categoryServer: CategoryServer
    ) {
        categoryCache = categoryCache?.map { category ->
            if (uuid == category.uuid) {
                categoryMapper.categoryServer(categoryServer)
            } else {
                category
            }
        }
    }

    override fun clearCache() {
        categoryCache = null
    }
}
