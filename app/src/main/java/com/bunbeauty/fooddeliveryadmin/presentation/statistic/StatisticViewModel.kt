package com.bunbeauty.fooddeliveryadmin.presentation.statistic

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.State
import com.bunbeauty.data.enums.Period.*
import com.bunbeauty.data.model.statistic.Statistic
import com.bunbeauty.domain.order.IOrderUtil
import com.bunbeauty.domain.product.IProductUtil
import com.bunbeauty.domain.repository.order.OrderRepo
import com.bunbeauty.domain.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
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
    abstract fun goToStatisticDetails(statistic: Statistic)
}

class StatisticViewModelImpl @Inject constructor(
    private val orderRepo: OrderRepo,
    private val stringUtil: IStringUtil,
    private val productUtil: IProductUtil,
    private val orderUtil: IOrderUtil,
    resourcesProvider: IResourcesProvider,
) : StatisticViewModel() {

    override val statisticState = MutableStateFlow<State<List<StatisticItem>>>(State.Loading())
    override var selectedAddressItem =
        AddressItem(resourcesProvider.getString(R.string.msg_statistic_all_cafes), null)
    override var selectedPeriodItem = PeriodItem(DAY.text)

    override fun getStatistic(cafeId: String?, period: String) {
        statisticState.value = State.Loading()

        val statisticListFlow = if (cafeId == null) {
            when (period) {
                DAY.text -> {
                    orderRepo.getAllCafeOrdersByDay()
                }
                WEEK.text -> {
                    orderRepo.getAllCafeOrdersByWeek()
                }
                MONTH.text -> {
                    orderRepo.getAllCafeOrdersByMonth()
                }
                else -> null
            }
        } else {
            when (period) {
                DAY.text -> {
                    orderRepo.getCafeOrdersByCafeIdAndDay(cafeId)
                }
                WEEK.text -> {
                    orderRepo.getCafeOrdersByCafeIdAndWeek(cafeId)
                }
                MONTH.text -> {
                    orderRepo.getCafeOrdersByCafeIdAndMonth(cafeId)
                }
                else -> null
            }
        }

        statisticListFlow?.onEach { statisticList ->
            statisticState.value = statisticList.map { statistic ->
                val proceeds = orderUtil.getProceeds(statistic.orderList)
                val proceedsString = stringUtil.getCostString(proceeds)
                StatisticItem(
                    statistic = statistic,
                    count = stringUtil.getOrderCountString(statistic.orderList.size),
                    proceeds = proceedsString
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

    override fun goToStatisticDetails(statistic: Statistic) {
        router.navigate(toStatisticDetailsFragment(statistic))
    }
}