package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.data.mapper.MenuProductMapper
import com.bunbeauty.data.mapper.toMenuProductPostServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductAdditionsPatchServer
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.model.menuproduct.MenuProductPost
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo

class MenuProductRepository(
    private val networkConnector: FoodDeliveryApi,
    private val menuProductMapper: MenuProductMapper,
    private val dataStoreRepository: DataStoreRepo,
) : MenuProductRepo {
    private var menuProductCache: List<MenuProduct>? = null

    override suspend fun getMenuProductList(
        companyUuid: String,
        takeRemote: Boolean,
    ): List<MenuProduct>? =
        if (menuProductCache == null || takeRemote) {
            val menuProductList =
                networkConnector
                    .getMenuProductList(
                        companyUuid = companyUuid,
                    ).dataOrNull()
                    ?.results
                    ?.map { menuProductServer ->
                        menuProductMapper.toModel(menuProductServer)
                    }
            menuProductCache = menuProductList

            menuProductList
        } else {
            menuProductCache
        }

    override suspend fun saveMenuProduct(
        token: String,
        menuProductPost: MenuProductPost,
    ): MenuProduct? =
        networkConnector
            .postMenuProduct(
                token = token,
                menuProductPostServer = menuProductPost.toMenuProductPostServer(),
            ).dataOrNull()
            ?.let { menuProductServer ->
                val menuProduct = menuProductMapper.toModel(menuProductServer)
                menuProductCache?.let { cache ->
                    menuProductCache = cache + menuProduct
                }
                menuProduct
            }

    override suspend fun getMenuProduct(
        menuProductUuid: String,
        companyUuid: String,
    ): MenuProduct? =
        getMenuProductList(companyUuid = companyUuid)?.find { menuProduct ->
            menuProduct.uuid == menuProductUuid
        }

    override suspend fun updateMenuProduct(
        menuProductUuid: String,
        updateMenuProduct: UpdateMenuProduct,
        token: String,
    ): MenuProduct? =
        networkConnector
            .patchMenuProduct(
                menuProductUuid = menuProductUuid,
                menuProductPatchServer = menuProductMapper.toPatchServer(updateMenuProduct),
                token = token,
            ).dataOrNull()
            ?.let { menuProductServer ->
                val menuProduct = menuProductMapper.toModel(menuProductServer)
                menuProductCache =
                    menuProductCache?.map { cachedMenuProduct ->
                        if (cachedMenuProduct.uuid == menuProductServer.uuid) {
                            menuProduct
                        } else {
                            cachedMenuProduct
                        }
                    }
                menuProduct
            }

    override suspend fun updateMenuProductAdditions(
        menuProductToAdditionGroupUuid: String,
        additionGroupUuid: String?,
        additionList: List<String>?,
    ) {
        networkConnector.patchMenuProductAdditions(
            token = dataStoreRepository.getToken() ?: throw NoTokenException(),
            menuProductToAdditionGroupUuid = menuProductToAdditionGroupUuid,
            menuProductAdditionsPatchServer =
                MenuProductAdditionsPatchServer(
                    additionGroupUuid = additionGroupUuid,
                    additionUuidList = additionList,
                ),
        )
    }

    override suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): String =
        when (val result = networkConnector.saveMenuProductPhoto(photoByteArray)) {
            is ApiResult.Success -> {
                result.data
            }

            is ApiResult.Error -> {
                ""
            }
        }

    override fun clearCache() {
        menuProductCache = null
    }
}
