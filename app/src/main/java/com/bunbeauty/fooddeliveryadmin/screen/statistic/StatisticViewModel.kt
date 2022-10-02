package com.bunbeauty.fooddeliveryadmin.screen.statistic

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.repository.StatisticRepository
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.fooddeliveryadmin.util.reduce
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.model.StatisticItemModel
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

// private const val ALL_CAFES_UUID = "ALL"

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val cafeRepo: CafeRepo,
    private val stringUtil: IStringUtil,
    private val resourcesProvider: IResourcesProvider,
    private val dataStoreRepo: DataStoreRepo,
    private val statisticRepository: StatisticRepository
) : BaseViewModel() {

    private val mutableStatisticState: MutableStateFlow<StatisticState> =
        MutableStateFlow(StatisticState())
    val statisticState: StateFlow<StatisticState> = mutableStatisticState.asStateFlow()

    private val allCafes = Cafe(
        uuid = null,
        title = resourcesProvider.getString(R.string.msg_statistic_all_cafes),
        isSelected = true
    )
    private var loadStatisticJob: Job? = null

    init {
        mutableStatisticState.reduce { statisticState ->
            statisticState.copy(cafeList = listOf(allCafes))
        }

        viewModelScope.launch {
            val cafeList = cafeRepo.getCafeList().map { cafe ->
                Cafe(
                    uuid = cafe.uuid,
                    title = cafe.address,
                    isSelected = false
                )
            } + allCafes
            mutableStatisticState.reduce { statisticState ->
                statisticState.copy(cafeList = cafeList)
            }
        }
    }

    fun setCafe(cafeUuid: String?) {
        mutableStatisticState.reduce { statisticState ->
            statisticState.copy(
                cafeList = statisticState.cafeList.map { cafe ->
                    if (cafe.uuid == cafeUuid) {
                        cafe.copy(isSelected = true)
                    } else {
                        cafe.copy(isSelected = false)
                    }
                }
            )
        }
    }

    fun setTimeInterval(timeInterval: String) {
        mutableStatisticState.reduce { statisticState ->
            statisticState.copy(timeInterval = TimeInterval.valueOf(timeInterval))
        }
    }

    fun loadStatistic() {
        loadStatisticJob?.cancel()
        loadStatisticJob = viewModelScope.launch {
            mutableStatisticState.reduce { statisticState ->
                statisticState.copy(isLoading = true)
            }

            val statisticResult = statisticRepository.getStatistic(
                token = dataStoreRepo.token.first(),
                cafeUuid = mutableStatisticState.value.selectedCafe?.uuid,
                period = mutableStatisticState.value.timeInterval.toString()
            )
            if (statisticResult is ApiResult.Success) {
                statisticResult.data.map { statistic ->
                    StatisticItemModel(
                        period = statistic.period,
                        count = stringUtil.getOrderCountString(statistic.orderCount),
                        proceeds = stringUtil.getCostString(statistic.proceeds),
                        statistic = statistic
                    )
                }.let { statisticItemList ->
                    mutableStatisticState.reduce { statisticState ->
                        statisticState.copy(statisticList = statisticItemList)
                    }
                }
            }

            mutableStatisticState.reduce { statisticState ->
                statisticState.copy(isLoading = false)
            }
        }
    }


//    enum class PeriodKey {
//        DAY,
//        WEEK,
//        MONTH,
//    }
//
//    private val dayPeriod =
//        Period(resourcesProvider.getString(R.string.msg_statistic_day_period), PeriodKey.DAY.name)
//    private val weekPeriod =
//        Period(resourcesProvider.getString(R.string.msg_statistic_week_period), PeriodKey.WEEK.name)
//    private val monthPeriod =
//        Period(
//            resourcesProvider.getString(R.string.msg_statistic_month_period),
//            PeriodKey.MONTH.name
//        )
//    private val allCafeAddress = CafeAddress(
//        title = resourcesProvider.getString(R.string.msg_statistic_all_cafes),
//        cafeUuid = ALL_CAFES_UUID
//    )
//
//    private val mutablePeriod: MutableStateFlow<Period> = MutableStateFlow(monthPeriod)
//    val period: StateFlow<Period> = mutablePeriod.asStateFlow()
//
//    private val mutableCafeAddress: MutableStateFlow<CafeAddress> = MutableStateFlow(allCafeAddress)
//    val cafeAddress: StateFlow<CafeAddress> = mutableCafeAddress.asStateFlow()
//
//
//    fun setCafeAddress(cafeAddress: CafeAddress) {
//        mutableCafeAddress.value = cafeAddress
//        getStatistic()
//    }
//
//    fun setPeriod(period: Period) {
//        mutablePeriod.value = period
//        getStatistic()
//    }
//
//    fun getStatistic() {
//        loadStatisticJob?.cancel()
//        loadStatisticJob = viewModelScope.launch(Dispatchers.Default) {
//            mutableStatisticState.value = State.Loading()
//            statisticRepo.getStatistic(
//                dataStoreRepo.token.first(), cafeAddress.value.cafeUuid, period.value.key
//            ).let { result ->
//                when (result) {
//                    is ApiResult.Success -> {
//                        Log.d("statistic_test", "getStatistic:${result.data} ")
//                        mutableStatisticState.value = result.data
//                            .map { statistic ->
//                                val proceedsString =
//                                    stringUtil.getCostString(statistic.proceeds)
//                                StatisticItemModel(
//                                    period = statistic.period,
//                                    count = stringUtil.getOrderCountString(statistic.orderCount),
//                                    proceeds = proceedsString,
//                                    statistic = statistic
//                                )
//                            }.toStateSuccess()
//                    }
//                    is ApiResult.Error -> {
//
//                        if (result.apiError.code != 7) {
//                            sendError(result.apiError.message)
//                            mutableStatisticState.value = State.Error()
//                        }
//
//                        Log.e(
//                            "statistic_test",
//                            "getStatistic: code ${result.apiError.code} msg ${result.apiError.message}"
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    fun goToAddressList() {
//        viewModelScope.launch {
//            val cafeAddressList = ArrayList(cafeRepo.getCafeList().map { cafe ->
//                CafeAddress(title = cafe.address, cafeUuid = cafe.uuid)
//            })
//            cafeAddressList.add(allCafeAddress)
//
//            withContext(Main) {
//                val listData = ListData(
//                    title = resourcesProvider.getString(R.string.title_statistic_select_cafe),
//                    listItem = cafeAddressList,
//                    requestKey = CAFE_ADDRESS_REQUEST_KEY,
//                    selectedKey = SELECTED_CAFE_ADDRESS_KEY
//                )
//                goTo(StatisticNavigationEvent.ToCafeAddressList(listData))
//            }
//        }
//    }
//
//    fun goToPeriodList() {
//        val listData = ListData(
//            title = resourcesProvider.getString(R.string.title_statistic_select_period),
//            listItem = listOf(dayPeriod, weekPeriod, monthPeriod),
//            requestKey = PERIOD_REQUEST_KEY,
//            selectedKey = SELECTED_PERIOD_KEY
//        )
//        goTo(StatisticNavigationEvent.ToPeriodList(listData))
//    }
//
//    fun goToStatisticDetails(statisticItemModel: StatisticItemModel) {
//        goTo(StatisticNavigationEvent.ToStatisticDetails(statisticItemModel.statistic))
//    }
}