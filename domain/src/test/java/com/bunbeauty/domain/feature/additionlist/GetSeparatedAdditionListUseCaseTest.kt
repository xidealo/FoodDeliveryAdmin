package com.bunbeauty.domain.feature.additionlist

import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetSeparatedAdditionListUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private val additionRepo: AdditionRepo = mockk()
    private lateinit var useCase: GetSeparatedAdditionListUseCase

    @BeforeTest
    fun setup() {
        useCase = GetSeparatedAdditionListUseCase(
            additionRepo = additionRepo,
            dataStoreRepo = dataStoreRepo,
        )
    }

    @Test
    fun `return empty lists when additionRepo return empty list`() = runTest {
        // Given
        val token = "token"
        val isRefreshing = true
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery {
            additionRepo.getAdditionList(
                token = token,
                takeRemote = isRefreshing
            )
        } returns emptyList()

        // When
        val separatedMenuProductList = useCase()
        // Then
        assertTrue(separatedMenuProductList.visibleList.isEmpty())
        assertTrue(separatedMenuProductList.hiddenList.isEmpty())
    }

    @Test
    fun `return empty visible products when additionRepo return list with empty isVisible`() =
        runTest {
            // Given
            val token = "token"
            val isRefreshing = true
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                additionRepo.getAdditionList(
                    token = token,
                    takeRemote = isRefreshing
                )
            } returns listOf(
                additionMock.copy(
                    uuid = "uuid1",
                    isVisible = false
                ),
                additionMock.copy(
                    uuid = "uuid2",
                    isVisible = false
                ),
                additionMock.copy(
                    uuid = "uuid3",
                    isVisible = false
                ),
                additionMock.copy(
                    uuid = "uuid4",
                    isVisible = false
                ),
            )
            // When
            val separatedMenuProductList = useCase()
            // Then
            assertTrue(separatedMenuProductList.visibleList.isEmpty())
            assertTrue(separatedMenuProductList.hiddenList.isNotEmpty())
        }

        @Test
    fun `return empty hidden products when additionRepo return list with full isVisible`() =
        runTest {
            // Given
            val token = "token"
            val isRefreshing = true
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                additionRepo.getAdditionList(
                    token = token,
                    takeRemote = isRefreshing
                )
            } returns listOf(
                additionMock.copy(
                    uuid = "uuid1",
                    isVisible = true
                ),
                additionMock.copy(
                    uuid = "uuid2",
                    isVisible = true
                ),
                additionMock.copy(
                    uuid = "uuid3",
                    isVisible = true
                ),
                additionMock.copy(
                    uuid = "uuid4",
                    isVisible = true
                ),
            )
            // When
            val separatedMenuProductList = useCase()
            // Then
            assertTrue(separatedMenuProductList.visibleList.isNotEmpty())
            assertTrue(separatedMenuProductList.hiddenList.isEmpty())
        }

    @Test
    fun `return 2 hidden 3 visible products when additionRepo has same data by visible`() =
        runTest {
            // Given
            val token = "token"
            val isRefreshing = true
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                additionRepo.getAdditionList(
                    token = token,
                    takeRemote = isRefreshing
                )
            } returns listOf(
                additionMock.copy(
                    uuid = "uuid1",
                    isVisible = false
                ),
                additionMock.copy(
                    uuid = "uuid2",
                    isVisible = false
                ),
                additionMock.copy(
                    uuid = "uuid3",
                    isVisible = true
                ),
                additionMock.copy(
                    uuid = "uuid4",
                    isVisible = true
                ),
                additionMock.copy(
                    uuid = "uuid5",
                    isVisible = true
                ),
            )
            // When
            val separatedMenuProductList = useCase()
            // Then
            assertTrue(separatedMenuProductList.visibleList.size == 3)
            assertTrue(separatedMenuProductList.hiddenList.size == 2)
        }


    @Test
    fun `return sorted by name started with A and finished with Z when additionRepo has not empty list`() =
        runTest {
            // Given
            val token = "token"
            val isRefreshing = true
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                additionRepo.getAdditionList(
                    token = token,
                    takeRemote = isRefreshing
                )
            } returns listOf(
                additionMock.copy(
                    uuid = "uuid2",
                    name = "A"
                ),
                additionMock.copy(
                    uuid = "uuid3",
                    name = "B"
                ),
                additionMock.copy(
                    uuid = "uuid4",
                    name = "C"
                ),
                additionMock.copy(
                    uuid = "uuid5",
                    name = "Z"
                ),
            )
            // When
            val separatedMenuProductList = useCase()
            // Then
            assertEquals(separatedMenuProductList.visibleList.first().name, "A")
            assertEquals(separatedMenuProductList.visibleList.last().name, "Z")
        }

    private val additionMock = Addition(
        uuid = "productUuidMock",
        name = "name",
        photoLink = "photoLink",
        isVisible = true,
        priority = 0,
        fullName = null,
        price = null,
    )
}