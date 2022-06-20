package com.bunbeauty.presentation.view_model.statistic

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.Constants.CAFE_ADDRESS_REQUEST_KEY
import com.bunbeauty.common.Constants.PERIOD_REQUEST_KEY
import com.bunbeauty.common.Constants.SELECTED_CAFE_ADDRESS_KEY
import com.bunbeauty.common.Constants.SELECTED_PERIOD_KEY
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.StatisticRepo
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.extension.toStateSuccess
import com.bunbeauty.presentation.model.list.CafeAddress
import com.bunbeauty.presentation.model.list.Period
import com.bunbeauty.presentation.model.ListData
import com.bunbeauty.presentation.model.StatisticItemModel
import com.bunbeauty.presentation.navigation_event.StatisticNavigationEvent
import com.bunbeauty.presentation.state.State
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val ALL_CAFE = "ALL"

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val cafeRepo: CafeRepo,
    private val stringUtil: IStringUtil,
    private val resourcesProvider: IResourcesProvider,
    private val dataStoreRepo: DataStoreRepo,
    private val statisticRepo: StatisticRepo
) : BaseViewModel() {

    private val delivery by lazy {
        runBlocking {
            dataStoreRepo.delivery.first()
        }
    }

    enum class PeriodKey {
        DAY,
        WEEK,
        MONTH,
    }

    private val dayPeriod =
        Period(resourcesProvider.getString(R.string.msg_statistic_day_period), PeriodKey.DAY.name)
    private val weekPeriod =
        Period(resourcesProvider.getString(R.string.msg_statistic_week_period), PeriodKey.WEEK.name)
    private val monthPeriod =
        Period(
            resourcesProvider.getString(R.string.msg_statistic_month_period),
            PeriodKey.MONTH.name
        )
    private val allCafeAddress = CafeAddress(
        title = resourcesProvider.getString(R.string.msg_statistic_all_cafes),
        cafeUuid = ALL_CAFE
    )

    private val mutablePeriod: MutableStateFlow<Period> = MutableStateFlow(monthPeriod)
    val period: StateFlow<Period> = mutablePeriod.asStateFlow()

    private val mutableCafeAddress: MutableStateFlow<CafeAddress> = MutableStateFlow(allCafeAddress)
    val cafeAddress: StateFlow<CafeAddress> = mutableCafeAddress.asStateFlow()

    private val mutableStatisticState: MutableStateFlow<State<List<StatisticItemModel>>> =
        MutableStateFlow(State.Loading())
    val statisticState: StateFlow<State<List<StatisticItemModel>>> =
        mutableStatisticState.asStateFlow()

    var requestJob: Job? = null

    init {
        getStatistic()
    }

    fun setCafeAddress(cafeAddress: CafeAddress) {
        mutableCafeAddress.value = cafeAddress
        getStatistic()
    }

    fun setPeriod(period: Period) {
        mutablePeriod.value = period
        getStatistic()
    }

    fun getStatistic() {
        requestJob?.cancel()
        requestJob = viewModelScope.launch(Dispatchers.Default) {
            mutableStatisticState.value = State.Loading()
            statisticRepo.getStatistic(
                dataStoreRepo.token.first(), cafeAddress.value.cafeUuid, period.value.key
            ).let { result ->
                when (result) {
                    is ApiResult.Success -> {
                        Log.d("statistic_test", "getStatistic:${result.data} ")
                        mutableStatisticState.value = result.data
                            .map { statistic ->
                                val proceedsString =
                                    stringUtil.getCostString(statistic.proceeds)
                                StatisticItemModel(
                                    period = statistic.period,
                                    count = stringUtil.getOrderCountString(statistic.orderCount),
                                    proceeds = proceedsString,
                                    statistic = statistic
                                )
                            }.toStateSuccess()
                    }
                    is ApiResult.Error -> {

                        if (result.apiError.code != 7) {
                            sendError(result.apiError.message)
                            mutableStatisticState.value = State.Error()
                        }

                        Log.e(
                            "statistic_test",
                            "getStatistic: code ${result.apiError.code} msg ${result.apiError.message}"
                        )
                    }
                }
            }
        }
    }

    fun goToAddressList() {
        viewModelScope.launch {
            val cafeAddressList = ArrayList(cafeRepo.getCafeList().map { cafe ->
                CafeAddress(title = cafe.address, cafeUuid = cafe.uuid)
            })
            cafeAddressList.add(allCafeAddress)

            withContext(Main) {
                val listData = ListData(
                    title = resourcesProvider.getString(R.string.title_statistic_select_cafe),
                    listItem = cafeAddressList,
                    requestKey = CAFE_ADDRESS_REQUEST_KEY,
                    selectedKey = SELECTED_CAFE_ADDRESS_KEY
                )
                goTo(StatisticNavigationEvent.ToCafeAddressList(listData))
            }
        }
    }

    fun goToPeriodList() {
        val listData = ListData(
            title = resourcesProvider.getString(R.string.title_statistic_select_period),
            listItem = listOf(dayPeriod, weekPeriod, monthPeriod),
            requestKey = PERIOD_REQUEST_KEY,
            selectedKey = SELECTED_PERIOD_KEY
        )
        goTo(StatisticNavigationEvent.ToPeriodList(listData))
    }

    fun goToStatisticDetails(statisticItemModel: StatisticItemModel) {
        goTo(StatisticNavigationEvent.ToStatisticDetails(statisticItemModel.statistic))
    }
}