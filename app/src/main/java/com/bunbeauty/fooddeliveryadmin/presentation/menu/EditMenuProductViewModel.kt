package com.bunbeauty.fooddeliveryadmin.presentation.menu

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.model.ServerMenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMenuProductViewModel @Inject constructor(private val menuProductRepo: MenuProductRepo) :
    BaseViewModel() {

     fun updateMenuProduct(serverMenuProduct: ServerMenuProduct) {
        viewModelScope.launch {
            //menuProductRepo.updateRequest(serverMenuProduct)
        }
    }
}