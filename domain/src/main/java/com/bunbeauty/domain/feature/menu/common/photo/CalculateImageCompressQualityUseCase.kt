package com.bunbeauty.domain.feature.menu.common.photo

import javax.inject.Inject

private const val REQUIRED_IMAGE_SIZE = 100.0

class CalculateImageCompressQualityUseCase @Inject constructor() {

    operator fun invoke(fileSize: Long): Int {
        return fileSize.takeIf { originalSize ->
            originalSize > 0
        }?.let { originalSize ->
            ((REQUIRED_IMAGE_SIZE / originalSize) * 100).toInt()
        }?.coerceIn(10..100) ?: 100
    }
}