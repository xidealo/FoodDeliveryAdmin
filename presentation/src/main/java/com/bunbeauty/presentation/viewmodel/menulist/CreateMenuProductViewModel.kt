package com.bunbeauty.presentation.viewmodel.menulist

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.presentation.extension.toByteArray
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateMenuProductViewModel @Inject constructor(
    private val stringUtil: IStringUtil,
    private val menuProductRepo: MenuProductRepo
) : BaseViewModel() {

    private val mutableIsVisible: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isVisible: StateFlow<Boolean> = mutableIsVisible.asStateFlow()

    private val mutableIsComboDescriptionVisible = MutableStateFlow(false)
    val isComboDescriptionVisible = mutableIsComboDescriptionVisible.asStateFlow()

    var photo: Bitmap? = null

    fun switchVisibility() {
        mutableIsVisible.value = !mutableIsVisible.value
    }

    fun createMenuProduct(
        name: String,
        productCodeString: String,
        cost: String,
        discountCost: String,
        weight: String,
        description: String,
        comboDescription: String
    ) {
        if (photo == null) {
            // sendError(resourcesProvider.getString(R.string.error_create_menu_product_image_not_loaded))
            return
        }

        if (name.isEmpty()) {
          /*  sendFieldError(
                PRODUCT_NAME_ERROR_KEY,
                resourcesProvider.getString(R.string.error_create_menu_product_empty_name)
            )*/
            return
        }

        val productCode = stringUtil.getProductCode(productCodeString)
        if (productCode == null) {
            // sendError(resourcesProvider.getString(R.string.error_create_menu_product_category_not_selected))
            return
        }

        val costInt = cost.toIntOrNull()
        if (costInt == null) {
           /* sendFieldError(
                PRODUCT_COST_ERROR_KEY,
                resourcesProvider.getString(R.string.error_create_menu_product_cost_incorrect)
            )*/
            return
        }

        val discountCostInt = discountCost.toIntOrNull()
        if (discountCostInt != null && discountCostInt >= costInt) {
       /*     sendFieldError(
                PRODUCT_DISCOUNT_COST_ERROR_KEY,
                resourcesProvider.getString(R.string.error_create_menu_product_discount_cost_incorrect)
            )*/
            return
        }

        if (productCode == ProductCode.COMBO && comboDescription.isEmpty()) {
         /*   sendFieldError(
                PRODUCT_COMBO_DESCRIPTION_ERROR_KEY,
                resourcesProvider.getString(R.string.error_create_menu_product_combo_description_incorrect)
            )*/
            return
        }

        val nullableComboDescription = if (productCode == ProductCode.COMBO) {
            comboDescription
        } else {
            null
        }
        viewModelScope.launch(IO) {
            val photoByteArray = withContext(Default) {
                photo!!.toByteArray()
            }
            menuProductRepo.saveMenuProductPhoto(photoByteArray)
        }
    }

    fun goToProductCodeList() {}
}
