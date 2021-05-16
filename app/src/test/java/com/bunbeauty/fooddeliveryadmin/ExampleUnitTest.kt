package com.bunbeauty.fooddeliveryadmin

import com.bunbeauty.common.State
import org.joda.time.DateTime
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun statisticToStringCorrect() {

        assertEquals(4, 2 + 2)
    }

    @Test
    fun isEqual() {

        assertEquals(State.Success(listOf(1)), State.Success(listOf(1)))
    }

    @Test
    fun isDateCorrect() {
        val dateTime = DateTime().withMonthOfYear(1).toString("dd MMMM YYYY")
        assertEquals("", dateTime)
    }
}