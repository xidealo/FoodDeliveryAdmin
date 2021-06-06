package com.bunbeauty.fooddeliveryadmin.ui.fragments.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.domain.util.resources.ResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentCreateMenuProductBinding
import com.bunbeauty.fooddeliveryadmin.extensions.launchWhenStarted
import com.bunbeauty.fooddeliveryadmin.presentation.menu.CreateMenuProductViewModel
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class CreateMenuProductFragment : BaseFragment<FragmentCreateMenuProductBinding>() {

    @Inject
    lateinit var resourcesProvider: IResourcesProvider

    override val viewModel: CreateMenuProductViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.fragmentCreateMenuProductBtnBack.setOnClickListener {
            viewModel.goBack()
        }
        viewModel.isVisible.onEach { isVisible ->
            if (isVisible) {
                viewDataBinding.fragmentCreateMenuProductBtnVisibility.icon =
                    resourcesProvider.getDrawable(R.drawable.ic_visible)
            } else {
                viewDataBinding.fragmentCreateMenuProductBtnVisibility.icon =
                    resourcesProvider.getDrawable(R.drawable.ic_invisible)
            }
        }.launchWhenStarted(lifecycleScope)
        viewDataBinding.fragmentCreateMenuProductBtnVisibility.setOnClickListener {
            viewModel.switchVisibility()
        }
        viewDataBinding.fragmentCreateMenuProductNcvProductCode.cardText = "fff"
    }
}