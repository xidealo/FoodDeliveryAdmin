package com.bunbeauty.fooddeliveryadmin.presentation.menu

import android.graphics.Bitmap
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.LIST_ARGS_KEY
import com.bunbeauty.common.Constants.PRODUCT_CODE_REQUEST_KEY
import com.bunbeauty.common.Constants.PRODUCT_COMBO_DESCRIPTION_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_DISCOUNT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_NAME_ERROR_KEY
import com.bunbeauty.common.Constants.REQUEST_KEY_ARGS_KEY
import com.bunbeauty.common.Constants.SELECTED_KEY_ARGS_KEY
import com.bunbeauty.common.Constants.SELECTED_PRODUCT_CODE_KEY
import com.bunbeauty.common.Constants.TITLE_ARGS_KEY
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.model.menu_product.MenuProductCode
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.extensions.toByteArray
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.ErrorEvent
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateMenuProductViewModel @Inject constructor(
    private val stringUtil: IStringUtil,
    private val resourcesProvider: IResourcesProvider,
    private val menuProductRepo: MenuProductRepo,
) : BaseViewModel() {

    private val menuProductCodeList = ProductCode.values().map { productCode ->
        MenuProductCode(title = stringUtil.getProductCodeString(productCode))
    }.toTypedArray()

    private var isVisible = true
    private val mutableVisibilityIcon =
        MutableStateFlow(resourcesProvider.getDrawable(R.drawable.ic_visible))
    val visibilityIcon = mutableVisibilityIcon.asStateFlow()

    private val mutableIsComboDescriptionVisible = MutableStateFlow(false)
    val isComboDescriptionVisible = mutableIsComboDescriptionVisible.asStateFlow()

    var photo: Bitmap? = null

    fun switchVisibility() {
        isVisible = !isVisible
        if (isVisible) {
            mutableVisibilityIcon.value = resourcesProvider.getDrawable(R.drawable.ic_visible)
        } else {
            mutableVisibilityIcon.value = resourcesProvider.getDrawable(R.drawable.ic_invisible)
        }
    }

    fun setProductCode(productCode: String) {
        mutableIsComboDescriptionVisible.value =
            (productCode == stringUtil.getProductCodeString(ProductCode.COMBO))
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
            viewModelScope.launch {
                mutableError.emit(
                    ErrorEvent.MessageError(
                        message = resourcesProvider.getString(R.string.error_create_menu_product_image_not_loaded)
                    )
                )
            }
            return
        }

        if (name.isEmpty()) {
            viewModelScope.launch {
                mutableError.emit(
                    ErrorEvent.FieldError(
                        key = PRODUCT_NAME_ERROR_KEY,
                        message = resourcesProvider.getString(R.string.error_create_menu_product_empty_name)
                    )
                )
            }
            return
        }

        val productCode = stringUtil.getProductCode(productCodeString)
        if (productCode == null) {
            viewModelScope.launch {
                mutableError.emit(
                    ErrorEvent.MessageError(
                        message = resourcesProvider.getString(R.string.error_create_menu_product_category_not_selected)
                    )
                )
            }
            return
        }

        val costInt = cost.toIntOrNull()
        if (costInt == null) {
            viewModelScope.launch {
                mutableError.emit(
                    ErrorEvent.FieldError(
                        key = PRODUCT_COST_ERROR_KEY,
                        message = resourcesProvider.getString(R.string.error_create_menu_product_cost_incorrect)
                    )
                )
            }
            return
        }

        val discountCostInt = discountCost.toIntOrNull()
        if (discountCostInt != null && discountCostInt >= costInt) {
            viewModelScope.launch {
                mutableError.emit(
                    ErrorEvent.FieldError(
                        key = PRODUCT_DISCOUNT_COST_ERROR_KEY,
                        message = resourcesProvider.getString(R.string.error_create_menu_product_discount_cost_incorrect)
                    )
                )
            }
            return
        }

        if (productCode == ProductCode.COMBO && comboDescription.isEmpty()) {
            viewModelScope.launch {
                mutableError.emit(
                    ErrorEvent.FieldError(
                        key = PRODUCT_COMBO_DESCRIPTION_ERROR_KEY,
                        message = resourcesProvider.getString(R.string.error_create_menu_product_combo_description_incorrect)
                    )
                )
            }
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
            menuProductRepo.saveMenuProductPhoto(photoByteArray).collect { photoLink ->
                val uuid = UUID.randomUUID().toString()
                val menuProduct = MenuProduct(
                    uuid = uuid,
                    name = name,
                    cost = costInt,
                    discountCost = discountCostInt,
                    weight = weight.toIntOrNull(),
                    description = description,
                    comboDescription = nullableComboDescription,
                    photoLink = photoLink,
                    onFire = false,
                    inOven = false,
                    productCode = productCode,
                    barcode = null,
                    visible = isVisible
                )
                menuProductRepo.saveMenuProduct(menuProduct)
                finishCreation(name)
            }
        }
    }

    private suspend fun finishCreation(productName: String) {
        withContext(Main) {
            mutableMessage.emit(
                productName + resourcesProvider.getString(R.string.msg_product_created)
            )
            goBack()
        }
    }

    fun goToProductCodeList() {
        router.navigate(
            R.id.to_listBottomSheet,
            bundleOf(
                TITLE_ARGS_KEY to resourcesProvider.getString(R.string.msg_create_menu_product_select_category),
                LIST_ARGS_KEY to menuProductCodeList,
                SELECTED_KEY_ARGS_KEY to SELECTED_PRODUCT_CODE_KEY,
                REQUEST_KEY_ARGS_KEY to PRODUCT_CODE_REQUEST_KEY,
            )
        )
    }
}