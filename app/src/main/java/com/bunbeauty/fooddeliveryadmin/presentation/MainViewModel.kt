package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.repository.cafe.CafeRepo
import com.bunbeauty.domain.repository.menu_product.MenuProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val cafeRepo: CafeRepo,
    private val menuProductRepo: MenuProductRepo
) : BaseViewModel() {

    fun refreshCafeList() {
        viewModelScope.launch(Dispatchers.IO) {
            cafeRepo.refreshCafeList()
        }
    }

    fun refreshProductList() {
        viewModelScope.launch(Dispatchers.IO) {
            menuProductRepo.getMenuProductRequest()
        }
    }
}