package com.bunbeauty.domain.feature.additiongrouplist

import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSeparatedAdditionGroupListUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private val additionGroupRepo: AdditionGroupRepo = mockk()
    private lateinit var useCase: GetSeparatedAdditionGroupListUseCase

    @BeforeTest
    fun setup() {
        useCase = GetSeparatedAdditionGroupListUseCase(
            additionGroupRepo = additionGroupRepo,
            dataStoreRepo = dataStoreRepo
        )
    }

    @Test
    fun `should return empty lists when additionRepo return empty list`() = runTest {
        // Given
        val token = "token"
        val isRefreshing = true
        val expectedSeparatedAdditionGroupList = SeparatedAdditionGroupList(
            visibleList = emptyList(),
            hiddenList = emptyList()
        )
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery {
            additionGroupRepo.getAdditionGroupList(
                token = token,
                refreshing = isRefreshing
            )
        } returns emptyList()

        // When
        val separatedAdditionGroupList = useCase(refreshing = isRefreshing)
        // Then
        assertEquals(expectedSeparatedAdditionGroupList, separatedAdditionGroupList)
    }

    @Test
    fun `should return empty visible products when additionGroupRepo return list with empty isVisible`() =
        runTest {
            // Given
            val token = "token"
            val isRefreshing = true
            val expectedSeparatedAdditionGroupList = SeparatedAdditionGroupList(
                visibleList = emptyList(),
                hiddenList = listOf(
                    additionGroupMock.copy(
                        uuid = "uuid1",
                        isVisible = false
                    ),
                    additionGroupMock.copy(
                        uuid = "uuid2",
                        isVisible = false
                    ),
                    additionGroupMock.copy(
                        uuid = "uuid3",
                        isVisible = false
                    ),
                    additionGroupMock.copy(
                        uuid = "uuid4",
                        isVisible = false
                    )
                )
            )
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                additionGroupRepo.getAdditionGroupList(
                    token = token,
                    refreshing = isRefreshing
                )
            } returns listOf(
                additionGroupMock.copy(
                    uuid = "uuid1",
                    isVisible = false
                ),
                additionGroupMock.copy(
                    uuid = "uuid2",
                    isVisible = false
                ),
                additionGroupMock.copy(
                    uuid = "uuid3",
                    isVisible = false
                ),
                additionGroupMock.copy(
                    uuid = "uuid4",
                    isVisible = false
                )
            )
            // When
            val separatedAdditionGroupList = useCase(refreshing = isRefreshing)
            // Then
            assertEquals(expectedSeparatedAdditionGroupList, separatedAdditionGroupList)
        }

    @Test
    fun `should return empty hidden products when additionGroupRepo return list with full isVisible`() =
        runTest {
            // Given
            val token = "token"
            val isRefreshing = true

            val expectedSeparatedAdditionGroupList = SeparatedAdditionGroupList(
                visibleList = listOf(
                    additionGroupMock.copy(
                        uuid = "uuid1",
                        isVisible = true
                    ),
                    additionGroupMock.copy(
                        uuid = "uuid2",
                        isVisible = true
                    ),
                    additionGroupMock.copy(
                        uuid = "uuid3",
                        isVisible = true
                    ),
                    additionGroupMock.copy(
                        uuid = "uuid4",
                        isVisible = true
                    )
                ),
                hiddenList = emptyList()
            )

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                additionGroupRepo.getAdditionGroupList(
                    token = token,
                    refreshing = isRefreshing
                )
            } returns listOf(
                additionGroupMock.copy(
                    uuid = "uuid1",
                    isVisible = true
                ),
                additionGroupMock.copy(
                    uuid = "uuid2",
                    isVisible = true
                ),
                additionGroupMock.copy(
                    uuid = "uuid3",
                    isVisible = true
                ),
                additionGroupMock.copy(
                    uuid = "uuid4",
                    isVisible = true
                )
            )
            // When
            val separatedAdditionGroupList = useCase(refreshing = isRefreshing)
            // Then
            assertEquals(expectedSeparatedAdditionGroupList, separatedAdditionGroupList)
        }

    @Test
    fun `should return 2 hidden 3 visible products when additionGroupRepo has same data by visible`() =
        runTest {
            // Given
            val token = "token"
            val isRefreshing = true

            val expectedSeparatedAdditionGroupList = SeparatedAdditionGroupList(
                visibleList = listOf(
                    additionGroupMock.copy(
                        uuid = "uuid3",
                        isVisible = true
                    ),
                    additionGroupMock.copy(
                        uuid = "uuid4",
                        isVisible = true
                    ),
                    additionGroupMock.copy(
                        uuid = "uuid5",
                        isVisible = true
                    )
                ),
                hiddenList = listOf(
                    additionGroupMock.copy(
                        uuid = "uuid1",
                        isVisible = false
                    ),
                    additionGroupMock.copy(
                        uuid = "uuid2",
                        isVisible = false
                    )
                )
            )

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                additionGroupRepo.getAdditionGroupList(
                    token = token,
                    refreshing = isRefreshing
                )
            } returns listOf(
                additionGroupMock.copy(
                    uuid = "uuid1",
                    isVisible = false
                ),
                additionGroupMock.copy(
                    uuid = "uuid2",
                    isVisible = false
                ),
                additionGroupMock.copy(
                    uuid = "uuid3",
                    isVisible = true
                ),
                additionGroupMock.copy(
                    uuid = "uuid4",
                    isVisible = true
                ),
                additionGroupMock.copy(
                    uuid = "uuid5",
                    isVisible = true
                )
            )
            // When
            val separatedAdditionGroupList = useCase(refreshing = isRefreshing)
            // Then
            assertEquals(expectedSeparatedAdditionGroupList, separatedAdditionGroupList)
        }

    @Test
    fun `should return sorted by name started with A and finished with Z when additionGroupRepo has not empty list`() =
        runTest {
            // Given
            val token = "token"
            val isRefreshing = true

            val expectedSeparatedAdditionGroupList = SeparatedAdditionGroupList(
                visibleList = listOf(
                    additionGroupMock.copy(
                        uuid = "uuid2",
                        name = "A"
                    ),
                    additionGroupMock.copy(
                        uuid = "uuid3",
                        name = "B"
                    ),
                    additionGroupMock.copy(
                        uuid = "uuid4",
                        name = "C"
                    ),
                    additionGroupMock.copy(
                        uuid = "uuid5",
                        name = "Z"
                    )
                ),
                hiddenList = listOf()
            )

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                additionGroupRepo.getAdditionGroupList(
                    token = token,
                    refreshing = isRefreshing
                )
            } returns listOf(
                additionGroupMock.copy(
                    uuid = "uuid2",
                    name = "A"
                ),
                additionGroupMock.copy(
                    uuid = "uuid4",
                    name = "C"
                ),
                additionGroupMock.copy(
                    uuid = "uuid3",
                    name = "B"
                ),
                additionGroupMock.copy(
                    uuid = "uuid5",
                    name = "Z"
                )
            )
            // When
            val separatedAdditionGroupList = useCase(refreshing = isRefreshing)
            // Then
            assertEquals(expectedSeparatedAdditionGroupList, separatedAdditionGroupList)
        }

    private val additionGroupMock = AdditionGroup(
        uuid = "productUuidMock",
        name = "name",
        isVisible = true,
        priority = 0,
        singleChoice = false
    )
}
