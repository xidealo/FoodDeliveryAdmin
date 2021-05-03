package com.bunbeauty.presentation.view_model

import androidx.lifecycle.viewModelScope
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.domain.repository.menu_product.MenuProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateNewMenuProductViewModel @Inject constructor(
    private val menuProductRepo: MenuProductRepo
) : BaseViewModel() {

    fun createMenuProduct(menuProduct: MenuProduct) {
        viewModelScope.launch(Dispatchers.Default) {
            menuProductRepo.insert(menuProduct)
        }
    }
}