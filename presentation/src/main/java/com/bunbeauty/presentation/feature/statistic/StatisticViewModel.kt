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
) : BaseStateViewModel<Statistic.DataState, Statistic.Action, Statistic.Event>(
    initState = Statistic.DataState(
        cafeUuid = null
    )
) {

    override fun reduce(action: Statistic.Action, dataState: Statistic.DataState) {
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
            onError = {
                setState {
                    copy(
                        hasError = true
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
            onError = {
                setState {
                    copy(
                        hasError = true
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
                    Statistic.DataState.StatisticItemModel(
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
                            hasError = false
                        )
                    }
                }
            },
            onError = {
                setState {
                    copy(
                        hasError = true,
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
            onError = {
                setState {
                    copy(
                        hasError = true
                    )
                }
            }
        )
    }
}
