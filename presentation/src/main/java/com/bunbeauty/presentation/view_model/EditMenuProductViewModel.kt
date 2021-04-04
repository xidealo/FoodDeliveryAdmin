package com.bunbeauty.presentation.view_model

import androidx.lifecycle.viewModelScope
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.domain.repository.menu_product.MenuProductRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditMenuProductViewModel @Inject constructor(private val menuProductRepo: MenuProductRepo) :
    BaseViewModel() {
    fun updateMenuProduct(menuProduct: MenuProduct){
        viewModelScope.launch {
            menuProductRepo.updateRequest(menuProduct)
        }
    }
}