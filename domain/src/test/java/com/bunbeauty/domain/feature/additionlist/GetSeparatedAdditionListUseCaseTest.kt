package com.bunbeauty.domain.feature.additionlist

import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.util.GetNewUuidUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetSeparatedAdditionListUseCaseTest {

    private val dataStoreRepo: DataStoreRepo = mockk()
    private val additionRepo: AdditionRepo = mockk()
    private val getNewUuidUseCase: GetNewUuidUseCase = mockk()
    private lateinit var useCase: GetSeparatedAdditionListUseCase

    @BeforeTest
    fun setup() {
        useCase = GetSeparatedAdditionListUseCase(
            additionRepo = additionRepo,
            dataStoreRepo = dataStoreRepo,
            getNewUuidUseCase = getNewUuidUseCase
        )
    }

    @Test
    fun `return empty lists when additionRepo return empty list`() = runTest {
        // Given
        val token = "token"
        val expectedSeparatedAdditionList = SeparatedAdditionList(
            visibleList = emptyList(),
            hiddenList = emptyList()
        )
        coEvery { dataStoreRepo.getToken() } returns token
        coEvery {
            additionRepo.getAdditionList(
                token = token
            )
        } returns emptyList()

        // When
        val separatedAdditionList = useCase(refreshing = false)
        // Then
        assertEquals(expectedSeparatedAdditionList, separatedAdditionList)
    }

    @Test
    fun `return empty visible products when additionRepo return list with empty isVisible`() =
        runTest {
            // Given
            val uuid = "uuid"
            coEvery { getNewUuidUseCase() } returns uuid

            val token = "token"
            val expectedSeparatedAdditionList = SeparatedAdditionList(
                visibleList = emptyList(),
                hiddenList = listOf(
                    groupedAdditionList.copy(
                        uuid = uuid,
                        additionList = listOf(
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
                            )
                        )
                    )
                )
            )
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                additionRepo.getAdditionList(
                    token = token
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
                )
            )
            // When
            val separatedAdditionList = useCase(refreshing = false)
            // Then
            assertEquals(expectedSeparatedAdditionList, separatedAdditionList)
        }

    @Test
    fun `return empty hidden products when additionRepo return list with full isVisible`() =
        runTest {
            // Given
            val token = "token"
            val uuid = "uuid"
            coEvery { getNewUuidUseCase() } returns uuid

            val expectedSeparatedAdditionList = SeparatedAdditionList(
                visibleList = listOf(
                    groupedAdditionList.copy(
                        uuid = uuid,
                        additionList = listOf(
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
                            )
                        )
                    )
                ),
                hiddenList = emptyList()
            )

            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                additionRepo.getAdditionList(
                    token = token
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
                )
            )
            // When
            val separatedAdditionList = useCase(refreshing = false)
            // Then
            assertEquals(expectedSeparatedAdditionList, separatedAdditionList)
        }

    @Test
    fun `return 2 hidden 3 visible products when additionRepo has same data by visible`() =
        runTest {
            // Given
            val token = "token"
            val uuid = "uuid"
            coEvery { getNewUuidUseCase() } returns uuid

            val expectedSeparatedAdditionList = SeparatedAdditionList(
                visibleList = listOf(
                    groupedAdditionList.copy(
                        uuid = uuid,
                        additionList = listOf(
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
                            )
                        )
                    )
                ),
                hiddenList = listOf(
                    groupedAdditionList.copy(
                        uuid = uuid,
                        additionList = listOf(
                            additionMock.copy(
                                uuid = "uuid1",
                                isVisible = false
                            ),
                            additionMock.copy(
                                uuid = "uuid2",
                                isVisible = false
                            )
                        )
                    )
                )
            )
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                additionRepo.getAdditionList(
                    token = token
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
                )
            )
            // When
            val separatedAdditionList = useCase(refreshing = false)
            // Then
            assertEquals(expectedSeparatedAdditionList, separatedAdditionList)
        }

    @Test
    fun `return sorted by name started with A and finished with Z when additionRepo has not empty list`() =
        runTest {
            // Given
            val token = "token"
            val uuid = "uuid"
            coEvery { getNewUuidUseCase() } returns uuid

            val expectedSeparatedAdditionList = SeparatedAdditionList(
                visibleList = listOf(
                    groupedAdditionList.copy(
                        uuid = uuid,
                        additionList = listOf(
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
                            )
                        )
                    )
                ),
                hiddenList = emptyList()
            )
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                additionRepo.getAdditionList(
                    token = token
                )
            } returns listOf(
                additionMock.copy(
                    uuid = "uuid5",
                    name = "Z"
                ),
                additionMock.copy(
                    uuid = "uuid2",
                    name = "A"
                ),
                additionMock.copy(
                    uuid = "uuid4",
                    name = "C"
                ),
                additionMock.copy(
                    uuid = "uuid3",
                    name = "B"
                )
            )
            // When
            val separatedAdditionList = useCase(refreshing = false)
            // Then
            assertEquals(expectedSeparatedAdditionList, separatedAdditionList)
        }

    @Test
    fun `return sorted by title where null must be in the end when additionRepo has not empty list`() =
        runTest {
            // Given
            val token = "token"
            val uuid = "uuid"
            coEvery { getNewUuidUseCase() } returns uuid

            val expectedSeparatedAdditionList = SeparatedAdditionList(
                visibleList = listOf(
                    groupedAdditionList.copy(
                        uuid = uuid,
                        additionList = listOf(
                            additionMock.copy(
                                uuid = "uuid3",
                                name = "B",
                                tag = "sss"
                            )
                        ),
                        title = "sss"
                    ),
                    groupedAdditionList.copy(
                        uuid = uuid,
                        additionList = listOf(
                            additionMock.copy(
                                uuid = "uuid2",
                                name = "A"
                            ),

                            additionMock.copy(
                                uuid = "uuid4",
                                name = "C"
                            ),
                            additionMock.copy(
                                uuid = "uuid5",
                                name = "Z"
                            )
                        )
                    )
                ),
                hiddenList = emptyList()
            )
            coEvery { dataStoreRepo.getToken() } returns token
            coEvery {
                additionRepo.getAdditionList(
                    token = token
                )
            } returns listOf(
                additionMock.copy(
                    uuid = "uuid5",
                    name = "Z"
                ),
                additionMock.copy(
                    uuid = "uuid2",
                    name = "A"
                ),
                additionMock.copy(
                    uuid = "uuid4",
                    name = "C"
                ),
                additionMock.copy(
                    uuid = "uuid3",
                    name = "B",
                    tag = "sss"
                )
            )
            // When
            val separatedAdditionList = useCase(refreshing = false)
            // Then
            assertEquals(expectedSeparatedAdditionList, separatedAdditionList)
        }

    private val groupedAdditionList = GroupedAdditionList(
        uuid = "",
        title = null,
        additionList = listOf()
    )

    private val additionMock = Addition(
        uuid = "productUuidMock",
        name = "name",
        photoLink = "photoLink",
        isVisible = true,
        priority = 0,
        fullName = null,
        price = null,
        tag = null
    )
}
