package com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage

import android.graphics.Color
import com.bunbeauty.fooddeliveryadmin.R
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

private const val DEFAULT_X_RATIO = 1000
private const val DEFAULT_Y_RATIO = 667
private const val MIN_WIDTH = 1000
private const val MIN_HEIGHT = 667

object CropImageDefaults {

    fun options(): CropImageOptions {
        return CropImageOptions(
            imageSourceIncludeCamera = false,
            cropShape = CropImageView.CropShape.RECTANGLE,
            showProgressBar = false,
            autoZoomEnabled = false,
            aspectRatioX = DEFAULT_X_RATIO,
            aspectRatioY = DEFAULT_Y_RATIO,
            minCropResultWidth = MIN_WIDTH,
            minCropResultHeight = MIN_HEIGHT,
            cropMenuCropButtonIcon = R.drawable.ic_menu,
            fixAspectRatio = true,
            toolbarColor = Color.WHITE,
            activityBackgroundColor = Color.WHITE,
            activityMenuIconColor = Color.BLACK,
            activityMenuTextColor = Color.BLACK,
            toolbarBackButtonColor = Color.BLACK
        )
    }
}
