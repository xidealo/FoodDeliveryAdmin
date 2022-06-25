package com.bunbeauty.fooddeliveryadmin.ui.fragments.login

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentLoginBinding
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.view_model.LoginViewModel
import com.bunbeauty.ui_core.EditTextType
import com.bunbeauty.ui_core.FoodDeliveryTheme
import com.bunbeauty.ui_core.compose
import com.bunbeauty.ui_core.element.EditText
import com.bunbeauty.ui_core.element.MainButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentLoginCvMain.compose {
            val isLoading by viewModel.isLoading.collectAsState()
            LoginScreen(isLoading)
        }


        /*  binding.run {
              fragmentLoginBtnLogin.setOnClickListener {
                  viewModel.login(
                      fragmentLoginEtLogin.text.toString(),
                      fragmentLoginEtPassword.text.toString()
                  )
              }
          }

          viewModel.run {
              isLoading.onEach { isLoading ->
                  if (isLoading) {
                      binding.fragmentLoginPbLoading.visible()
                      binding.fragmentLoginGroupLogin.gone()
                  } else {
                      binding.fragmentLoginPbLoading.gone()
                      binding.fragmentLoginGroupLogin.visible()
                  }
              }.startedLaunch(viewLifecycleOwner)
          }*/
    }


    @Composable
    private fun LoginScreen(isLoading: Boolean) {
        if (isLoading) {
            LoadingScreen(background = FoodDeliveryTheme.colors.surface)
        } else {
            LoginSuccessScreen()
        }
    }

    @Composable
    private fun LoginSuccessScreen() {

        var loginText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }
        var loginError: Int? by rememberSaveable {
            mutableStateOf(null)
        }

        var passwordText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }
        var passwordError: Int? by rememberSaveable {
            mutableStateOf(null)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FoodDeliveryTheme.colors.surface)
                .padding(FoodDeliveryTheme.dimensions.mediumSpace)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Введите логин и пароль",
                    style = FoodDeliveryTheme.typography.h2,
                    color = FoodDeliveryTheme.colors.onSurface
                )
                EditText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = FoodDeliveryTheme.dimensions.smallSpace),
                    onTextChanged = {
                        loginText = it
                    },
                    textFieldValue = loginText,
                    editTextType = EditTextType.TEXT,
                    labelStringId = R.string.hint_login_login,
                    isLast = false,
                    focus = true,
                    errorMessageId = loginError,
                )

                EditText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = FoodDeliveryTheme.dimensions.smallSpace),
                    onTextChanged = {
                        passwordText = it
                    },
                    textFieldValue = passwordText,
                    editTextType = EditTextType.TEXT,
                    labelStringId = R.string.hint_login_password,
                    isLast = true,
                    focus = false,
                    errorMessageId = passwordError,
                )
            }
            MainButton(
                modifier = Modifier.align(Alignment.BottomCenter),
                textStringId = R.string.action_login_enter
            ) {

                viewModel.isCorrectUsername(loginText.text)?.let { error ->
                    loginError = error
                    return@MainButton
                }

                viewModel.isCorrectPassword(passwordText.text)?.let { error ->
                    passwordError = error
                    return@MainButton
                }

                viewModel.login(loginText.text.trim(), passwordText.text)
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun LoginScreenPreview() {
        LoginScreen(false)
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun LoginScreenLoadingPreview() {
        LoginScreen(true)
    }


}