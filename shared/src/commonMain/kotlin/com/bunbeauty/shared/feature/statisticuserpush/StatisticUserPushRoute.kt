package com.bunbeauty.shared.feature.statisticuserpush

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
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
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.feature.statisticuserpush.navigation.StatisticUserPushScreenDestination
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_statistic_user_push_send
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_push_body
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_push_title
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_hint
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_saved
import fooddeliveryadmin.shared.generated.resources.title_statistic_user_push
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

private const val MAX_TITLE_SYMBOLS = 80
private const val MAX_BODY_SYMBOLS = 250
private const val BODY_MAX_LINES = 4

@Composable
fun StatisticUserPushRouteScreen(
    backStackEntry: NavBackStackEntry,
    viewModel: StatisticUserPushViewModel = koinViewModel(),
    goBack: () -> Unit,
    showInfoMessage: (String, Dp) -> Unit,
) {
    val route = backStackEntry.toRoute<StatisticUserPushScreenDestination>()
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { action: StatisticUserPush.Action ->
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
        onAction(
            StatisticUserPush.Action.Init(
                phoneNumber = route.phoneNumber,
            ),
        )
    }

    StatisticUserPushEffect(
        effects = effects,
        goBack = goBack,
        showInfoMessage = showInfoMessage,
        consumeEffects = consumeEffects,
    )

    StatisticUserPushScreen(
        state = viewState.toViewState(),
        onAction = onAction,
    )
}

@Composable
private fun StatisticUserPushEffect(
    effects: List<StatisticUserPush.Event>,
    goBack: () -> Unit,
    showInfoMessage: (String, Dp) -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                StatisticUserPush.Event.GoBack -> goBack()
                StatisticUserPush.Event.ShowSavedMessage -> {
                    showInfoMessage(
                        getString(Res.string.msg_statistic_user_push_saved),
                        ButtonDefaults.MinHeight + 12.dp,
                    )
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun StatisticUserPushScreen(
    state: StatisticUserPushViewState,
    onAction: (StatisticUserPush.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_statistic_user_push),
        backgroundColor = AdminTheme.colors.main.surface,
        backActionClick = {
            onAction(StatisticUserPush.Action.BackClick)
        },
        actionButton = {
            LoadingButton(
                modifier =
                    Modifier
                        .padding(horizontal = 16.dp)
                        .bottomBarPadding(),
                text = stringResource(Res.string.action_statistic_user_push_send),
                isLoading = state.isLoading,
                onClick = {
                    onAction(StatisticUserPush.Action.OnSendClick)
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
                        append(stringResource(Res.string.msg_statistic_user_push_hint))
                        append(" ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(state.phoneNumber)
                        }
                    },
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.main.onSurface,
            )

            Spacer(modifier = Modifier.height(16.dp))

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(Res.string.hint_statistic_user_push_title),
                value = state.titleField.value,
                onValueChange = { title ->
                    onAction(StatisticUserPush.Action.TitleChanged(title))
                },
                maxSymbols = MAX_TITLE_SYMBOLS,
                isError = state.titleField.isError,
                errorText = state.titleField.errorResId,
                enabled = !state.isLoading,
            )

            Spacer(modifier = Modifier.height(16.dp))

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(Res.string.hint_statistic_user_push_body),
                value = state.bodyField.value,
                onValueChange = { body ->
                    onAction(StatisticUserPush.Action.BodyChanged(body))
                },
                maxSymbols = MAX_BODY_SYMBOLS,
                maxLines = BODY_MAX_LINES,
                isError = state.bodyField.isError,
                errorText = state.bodyField.errorResId,
                enabled = !state.isLoading,
            )
        }
    }
}

@Preview
@Composable
private fun StatisticUserPushScreenPreview() {
    AdminTheme {
        StatisticUserPushScreen(
            state =
                StatisticUserPushViewState(
                    phoneNumber = "+7 (996)-922-41-86",
                    titleField =
                        TextFieldUi(
                            value = "",
                            isError = false,
                            errorResId = null,
                        ),
                    bodyField =
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
