package com.bunbeauty.domain.feature.settings

import com.bunbeauty.domain.exception.NoCafeException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.profile.model.UpdateWorkCafeUseCase
import com.bunbeauty.domain.model.cafe.UpdateCafe
import com.bunbeauty.domain.model.settings.WorkLoad
import com.bunbeauty.domain.model.settings.WorkType
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class UpdateWorkCafeUseCaseTest {

    private val cafeRepo: CafeRepo = mockk(relaxed = true)
    private val dataStoreRepo: DataStoreRepo = mockk()
    private lateinit var updateWorkCafeUseCase: UpdateWorkCafeUseCase

    @BeforeTest
    fun setup() {
        updateWorkCafeUseCase = UpdateWorkCafeUseCase(
            workLoadRepository = cafeRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `invoke() should call updateWorkCafe with correct parameters`() = runTest {
        // Given
        val testCafeUuid = "uuid-123"
        val testToken = "token-abc"
        val testWorkLoad = WorkLoad.HIGH
        val testWorkType = WorkType.DELIVERY
        val testKitchenAppliances = false

        coEvery { dataStoreRepo.cafeUuid } returns flowOf(testCafeUuid)
        coEvery { dataStoreRepo.getToken() } returns testToken

        // When
        updateWorkCafeUseCase(
            testWorkLoad,
            testWorkType,
            testKitchenAppliances
        )

        // Then
        coVerify {
            cafeRepo.patchCafe(
                updateCafe = UpdateCafe(
                    workload = testWorkLoad,
                    workType = testWorkType,
                    additionalUtensils = testKitchenAppliances

                ),
                cafeUuid = testCafeUuid,
                token = testToken
            )
        }
    }

    @Test
    fun `invoke() should throw NoCafeException when cafeUuid is missing`() = runTest {
        coEvery { dataStoreRepo.cafeUuid } returns emptyFlow()

        assertFailsWith<NoCafeException> {
            updateWorkCafeUseCase(WorkLoad.LOW, WorkType.CLOSED, isKitchenAppliances = false)
        }

        coVerify(exactly = 0) { cafeRepo.patchCafe(any(), any(), any()) }
    }

    @Test
    fun `invoke() should throw NoTokenException when token is null`() = runTest {
        val testCafeUuid = "uuid-123"
        coEvery { dataStoreRepo.cafeUuid } returns flowOf(testCafeUuid)
        coEvery { dataStoreRepo.getToken() } returns null

        assertFailsWith<NoTokenException> {
            updateWorkCafeUseCase(WorkLoad.AVERAGE, WorkType.PICKUP, isKitchenAppliances = true)
        }

        coVerify(exactly = 0) { cafeRepo.patchCafe(any(), any(), any()) }
    }
}
