package com.bunbeauty.fooddeliveryadmin.presentation.menu

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val menuProductRepo: MenuProductRepo
) : BaseViewModel() {

    val productListSharedFlow: StateFlow<List<MenuProduct>>
        get() = _productListSharedFlow
    private val _productListSharedFlow: MutableStateFlow<List<MenuProduct>> =
        MutableStateFlow(listOf())

    fun getProducts() {
        viewModelScope.launch {
            menuProductRepo.getMenuProductList()
                .map { list ->
                    list.sortedBy { it.name }
                }.collect { menuProductList ->
                    if (menuProductList.isNotEmpty()) {
                        _productListSharedFlow.emit(menuProductList)
                    }
                }
        }
    }

}