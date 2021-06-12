package com.bunbeauty.fooddeliveryadmin.ui.fragments.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.extensions.gone
import com.bunbeauty.fooddeliveryadmin.extensions.launchWhenStarted
import com.bunbeauty.fooddeliveryadmin.extensions.visible
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentLoginBinding
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.presentation.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.startCheckingToken()
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