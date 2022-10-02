package com.bunbeauty.fooddeliveryadmin.screen.statistic

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementStatisticBinding
import com.bunbeauty.presentation.model.StatisticItemModel
import com.mikepenz.fastadapter.binding.AbstractBindingItem

data class StatisticItem(
    var statisticItemModel: StatisticItemModel
) : AbstractBindingItem<ElementStatisticBinding>() {

    override val type = R.id.element_statistic_mvc_main

    override fun bindView(binding: ElementStatisticBinding, payloads: List<Any>) {
        binding.run {
            elementStatisticTvPeriod.text = statisticItemModel.period
            elementStatisticTvCount.text = statisticItemModel.count
            elementStatisticTvProceeds.text = statisticItemModel.proceeds
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementStatisticBinding {
        return ElementStatisticBinding.inflate(inflater, parent, false)
    }
}
