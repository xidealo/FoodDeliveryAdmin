package com.bunbeauty.fooddeliveryadmin.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.bunbeauty.fooddeliveryadmin.FoodDeliveryAdminApplication
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.presentation.view_model.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

abstract class BaseFragment<B : ViewDataBinding> : Fragment(){

    abstract var layoutId: Int
    lateinit var viewDataBinding: B
    abstract val viewModel: BaseViewModel

    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val viewModelComponent =
            (requireActivity().application as FoodDeliveryAdminApplication).appComponent
                .getViewModelComponent()
                .create(this)
        inject(viewModelComponent)
    }

    abstract fun inject(viewModelComponent: ViewModelComponent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.lifecycleOwner = this
        viewDataBinding.executePendingBindings()
    }

    protected fun <T> subscribe(liveData: LiveData<T>, observer: (T) -> Unit) {
        liveData.observe(viewLifecycleOwner, observer::invoke)
    }

    fun <T> Flow<T>.launchWhenStarted(lifecycleCoroutineScope: LifecycleCoroutineScope){
        lifecycleCoroutineScope.launchWhenStarted {
            this@launchWhenStarted.collect()
        }
    }

}