package com.bunbeauty.fooddeliveryadmin.screen.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentLoginBinding
import com.bunbeauty.fooddeliveryadmin.util.gone
import com.bunbeauty.fooddeliveryadmin.util.startedLaunch
import com.bunbeauty.fooddeliveryadmin.util.visible
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.screen.order_list.OrderListFragmentDirections
import com.bunbeauty.fooddeliveryadmin.screen.order_list.OrderListState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentLoginBtnLogin.setOnClickListener {
                viewModel.login(
                    fragmentLoginEtLogin.text.toString(),
                    fragmentLoginEtPassword.text.toString()
                )
            }
        }

        viewModel.loginViewState.collectWithLifecycle { state ->
            if (state.isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
            state.appVersion?.let { version ->
                binding.versionTextView.text =
                    resources.getString(R.string.msg_login_version, version)
            }
            handleEvents(state.events)
        }
    }

    private fun handleEvents(eventList: List<LoginViewState.Event>) {
        eventList.forEach { event ->
            when (event) {
                is LoginViewState.Event.OpenOrderListEvent -> {
                    findNavController().navigate(LoginFragmentDirections.toOrdersFragment())
                }
                is LoginViewState.Event.ShowLoginError -> {
                    showSnackbar(
                        resources.getString(R.string.error_login_wrong_data),
                        R.color.lightTextColor,
                        R.color.errorColor
                    )
                }
            }
        }
        viewModel.consumeEvents(eventList)
    }

    private fun showLoading() {
        binding.apply {
            fragmentLoginPbLoading.isVisible = true
            fragmentLoginGroupLogin.isVisible = false
        }
    }

    private fun hideLoading() {
        binding.apply {
            fragmentLoginPbLoading.isVisible = false
            fragmentLoginGroupLogin.isVisible = true
        }
    }
}