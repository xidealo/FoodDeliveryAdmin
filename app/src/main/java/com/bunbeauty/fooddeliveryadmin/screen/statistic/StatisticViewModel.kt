package com.bunbeauty.fooddeliveryadmin.screen.statistic

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.lifecycle.viewModelScope
import com.bunbeauty.data.repository.CafeRepository
import com.bunbeauty.data.repository.StatisticRepository
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.use_case.GetStatisticUseCase
import com.bunbeauty.domain.use_case.LogoutUseCase
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.fooddeliveryadmin.screen.order_list.LogoutOption
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val cafeRepository: CafeRepository,
    private val stringUtil: IStringUtil,
    private val resources: Resources,
    private val dataStoreRepo: DataStoreRepo,
    private val getStatisticUseCase: GetStatisticUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel() {

    private val mutableStatisticState: MutableStateFlow<StatisticState> =
        MutableStateFlow(StatisticState())
    val statisticState: StateFlow<StatisticState> = mutableStatisticState.asStateFlow()

    private val allCafes = SelectedCafe(
        uuid = null,
        address = resources.getString(R.string.msg_statistic_all_cafes)
    )
    private var loadStatisticJob: Job? = null

    private fun getExceptionHandler(retryAction: RetryAction) =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            mutableStatisticState.update { state ->
                state.copy(isLoading = false) + StatisticState.Event.ShowError(retryAction)
            }
        }

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
        viewModelScope.launch(getExceptionHandler(RetryAction.LOAD_CAFE_LIST)) {
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
        viewModelScope.launch(getExceptionHandler(RetryAction.LOAD_CAFE_LIST)) {
            mutableStatisticState.update { statisticState ->
                statisticState.copy(isLoading = true)
            }

            val selectedCafe = cafeUuid?.let { cafeUuid ->
                cafeRepository.getCafeByUuid(cafeUuid)
            }?.let { cafe ->
                SelectedCafe(
                    uuid = cafe.uuid,
                    address = cafe.address
                )
            } ?: allCafes

            mutableStatisticState.update { statisticState ->
                statisticState.copy(
                    selectedCafe = selectedCafe,
                    isLoading = false
                )
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
        loadStatisticJob = viewModelScope.launch(getExceptionHandler(RetryAction.LOAD_STATISTIC)) {
            mutableStatisticState.update { statisticState ->
                statisticState.copy(isLoading = true)
            }
            getStatisticUseCase(
                token = dataStoreRepo.token.first(),
                cafeUuid = mutableStatisticState.value.selectedCafe?.uuid,
                period = mutableStatisticState.value.selectedTimeIntervalCode
            ).map { statistic ->
                StatisticItemModel(
                    startMillis = statistic.startPeriodTime,
                    period = statistic.period,
                    count = stringUtil.getOrderCountString(statistic.orderCount),
                    proceeds = resources.getString(R.string.with_ruble, statistic.proceeds)
                )
            }.let { statisticItemList ->
                mutableStatisticState.update { statisticState ->
                    statisticState.copy(
                        statisticList = statisticItemList,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onRetryClicked(retryAction: RetryAction) {
        when (retryAction) {
            RetryAction.LOAD_CAFE_LIST -> updateData()
            RetryAction.LOAD_STATISTIC -> loadStatistic()
        }
    }

    fun onLogout(option: String) {
        if (LogoutOption.valueOf(option) == LogoutOption.LOGOUT) {
            viewModelScope.launch {
                logoutUseCase()
                mutableStatisticState.update { state ->
                    state + StatisticState.Event.OpenLoginEvent
                }
            }
        }
    }

    fun consumeEvents(events: List<StatisticState.Event>) {
        mutableStatisticState.update { statisticState ->
            statisticState - events
        }
    }

    private fun updateData() {
        viewModelScope.launch(getExceptionHandler(RetryAction.LOAD_CAFE_LIST)) {
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
}
