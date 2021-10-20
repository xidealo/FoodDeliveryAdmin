package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.Constants.RELOAD_DELAY
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.dao.CategoryDao
import com.bunbeauty.data.dao.MenuProductCategoryDao
import com.bunbeauty.data.dao.MenuProductDao
import com.bunbeauty.data.mapper.CategoryMapper
import com.bunbeauty.data.mapper.MenuProductMapper
import com.bunbeauty.data.model.entity.menu_product.MenuProductCategoryEntity
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MenuProductRepository @Inject constructor(
    private val networkConnector: NetworkConnector,
    private val menuProductMapper: MenuProductMapper,
    private val menuProductDao: MenuProductDao,
    private val menuProductCategoryDao: MenuProductCategoryDao
) : MenuProductRepo {

    override suspend fun refreshMenuProductList(companyUuid: String) {
        when (val result = networkConnector.getMenuProductList(companyUuid)) {
            is ApiResult.Success -> {
                menuProductDao.deleteAll()
                result.data.let { listServer ->
                    menuProductDao.insertAll(
                        listServer.results.map { serverMenuProductList ->
                            menuProductMapper.toEntity(serverMenuProductList)
                        })

                    listServer.results.forEach { menuProductServer ->
                        menuProductServer.categories.forEach { category ->
                            menuProductCategoryDao.insert(
                                MenuProductCategoryEntity(
                                    menuProductServer.uuid, category.uuid
                                )
                            )
                        }
                    }
                }
            }
            is ApiResult.Error -> {
                delay(RELOAD_DELAY)
                //refreshMenuProductList()
            }
        }
    }

    override fun getMenuProductList(): Flow<List<MenuProduct>> {
        return menuProductDao.getListFlow().map { list ->
            list.map {
                menuProductMapper.toModel(it)
            }.sortedByDescending { menuProduct ->
                menuProduct.isVisible
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

    override suspend fun saveMenuProduct(menuProduct: MenuProduct) {
        // networkConnector.saveMenuProduct(menuProductMapper.toServerModel(menuProduct))
    }

    override suspend fun updateMenuProduct(menuProduct: MenuProduct) {
        /*   networkConnector.updateMenuProduct(
               menuProductMapper.toServerModel(menuProduct),
               menuProduct.uuid!!
           )*/
    }

    override suspend fun deleteMenuProduct(uuid: String) {
        networkConnector.deleteMenuProduct(uuid)
    }
}