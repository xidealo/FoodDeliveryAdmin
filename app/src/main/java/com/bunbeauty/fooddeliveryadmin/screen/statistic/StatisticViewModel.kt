package com.bunbeauty.fooddeliveryadmin.screen.statistic

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.repository.CafeRepository
import com.bunbeauty.data.repository.StatisticRepository
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val cafeRepository: CafeRepository,
    private val stringUtil: IStringUtil,
    private val resources: Resources,
    private val dataStoreRepo: DataStoreRepo,
    private val statisticRepository: StatisticRepository
) : BaseViewModel() {

    private val mutableStatisticState: MutableStateFlow<StatisticState> =
        MutableStateFlow(StatisticState())
    val statisticState: StateFlow<StatisticState> = mutableStatisticState.asStateFlow()

    private val allCafes = SelectedCafe(
        uuid = null,
        address = resources.getString(R.string.msg_statistic_all_cafes)
    )
    private var loadStatisticJob: Job? = null

    init {
        mutableStatisticState.update { statisticState ->
            statisticState.copy(
                selectedCafe = allCafes,
                selectedTimeInterval = SelectedTimeInterval(
                    code = TimeIntervalCode.MONTH,
                    name = getTimeIntervalName(TimeIntervalCode.MONTH)
                )
            )
        }
        updateData()
    }

    fun onCafeClicked() {
        viewModelScope.launch {
            val cafeList = buildList {
                add(
                    Option(
                        id = allCafes.uuid,
                        title = allCafes.address,
                    )
                )
                val cityUuid = dataStoreRepo.managerCity.first()
                cafeRepository.getCafeListByCityUuid(cityUuid).map { cafe ->
                    Option(
                        id = cafe.uuid,
                        title = cafe.address,
                    )
                }.let { cafeAddressList ->
                    addAll(cafeAddressList)
                }
            }

            mutableStatisticState.update { statisticState ->
                statisticState + StatisticState.Event.OpenCafeListEvent(cafeList)
            }
        }
    }

    fun onCafeSelected(cafeUuid: String?) {
        viewModelScope.launch {
            val selectedCafe = cafeUuid?.let { cafeUuid ->
                cafeRepository.getCafeByUuid(cafeUuid)
            }?.let { cafe ->
                SelectedCafe(
                    uuid = cafe.uuid,
                    address = cafe.address
                )
            } ?: allCafes

            mutableStatisticState.update { statisticState ->
                statisticState.copy(selectedCafe = selectedCafe)
            }
        }
    }

    fun onTimeIntervalClicked() {
        viewModelScope.launch {
            val timeIntervalList = TimeIntervalCode.values().map { timeInterval ->
                Option(
                    id = timeInterval.name,
                    title = getTimeIntervalName(timeInterval)
                )
            }

            mutableStatisticState.update { statisticState ->
                statisticState + StatisticState.Event.OpenTimeIntervalListEvent(timeIntervalList)
            }
        }
    }

    fun onTimeIntervalSelected(timeInterval: String) {
        val timeIntervalCode = TimeIntervalCode.valueOf(timeInterval)
        mutableStatisticState.update { statisticState ->
            statisticState.copy(
                selectedTimeInterval = SelectedTimeInterval(
                    code = timeIntervalCode,
                    name = getTimeIntervalName(timeIntervalCode)
                )
            )
        }
    }

    @SuppressLint("StringFormatMatches")
    fun loadStatistic() {
        loadStatisticJob?.cancel()
        loadStatisticJob = viewModelScope.launch {
            mutableStatisticState.update { statisticState ->
                statisticState.copy(isLoading = true)
            }

            val statisticResult = statisticRepository.getStatistic(
                token = dataStoreRepo.token.first(),
                cafeUuid = mutableStatisticState.value.selectedCafe?.uuid,
                period = mutableStatisticState.value.selectedTimeIntervalCode
            )
            if (statisticResult is ApiResult.Success) {
                statisticResult.data.map { statistic ->
                    StatisticItemModel(
                        startMillis = statistic.startPeriodTime,
                        period = statistic.period,
                        count = stringUtil.getOrderCountString(statistic.orderCount),
                        proceeds = resources.getString(R.string.with_ruble, statistic.proceeds)
                    )
                }.let { statisticItemList ->
                    mutableStatisticState.update { statisticState ->
                        statisticState.copy(statisticList = statisticItemList)
                    }
                }
            }

            mutableStatisticState.update { statisticState ->
                statisticState.copy(isLoading = false)
            }
        }
    }

    fun onRetryClicked() {
        updateData()
    }

    fun consumeEvents(events: List<StatisticState.Event>) {
        mutableStatisticState.update { statisticState ->
            statisticState - events
        }
    }

    private fun updateData() {
        handleWithState {
            cafeRepository.refreshCafeList(
                token = dataStoreRepo.token.first(),
                cityUuid = dataStoreRepo.managerCity.first()
            )
        }
    }

    private fun getTimeIntervalName(timeInterval: TimeIntervalCode): String {
        return when (timeInterval) {
            TimeIntervalCode.DAY -> resources.getString(R.string.msg_statistic_day_interval)
            TimeIntervalCode.WEEK -> resources.getString(R.string.msg_statistic_week_interval)
            TimeIntervalCode.MONTH -> resources.getString(R.string.msg_statistic_month_interval)
        }
    }

    private inline fun handleWithState(crossinline block: suspend () -> Unit) {
        viewModelScope.launch {
            mutableStatisticState.update { state ->
                state.copy(isLoading = true)
            }
            try {
                block()
                mutableStatisticState.update { state ->
                    state.copy(isLoading = false)
                }
            } catch (exception: Exception) {
                mutableStatisticState.update { state ->
                    state.copy(isLoading = false) + StatisticState.Event.ShowError
                }
            }
        }
    }
}