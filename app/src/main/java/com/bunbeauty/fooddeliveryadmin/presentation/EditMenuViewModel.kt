package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditMenuViewModel @Inject constructor(
    private val menuProductRepo: MenuProductRepo
) : BaseViewModel() {

    val productListSharedFlow = MutableStateFlow<List<MenuProduct>>(listOf())

    fun getProducts() {
        viewModelScope.launch {
            menuProductRepo.getMenuProductList()
                .map { list ->
                    list.sortedBy { it.name }
                }.collect { menuProductList ->
                    if (menuProductList.isNotEmpty()) {
                        productListSharedFlow.emit(menuProductList)
                    }
                }
        }
    }

}