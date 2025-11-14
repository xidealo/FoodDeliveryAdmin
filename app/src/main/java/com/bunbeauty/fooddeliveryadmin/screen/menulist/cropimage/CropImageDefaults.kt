package com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage

import android.graphics.Color
import com.bunbeauty.fooddeliveryadmin.R
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

object CropImageDefaults {
    private object MenuProductOptions {
        const val DEFAULT_X_RATIO = 1000
        const val DEFAULT_Y_RATIO = 667
        const val MIN_WIDTH = 1000
        const val MIN_HEIGHT = 667
    }

    private object AdditionOptions {
        const val DEFAULT_X_RATIO = 320
        const val DEFAULT_Y_RATIO = 320
        const val MIN_WIDTH = 320
        const val MIN_HEIGHT = 320
    }

    fun menuProductOptions(): CropImageOptions =
        CropImageOptions(
            imageSourceIncludeCamera = false,
            cropShape = CropImageView.CropShape.RECTANGLE,
            showProgressBar = false,
            autoZoomEnabled = false,
            aspectRatioX = MenuProductOptions.DEFAULT_X_RATIO,
            aspectRatioY = MenuProductOptions.DEFAULT_Y_RATIO,
            minCropResultWidth = MenuProductOptions.MIN_WIDTH,
            minCropResultHeight = MenuProductOptions.MIN_HEIGHT,
            cropMenuCropButtonIcon = R.drawable.ic_menu,
            fixAspectRatio = true,
            toolbarColor = Color.WHITE,
            activityBackgroundColor = Color.WHITE,
            activityMenuIconColor = Color.BLACK,
            activityMenuTextColor = Color.BLACK,
            toolbarBackButtonColor = Color.BLACK,
        )

    fun additionOptions(): CropImageOptions =
        CropImageOptions(
            imageSourceIncludeCamera = false,
            cropShape = CropImageView.CropShape.RECTANGLE,
            showProgressBar = false,
            autoZoomEnabled = false,
            aspectRatioX = AdditionOptions.DEFAULT_X_RATIO,
            aspectRatioY = AdditionOptions.DEFAULT_Y_RATIO,
            minCropResultWidth = AdditionOptions.MIN_WIDTH,
            minCropResultHeight = AdditionOptions.MIN_HEIGHT,
            cropMenuCropButtonIcon = R.drawable.ic_menu,
            fixAspectRatio = true,
            toolbarColor = Color.WHITE,
            activityBackgroundColor = Color.WHITE,
            activityMenuIconColor = Color.BLACK,
            activityMenuTextColor = Color.BLACK,
            toolbarBackButtonColor = Color.BLACK,
        )
}
