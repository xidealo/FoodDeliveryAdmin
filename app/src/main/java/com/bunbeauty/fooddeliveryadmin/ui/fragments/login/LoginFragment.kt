package com.bunbeauty.fooddeliveryadmin.ui.fragments.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.extensions.gone
import com.bunbeauty.fooddeliveryadmin.extensions.visible
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentLoginBinding
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
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
                    binding.fragmentLoginPbLoading.visible()
                    binding.fragmentLoginGroupLogin.gone()
                }
                is State.Empty -> {
                    binding.fragmentLoginPbLoading.gone()
                    binding.fragmentLoginGroupLogin.visible()
                }
                is State.Error -> {
                    binding.fragmentLoginPbLoading.gone()
                    binding.fragmentLoginGroupLogin.visible()
                    showError(loginState.message)
                }
                else -> {}
            }
        }.startedLaunch(viewLifecycleOwner)

        binding.fragmentLoginBtnLogin.setOnClickListener {
            viewModel.login(
                binding.fragmentLoginEtLogin.text.toString(),
                binding.fragmentLoginEtPassword.text.toString()
            )
        }
    }
}