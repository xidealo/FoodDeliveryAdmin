package com.bunbeauty.shared.feature.additionlist.createaddition

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.shared.designsystem.compose.TextFieldUi
import com.bunbeauty.shared.designsystem.compose.element.image.ImageData
import com.bunbeauty.shared.feature.image.ImageFieldData
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.error_create_addition_empty_name

@Immutable
data class CreateAdditionViewState(
    val nameField: TextFieldUi,
    val fullName: String,
    val price: String,
    val isVisible: Boolean,
    val isLoading: Boolean,
    val tag: String,
    val imageFieldUi: ImageFieldUi,
) : BaseViewState {
    @Immutable
    data class ImageFieldUi(
        val value: ImageData?,
        val isError: Boolean,
        val isSelected: Boolean,
    ) {
        companion object {
            val empty =
                ImageFieldUi(
                    value = null,
                    isError = false,
                    isSelected = false,
                )
        }
    }
}

@Composable
internal fun CreateAddition.DataState.toViewState(): CreateAdditionViewState =
    CreateAdditionViewState(
        nameField =
            TextFieldUi(
                value = name,
                isError = hasEditNameError,
                errorResId =
                    if (hasEditNameError) {
                        Res.string.error_create_addition_empty_name
                    } else {
                        null
                    },
            ),
        fullName = fullName,
        price = price,
        isVisible = isVisible,
        isLoading = isLoading,
        tag = tag,
        imageFieldUi = imageField.toImageFieldUi(),
    )

internal fun ImageFieldData.toImageFieldUi(): CreateAdditionViewState.ImageFieldUi =
    CreateAdditionViewState.ImageFieldUi(
        value = ImageData.HttpUrl(value.orEmpty()),
        isError = this.isError,
        isSelected = this.value != null,
    )
