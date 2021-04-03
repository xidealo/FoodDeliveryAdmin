package com.bunbeauty.presentation.view_model

import androidx.lifecycle.viewModelScope
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.domain.repository.menu_product.MenuProductRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditMenuViewModel @Inject constructor(
    private val menuProductRepo: MenuProductRepo
) : BaseViewModel() {

    val productListSharedFlow = MutableSharedFlow<List<MenuProduct>>()

    fun getProducts() {
        viewModelScope.launch {
            menuProductRepo.getMenuProductList()
                .map { list ->
                    list.sortedBy { it.name }.filter { it.visible }
                }.collect { menuProductList ->
                    if (menuProductList.isNotEmpty()) {
                        productListSharedFlow.emit(menuProductList)
                    }
                }
        }
    }

}