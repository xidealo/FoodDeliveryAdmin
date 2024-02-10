package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.extension.onSuccess
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.dao.MenuProductDao
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.data.mapper.MenuProductMapper
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import javax.inject.Inject

class MenuProductRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi,
    private val menuProductMapper: MenuProductMapper,
    private val menuProductDao: MenuProductDao,
) : MenuProductRepo {

    override suspend fun getMenuProductList(
        companyUuid: String,
        takeRemote: Boolean
    ): List<MenuProduct>? {
        val menuProductListFromLocal = getMenuProductListLocal()

        return if (menuProductListFromLocal.isEmpty() || takeRemote) {
            networkConnector.getMenuProductList(
                companyUuid = companyUuid
            ).dataOrNull()
                ?.results
                ?.onEach { menuProductServer ->
                    menuProductDao.insert(menuProductMapper.toEntity(menuProductServer))
                }?.map { menuProductServer ->
                    menuProductMapper.toModel(menuProductServer)
                }
        } else {
            menuProductListFromLocal
        }
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

    override suspend fun updateMenuProduct(
        menuProductUuid: String,
        updateMenuProduct: UpdateMenuProduct,
        token: String
    ) {
        networkConnector.patchMenuProduct(
            menuProductUuid = menuProductUuid,
            menuProductPatchServer = menuProductMapper.toPatchServer(updateMenuProduct),
            token = token
        ).onSuccess { menuProductServer ->
            menuProductDao.update(menuProductMapper.toEntity(menuProductServer))
        }
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

    override suspend fun clearCache() {
        menuProductDao.deleteAll()
    }
}