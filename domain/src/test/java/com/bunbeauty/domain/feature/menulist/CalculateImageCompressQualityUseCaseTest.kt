package com.bunbeauty.domain.feature.menulist

import com.bunbeauty.domain.feature.menu.common.CalculateImageCompressQualityUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CalculateImageCompressQualityUseCaseTest {

    lateinit var calculateImageCompressQualityUseCase: CalculateImageCompressQualityUseCase

    @BeforeTest
    fun setup() {
        calculateImageCompressQualityUseCase = CalculateImageCompressQualityUseCase()
    }

    @Test
    fun `return half when file size twice more than 100 MB`() = runTest {
        val expected = 50

        val result = calculateImageCompressQualityUseCase(fileSize = 199)

        assertEquals(expected, result)
    }

}