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
    private val stringUtil: IStringUtil,
    private val dateTimeUtil: DateTimeUtil,
    private val getStatisticUseCase: GetStatisticUseCase,
) : BaseStateViewModel<Statistic.ViewDataState, Statistic.Action, Statistic.Event>(
    initState = Statistic.ViewDataState(
        cafeUuid = null
    )
) {

    private val allCafes = Statistic.SelectedCafe(
        uuid = null,
        address = null
    )

    private var loadStatisticJob: Job? = null

    override fun reduce(action: Statistic.Action, dataState: Statistic.ViewDataState) {
        when (action) {
            is Statistic.Action.Init -> {
                setState {
                    copy(
                        selectedCafe = allCafes,
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
        viewModelScope.launch {
            addEvent {
                Statistic.Event.OpenCafeListEvent(
                    getCafeListByCityUuidUseCase()
                )
            }
        }
    }

    fun onCafeSelected(cafeUuid: String?) {
        viewModelScope.launch {
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
            } ?: allCafes

            setState {
                copy(
                    selectedCafe = selectedCafe,
                    isLoading = false
                )
            }
        }
    }

    fun onTimeIntervalClicked() {
        viewModelScope.launch {
            val timeIntervalList = Statistic.TimeIntervalCode.entries.map { timeInterval ->
                Option(
                    id = timeInterval.name,
                    title = timeInterval.name
                )
            }
            addEvent {
                Statistic.Event.OpenTimeIntervalListEvent(timeIntervalList)
            }
        }
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
        loadStatisticJob?.cancel()
        loadStatisticJob = viewModelScope.launch {
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
                    count = stringUtil.getOrderCountString(statistic.orderCount),
                    proceeds = "${statistic.proceeds} ${statistic.currency}",
                    date = dateTimeUtil.formatDateTime(statistic.startPeriodTime, "MMMM")
                )
            }.let { statisticItemList ->
                setState {
                    copy(
                        statisticList = statisticItemList,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onRetryClicked(retryAction: Statistic.RetryAction) {
        when (retryAction) {
            Statistic.RetryAction.LOAD_CAFE_LIST -> updateData()
            Statistic.RetryAction.LOAD_STATISTIC -> loadStatistic(
                cafeUuid = null,
                period = Statistic.TimeIntervalCode.MONTH
            )
        }
    }

    private fun updateData() {
        viewModelScope.launch {
            getCafeListUseCase()
        }
    }
}
