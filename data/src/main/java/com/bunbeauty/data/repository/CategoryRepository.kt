package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.dao.CategoryDao
import com.bunbeauty.data.mapper.CategoryMapper
import com.bunbeauty.domain.repo.CategoryRepo
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val networkConnector: FoodDeliveryApi,
    private val categoryMapper: CategoryMapper
) : CategoryRepo {

    override suspend fun refreshCategories(token: String, companyUuid: String) {
        when (val result = networkConnector.getCategoriesByCompanyUuid(token, companyUuid)) {
            is ApiResult.Success -> {
                categoryDao.deleteAll()
                categoryDao.insertAll(result.data.results.map(categoryMapper::toEntity))
            }
            is ApiResult.Error -> {}
        }
    }
}
