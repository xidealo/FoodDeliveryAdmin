package com.bunbeauty.presentation.feature.statistic

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.common.GetCafeListUseCase
import com.bunbeauty.domain.feature.statistic.GetCafeByUuidUseCase
import com.bunbeauty.domain.feature.statistic.GetCafeListByCityUuidUseCase
import com.bunbeauty.domain.usecase.GetStatisticUseCase
import com.bunbeauty.domain.util.datetime.DateTimeUtil
import com.bunbeauty.domain.util.datetime.PATTERN_DD_MMMM_YYYY
import com.bunbeauty.domain.util.datetime.PATTERN_MMMM
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val getCafeListUseCase: GetCafeListUseCase,
    private val getCafeListByCityUuidUseCase: GetCafeListByCityUuidUseCase,
    private val getCafeByUuidUseCase: GetCafeByUuidUseCase,
    private val dateTimeUtil: DateTimeUtil,
    private val getStatisticUseCase: GetStatisticUseCase
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

            Statistic.Action.SelectTimeIntervalClick -> {
                onTimeIntervalClicked()
            }

            Statistic.Action.SelectGoBackClick -> {
                onGoBackClicked()
            }
        }
    }

    private fun onCafeClicked() {
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

    private fun onGoBackClicked() {
        addEvent {
            Statistic.Event.GoBack
        }
    }

    private fun onTimeIntervalClicked() {
        addEvent {
            Statistic.Event.OpenTimeIntervalListEvent(Statistic.TimeIntervalCode.entries)
        }
    }

    fun onTimeIntervalSelected(timeInterval: String) {
        val timeIntervalCode = Statistic.TimeIntervalCode.valueOf(timeInterval)
        setState {
            copy(
                selectedTimeInterval = timeIntervalCode
            )
        }
    }

    private fun loadStatistic(
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
                        date = when (period) {
                            Statistic.TimeIntervalCode.DAY -> dateTimeUtil.formatDateTime(
                                statistic.startPeriodTime,
                                PATTERN_DD_MMMM_YYYY
                            )

                            Statistic.TimeIntervalCode.MONTH -> dateTimeUtil.formatDateTime(
                                statistic.startPeriodTime,
                                PATTERN_MMMM
                            )

                            Statistic.TimeIntervalCode.WEEK -> dateTimeUtil.getWeekPeriod(statistic.startPeriodTime)
                        }

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
