package com.bunbeauty.data.repository

import com.bunbeauty.data.mapper.menu_product.IServerMenuProductMapper
import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MenuProductRepository @Inject constructor(
    private val apiRepo: ApiRepo,
    private val serverMenuProductMapper: IServerMenuProductMapper
) : MenuProductRepo {

    override val menuProductList: Flow<List<MenuProduct>>
        get() = apiRepo.menuProductList
            .flowOn(IO)
            .map { serverMenuProductList ->
                serverMenuProductList.map(serverMenuProductMapper::from)
                    .sortedByDescending { menuProduct ->
                        menuProduct.visible
                    }
            }.flowOn(Default)
}