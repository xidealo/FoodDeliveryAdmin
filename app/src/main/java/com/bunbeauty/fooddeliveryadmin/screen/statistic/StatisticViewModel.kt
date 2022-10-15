package com.bunbeauty.fooddeliveryadmin.screen.statistic

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.repository.CafeRepository
import com.bunbeauty.data.repository.StatisticRepository
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.fooddeliveryadmin.shared.cafe.CafeSelector
import com.bunbeauty.fooddeliveryadmin.shared.cafe.CafeUi
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
    private val statisticRepository: StatisticRepository,
    private val cafeSelector: CafeSelector,
) : BaseViewModel() {

    private val mutableStatisticState: MutableStateFlow<StatisticState> =
        MutableStateFlow(StatisticState())
    val statisticState: StateFlow<StatisticState> = mutableStatisticState.asStateFlow()

    private val allCafes = CafeUi(
        uuid = null,
        title = resourcesProvider.getString(R.string.msg_statistic_all_cafes),
        isSelected = true
    )
    private var loadStatisticJob: Job? = null

    init {
        mutableStatisticState.update { statisticState ->
            statisticState.copy(cafeList = listOf(allCafes))
        }

        viewModelScope.launch {
            val cafeList = cafeRepository.getCafeList().map { cafe ->
                CafeUi(
                    uuid = cafe.uuid,
                    title = cafe.address,
                    isSelected = false
                )
            } + allCafes
            mutableStatisticState.update { statisticState ->
                statisticState.copy(cafeList = cafeList)
            }
        }
    }

    fun getIntervalName(timeInterval: TimeInterval): String {
        return when (timeInterval) {
            TimeInterval.DAY -> resourcesProvider.getString(R.string.msg_statistic_day_interval)
            TimeInterval.WEEK -> resourcesProvider.getString(R.string.msg_statistic_week_interval)
            TimeInterval.MONTH -> resourcesProvider.getString(R.string.msg_statistic_month_interval)
        }
    }

    fun onCafeClicked() {
        mutableStatisticState.update { statisticState ->
            statisticState.copy(isCafesOpen = true)
        }
    }

    fun setCafe(cafeUuid: String?) {
        mutableStatisticState.update { statisticState ->
            statisticState.copy(
                cafeList = cafeSelector.selectCafe(statisticState.cafeList, cafeUuid)
            )
        }
    }

    fun cafesClosed() {
        mutableStatisticState.update { statisticState ->
            statisticState.copy(isCafesOpen = false)
        }
    }

    fun onTimeIntervalClicked() {
        mutableStatisticState.update { statisticState ->
            statisticState.copy(isTimeIntervalsOpen = true)
        }
    }

    fun timeIntervalsClosed() {
        mutableStatisticState.update { statisticState ->
            statisticState.copy(isTimeIntervalsOpen = false)
        }
    }

    fun setTimeInterval(timeInterval: String) {
        mutableStatisticState.update { statisticState ->
            statisticState.copy(selectedTimeInterval = TimeInterval.valueOf(timeInterval))
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
                period = mutableStatisticState.value.selectedTimeInterval.toString()
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
}