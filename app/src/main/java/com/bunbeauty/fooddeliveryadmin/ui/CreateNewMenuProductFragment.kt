package com.bunbeauty.fooddeliveryadmin.ui

import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentCreateNewMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.presentation.view_model.CreateNewMenuProductViewModel

class CreateNewMenuProductFragment : BaseFragment<FragmentCreateNewMenuProductBinding>() {

    override var layoutId = R.layout.fragment_create_new_menu_product
    override val viewModel: CreateNewMenuProductViewModel by viewModels { modelFactory }

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

}