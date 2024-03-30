package com.bunbeauty.presentation.feature.statistic

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.common.GetCafeListUseCase
import com.bunbeauty.domain.feature.statistic.GetCafeByUuidUseCase
import com.bunbeauty.domain.feature.statistic.GetCafeListByCityUuidUseCase
import com.bunbeauty.domain.usecase.GetStatisticUseCase
import com.bunbeauty.presentation.Option
import com.bunbeauty.presentation.R
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
    private val resources: Resources,
    private val getStatisticUseCase: GetStatisticUseCase,
) : BaseStateViewModel<Statistic.ViewDataState, Statistic.Action, Statistic.Event>(
    initState = Statistic.ViewDataState(
        cafeUuid = null
    )
) {

    private val allCafes = Statistic.SelectedCafe(
        uuid = null,
        address = resources.getString(R.string.msg_statistic_all_cafes)
    )

    private var loadStatisticJob: Job? = null

    override fun reduce(action: Statistic.Action, dataState: Statistic.ViewDataState) {
        when (action) {
            is Statistic.Action.Init -> {
                setState {
                    copy(
                        selectedCafe = allCafes,
                        selectedTimeInterval = Statistic.SelectedTimeInterval(
                            code = Statistic.TimeIntervalCode.MONTH,
                            name = getTimeIntervalName(Statistic.TimeIntervalCode.MONTH)
                        )
                    )
                }
                updateData()
            }

            Statistic.Action.LoadStatisticClick -> {
                loadStatistic(
                    cafeUuid = dataState.selectedCafe?.uuid,
                    period = dataState.selectedTimeIntervalCode
                )
            }

            Statistic.Action.SelectCafeClick -> {
                onCafeClicked()
            }
        }
    }

    fun onCafeClicked() {
        viewModelScope.launch {
            val cafeList = buildList {
                add(
                    Option(
                        id = allCafes.uuid,
                        title = allCafes.address
                    )
                )
                getCafeListByCityUuidUseCase().map { cafe ->
                    Option(
                        id = cafe.uuid,
                        title = cafe.address
                    )
                }.let { cafeAddressList ->
                    addAll(cafeAddressList)
                }
            }

            addEvent {
                Statistic.Event.OpenCafeListEvent(cafeList)
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
                    title = getTimeIntervalName(timeInterval)
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
                selectedTimeInterval = Statistic.SelectedTimeInterval(
                    code = timeIntervalCode,
                    name = getTimeIntervalName(timeIntervalCode)
                )
            )
        }
    }

    @SuppressLint("StringFormatMatches")
    fun loadStatistic(
        cafeUuid: String?,
        period: String
    ) {
        loadStatisticJob?.cancel()
        loadStatisticJob = viewModelScope.launch {
            setState {
                copy(isLoading = true)
            }
            getStatisticUseCase(
                cafeUuid = cafeUuid,
                period = period
            ).map { statistic ->
                Statistic.ViewDataState.StatisticItemModel(
                    startMillis = statistic.startPeriodTime,
                    period = statistic.period,
                    count = stringUtil.getOrderCountString(statistic.orderCount),
                    proceeds = resources.getString(R.string.common_with_ruble, statistic.proceeds)
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
                period = ""
            )
        }
    }

    private fun updateData() {
        viewModelScope.launch {
            getCafeListUseCase()
        }
    }

    private fun getTimeIntervalName(timeInterval: Statistic.TimeIntervalCode): String {
        return when (timeInterval) {
            Statistic.TimeIntervalCode.DAY -> resources.getString(R.string.msg_statistic_day_interval)
            Statistic.TimeIntervalCode.WEEK -> resources.getString(R.string.msg_statistic_week_interval)
            Statistic.TimeIntervalCode.MONTH -> resources.getString(R.string.msg_statistic_month_interval)
        }
    }
}
