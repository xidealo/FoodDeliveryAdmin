package com.bunbeauty.fooddeliveryadmin.ui.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.bunbeauty.fooddeliveryadmin.FoodDeliveryAdminApplication
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.main.MainActivity
import com.bunbeauty.presentation.view_model.BaseViewModel
import javax.inject.Inject

abstract class BaseDialog<T : ViewDataBinding, V : com.bunbeauty.presentation.view_model.BaseViewModel<*>> : DialogFragment() {

    abstract var layoutId: Int
    abstract var dataBindingVariable: Int
    abstract var viewModelClass: Class<V>

    lateinit var viewDataBinding: T
    private var mActivity: MainActivity? = null

    lateinit var viewModel: V

    @Inject
    lateinit var modelFactory: ViewModelProvider.Factory

    protected var width = 0
    protected var height = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)

        width = MATCH_PARENT
        height = WRAP_CONTENT

        val viewModelComponent = (requireActivity().application as FoodDeliveryAdminApplication).appComponent
            .getViewModelComponent()
            .create(this)
        inject(viewModelComponent)

        viewModel = ViewModelProvider(this, modelFactory).get(viewModelClass)

        if (context is MainActivity) {
            val mActivity: MainActivity = context
            this.mActivity = mActivity
        }
    }

    abstract fun inject(viewModelComponent: ViewModelComponent)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(LinearLayout(context))
            setCanceledOnTouchOutside(false)
            window?.setLayout(width, height)
        }
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

        viewDataBinding.setVariable(dataBindingVariable, viewModel)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.executePendingBindings()
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    override fun show(fragmentManager: FragmentManager, tag: String?) {
        val transaction = fragmentManager.beginTransaction()
        val prevFragment = fragmentManager.findFragmentByTag(tag)
        if (prevFragment != null) {
            transaction.remove(prevFragment)
        }
        transaction.addToBackStack(null)
        show(transaction, tag)
    }

    open fun dismissDialog() {
        dismiss()
    }

    open fun getBaseActivity(): MainActivity? {
        return mActivity
    }
}