package com.bunbeauty.fooddeliveryadmin.ui.statistic

import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentStatisticBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.view_model.StatisticViewModel
import java.lang.ref.WeakReference

class StatisticFragment : BaseFragment<FragmentStatisticBinding, StatisticViewModel>(),
    StatisticNavigator {

    override var viewModelVariable: Int = BR.viewModel
    override var layoutId: Int = R.layout.fragment_statistic
    override var viewModelClass = StatisticViewModel::class.java
    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = WeakReference(this)
    }

    override fun getStatistic() {
        viewModel.getStatistic(viewDataBinding.fragmentStatisticEtDaysCount.text.toString().toInt())
    }
}