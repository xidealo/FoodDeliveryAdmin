package com.bunbeauty.fooddeliveryadmin.screen.statistic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementStatisticBinding
import javax.inject.Inject

class StatisticAdapter @Inject constructor(
    statisticDiffUtilItemCallback: StatisticDiffUtilItemCallback
) : ListAdapter<StatisticItemModel, StatisticAdapter.StatisticViewHolder>(
    statisticDiffUtilItemCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ElementStatisticBinding.inflate(
            inflater,
            parent,
            false
        )
        return StatisticViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatisticViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StatisticViewHolder(val binding: ElementStatisticBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(statisticItemModel: StatisticItemModel) {
            binding.elementStatisticTvPeriod.text = statisticItemModel.period
            binding.elementStatisticTvCount.text = statisticItemModel.count
            binding.elementStatisticTvProceeds.text =
                binding.root.resources.getString(R.string.with_ruble, statisticItemModel.proceeds)
        }
    }
}