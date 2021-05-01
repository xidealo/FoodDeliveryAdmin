package com.bunbeauty.fooddeliveryadmin.ui.base

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding

abstract class BarsFragment<T : ViewDataBinding> : BaseFragment<T>() {

    //override val viewModel: ToolbarViewModel by viewModels { modelFactory }

    open val isToolbarVisible = true
    open val isToolbarLogoVisible = false
    open val isToolbarCartProductVisible = true

    open val isBottomBarVisible = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? IBottomNavigationBar)?.setupBottomNavigationBar(isBottomBarVisible)

     /*   (activity as? IToolbar)?.setToolbarConfiguration(
            isToolbarVisible,
            isToolbarLogoVisible,
            isToolbarCartProductVisible
        )
       */
    }
}