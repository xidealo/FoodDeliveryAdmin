package com.bunbeauty.fooddeliveryadmin.screen.statistic

import androidx.recyclerview.widget.DiffUtil
import com.bunbeauty.presentation.model.StatisticItemModel
import javax.inject.Inject

class StatisticDiffUtilItemCallback @Inject constructor(): DiffUtil.ItemCallback<StatisticItemModel>() {

    override fun areItemsTheSame(
        oldItem: StatisticItemModel,
        newItem: StatisticItemModel
    ): Boolean = oldItem.startMillis == newItem.startMillis

    override fun areContentsTheSame(
        oldItem: StatisticItemModel,
        newItem: StatisticItemModel
    ): Boolean = oldItem == newItem


}