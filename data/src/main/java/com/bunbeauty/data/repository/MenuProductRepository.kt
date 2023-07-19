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

    private var menuProductListCache: List<MenuProduct>? = null

    override suspend fun getMenuProductList(
        companyUuid: String,
        isRefreshing: Boolean
    ): List<MenuProduct> {
        return if (menuProductListCache.isNullOrEmpty() || isRefreshing) {
            networkConnector.getMenuProductList(
                companyUuid = companyUuid
            ).let { listServer ->
                return withContext(Dispatchers.Default) {
                    listServer
                        .results
                        .map { menuProductMapper.toModel(it) }
                        .sortedByDescending { menuProduct ->
                            menuProduct.isVisible
                        }.also {
                            menuProductListCache = it
                        }
                }
            }
        } else menuProductListCache ?: emptyList()
    }

    override suspend fun getMenuProductList(): List<MenuProduct> {
        return menuProductDao.getList().map { menuProduct ->
            menuProductMapper.toModel(menuProduct)
        }.sortedByDescending { menuProduct ->
            menuProduct.isVisible
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

    override suspend fun saveMenuProduct(menuProduct: MenuProduct) {
        // networkConnector.saveMenuProduct(menuProductMapper.toServerModel(menuProduct))
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

        menuProductListCache = menuProductListCache?.map { menuProductItem ->
            if (uuid == menuProductItem.uuid) {
                menuProductItem.copy(
                    isVisible = isVisible
                )
            } else {
                menuProductItem
            }
        }
    }

    override suspend fun deleteMenuProduct(uuid: String) {
        networkConnector.deleteMenuProduct(uuid)
    }

    override suspend fun clearCache() {
        menuProductListCache = null
    }
}