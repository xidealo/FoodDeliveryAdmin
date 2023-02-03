package com.bunbeauty.fooddeliveryadmin.screen.statistic

import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option

data class StatisticState(
    val selectedCafe: SelectedCafe? = null,
    val selectedTimeInterval: SelectedTimeInterval? = null,
    val statisticList: List<StatisticItemModel> = emptyList(),
    val eventList: List<Event> = emptyList(),
    val isLoading: Boolean = false
) {

    val selectedTimeIntervalCode: String
        get() = (selectedTimeInterval?.code ?: TimeIntervalCode.MONTH).toString()

    sealed class Event {
        class OpenCafeListEvent(val cafeList: List<Option>) : Event()
        class OpenTimeIntervalListEvent(val timeIntervalList: List<Option>) : Event()
        object OpenLoginEvent : Event()
        class ShowError(val retryAction: RetryAction) : Event()
    }

    operator fun plus(event: Event) = copy(eventList = eventList + event)
    operator fun minus(events: List<Event>) = copy(eventList = eventList - events.toSet())
}

enum class RetryAction {
    LOAD_CAFE_LIST,
    LOAD_STATISTIC,
}

data class SelectedCafe(
    val uuid: String?,
    val address: String
)

data class SelectedTimeInterval(
    val code: TimeIntervalCode,
    val name: String,
)

enum class TimeIntervalCode {
    DAY,
    WEEK,
    MONTH,
}
