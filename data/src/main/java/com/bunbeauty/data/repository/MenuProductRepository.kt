package com.bunbeauty.data.repository

import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.data.dao.MenuProductDao
import com.bunbeauty.data.mapper.IMenuProductMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MenuProductRepository @Inject constructor(
    private val menuProductDao: MenuProductDao,
    private val apiRepo: ApiRepo,
    private val menuProductMapper: IMenuProductMapper
) : MenuProductRepo {

    override suspend fun insert(menuProduct: MenuProduct) {
        menuProductDao.insert(menuProduct)
    }

    override suspend fun updateRequest(menuProduct: MenuProduct) {
        apiRepo.updateMenuProduct(menuProductMapper.from(menuProduct), menuProduct.uuid)
        menuProductDao.update(menuProduct)
    }

    override suspend fun refreshProductList() {
        menuProductDao.deleteAll()
        apiRepo.getMenuProductList().collect { menuProductList ->
            menuProductDao.insertAll(menuProductList)
        }
    }

    override fun getMenuProductList(): Flow<List<MenuProduct>> {
        return menuProductDao.getMenuProductListFlow()
    }
}