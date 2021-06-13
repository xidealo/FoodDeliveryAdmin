package com.bunbeauty.fooddeliveryadmin.ui.fragments.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.common.State
import com.bunbeauty.common.extensions.gone
import com.bunbeauty.common.extensions.launchWhenStarted
import com.bunbeauty.common.extensions.visible
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentLoginBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.presentation.LoginViewModel
import kotlinx.coroutines.flow.onEach

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginState.onEach { loginState ->
            when(loginState) {
                is State.Loading -> {
                    viewDataBinding.fragmentLoginPbLoading.visible()
                    viewDataBinding.fragmentLoginGroupLogin.gone()
                }
                is State.Empty -> {
                    viewDataBinding.fragmentLoginPbLoading.gone()
                    viewDataBinding.fragmentLoginGroupLogin.visible()
                }
                is State.Error -> {
                    viewDataBinding.fragmentLoginPbLoading.gone()
                    viewDataBinding.fragmentLoginGroupLogin.visible()
                    showError(loginState.message)
                }
                else -> {}
            }
        }.launchWhenStarted(lifecycleScope)

        viewDataBinding.fragmentLoginBtnLogin.setOnClickListener {
            viewModel.login(
                viewDataBinding.fragmentLoginEtLogin.text.toString(),
                viewDataBinding.fragmentLoginEtPassword.text.toString()
            )
        }
    }
}