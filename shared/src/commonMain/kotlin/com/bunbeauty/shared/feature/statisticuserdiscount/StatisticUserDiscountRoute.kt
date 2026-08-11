package com.bunbeauty.shared.feature.statisticuserdiscount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.TextFieldUi
import com.bunbeauty.shared.designsystem.compose.bottomBarPadding
import com.bunbeauty.shared.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.shared.designsystem.compose.element.textfield.AdminTextField
import com.bunbeauty.shared.designsystem.compose.element.textfield.AdminTextFieldDefaults
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.feature.statisticuserdiscount.navigation.StatisticUserDiscountScreenDestination
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_statistic_user_discount_send
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_discount_percent
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_discount_hint
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_discount_next_order_suffix
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_discount_saved
import fooddeliveryadmin.shared.generated.resources.title_statistic_user_discount
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

private const val MAX_PERCENT_SYMBOLS = 2

@Composable
fun StatisticUserDiscountRouteScreen(
    backStackEntry: NavBackStackEntry,
    viewModel: StatisticUserDiscountViewModel = koinViewModel(),
    goBack: () -> Unit,
    onDiscountSaved: (Int) -> Unit,
    showInfoMessage: (String, Dp) -> Unit,
) {
    val route = backStackEntry.toRoute<StatisticUserDiscountScreenDestination>()
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { action: StatisticUserDiscount.Action ->
                viewModel.onAction(action)
            }
        }

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }

    LaunchedEffect(route.phoneNumber) {
        onAction(StatisticUserDiscount.Action.Init(phoneNumber = route.phoneNumber))
    }

    StatisticUserDiscountEffect(
        effects = effects,
        goBack = goBack,
        onDiscountSaved = onDiscountSaved,
        showInfoMessage = showInfoMessage,
        consumeEffects = consumeEffects,
    )

    StatisticUserDiscountScreen(
        state = viewState.toViewState(),
        onAction = onAction,
    )
}

@Composable
private fun StatisticUserDiscountEffect(
    effects: List<StatisticUserDiscount.Event>,
    goBack: () -> Unit,
    onDiscountSaved: (Int) -> Unit,
    showInfoMessage: (String, Dp) -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                StatisticUserDiscount.Event.GoBack -> goBack()
                is StatisticUserDiscount.Event.ShowSavedMessage -> {
                    onDiscountSaved(effect.personalDiscountPercent ?: RESET_PERSONAL_DISCOUNT_PERCENT)
                    showInfoMessage(
                        getString(Res.string.msg_statistic_user_discount_saved),
                        0.dp,
                    )
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

private const val RESET_PERSONAL_DISCOUNT_PERCENT = 0

@Composable
private fun StatisticUserDiscountScreen(
    state: StatisticUserDiscountViewState,
    onAction: (StatisticUserDiscount.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_statistic_user_discount),
        backgroundColor = AdminTheme.colors.main.surface,
        backActionClick = {
            onAction(StatisticUserDiscount.Action.BackClick)
        },
        actionButton = {
            LoadingButton(
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .bottomBarPadding(),
                text = stringResource(Res.string.action_statistic_user_discount_send),
                isLoading = state.isLoading,
                onClick = {
                    onAction(StatisticUserDiscount.Action.OnSendClick)
                },
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text =
                    buildAnnotatedString {
                        append(stringResource(Res.string.msg_statistic_user_discount_hint))
                        append(" ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(state.phoneNumber)
                        }
                        append(" ")
                        append(stringResource(Res.string.msg_statistic_user_discount_next_order_suffix))
                    },
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.main.onSurface,
            )

            Spacer(modifier = Modifier.height(16.dp))

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(Res.string.hint_statistic_user_discount_percent),
                value = state.percentField.value,
                onValueChange = { percent ->
                    onAction(StatisticUserDiscount.Action.PercentChanged(percent))
                },
                keyboardOptions =
                    AdminTextFieldDefaults.keyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                maxSymbols = MAX_PERCENT_SYMBOLS,
                isError = state.percentField.isError,
                errorText = state.percentField.errorResId,
                enabled = !state.isLoading,
            )
        }
    }
}

@Preview
@Composable
private fun StatisticUserDiscountScreenPreview() {
    AdminTheme {
        StatisticUserDiscountScreen(
            state =
                StatisticUserDiscountViewState(
                    phoneNumber = "+7 (996)-922-41-86",
                    percentField =
                        TextFieldUi(
                            value = "",
                            isError = false,
                            errorResId = null,
                        ),
                    isLoading = false,
                ),
            onAction = {},
        )
    }
}
