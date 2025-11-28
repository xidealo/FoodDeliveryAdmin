package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.additionlist.GetSeparatedAdditionListUseCase
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.util.GetNewUuidUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetSeparatedAdditionListUseCaseTest {
    private val additionRepo: AdditionRepo = mockk()
    private val dataStoreRepo: DataStoreRepo = mockk()
    private val getNewUuidUseCase: GetNewUuidUseCase = mockk()
    private lateinit var getSeparatedAdditionListUseCase: GetSeparatedAdditionListUseCase

    @BeforeTest
    fun setup() {
        getSeparatedAdditionListUseCase =
            GetSeparatedAdditionListUseCase(
                additionRepo = additionRepo,
                dataStoreRepo = dataStoreRepo,
                getNewUuidUseCase = getNewUuidUseCase,
            )
    }

    @Test
    fun `return separated lists when additions exist`() =
        runTest {
            // Given
            val token = "token"
            val visibleAddition1 = addition.copy(uuid = "uuid-1", name = "name-A", isVisible = true, tag = "Tag1")
            val visibleAddition2 = addition.copy(uuid = "uuid-2", name = "name-B", isVisible = true, tag = null)
            val hiddenAddition = addition.copy(uuid = "uuid-3", name = "name-C", isVisible = false, tag = "Tag2")

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { additionRepo.getAdditionList(token, refreshing = true) } returns
                listOf(
                    visibleAddition1,
                    visibleAddition2,
                    hiddenAddition,
                )
            coEvery { getNewUuidUseCase() } returnsMany listOf("uuid1", "uuid2", "uuid3")

            // When
            val result = getSeparatedAdditionListUseCase(refreshing = true)

            // Then
            assertEquals(2, result.visibleList.size)
            assertEquals(1, result.hiddenList.size)
            assertTrue(result.visibleList.any { additionList -> additionList.title == "Tag1" })
            assertTrue(result.hiddenList.any { additionList -> additionList.title == "Tag2" })

            coVerify { dataStoreRepo.getToken() }
            coVerify { additionRepo.getAdditionList(token, refreshing = true) }
            confirmVerified(dataStoreRepo, additionRepo)
        }

    @Test
    fun `throw NoTokenException when token is null`() =
        runTest {
            // Given
            coEvery { dataStoreRepo.getToken() } returns null

            // When & Then
            assertFailsWith<NoTokenException> {
                getSeparatedAdditionListUseCase(refreshing = false)
            }

            coVerify { dataStoreRepo.getToken() }
            confirmVerified(dataStoreRepo)
        }

    @Test
    fun `sort additions inside grouped list by name`() =
        runTest {
            // Given
            val token = "token"
            val additionB = addition.copy(uuid = "uuid-1", name = "name-B", isVisible = true, tag = "Tag")
            val additionA = addition.copy(uuid = "uuid-2", name = "name-A", isVisible = true, tag = "Tag")

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery { additionRepo.getAdditionList(token, refreshing = false) } returns listOf(additionB, additionA)
            coEvery { getNewUuidUseCase() } returns "uuid1"

            // When
            val result = getSeparatedAdditionListUseCase(refreshing = false)

            // Then
            val grouped = result.visibleList.first()
            assertEquals(listOf("name-A", "name-B"), grouped.additionList.map { it.name })
        }

    private val addition =
        Addition(
            uuid = "uuid",
            name = "name",
            priority = 1,
            fullName = null,
            price = 1,
            photoLink = "photo",
            isVisible = true,
            tag = null,
        )
}
