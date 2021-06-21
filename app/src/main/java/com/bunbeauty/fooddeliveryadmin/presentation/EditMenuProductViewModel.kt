package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
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