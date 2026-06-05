package com.bunbeauty.shared.feature.statisticdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.domain.model.statistic.StatisticDetailPeriod
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.msg_statistic_detail_date
import fooddeliveryadmin.shared.generated.resources.msg_statistic_detail_period_label
import fooddeliveryadmin.shared.generated.resources.msg_statistic_detail_period_range
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@Immutable
data class StatisticDetailsViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val periodHintText: String,
            val periodSummaryText: String,
            val orderCount: Int,
            val orderProceedsTotal: Int,
            val orderProceedsProducts: Int,
            val averageCheck: Double,
            val deliveryOrderCount: Int,
            val pickupOrderCount: Int,
            val currency: String,
            val products: ImmutableList<ProductRow>,
        ) : State {
            @Immutable
            data class ProductRow(
                val menuProductUuid: String,
                val name: String,
                val photoLink: String,
                val productCount: Int,
                val proceeds: Int,
                val currency: String,
            )
        }
    }
}

@Composable
internal fun StatisticDetails.DataState.toViewState(): StatisticDetailsViewState =
    StatisticDetailsViewState(
        state =
            when (state) {
                StatisticDetails.DataState.State.LOADING ->
                    StatisticDetailsViewState.State.Loading

                StatisticDetails.DataState.State.ERROR ->
                    StatisticDetailsViewState.State.Error

                StatisticDetails.DataState.State.SUCCESS -> {
                    val detail = dayDetail
                    if (detail != null) {
                        StatisticDetailsViewState.State.Success(
                            periodHintText =
                                when (detail.periodType) {
                                    StatisticDetailPeriod.DAY ->
                                        stringResource(Res.string.msg_statistic_detail_date)

                                    else ->
                                        stringResource(Res.string.msg_statistic_detail_period_label)
                                },
                            periodSummaryText =
                                when (detail.periodType) {
                                    StatisticDetailPeriod.DAY -> detail.date

                                    else ->
                                        if (detail.periodStart == detail.periodEnd) {
                                            detail.periodStart
                                        } else {
                                            stringResource(
                                                Res.string.msg_statistic_detail_period_range,
                                                detail.periodStart,
                                                detail.periodEnd,
                                            )
                                        }
                                },
                            orderCount = detail.orderCount,
                            orderProceedsTotal = detail.orderProceedsTotal,
                            orderProceedsProducts = detail.orderProceedsProducts,
                            averageCheck = round(detail.averageCheck),
                            deliveryOrderCount = detail.deliveryOrderCount,
                            pickupOrderCount = detail.pickupOrderCount,
                            currency = detail.currency,
                            products =
                                detail.products
                                    .map { product ->
                                        StatisticDetailsViewState.State.Success.ProductRow(
                                            menuProductUuid = product.menuProductUuid,
                                            name = product.name,
                                            photoLink = product.photoLink,
                                            productCount = product.productCount,
                                            proceeds = product.proceeds,
                                            currency = product.currency,
                                        )
                                    }.toPersistentList(),
                        )
                    } else {
                        StatisticDetailsViewState.State.Loading
                    }
                }
            },
    )
