package com.bunbeauty.shared.feature.statisticuserpush

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.TextFieldUi
import com.bunbeauty.shared.designsystem.compose.bottomBarPadding
import com.bunbeauty.shared.designsystem.compose.element.button.AdminButtonDefaults
import com.bunbeauty.shared.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.shared.designsystem.compose.element.button.SecondaryButton
import com.bunbeauty.shared.designsystem.compose.element.textfield.AdminTextField
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.medium
import com.bunbeauty.shared.feature.statisticuserpush.navigation.StatisticUserPushScreenDestination
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_statistic_user_push_send
import fooddeliveryadmin.shared.generated.resources.error_statistic_user_push_body
import fooddeliveryadmin.shared.generated.resources.error_statistic_user_push_title
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_push_custom_body
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_push_custom_title
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_saved
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_variants
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_variants_body
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_push_variants_title
import fooddeliveryadmin.shared.generated.resources.title_statistic_user_push
import fooddeliveryadmin.shared.generated.resources.title_statistic_user_push_custom
import fooddeliveryadmin.shared.generated.resources.title_statistic_user_push_quick
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
    showErrorMessage: (String) -> Unit,
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
        showErrorMessage = showErrorMessage,
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
    showErrorMessage: (String) -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                StatisticUserPush.Event.GoBack -> goBack()
                StatisticUserPush.Event.ShowSentMessage -> {
                    showInfoMessage(
                        getString(Res.string.msg_statistic_user_push_saved),
                        ButtonDefaults.MinHeight + 12.dp,
                    )
                    goBack()
                }

                is StatisticUserPush.Event.ShowErrorMessage ->
                    showErrorMessage(getString(effect.messageResource))
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
                isLoading = state.isCustomSending,
                onClick = {
                    onAction(StatisticUserPush.Action.SendCustomClick)
                },
            )
        },
    ) {
        Column(
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
        ) {
            SectionTitle(text = stringResource(Res.string.title_statistic_user_push_quick))

            state.quickPushes.forEachIndexed { index, quickPush ->
                Spacer(modifier = Modifier.height(if (index == 0) 16.dp else 24.dp))

                QuickPushVariants(template = quickPush.template)

                Spacer(modifier = Modifier.height(16.dp))

                SecondaryButton(
                    textStringId = quickPush.template.buttonTextResource,
                    onClick = {
                        onAction(StatisticUserPush.Action.QuickPushClick(quickPush.template))
                    },
                    elevated = false,
                    isEnabled = quickPush.isEnabled,
                    borderColor = AdminTheme.colors.main.primary,
                    buttonColors =
                        if (quickPush.isSending) {
                            AdminButtonDefaults.accentSecondaryButtonColors
                        } else {
                            ButtonDefaults.buttonColors(
                                containerColor = AdminTheme.colors.main.secondary,
                                contentColor = AdminTheme.colors.main.primary,
                                disabledContainerColor = AdminTheme.colors.main.secondary,
                                disabledContentColor = AdminTheme.colors.main.primary,
                            )
                        },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle(text = stringResource(Res.string.title_statistic_user_push_custom))

            Spacer(modifier = Modifier.height(8.dp))

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(Res.string.hint_statistic_user_push_custom_title),
                value = state.customTitleField.value,
                onValueChange = { title ->
                    onAction(StatisticUserPush.Action.CustomTitleChanged(title))
                },
                maxSymbols = MAX_TITLE_SYMBOLS,
                isError = state.customTitleField.isError,
                errorText = state.customTitleField.errorResId,
                enabled = state.areActionsEnabled,
            )

            Spacer(modifier = Modifier.height(8.dp))

            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(Res.string.hint_statistic_user_push_custom_body),
                value = state.customBodyField.value,
                onValueChange = { body ->
                    onAction(StatisticUserPush.Action.CustomBodyChanged(body))
                },
                maxSymbols = MAX_BODY_SYMBOLS,
                maxLines = BODY_MAX_LINES,
                isError = state.customBodyField.isError,
                errorText = state.customBodyField.errorResId,
                enabled = state.areActionsEnabled,
            )

            Spacer(modifier = Modifier.height(AdminTheme.dimensions.scrollScreenBottomSpace()))
        }
    }
}

@Composable
private fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = text,
        style = AdminTheme.typography.titleMedium.medium,
        color = AdminTheme.colors.main.onSurface,
    )
}

@Composable
private fun QuickPushVariants(
    template: QuickPushTemplate,
    modifier: Modifier = Modifier,
) {
    val variantsLabel = stringResource(Res.string.msg_statistic_user_push_variants)
    val titleLabel = stringResource(Res.string.msg_statistic_user_push_variants_title)
    val bodyLabel = stringResource(Res.string.msg_statistic_user_push_variants_body)
    val titles = template.titleVariants.map { titleVariant -> stringResource(titleVariant) }
    val bodies = template.bodyVariants.map { bodyVariant -> stringResource(bodyVariant) }

    Text(
        modifier = modifier.fillMaxWidth(),
        text =
            buildQuickPushVariantsText(
                variantsLabel = variantsLabel,
                titleLabel = titleLabel,
                titles = titles,
                bodyLabel = bodyLabel,
                bodies = bodies,
            ),
        style = AdminTheme.typography.bodySmall,
        color = AdminTheme.colors.main.onSurfaceVariant,
    )
}

private fun buildQuickPushVariantsText(
    variantsLabel: String,
    titleLabel: String,
    titles: List<String>,
    bodyLabel: String,
    bodies: List<String>,
): String =
    buildList {
        add(variantsLabel)
        add(titleLabel)
        titles.forEachIndexed { index, title ->
            add("${index + 1}. $title")
        }
        add(bodyLabel)
        bodies.forEachIndexed { index, body ->
            add("${index + 1}. $body")
        }
    }.joinToString(separator = "\n")

@Preview
@Composable
private fun StatisticUserPushScreenPreview() {
    AdminTheme {
        StatisticUserPushScreen(
            state =
                StatisticUserPushViewState(
                    quickPushes =
                        QuickPushTemplate.entries.map { template ->
                            QuickPushUi(
                                template = template,
                                isEnabled = true,
                                isSending = false,
                            )
                        },
                    customTitleField =
                        TextFieldUi(
                            value = "",
                            isError = false,
                            errorResId = Res.string.error_statistic_user_push_title,
                        ),
                    customBodyField =
                        TextFieldUi(
                            value = "",
                            isError = false,
                            errorResId = Res.string.error_statistic_user_push_body,
                        ),
                    isCustomSending = false,
                    areActionsEnabled = true,
                ),
            onAction = {},
        )
    }
}
