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
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.ErrorEvent
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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
    private val _visibilityIcon =
        MutableStateFlow(resourcesProvider.getDrawable(R.drawable.ic_visible))
    val visibilityIcon = _visibilityIcon.asStateFlow()

    private val _isComboDescriptionVisible = MutableStateFlow(false)
    val isComboDescriptionVisible = _isComboDescriptionVisible.asStateFlow()

    var image: Bitmap? = null

    private val _error = MutableStateFlow<ErrorEvent?>(null)
    val error: StateFlow<ErrorEvent?>
        get() {
            _error.value = null
            return _error.asStateFlow()
        }

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?>
        get() {
            _message.value = null
            return _message.asStateFlow()
        }

    fun switchVisibility() {
        isVisible = !isVisible
        if (isVisible) {
            _visibilityIcon.value = resourcesProvider.getDrawable(R.drawable.ic_visible)
        } else {
            _visibilityIcon.value = resourcesProvider.getDrawable(R.drawable.ic_invisible)
        }
    }

    fun setProductCode(productCode: String) {
        _isComboDescriptionVisible.value =
            (productCode == stringUtil.getProductCodeString(ProductCode.COMBO))
    }

    fun createMenuProduct(
        bitmap: Bitmap,
        name: String,
        productCodeString: String,
        cost: String,
        discountCost: String,
        weight: String,
        description: String,
        comboDescription: String
    ) {
        if (image == null) {
            _error.value =
                ErrorEvent.MessageError(message = resourcesProvider.getString(R.string.error_image_not_loaded))
            return
        }

        if (name.isEmpty()) {
            _error.value =
                ErrorEvent.FieldError(
                    key = PRODUCT_NAME_ERROR_KEY,
                    message = resourcesProvider.getString(R.string.error_empty_name)
                )
            return
        }

        val productCode = stringUtil.getProductCode(productCodeString)
        if (productCode == null) {
            _error.value = ErrorEvent.MessageError(
                message = resourcesProvider.getString(R.string.error_category_not_selected)
            )
            return
        }

        val costInt = cost.toIntOrNull()
        if (costInt == null) {
            _error.value = ErrorEvent.FieldError(
                key = PRODUCT_COST_ERROR_KEY,
                message = resourcesProvider.getString(R.string.error_cost_incorrect)
            )
            return
        }

        val discountCostInt = discountCost.toIntOrNull()
        if (discountCostInt != null && discountCostInt >= costInt) {
            _error.value = ErrorEvent.FieldError(
                key = PRODUCT_DISCOUNT_COST_ERROR_KEY,
                message = resourcesProvider.getString(R.string.error_discount_cost_incorrect)
            )
            return
        }

        if (productCode == ProductCode.COMBO && comboDescription.isEmpty()) {
            _error.value = ErrorEvent.FieldError(
                key = PRODUCT_COMBO_DESCRIPTION_ERROR_KEY,
                message = resourcesProvider.getString(R.string.error_combo_description_incorrect)
            )
            return
        }

        val nullableComboDescription = if (productCode == ProductCode.COMBO) {
            comboDescription
        } else {
            null
        }

        viewModelScope.launch(IO) {
            val photoByteArray = covertPhotoToByteArray(bitmap)
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
                _message.value = name + resourcesProvider.getString(R.string.msg_product_created)

                withContext(Main) {
                    goBack()
                }
            }
        }
    }

    private suspend fun covertPhotoToByteArray(bitmap: Bitmap): ByteArray {
        return withContext(Dispatchers.Default) {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.toByteArray()
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