package com.bunbeauty.fooddeliveryadmin.ui.items

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bunbeauty.domain.model.statistic.Statistic
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementStatisticBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatisticItem(
    var statistic: Statistic,
    var count: String,
    var proceeds: String
) : AbstractBindingItem<ElementStatisticBinding>(), Parcelable {

    @IgnoredOnParcel
    override val type = R.id.element_statistic_mvc_main

    override fun bindView(binding: ElementStatisticBinding, payloads: List<Any>) {
        binding.elementStatisticTvPeriod.text = statistic.period
        binding.elementStatisticTvCount.text = count
        binding.elementStatisticTvProceeds.text = proceeds
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementStatisticBinding {
        return ElementStatisticBinding.inflate(inflater, parent, false)
    }
}