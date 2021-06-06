package com.bunbeauty.fooddeliveryadmin.presentation.menu

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.model.ServerMenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateMenuProductViewModel @Inject constructor(
    private val stringUtil: IStringUtil,
    private val menuProductRepo: MenuProductRepo,
) : BaseViewModel() {

    private val _isVisible = MutableStateFlow(true)
    val isVisible: StateFlow<Boolean>
        get() = _isVisible.asStateFlow()


    private val _isComboDescriptionVisible = MutableStateFlow(false)
    val isComboDescriptionVisible: StateFlow<Boolean>
        get() = _isComboDescriptionVisible.asStateFlow()

    fun switchVisibility() {
        _isVisible.value = !_isVisible.value
    }

    fun setProductCode(productCode: String) {
        _isComboDescriptionVisible.value =
            (productCode == stringUtil.getProductCodeString(ProductCode.COMBO))
    }

    fun createMenuProduct(serverMenuProduct: ServerMenuProduct) {
        viewModelScope.launch(Dispatchers.Default) {
            //menuProductRepo.insert(serverMenuProduct)
        }
    }
}