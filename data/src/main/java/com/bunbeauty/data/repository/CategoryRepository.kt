package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.extension.onSuccess
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.data.mapper.CategoryMapper
import com.bunbeauty.data.model.server.category.CategoryPatchServer
import com.bunbeauty.data.model.server.category.CategoryServer
import com.bunbeauty.data.model.server.category.CreateCategoryPostServer
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.feature.menu.common.model.CategoryPost
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

    override suspend fun getCategoryList(token: String, companyUuid: String, refreshing: Boolean): List<Category> {
        return if (refreshing) {
            fetchCategories(token = token, companyUuid = companyUuid)
        } else {
            categoryCache ?: fetchCategories(token = token, companyUuid = companyUuid)
        }
    }

    override suspend fun postCategory(token: String, categoryPost: CategoryPost): Category {
        return networkConnector.postCategory(
            token = token,
            categoryServerPost = categoryPost.toCategoryPostServer()
        ).dataOrNull()?.let { createCategoryServer ->
            val categoryCreate = categoryMapper.toModel(createCategoryServer)
            categoryCache?.let { cache ->
                categoryCache = cache + categoryCreate
            }
            categoryCreate
        }
            ?: throw IllegalStateException("Ошибка при создании категории: сервер вернул null") // доработать
    }

    val toCategoryPostServer: CategoryPost.() -> CreateCategoryPostServer = {
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
        token: String,
        companyUuid: String
    ) {
        networkConnector.patchCategory(
            uuid = categoryUuid,
            token = token,
            companyUuid = companyUuid,
            patchCategory = updateCategory.mapUpdateCategoryServerToPatchCategory()
        ).onSuccess { categoryServer ->
            updateLocalCache(
                uuid = categoryUuid,
                categoryServer = categoryServer
            )
        }
    }

    val mapUpdateCategoryServerToPatchCategory: UpdateCategory.() -> CategoryPatchServer = {
        CategoryPatchServer(
            name = name,
            priority = priority
        )
    }

    private fun updateLocalCache(
        uuid: String,
        categoryServer: CategoryServer
    ) {
        categoryCache = categoryCache?.map { category ->
            if (uuid == category.uuid) {
                categoryServer.mapCategoryServerToCategory()
            } else {
                category
            }
        }
    }

    val mapCategoryServerToCategory: CategoryServer.() -> Category = {
        Category(
            uuid = uuid,
            name = name,
            priority = priority

        )
    }

    override fun clearCache() {
        categoryCache = null
    }
}
