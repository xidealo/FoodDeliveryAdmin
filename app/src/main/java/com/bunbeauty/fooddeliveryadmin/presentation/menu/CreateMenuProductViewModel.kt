package com.bunbeauty.fooddeliveryadmin.presentation.menu

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.LIST_ARGS_KEY
import com.bunbeauty.common.Constants.PRODUCT_CODE_REQUEST_KEY
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
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
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

    private val menuProductCodeList: Array<MenuProductCode>
        get() = ProductCode.values().map { productCode ->
            MenuProductCode(title = stringUtil.getProductCodeString(productCode))
        }.toTypedArray()

    private var isVisible = true
    private val _visibilityIcon =
        MutableStateFlow(resourcesProvider.getDrawable(R.drawable.ic_visible))
    val visibilityIcon: StateFlow<Drawable?>
        get() = _visibilityIcon.asStateFlow()

    private val _isComboDescriptionVisible = MutableStateFlow(false)
    val isComboDescriptionVisible: StateFlow<Boolean>
        get() = _isComboDescriptionVisible.asStateFlow()

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
        productCode: String,
        cost: String,
        discountCost: String,
        weight: String,
        description: String,
        comboDescription: String
    ) {
        viewModelScope.launch(Default) {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val photoByteArray = outputStream.toByteArray()

            withContext(IO) {
                menuProductRepo.saveMenuProductPhoto(photoByteArray).collect { photoLink ->
                    val uuid = UUID.randomUUID().toString()
                    val menuProduct = MenuProduct(
                        uuid = uuid,
                        name = name,
                        cost = cost.toInt(),
                        discountCost = discountCost.toInt(),
                        weight = weight.toInt(),
                        description = description,
                        comboDescription = comboDescription,
                        photoLink = photoLink,
                        onFire = false,
                        inOven = false,
                        productCode = stringUtil.getProductCode(productCode),
                        barcode = null,
                        visible = isVisible
                    )
                    menuProductRepo.saveMenuProduct(menuProduct)

                    withContext(Main) {
                        goBack()
                    }
                }
            }
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