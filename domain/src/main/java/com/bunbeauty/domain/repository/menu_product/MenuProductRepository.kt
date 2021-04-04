package com.bunbeauty.domain.repository.menu_product

import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.data.model.mapper.MenuProductMapper
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MenuProductRepository @Inject constructor(
    private val menuProductDao: MenuProductDao,
    private val apiRepository: IApiRepository,
    private val mapper: MenuProductMapper
) : MenuProductRepo {

    override suspend fun insert(menuProduct: MenuProduct) {
        menuProductDao.insert(menuProduct)
    }

    override suspend fun updateRequest(menuProduct: MenuProduct) {
        apiRepository.updateMenuProduct(mapper.from(menuProduct), menuProduct.uuid)
        menuProductDao.update(menuProduct)
    }

    override suspend fun getMenuProductRequest() {
        menuProductDao.deleteAll()
        apiRepository.getMenuProductList().collect { menuProductList ->
            menuProductDao.insertAll(menuProductList)
        }
    }

    override fun getMenuProductList(): Flow<List<MenuProduct>> {
        return menuProductDao.getMenuProductListFlow()
    }
}