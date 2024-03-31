package com.bunbeauty.presentation.feature.statistic

import android.annotation.SuppressLint
import android.icu.util.Calendar
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.common.GetCafeListUseCase
import com.bunbeauty.domain.feature.statistic.GetCafeByUuidUseCase
import com.bunbeauty.domain.feature.statistic.GetCafeListByCityUuidUseCase
import com.bunbeauty.domain.usecase.GetStatisticUseCase
import com.bunbeauty.domain.util.datetime.DateTimeUtil
import com.bunbeauty.presentation.Option
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val getCafeListUseCase: GetCafeListUseCase,
    private val getCafeListByCityUuidUseCase: GetCafeListByCityUuidUseCase,
    private val getCafeByUuidUseCase: GetCafeByUuidUseCase,
    private val dateTimeUtil: DateTimeUtil,
    private val getStatisticUseCase: GetStatisticUseCase,
) : BaseStateViewModel<Statistic.ViewDataState, Statistic.Action, Statistic.Event>(
    initState = Statistic.ViewDataState(
        cafeUuid = null
    )
) {

    override fun reduce(action: Statistic.Action, dataState: Statistic.ViewDataState) {
        when (action) {
            is Statistic.Action.Init -> {
                setState {
                    copy(
                        selectedCafe = null,
                        selectedTimeInterval = Statistic.TimeIntervalCode.MONTH
                    )
                }
                updateData()
            }

            Statistic.Action.LoadStatisticClick -> {
                loadStatistic(
                    cafeUuid = dataState.selectedCafe?.uuid,
                    period = dataState.selectedTimeInterval
                )
            }

            Statistic.Action.SelectCafeClick -> {
                onCafeClicked()
            }
        }
    }

    fun onCafeClicked() {
        viewModelScope.launchSafe(
            block = {
                addEvent {
                    Statistic.Event.OpenCafeListEvent(
                        getCafeListByCityUuidUseCase()
                    )
                }
            },
            onError = { throwable ->
                setState {
                    copy(
                        error = throwable
                    )
                }
            }
        )
    }

    fun onCafeSelected(cafeUuid: String?) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(isLoading = true)
                }

                val selectedCafe = cafeUuid?.let { cafeUuid ->
                    getCafeByUuidUseCase(cafeUuid)
                }?.let { cafe ->
                    Statistic.SelectedCafe(
                        uuid = cafe.uuid,
                        address = cafe.address
                    )
                }

                setState {
                    copy(
                        selectedCafe = selectedCafe,
                        isLoading = false
                    )
                }
            },
            onError = { throwable ->
                setState {
                    copy(
                        error = throwable
                    )
                }
            }
        )
    }

    fun onTimeIntervalClicked() {
        viewModelScope.launchSafe(
            block = {
                val timeIntervalList = Statistic.TimeIntervalCode.entries.map { timeInterval ->
                    Option(
                        id = timeInterval.name,
                        title = timeInterval.name
                    )
                }
                addEvent {
                    Statistic.Event.OpenTimeIntervalListEvent(timeIntervalList)
                }
            },
            onError = { throwable ->
                setState {
                    copy(
                        error = throwable
                    )
                }
            }
        )
    }

    fun onTimeIntervalSelected(timeInterval: String) {
        val timeIntervalCode = Statistic.TimeIntervalCode.valueOf(timeInterval)
        setState {
            copy(
                selectedTimeInterval = timeIntervalCode,
            )
        }
    }

    @SuppressLint("StringFormatMatches")
    fun loadStatistic(
        cafeUuid: String?,
        period: Statistic.TimeIntervalCode
    ) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(isLoading = true)
                }
                getStatisticUseCase(
                    cafeUuid = cafeUuid,
                    period = period.name
                ).map { statistic ->
                    Statistic.ViewDataState.StatisticItemModel(
                        startMillis = statistic.startPeriodTime,
                        period = statistic.period,
                        count = statistic.orderCount,
                        proceeds = "${statistic.proceeds} ${statistic.currency}",
                        date = dateTimeUtil.formatDateTime(statistic.startPeriodTime, "MMMM")
                    )
                }.let { statisticItemList ->
                    setState {
                        copy(
                            statisticList = statisticItemList,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            },
            onError = { throwable ->
                setState {
                    copy(
                        error = throwable,
                        isLoading = false
                    )
                }
            }
        )
    }

    private fun updateData() {
        viewModelScope.launchSafe(
            block = {
                getCafeListUseCase()
            },
            onError = { throwable ->
                setState {
                    copy(
                        error = throwable
                    )
                }
            }
        )
    }
}
