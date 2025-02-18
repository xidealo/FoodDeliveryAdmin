package com.bunbeauty.domain.feature.time

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class GetCurrentTimeFlowUseCaseTest {

    private val timeService: TimeService = mockk()
    private lateinit var getCurrentTimeFlow: GetCurrentTimeFlowUseCase
    private val timeZone = 3
    private val currentTime = Time(10, 30, 59)

    @BeforeTest
    fun setup() {
        getCurrentTimeFlow = GetCurrentTimeFlowUseCase(
            timeService = timeService
        )
    }

    @Test
    fun `return first current time`() = runTest {
        // Given
        coEvery { timeService.getCurrentTime(timeZone) } returns currentTime

        // When
        val result = getCurrentTimeFlow(timeZone, 60)

        // Then
        assertEquals(currentTime, result.first())
    }

    @Test
    fun `call TimeService exactly 3 times`() = runTest {
        // Given
        val count = 3
        coEvery { timeService.getCurrentTime(timeZone) } returns currentTime

        // When
        val results = getCurrentTimeFlow(timeZone, 60).take(count).toList()

        // Then
        coVerify(exactly = count) { timeService.getCurrentTime(timeZone) }
        assertEquals(count, results.size)
    }
}
