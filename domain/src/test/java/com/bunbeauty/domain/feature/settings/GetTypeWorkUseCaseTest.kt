package com.bunbeauty.domain.feature.settings

import com.bunbeauty.domain.exception.NotFoundWorkInfoException
import com.bunbeauty.domain.feature.profile.model.GetTypeWorkUseCase
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.settings.WorkLoad
import com.bunbeauty.domain.model.settings.WorkType
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import kotlin.test.Test

class GetTypeWorkUseCaseTest {

    private val workInfoRepository: CafeRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()

    private lateinit var getTypeWorkUseCase: GetTypeWorkUseCase

    private val testCafe = Cafe(
        uuid = "uuid",
        address = "123",
        latitude = 0.0,
        longitude = 0.0,
        fromTime = 0,
        toTime = 0,
        offset = 1,
        phone = "123",
        visible = true,
        cityUuid = "cityUuid",
        workload = WorkLoad.AVERAGE,
        workType = WorkType.DELIVERY
    )

    @Before
    fun setUp() {
        getTypeWorkUseCase = GetTypeWorkUseCase(
            workInfoRepository = workInfoRepository,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `invoke() should return CafeData when cafeUuid exists`() = runTest {
        // Given
        val cafeUuidTest = "uuid"

        coEvery { dataStoreRepo.cafeUuid } returns flowOf(cafeUuidTest)
        coEvery { workInfoRepository.getCafeByUuid(cafeUuidTest) } returns testCafe

        // When
        val result = getTypeWorkUseCase()

        // Then
        assertEquals(testCafe, result)
        assertEquals(WorkType.DELIVERY, result.workType)
        assertEquals(WorkLoad.AVERAGE, result.workload)

        coVerify { dataStoreRepo.cafeUuid }
        coVerify { workInfoRepository.getCafeByUuid(cafeUuidTest) }
    }

    @Test
    fun `invoke() should throw NotFoundWorkInfoException when getTypeWork returns null`() =
        runTest {
            // Given
            val cafeUuidTest = "cafe-uuid"

            coEvery { dataStoreRepo.cafeUuid } returns flowOf(cafeUuidTest)
            coEvery { workInfoRepository.getCafeByUuid(cafeUuidTest) } returns null

            // When & Then
            val exception = assertThrows(NotFoundWorkInfoException::class.java) {
                runBlocking { getTypeWorkUseCase() }
            }
            assertNotNull(exception)

            coVerify { dataStoreRepo.cafeUuid }
            coVerify { workInfoRepository.getCafeByUuid(cafeUuidTest) }
        }
}
