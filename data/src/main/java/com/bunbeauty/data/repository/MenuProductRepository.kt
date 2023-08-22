package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.dao.MenuProductDao
import com.bunbeauty.data.mapper.MenuProductMapper
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuProductRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi,
    private val menuProductMapper: MenuProductMapper,
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
                            menuProductDao.insert(menuProductMapper.toEntity(menuProduct))
                        }
                        .map { menuProductServer -> menuProductMapper.toModel(menuProductServer) }
                }
            }
        } else menuProductListFromLocal
    }

    private suspend fun getMenuProductListLocal(): List<MenuProduct> {
        return menuProductDao.getList().map { menuProductWithCategoriesEntity ->
            menuProductMapper.toModel(menuProductWithCategoriesEntity)
        }
    }

    override suspend fun getMenuProduct(menuProductUuid: String): MenuProduct? {
        return menuProductDao.getByUuid(uuid = menuProductUuid)
            .let { menuProductWithCategoriesEntity ->
                if (menuProductWithCategoriesEntity == null) {
                    null
                } else {
                    menuProductMapper.toModel(menuProductWithCategoriesEntity)
                }
            }
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
            menuProductServer = menuProductMapper.toServer(menuProduct),
            token = token
        )
        menuProductDao.update(menuProductMapper.toEntity(menuProduct))
    }

    override suspend fun updateVisibleMenuProductUseCase(
        uuid: String,
        isVisible: Boolean,
        token: String
    ) {
        networkConnector.updateVisibleMenuProductUseCase(
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

    override suspend fun clearCache() {
        menuProductDao.deleteAll()
    }
}