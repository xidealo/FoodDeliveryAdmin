package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.dao.MenuProductCategoryDao
import com.bunbeauty.data.dao.MenuProductDao
import com.bunbeauty.data.mapper.MenuProductMapper
import com.bunbeauty.data.model.entity.menu_product.MenuProductCategoryEntity
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuProductRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi,
    private val menuProductMapper: MenuProductMapper,
    private val menuProductDao: MenuProductDao,
    private val menuProductCategoryDao: MenuProductCategoryDao
) : MenuProductRepo {

    private var menuProductList: List<MenuProduct>? = null

    override suspend fun getMenuProductList(
        companyUuid: String,
        loadFromServer: Boolean
    ): List<MenuProduct> {
        return if (menuProductList.isNullOrEmpty()) {
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
                            menuProductList = it
                        }
                }
            }
        } else menuProductList ?: emptyList()
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

        menuProductList = menuProductList?.map { menuProductItem ->
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
}