package com.bunbeauty.fooddeliveryadmin.ui.statistic

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.data.model.Statistic
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentStatisticBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.adapter.StatisticAdapter
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.presentation.view_model.StatisticViewModel
import javax.inject.Inject

class StatisticFragment : BaseFragment<FragmentStatisticBinding>() {

    override var layoutId = R.layout.fragment_statistic
    override val viewModel: StatisticViewModel by viewModels { modelFactory }

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var statisticAdapter: StatisticAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statisticAdapter.onItemClickListener = {
            goToSelectedStatistic(it)
        }
        viewDataBinding.fragmentStatisticRvResult.adapter = statisticAdapter

        viewDataBinding.fragmentStatisticBtnGetStatistic.setOnClickListener {
            getStatistic()
        }
    }

    private fun getStatistic() {
        val daysCountString = viewDataBinding.fragmentStatisticEtDaysCount.text.toString()
        if (daysCountString.isEmpty()) {
            viewDataBinding.fragmentStatisticEtDaysCount.error =
                resources.getString(R.string.error_statics_days_count)
            viewDataBinding.fragmentStatisticEtDaysCount.requestFocus()
            viewModel.isLoadingField.set(false)
            return
        }
        viewModel.getStatistic(
            viewDataBinding.fragmentStatisticEtDaysCount.text.toString().toInt(),
            viewDataBinding.fragmentStatisticCbAllCafes.isChecked,
            viewDataBinding.fragmentStatisticCbAllTime.isChecked
        )
    }

    private fun goToSelectedStatistic(statistic: Statistic) {
        findNavController().navigate(
            StatisticFragmentDirections.actionStatisticFragmentToSelectedStatisticBottomSheet(
                statistic
            )
        )
    }
}