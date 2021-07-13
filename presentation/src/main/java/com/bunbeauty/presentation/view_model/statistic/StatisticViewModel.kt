package com.bunbeauty.presentation.view_model.statistic

import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.CAFE_ADDRESS_REQUEST_KEY
import com.bunbeauty.common.Constants.LIST_ARGS_KEY
import com.bunbeauty.common.Constants.PERIOD_REQUEST_KEY
import com.bunbeauty.common.Constants.REQUEST_KEY_ARGS_KEY
import com.bunbeauty.common.Constants.SELECTED_CAFE_ADDRESS_KEY
import com.bunbeauty.common.Constants.SELECTED_KEY_ARGS_KEY
import com.bunbeauty.common.Constants.SELECTED_PERIOD_KEY
import com.bunbeauty.common.Constants.TITLE_ARGS_KEY
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.domain.util.order.IOrderUtil
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.presentation.view_model.BaseViewModel
import com.bunbeauty.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.StatisticFragmentDirections.toStatisticDetailsFragment
import com.bunbeauty.fooddeliveryadmin.ui.items.StatisticItem
import com.bunbeauty.fooddeliveryadmin.ui.items.list.CafeAddress
import com.bunbeauty.fooddeliveryadmin.ui.items.list.Period
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
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
    var selectedPeriod = monthPeriod

    private val mutableStatisticState: MutableStateFlow<State<List<StatisticItem>>> =
        MutableStateFlow(State.Loading())
    val statisticState: StateFlow<State<List<StatisticItem>>> = mutableStatisticState.asStateFlow()

    private val allCafeAddress = CafeAddress(
        title = resourcesProvider.getString(R.string.msg_statistic_all_cafes),
        cafeUuid = null
    )
    var selectedCafeAddress = allCafeAddress

    fun getStatistic(cafeAddress: CafeAddress, period: Period) {
        mutableStatisticState.value = State.Loading()

        val statisticListFlow = if (cafeAddress.cafeUuid == null) {
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

        statisticListFlow?.onEach { statisticList ->
            mutableStatisticState.value = statisticList.map { statistic ->
                val proceeds = orderUtil.getProceeds(statistic.orderList, delivery)
                val proceedsString = stringUtil.getCostString(proceeds)
                StatisticItem(
                    statistic = statistic,
                    count = stringUtil.getOrderCountString(statistic.orderList.size),
                    proceeds = proceedsString
                )
            }.toStateSuccess()
        }?.launchIn(viewModelScope)
    }

    fun goToAddressList() {
        viewModelScope.launch {
            val cafeAddressList = ArrayList(cafeRepo.getCafeList().map { cafe ->
                CafeAddress(title = cafe.address, cafeUuid = cafe.uuid)
            })
            cafeAddressList.add(allCafeAddress)
            val cafeAddressArray = cafeAddressList.toTypedArray()

            withContext(Main) {
                router.navigate(
                    R.id.to_listBottomSheet,
                    bundleOf(
                        TITLE_ARGS_KEY to resourcesProvider.getString(R.string.title_statistic_select_cafe),
                        LIST_ARGS_KEY to cafeAddressArray,
                        SELECTED_KEY_ARGS_KEY to SELECTED_CAFE_ADDRESS_KEY,
                        REQUEST_KEY_ARGS_KEY to CAFE_ADDRESS_REQUEST_KEY,
                    )
                )
            }
        }
    }

    fun goToPeriodList() {
        router.navigate(
            R.id.to_listBottomSheet,
            bundleOf(
                TITLE_ARGS_KEY to resourcesProvider.getString(R.string.title_statistic_select_period),
                LIST_ARGS_KEY to arrayOf(dayPeriod, weekPeriod, monthPeriod),
                SELECTED_KEY_ARGS_KEY to SELECTED_PERIOD_KEY,
                REQUEST_KEY_ARGS_KEY to PERIOD_REQUEST_KEY,
            )
        )
    }

    fun goToStatisticDetails(statistic: Statistic) {
        router.navigate(toStatisticDetailsFragment(statistic))
    }
}