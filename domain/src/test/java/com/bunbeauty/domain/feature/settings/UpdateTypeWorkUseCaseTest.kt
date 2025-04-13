package com.bunbeauty.domain.feature.settings

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.profile.model.UpdateTypeWorkUseCase
import com.bunbeauty.domain.model.settings.WorkType
import com.bunbeauty.domain.repo.CompanyRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Before
import kotlin.test.Test

class UpdateTypeWorkUseCaseTest {

    private val workInfoRepository: CompanyRepo = mockk()

    private val dataStoreRepo: DataStoreRepo = mockk()

    private lateinit var updateTypeWorkUseCase: UpdateTypeWorkUseCase

    @Before
    fun setUp() {
        updateTypeWorkUseCase =
            UpdateTypeWorkUseCase(
                workInfoRepository = workInfoRepository,
                dataStoreRepo = dataStoreRepo
            )
    }

    @Test
    fun `invoke() should call updateTypeWork with valid data`() = runTest {
        // Given
        val workType = WorkType.DELIVERY_AND_PICKUP
        val companyUuid = "UUID"
        val token = "Test token"

        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { dataStoreRepo.companyUuid } returns flowOf(companyUuid)
        coEvery {
            workInfoRepository.updateTypeWork(workType, companyUuid, token)
        } returns Unit

        // When
        updateTypeWorkUseCase(workType)

        // Then
        coVerify { workInfoRepository.updateTypeWork(workType, companyUuid, token) }
        coVerify { dataStoreRepo.getToken() }
        coVerify { dataStoreRepo.companyUuid }
    }

    @Test
    fun `invoke() should throw NoTokenException when token is null`() = runTest {
        // Given
        val workType = WorkType.DELIVERY_AND_PICKUP

        coEvery { dataStoreRepo.getToken() } returns null

        // When & Then
        val exception = assertThrows(NoTokenException::class.java) {
            runBlocking { updateTypeWorkUseCase(workType) }
        }
        assertNotNull(exception)

        coVerify { dataStoreRepo.getToken() }
        coVerify(exactly = 0) { workInfoRepository.updateTypeWork(any(), any(), any()) }
    }

    @Test
    fun `invoke should throw NoCompanyUuidException when companyUuid is null`() = runTest {
        // Given
        val workType = WorkType.DELIVERY_AND_PICKUP
        val token = "Test token"

        coEvery { dataStoreRepo.getToken() } returns token
        coEvery { dataStoreRepo.companyUuid } returns emptyFlow()
        // When & Then
        val exception = assertThrows(NoCompanyUuidException::class.java) {
            runBlocking { updateTypeWorkUseCase(workType) }
        }
        assertNotNull(exception)

        coVerify { dataStoreRepo.getToken() }
        coVerify { dataStoreRepo.companyUuid }
        coVerify(exactly = 0) { workInfoRepository.updateTypeWork(any(), any(), any()) }
    }
}
