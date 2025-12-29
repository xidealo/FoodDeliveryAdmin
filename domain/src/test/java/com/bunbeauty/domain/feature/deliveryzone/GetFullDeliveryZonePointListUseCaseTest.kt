package com.bunbeauty.domain.feature.deliveryzone

import com.bunbeauty.domain.feature.mapzonedelivery.editinfodeliveryzone.GetFullDeliveryZonePointListUseCase
import com.bunbeauty.domain.model.cafe.DeliveryZone
import com.bunbeauty.domain.model.cafe.DeliveryZonePoint
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetFullDeliveryZonePointListUseCaseTest {
    private lateinit var getFullDeliveryZonePointListUseCase: GetFullDeliveryZonePointListUseCase

    @BeforeTest
    fun setup() {
        getFullDeliveryZonePointListUseCase = GetFullDeliveryZonePointListUseCase()
    }

    @Test
    fun `invoke should return same list when positions are empty`() {
        // Given
        val deliveryZone = DeliveryZone.mock.copy(deliveryZonePoint = emptyList())

        // When
        val result = getFullDeliveryZonePointListUseCase.invoke(deliveryZone)

        // Then
        assertEquals(emptyList(), result)
    }

    @Test
    fun `invoke should add first point to end when positions are not empty and first != last`() {
        // Given
        val point1 = DeliveryZonePoint(latitude = 55.7558, longitude = 37.6173)
        val point2 = DeliveryZonePoint(latitude = 55.7658, longitude = 37.6273)
        val point3 = DeliveryZonePoint(latitude = 55.7758, longitude = 37.6373)

        val originalPoints = listOf(point1, point2, point3)
        val deliveryZone = DeliveryZone.mock.copy(deliveryZonePoint = originalPoints)

        // When
        val result = getFullDeliveryZonePointListUseCase.invoke(deliveryZone)

        // Then
        val expectedPoints = listOf(point1, point2, point3, point1)
        assertEquals(expectedPoints, result)
        assertEquals(4, result.size)
        assertEquals(point1, result.first())
        assertEquals(point1, result.last())
    }

    @Test
    fun `invoke should return same list when positions have only one point`() {
        // Given
        val point = DeliveryZonePoint(latitude = 55.7558, longitude = 37.6173)
        val deliveryZone = DeliveryZone.mock.copy(deliveryZonePoint = listOf(point))

        // When
        val result = getFullDeliveryZonePointListUseCase.invoke(deliveryZone)

        // Then
        assertEquals(listOf(point), result)
        assertEquals(1, result.size)
    }

    @Test
    fun `invoke should return same list when first and last positions are equal`() {
        // Given
        val point1 = DeliveryZonePoint(latitude = 55.7558, longitude = 37.6173)
        val point2 = DeliveryZonePoint(latitude = 55.7658, longitude = 37.6273)
        val point3 = DeliveryZonePoint(latitude = 55.7558, longitude = 37.6173)

        val originalPoints = listOf(point1, point2, point3)
        val deliveryZone = DeliveryZone.mock.copy(deliveryZonePoint = originalPoints)

        // When
        val result = getFullDeliveryZonePointListUseCase.invoke(deliveryZone)

        // Then
        assertEquals(originalPoints, result)
        assertEquals(3, result.size)
        assertEquals(point1, result.first())
        assertEquals(point1, result.last())
    }

    @Test
    fun `invoke should add first point when multiple different points provided`() {
        // Given
        val points =
            listOf(
                DeliveryZonePoint(latitude = 1.0, longitude = 1.0),
                DeliveryZonePoint(latitude = 2.0, longitude = 2.0),
                DeliveryZonePoint(latitude = 3.0, longitude = 3.0),
                DeliveryZonePoint(latitude = 4.0, longitude = 4.0),
            )
        val deliveryZone = DeliveryZone.mock.copy(deliveryZonePoint = points)

        // When
        val result = getFullDeliveryZonePointListUseCase.invoke(deliveryZone)

        // Then
        val expected = points + points.first()
        assertEquals(expected, result)
    }

    @Test
    fun `invoke should work correctly with two different points`() {
        // Given
        val point1 = DeliveryZonePoint(latitude = 10.0, longitude = 20.0)
        val point2 = DeliveryZonePoint(latitude = 30.0, longitude = 40.0)
        val deliveryZone = DeliveryZone.mock.copy(deliveryZonePoint = listOf(point1, point2))

        // When
        val result = getFullDeliveryZonePointListUseCase.invoke(deliveryZone)

        // Then
        val expected = listOf(point1, point2, point1)
        assertEquals(expected, result)
    }
}
