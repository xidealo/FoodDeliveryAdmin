package com.bunbeauty.fooddeliveryadmin.ui

import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentEditMenuBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.presentation.view_model.EditMenuViewModel

class EditMenuFragment : BaseFragment<FragmentEditMenuBinding, EditMenuViewModel>() {

    override var viewModelVariable: Int = BR.viewModel
    override var layoutId: Int = R.layout.fragment_edit_menu
    override var viewModelClass = EditMenuViewModel::class.java

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

}