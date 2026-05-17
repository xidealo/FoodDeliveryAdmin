package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.data.mapper.MenuProductMapper
import com.bunbeauty.data.mapper.toMenuProductPostServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductAdditionsPatchServer
import com.bunbeauty.data.model.server.menuproduct.MenuProductAdditionsPostServer
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.editmenuproduct.exception.MenuProductNotUpdatedException
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.model.menuproduct.MenuProductPost
import com.bunbeauty.domain.model.menuproduct.UpdateMenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import common.ApiResult
import common.log.AppLogMessages
import common.log.AppLogger

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

    override suspend fun createMenuProductAdditions(
        menuProductUuid: String,
        additionGroupUuid: String,
        additionList: List<String>,
    ) {
        val result =
            networkConnector.postMenuProductAdditions(
                token = dataStoreRepository.getToken() ?: throw NoTokenException(),
                menuProductAdditionsPostServer =
                    MenuProductAdditionsPostServer(
                        menuProductUuids = listOf(menuProductUuid),
                        additionGroupUuid = additionGroupUuid,
                        additionUuids = additionList,
                    ),
            )
        if (result is ApiResult.Error) {
            throw Exception(result.apiError.message)
        }
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
    ): MenuProduct? {
        AppLogger.d(
            tag = AppLogMessages.MENU_PRODUCT_REPOSITORY_TAG,
            message =
                AppLogMessages.menuProductPatchStart(
                    uuid = menuProductUuid,
                    photoLink = updateMenuProduct.photoLink,
                ),
        )
        val result =
            networkConnector.patchMenuProduct(
                menuProductUuid = menuProductUuid,
                menuProductPatchServer = menuProductMapper.toPatchServer(updateMenuProduct),
                token = token,
            )

        return when (result) {
            is ApiResult.Success -> {
                val menuProductServer = result.data
                val menuProduct = menuProductMapper.toModel(menuProductServer)
                AppLogger.d(
                    tag = AppLogMessages.MENU_PRODUCT_REPOSITORY_TAG,
                    message =
                        AppLogMessages.menuProductPatchSuccess(
                            uuid = menuProductUuid,
                            photoLink = menuProduct.photoLink,
                        ),
                )
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

            is ApiResult.Error -> {
                val detail =
                    buildString {
                        if (result.apiError.code != 0) {
                            append("${result.apiError.code}: ")
                        }
                        append(result.apiError.message)
                    }.trimEnd(' ', ':')
                        .takeIf { it.isNotBlank() }
                AppLogger.e(
                    tag = AppLogMessages.MENU_PRODUCT_REPOSITORY_TAG,
                    message =
                        AppLogMessages.menuProductPatchError(
                            uuid = menuProductUuid,
                            detail = detail.orEmpty(),
                        ),
                )
                throw MenuProductNotUpdatedException(serverDetail = detail)
            }
        }
    }

    override suspend fun updateMenuProductAdditions(
        menuProductToAdditionGroupUuid: String,
        additionGroupUuid: String?,
        additionList: List<String>?,
    ) {
        val result =
            networkConnector.patchMenuProductAdditions(
                token = dataStoreRepository.getToken() ?: throw NoTokenException(),
                menuProductToAdditionGroupUuid = menuProductToAdditionGroupUuid,
                menuProductAdditionsPatchServer =
                    MenuProductAdditionsPatchServer(
                        additionGroupUuid = additionGroupUuid,
                        additionUuidList = additionList,
                    ),
            )
        if (result is ApiResult.Error) {
            throw Exception(result.apiError.message)
        }
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
