package com.bunbeauty.data.repository

import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.data.mapper.MenuProductMapper
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.data.dao.MenuProductDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MenuProductRepository @Inject constructor(
    private val menuProductDao: MenuProductDao,
    private val apiRepo: ApiRepo,
    private val mapper: MenuProductMapper
) : MenuProductRepo {

    override suspend fun insert(menuProduct: MenuProduct) {
        menuProductDao.insert(menuProduct)
    }

    override suspend fun updateRequest(menuProduct: MenuProduct) {
        apiRepo.updateMenuProduct(mapper.from(menuProduct), menuProduct.uuid)
        menuProductDao.update(menuProduct)
    }

    override suspend fun getMenuProductRequest() {
        menuProductDao.deleteAll()
        apiRepo.getMenuProductList().collect { menuProductList ->
            menuProductDao.insertAll(menuProductList)
        }
    }

    override fun getMenuProductList(): Flow<List<MenuProduct>> {
        return menuProductDao.getMenuProductListFlow()
    }
}