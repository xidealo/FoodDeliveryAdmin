package com.bunbeauty.presentation.feature.statistic

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.common.GetCafeUseCase
import com.bunbeauty.domain.feature.statistic.GetCafeByUuidUseCase
import com.bunbeauty.domain.usecase.GetStatisticUseCase
import com.bunbeauty.domain.util.datetime.DateTimeUtil
import com.bunbeauty.domain.util.datetime.PATTERN_DD_MMMM_YYYY
import com.bunbeauty.domain.util.datetime.PATTERN_MMMM
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class StatisticViewModel(
    private val getCafeUseCase: GetCafeUseCase,
    private val getCafeByUuidUseCase: GetCafeByUuidUseCase,
    private val dateTimeUtil: DateTimeUtil,
    private val getStatisticUseCase: GetStatisticUseCase
) : BaseStateViewModel<Statistic.DataState, Statistic.Action, Statistic.Event>(
    initState = Statistic.DataState(
        cafeUuid = null,
        loadingStatistic = false
    )
) {

    override fun reduce(action: Statistic.Action, dataState: Statistic.DataState) {
        when (action) {
            is Statistic.Action.Init -> {
                setState {
                    copy(
                        selectedCafe = null,
                        selectedTimeInterval = TimeIntervalCode.MONTH
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

            Statistic.Action.CloseTimeIntervalListBottomSheet -> closeTimeIntervalListBottomSheet()

            is Statistic.Action.SelectedTimeInterval -> onTimeIntervalSelected(
                timeInterval = action.timeInterval
            )

            is Statistic.Action.SelectedCafe -> onCafeSelected(cafeUuid = action.cafeUuid)
            Statistic.Action.CloseCafeListBottomSheet -> closeCafeListBottomSheet()
        }
    }

    private fun onCafeClicked() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        isCafeListShown = true
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

    private fun onCafeSelected(cafeUuid: String?) {
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
                        isLoading = false,
                        isCafeListShown = false
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
        sendEvent {
            Statistic.Event.GoBack
        }
    }

    private fun onTimeIntervalClicked() {
        setState {
            copy(
                isTimeIntervalListShown = true
            )
        }
    }

    private fun closeTimeIntervalListBottomSheet() {
        setState {
            copy(
                isTimeIntervalListShown = false
            )
        }
    }

    private fun closeCafeListBottomSheet() {
        setState {
            copy(
                isCafeListShown = false
            )
        }
    }

    private fun onTimeIntervalSelected(timeInterval: TimeIntervalCode) {
        setState {
            copy(
                selectedTimeInterval = timeInterval,
                isTimeIntervalListShown = false
            )
        }
    }

    private fun loadStatistic(
        cafeUuid: String?,
        period: TimeIntervalCode
    ) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(loadingStatistic = true)
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
                            TimeIntervalCode.DAY -> dateTimeUtil.formatDateTime(
                                statistic.startPeriodTime,
                                PATTERN_DD_MMMM_YYYY
                            )

                            TimeIntervalCode.MONTH -> dateTimeUtil.formatDateTime(
                                statistic.startPeriodTime,
                                PATTERN_MMMM
                            )

                            TimeIntervalCode.WEEK -> dateTimeUtil.getWeekPeriod(statistic.startPeriodTime)
                        }

                    )
                }.let { statisticItemList ->
                    setState {
                        copy(
                            statisticList = statisticItemList,
                            loadingStatistic = false,
                            hasError = false
                        )
                    }
                }
            },
            onError = {
                setState {
                    copy(
                        hasError = true,
                        loadingStatistic = false
                    )
                }
            }
        )
    }

    private fun updateData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        cafeList = listOf(getCafeUseCase()),
                        isLoading = false
                    )
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
}
