package com.bunbeauty.fooddeliveryadmin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bunbeauty.data.model.Statistic
import com.bunbeauty.fooddeliveryadmin.databinding.ElementStatisticBinding
import com.bunbeauty.fooddeliveryadmin.ui.statistic.StatisticNavigator
import com.bunbeauty.domain.string_helper.IStringHelper
import java.lang.ref.WeakReference
import javax.inject.Inject

class StatisticAdapter @Inject constructor(
    private val context: Context,
    private val iStringHelper: IStringHelper
) : BaseAdapter<StatisticAdapter.StatisticViewHolder, Statistic>() {

    lateinit var statisticNavigator: WeakReference<StatisticNavigator>

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): StatisticViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ElementStatisticBinding.inflate(inflater, viewGroup, false)

        return StatisticViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: StatisticViewHolder, i: Int) {
        holder.binding?.context = context
        holder.binding?.iStringHelper = iStringHelper
        holder.binding?.statistic = itemList[i]
        holder.setListener(itemList[i])
    }

    inner class StatisticViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ElementStatisticBinding>(view)
        fun setListener(statistic: Statistic) {
            binding?.elementOrderMvcMain?.setOnClickListener {
                statisticNavigator.get()?.goToSelectedStatistic(statistic)
            }
        }
    }
}