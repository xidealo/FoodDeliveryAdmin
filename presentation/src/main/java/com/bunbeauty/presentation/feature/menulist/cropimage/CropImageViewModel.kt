package com.bunbeauty.presentation.feature.menulist.cropimage

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.net.toUri
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val REQUIRED_IMAGE_SIZE = 100.0

@HiltViewModel
class CropImageViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseStateViewModel<CropImage.DataState, CropImage.Action, CropImage.Event>(
    initState = CropImage.DataState(
        isLoading = false,
        uri = null,
    )
) {

    override fun reduce(action: CropImage.Action, dataState: CropImage.DataState) {
        when (action) {
            is CropImage.Action.SetImageUrl -> setState {
                copy(uri = action.uri)
            }

            is CropImage.Action.SaveClick -> {
                setState { copy(isLoading = true) }

                val compressQuality = dataState.uri?.toUri()?.fileSize()?.takeIf { originalSize ->
                    originalSize > 0
                }?.let { originalSize ->
                    ((REQUIRED_IMAGE_SIZE / originalSize) * 100).toInt()
                } ?: 100

                sendEvent {
                    CropImage.Event.CropImage(compressQuality = compressQuality)
                }
            }
        }
    }

    private fun Uri.fileSize(): Long {
        var fileSize: Long = 0

        context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst()) {
                fileSize = cursor.getLong(sizeIndex)
            }
        }

        return fileSize / 1000
    }


}