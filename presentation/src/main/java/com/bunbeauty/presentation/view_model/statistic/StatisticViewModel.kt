package com.bunbeauty.presentation.view_model.statistic

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.CAFE_ADDRESS_REQUEST_KEY
import com.bunbeauty.common.Constants.PERIOD_REQUEST_KEY
import com.bunbeauty.common.Constants.SELECTED_CAFE_ADDRESS_KEY
import com.bunbeauty.common.Constants.SELECTED_PERIOD_KEY
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.domain.util.order.IOrderUtil
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
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val orderRepo: OrderRepo,
    private val cafeRepo: CafeRepo,
    private val stringUtil: IStringUtil,
    private val orderUtil: IOrderUtil,
    private val resourcesProvider: IResourcesProvider,
    dataStoreRepo: DataStoreRepo,
) : BaseViewModel() {

    private val delivery by lazy {
        runBlocking {
            dataStoreRepo.delivery.first()
        }
    }

    private val dayPeriod = Period(resourcesProvider.getString(R.string.msg_statistic_day_period))
    private val weekPeriod = Period(resourcesProvider.getString(R.string.msg_statistic_week_period))
    private val monthPeriod =
        Period(resourcesProvider.getString(R.string.msg_statistic_month_period))
    private val allCafeAddress = CafeAddress(
        title = resourcesProvider.getString(R.string.msg_statistic_all_cafes),
        cafeUuid = null
    )

    private val mutablePeriod: MutableStateFlow<Period> = MutableStateFlow(monthPeriod)
    val period: StateFlow<Period> = mutablePeriod.asStateFlow()

    private val mutableCafeAddress: MutableStateFlow<CafeAddress> = MutableStateFlow(allCafeAddress)
    val cafeAddress: StateFlow<CafeAddress> = mutableCafeAddress.asStateFlow()

    private val mutableStatisticState: MutableStateFlow<State<List<StatisticItemModel>>> =
        MutableStateFlow(State.Loading())
    val statisticState: StateFlow<State<List<StatisticItemModel>>> =
        mutableStatisticState.asStateFlow()

    init {
        subscribeOnStatistic()
    }

    fun setCafeAddress(cafeAddress: CafeAddress) {
        mutableCafeAddress.value = cafeAddress
    }

    fun setPeriod(period:Period) {
        mutablePeriod.value = period
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

    private fun subscribeOnStatistic() {
        cafeAddress.flatMapLatest { cafeAddress ->
            period.flatMapLatest { period ->
                getStatisticList(cafeAddress, period)!!.onEach { statisticList ->
                    mutableStatisticState.value = statisticList
                        .map { statistic ->
                            val proceeds = orderUtil.getProceeds(statistic.orderList, delivery)
                            val proceedsString = stringUtil.getCostString(proceeds)
                            StatisticItemModel(
                                period = statistic.period,
                                count = stringUtil.getOrderCountString(statistic.orderList.size),
                                proceeds = proceedsString,
                                statistic = statistic
                            )
                        }.toStateSuccess()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getStatisticList(cafeAddress: CafeAddress, period: Period): Flow<List<Statistic>>? {
        return if (cafeAddress.cafeUuid == null) {
            when (period) {
                dayPeriod -> {
                    orderRepo.getAllCafeOrdersByDay()
                }
                weekPeriod -> {
                    orderRepo.getAllCafeOrdersByWeek()
                }
                monthPeriod -> {
                    orderRepo.getAllCafeOrdersByMonth()
                }
                else -> null
            }
        } else {
            when (period) {
                dayPeriod -> {
                    orderRepo.getCafeOrdersByCafeIdAndDay(cafeAddress.cafeUuid)
                }
                weekPeriod -> {
                    orderRepo.getCafeOrdersByCafeIdAndWeek(cafeAddress.cafeUuid)
                }
                monthPeriod -> {
                    orderRepo.getCafeOrdersByCafeIdAndMonth(cafeAddress.cafeUuid)
                }
                else -> null
            }
        }
    }
}