package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.Constants.RELOAD_DELAY
import com.bunbeauty.data.mapper.menu_product.IMenuProductMapper
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.dao.MenuProductDao
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MenuProductRepository @Inject constructor(
    private val networkConnector: NetworkConnector,
    private val menuProductMapper: IMenuProductMapper,
    private val menuProductDao: MenuProductDao
) : MenuProductRepo {

    override suspend fun refreshMenuProductList() {
        when (val result = networkConnector.getMenuProductList()) {
            is ApiResult.Success -> {
                result.data?.let { listServer ->
                    menuProductDao.insertAll(
                        listServer.results.map { serverMenuProductList ->
                            menuProductMapper.toEntityModel(serverMenuProductList)
                        })
                }
            }
            is ApiResult.Error -> {
                delay(RELOAD_DELAY)
                refreshMenuProductList()
            }
        }
    }

    override fun getMenuProductList(): Flow<List<MenuProduct>> {
        return menuProductDao.getListFlow().map { list ->
            list.map{
                menuProductMapper.toModel(it)
            }.sortedByDescending { menuProduct ->
                menuProduct.visible
            }
        }

        /*    .flowOn(IO)
            .map { serverMenuProductList ->
                serverMenuProductList.map(serverMenuProductMapper::from)
                    .sortedByDescending { menuProduct ->
                        menuProduct.visible
                    }
            }.flowOn(Default)*/
    }

    override suspend fun deleteMenuProductPhoto(photoLink: String) {
        val photoName = photoLink.split("%2F", "?alt=media")[1]
        return networkConnector.deleteMenuProductPhoto(photoName)
    }

    override suspend fun saveMenuProductPhoto(photoByteArray: ByteArray): String {
        return when (val result = networkConnector.saveMenuProductPhoto(photoByteArray)) {
            is ApiResult.Success -> {
                result.data ?: ""
            }
            is ApiResult.Error -> {
                ""
            }
        }
    }

    override suspend fun saveMenuProduct(menuProduct: MenuProduct) {
        networkConnector.saveMenuProduct(menuProductMapper.toServerModel(menuProduct))
    }

    override suspend fun updateMenuProduct(menuProduct: MenuProduct) {
        networkConnector.updateMenuProduct(
            menuProductMapper.toServerModel(menuProduct),
            menuProduct.uuid!!
        )
    }

    override suspend fun deleteMenuProduct(uuid: String) {
        networkConnector.deleteMenuProduct(uuid)
    }
}