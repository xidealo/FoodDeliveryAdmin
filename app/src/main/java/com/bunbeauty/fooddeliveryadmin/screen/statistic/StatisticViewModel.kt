package com.bunbeauty.fooddeliveryadmin.screen.statistic

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.repository.CafeRepository
import com.bunbeauty.data.repository.StatisticRepository
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.utils.IResourcesProvider
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
    private val resourcesProvider: IResourcesProvider,
    private val dataStoreRepo: DataStoreRepo,
    private val statisticRepository: StatisticRepository
) : BaseViewModel() {

    private val mutableStatisticState: MutableStateFlow<StatisticState> =
        MutableStateFlow(StatisticState())
    val statisticState: StateFlow<StatisticState> = mutableStatisticState.asStateFlow()

    private val allCafes = SelectedCafe(
        uuid = null,
        address = resourcesProvider.getString(R.string.msg_statistic_all_cafes)
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

        viewModelScope.launch {
            cafeRepository.refreshCafeList(
                token = dataStoreRepo.token.first(),
                cityUuid = dataStoreRepo.managerCity.first()
            )
        }
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
            val openCafeListEvent = StatisticState.Event.OpenCafeListEvent(cafeList)

            mutableStatisticState.update { statisticState ->
                statisticState.copy(eventList = statisticState.eventList + openCafeListEvent)
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
            val openCafeListEvent = StatisticState.Event.OpenTimeIntervalListEvent(timeIntervalList)

            mutableStatisticState.update { statisticState ->
                statisticState.copy(eventList = statisticState.eventList + openCafeListEvent)
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
                        proceeds = stringUtil.getCostString(statistic.proceeds)
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

    fun consumeEvents(events: List<StatisticState.Event>) {
        mutableStatisticState.update { statisticState ->
            statisticState.copy(eventList = statisticState.eventList - events.toSet())
        }
    }

    private fun getTimeIntervalName(timeInterval: TimeIntervalCode): String {
        return when (timeInterval) {
            TimeIntervalCode.DAY -> resourcesProvider.getString(R.string.msg_statistic_day_interval)
            TimeIntervalCode.WEEK -> resourcesProvider.getString(R.string.msg_statistic_week_interval)
            TimeIntervalCode.MONTH -> resourcesProvider.getString(R.string.msg_statistic_month_interval)
        }
    }
}