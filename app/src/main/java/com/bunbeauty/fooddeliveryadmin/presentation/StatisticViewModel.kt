package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.State
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.data.enums.Period
import com.bunbeauty.domain.product.IProductUtil
import com.bunbeauty.domain.repository.order.OrderRepo
import com.bunbeauty.domain.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.AddressItem
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.PeriodItem
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.StatisticItem
import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.StatisticFragmentDirections.*
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

abstract class StatisticViewModel : BaseViewModel() {

    abstract val statisticState: StateFlow<State<List<StatisticItem>>>
    abstract var selectedAddressItem: AddressItem
    abstract var selectedPeriodItem: PeriodItem

    abstract fun getStatistic(cafeId: String?, period: String)
    abstract fun goToAddressList()
    abstract fun goToPeriodList()
    abstract fun goToStatisticDetails(statisticItem: StatisticItem)
}

class StatisticViewModelImpl @Inject constructor(
    private val orderRepo: OrderRepo,
    private val stringUtil: IStringUtil,
    private val productUtil: IProductUtil,
    resourcesProvider: IResourcesProvider
) : StatisticViewModel() {

    override val statisticState = MutableStateFlow<State<List<StatisticItem>>>(State.Loading())
    override var selectedAddressItem =
        AddressItem(resourcesProvider.getString(R.string.msg_statistic_all_cafes), null)
    override var selectedPeriodItem = PeriodItem(Period.DAY.text)

    override fun getStatistic(cafeId: String?, period: String) {
        statisticState.value = State.Loading()

        val orderMapFlow = if (cafeId == null) {
            when (period) {
                Period.DAY.text -> {
                    orderRepo.getAllCafeOrdersByDay()
                }
                Period.WEEK.text -> {
                    orderRepo.getAllCafeOrdersByWeek()
                }
                Period.MONTH.text -> {
                    orderRepo.getAllCafeOrdersByMonth()
                }
                else -> null
            }
        } else {
            when (period) {
                Period.DAY.text -> {
                    orderRepo.getCafeOrdersByCafeIdAndDay(cafeId)
                }
                Period.WEEK.text -> {
                    orderRepo.getCafeOrdersByCafeIdAndWeek(cafeId)
                }
                Period.MONTH.text -> {
                    orderRepo.getCafeOrdersByCafeIdAndMonth(cafeId)
                }
                else -> null
            }
        }

        orderMapFlow?.onEach { orderMap ->
            statisticState.value = orderMap.map { orderEntry ->
                val cartProductList = orderEntry.value.flatMap { order ->
                    order.cartProducts
                }
                val proceeds = productUtil.getNewTotalCost(cartProductList)
                val proceedsString = stringUtil.getCostString(proceeds)
                StatisticItem(
                    period = orderEntry.key,
                    count = stringUtil.getOrderCountString(orderEntry.value.size),
                    proceeds = proceedsString,
                    orderList = orderEntry.value
                )
            }.toStateSuccess()
        }?.launchIn(viewModelScope)
    }

    override fun goToAddressList() {
        router.navigate(toStatisticAddressListBottomSheet())
    }

    override fun goToPeriodList() {
        router.navigate(toStatisticPeriodListBottomSheet())
    }

    override fun goToStatisticDetails(statisticItem: StatisticItem) {
        router.navigate(toStatisticDetailsBottomSheet(statisticItem))
    }
}