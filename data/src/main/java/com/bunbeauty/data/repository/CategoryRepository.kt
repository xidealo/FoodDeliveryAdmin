package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.CategoryMapper
import com.bunbeauty.domain.model.Category
import com.bunbeauty.domain.repo.CategoryRepo
import javax.inject.Inject

class CategoryRepository @Inject constructor(
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

    override suspend fun getCategoryList(token: String, companyUuid: String): List<Category> {
        return categoryCache ?: fetchCategories(token = token, companyUuid = companyUuid)
    }
}
