package com.bunbeauty.domain.feature.settings

import com.bunbeauty.domain.exception.NotFoundWorkInfoException
import com.bunbeauty.domain.feature.profile.model.GetTypeWorkUseCase
import com.bunbeauty.domain.model.settings.WorkInfo
import com.bunbeauty.domain.repo.CompanyRepo
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

    private val workInfoRepository: CompanyRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()

    private lateinit var getTypeWorkUseCase: GetTypeWorkUseCase

    @Before
    fun setUp() {
        getTypeWorkUseCase = GetTypeWorkUseCase(
            workInfoRepository = workInfoRepository,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `invoke() should return WorkInfoData when companyUuid exists`() = runTest {
        // Given
        val companyUuidTest = "UID"
        val expectedWorkInfo = WorkInfo(WorkInfo.WorkType.DELIVERY)

        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuidTest)
        coEvery { workInfoRepository.getTypeWork(companyUuidTest) } returns expectedWorkInfo

        // When
        val result = getTypeWorkUseCase()

        // Then
        assertEquals(expectedWorkInfo, result)

        coVerify { dataStoreRepo.companyUuid }
        coVerify { workInfoRepository.getTypeWork(companyUuidTest) }
    }

    @Test
    fun `invoke() should throw NotFoundWorkInfoException when getTypeWork returns null`() =
        runTest {
            // Given
            val companyUuidTest = "123-UUID"

            coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuidTest)
            coEvery { workInfoRepository.getTypeWork(companyUuidTest) } returns null

            // When & Then
            val exception = assertThrows(NotFoundWorkInfoException::class.java) {
                runBlocking { getTypeWorkUseCase() }
            }
            assertNotNull(exception)

            coVerify { dataStoreRepo.companyUuid }
            coVerify { workInfoRepository.getTypeWork(companyUuidTest) }
        }
}
