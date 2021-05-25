package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.repository.api.firebase.ApiRepository
import com.bunbeauty.domain.repository.cafe.CafeRepo
import com.bunbeauty.domain.repository.menu_product.MenuProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val cafeRepo: CafeRepo,
    private val menuProductRepo: MenuProductRepo,
) : BaseViewModel() {

    fun refreshCafeList() {
        viewModelScope.launch(IO) {
            cafeRepo.refreshCafeList()
        }
    }

    fun refreshProductList() {
        viewModelScope.launch(IO) {
            menuProductRepo.getMenuProductRequest()
        }
    }
}