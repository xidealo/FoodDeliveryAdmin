package com.bunbeauty.fooddeliveryadmin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bunbeauty.data.model.Statistic
import com.bunbeauty.domain.string.IStringUtil
import com.bunbeauty.fooddeliveryadmin.databinding.ElementStatisticBinding
import javax.inject.Inject

class StatisticAdapter @Inject constructor(
    private val iStringUtil: IStringUtil
) : BaseAdapter<StatisticAdapter.StatisticViewHolder, Statistic>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): StatisticViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ElementStatisticBinding.inflate(inflater, viewGroup, false)

        return StatisticViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: StatisticViewHolder, i: Int) {
        holder.binding?.iStringHelper = iStringUtil
        holder.binding?.statistic = itemList[i]
        holder.binding?.elementStatisticMvcMain?.setOnClickListener{
            onItemClickListener?.invoke(itemList[i])
        }
    }

    inner class StatisticViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ElementStatisticBinding>(view)
    }
}