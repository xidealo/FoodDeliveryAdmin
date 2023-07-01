package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.dao.MenuProductDao
import com.bunbeauty.data.mapper.CategoryMapper
import com.bunbeauty.data.mapper.toEntity
import com.bunbeauty.data.mapper.toModel
import com.bunbeauty.data.mapper.toServer
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuProductRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi,
    private val categoryMapper: CategoryMapper,
    private val menuProductDao: MenuProductDao,
) : MenuProductRepo {

    override suspend fun getMenuProductList(
        companyUuid: String,
        isRefreshing: Boolean
    ): List<MenuProduct> {
        val menuProductListFromLocal = getMenuProductListLocal()

        return if (menuProductListFromLocal.isEmpty() || isRefreshing) {
            networkConnector.getMenuProductList(
                companyUuid = companyUuid
            ).let { listServer ->
                return withContext(Dispatchers.Default) {
                    listServer
                        .results
                        .onEach { menuProduct ->
                            menuProductDao.insert(menuProduct.toEntity())
                        }
                        .map { menuProductServer -> menuProductServer.toModel(categoryMapper) }
                }
            }
        } else menuProductListFromLocal
    }

    private suspend fun getMenuProductListLocal(): List<MenuProduct> {
        return menuProductDao.getList().map { menuProductWithCategoriesEntity ->
            menuProductWithCategoriesEntity.toModel(categoryMapper)
        }
    }

    override suspend fun getMenuProduct(menuProductUuid: String): MenuProduct? {
        return menuProductDao.getByUuid(uuid = menuProductUuid)?.toModel(categoryMapper)
    }

    override suspend fun deleteMenuProductPhoto(photoLink: String) {
        val photoName = photoLink.split("%2F", "?alt=media")[1]
        return networkConnector.deleteMenuProductPhoto(photoName)
    }

    override suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): String {
        return when (val result = networkConnector.saveMenuProductPhoto(photoByteArray)) {
            is ApiResult.Success -> {
                result.data
            }
            is ApiResult.Error -> {
                ""
            }
        }
    }

    override suspend fun updateMenuProduct(
        menuProduct: MenuProduct,
        token: String
    ) {
        networkConnector.patchMenuProduct(
            menuProductServer = menuProduct.toServer(),
            token = token
        )
        menuProductDao.update(menuProduct.toEntity())
    }

    override suspend fun updateVisibleMenuProductUseCase(
        uuid: String,
        isVisible: Boolean,
        token: String
    ) {
        networkConnector.updateVisibleMenuProduct(
            uuid = uuid,
            isVisible = isVisible,
            token = token
        )

        menuProductDao.getByUuid(uuid = uuid)
            ?.menuProductEntity
            ?.copy(
                isVisible = isVisible
            )?.let { menuProductEntity ->
                menuProductDao.update(menuProductEntity)
            }
    }

    override suspend fun deleteMenuProduct(uuid: String) {
        networkConnector.deleteMenuProduct(uuid)
        //todo remove local
    }

    override suspend fun clearMenuProductList() {
        menuProductDao.deleteAll()
    }
}