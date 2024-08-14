package com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct.cropimage

import android.graphics.Color
import com.bunbeauty.fooddeliveryadmin.R
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

object CropImageDefaults {

    fun options(): CropImageOptions {
        return CropImageOptions(
            imageSourceIncludeCamera = false,
            cropShape = CropImageView.CropShape.RECTANGLE,
            autoZoomEnabled = false,
            aspectRatioX = 1000,
            aspectRatioY = 667,
            minCropResultWidth = 1000,
            minCropResultHeight = 667,
            cropMenuCropButtonIcon = R.drawable.ic_menu,
            fixAspectRatio = true,
            activityTitle = "Title",
            toolbarColor = Color.WHITE,
            activityBackgroundColor = Color.WHITE,
            activityMenuIconColor = Color.BLACK,
            activityMenuTextColor = Color.BLACK,
            toolbarBackButtonColor = Color.BLACK,
        )
    }

}