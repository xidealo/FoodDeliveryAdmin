package com.bunbeauty.fooddeliveryadmin.screen.additiongrouplist.editadditiongroup

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.SwitcherCard
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.TextFieldUi
import com.bunbeauty.presentation.feature.additiongrouplist.editadditiongroup.EditAdditionGroupDataState
import com.bunbeauty.presentation.feature.additiongrouplist.editadditiongroup.EditAdditionGroupViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditAdditionGroupFragment :
    BaseComposeFragment<EditAdditionGroupDataState.DataState, EditAdditionGroupViewState, EditAdditionGroupDataState.Action, EditAdditionGroupDataState.Event>() {
    override val viewModel: EditAdditionGroupViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(EditAdditionGroupDataState.Action.Init)
    }

    @Composable
    override fun Screen(
        state: EditAdditionGroupViewState,
        onAction: (EditAdditionGroupDataState.Action) -> Unit,
    ) {
        EditAdditionGroupScreen(
            state = state,
            onAction = onAction,
        )
    }

    @Composable
    private fun EditAdditionGroupScreen(
        state: EditAdditionGroupViewState,
        onAction: (EditAdditionGroupDataState.Action) -> Unit,
    ) {
        AdminScaffold(
            backgroundColor = AdminTheme.colors.main.surface,
            title = stringResource(R.string.title_edit_addition_group),
            backActionClick = {
                onAction(EditAdditionGroupDataState.Action.OnBackClicked)
            },
        ) {
            when (state.state) {
                EditAdditionGroupViewState.State.Loading -> {
                    LoadingScreen()
                }

                EditAdditionGroupViewState.State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            onAction(EditAdditionGroupDataState.Action.Init)
                        },
                    )
                }

                is EditAdditionGroupViewState.State.Success -> {
                    EditAdditionGroupSuccessScreen(state = state.state, onAction = onAction)
                }
            }
        }
    }

    @Composable
    private fun EditAdditionGroupSuccessScreen(
        state: EditAdditionGroupViewState.State.Success,
        onAction: (EditAdditionGroupDataState.Action) -> Unit,
    ) {
        Column {
            AdminTextField(
                modifier = Modifier.padding(16.dp),
                labelText = stringResource(R.string.hint_edit_addition_group_name),
                value = state.nameField.value,
                onValueChange = { name ->
                    onAction(
                        EditAdditionGroupDataState.Action.EditNameAdditionGroup(name),
                    )
                },
                errorText = stringResource(state.nameField.errorResId),
                isError = state.nameField.isError,
                enabled = !state.isLoading,
            )
            SwitcherCard(
                elevated = false,
                text = stringResource(R.string.msg_edit_addition_group_is_visible_menu),
                checked = state.isVisible,
                onCheckChanged = { isVisible ->
                    onAction(
                        EditAdditionGroupDataState.Action.OnVisibleMenu(
                            isVisible = isVisible,
                        ),
                    )
                },
            )
            AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SwitcherCard(
                hint = stringResource(R.string.msg_edit_addition_group_single_сhoice_hint),
                elevated = false,
                text = stringResource(R.string.msg_edit_addition_group_single_addition_menu),
                checked = state.isVisibleSingleChoice,
                onCheckChanged = { isVisibleSingleChoice ->
                    onAction(
                        EditAdditionGroupDataState.Action.OnVisibleSingleChoice(
                            isVisibleSingleChoice = isVisibleSingleChoice,
                        ),
                    )
                },
            )
            AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.weight(1f))

            LoadingButton(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.action_edit_addition_group_save),
                isLoading = state.isLoading,
                onClick = {
                    onAction(EditAdditionGroupDataState.Action.OnSaveEditAdditionGroupClick)
                },
            )
        }
    }

    override fun handleEvent(event: EditAdditionGroupDataState.Event) {
        when (event) {
            EditAdditionGroupDataState.Event.GoBackEvent -> {
                findNavController().navigateUp()
            }

            is EditAdditionGroupDataState.Event.ShowUpdateAdditionGroupSuccess -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(R.string.msg_edit_addition_group_updated, event.additionGroupName),
                )
                findNavController().popBackStack()
            }
        }
    }

    @Composable
    override fun mapState(state: EditAdditionGroupDataState.DataState): EditAdditionGroupViewState =
        EditAdditionGroupViewState(
            state =
                when (state.state) {
                    EditAdditionGroupDataState.DataState.State.LOADING -> EditAdditionGroupViewState.State.Loading
                    EditAdditionGroupDataState.DataState.State.ERROR -> EditAdditionGroupViewState.State.Error
                    EditAdditionGroupDataState.DataState.State.SUCCESS ->
                        EditAdditionGroupViewState.State.Success(
                            isLoading = state.isLoading,
                            isVisible = state.isVisible,
                            isVisibleSingleChoice = state.isSingleChoice,
                            nameField =
                                TextFieldUi(
                                    value = state.name.value,
                                    isError = state.name.isError,
                                    errorResId =
                                        when (state.nameStateError) {
                                            EditAdditionGroupDataState.DataState.NameStateError.EMPTY_NAME ->
                                                R.string.error_common_edit_addition_group_empty_name

                                            EditAdditionGroupDataState.DataState.NameStateError.DUPLICATE_NAME ->
                                                R.string.error_common_edit_addition_group_duplicate_name

                                            EditAdditionGroupDataState.DataState.NameStateError.NO_ERROR ->
                                                R.string.error_common_something_went_wrong
                                        },
                                ),
                        )
                },
        )

    @Preview(showSystemUi = true)
    @Composable
    fun EditCategoryPreview() {
        AdminTheme {
            EditAdditionGroupScreen(
                state =
                    EditAdditionGroupViewState(
                        state =
                            EditAdditionGroupViewState.State.Success(
                                nameField =
                                    TextFieldUi(
                                        value = "",
                                        isError = false,
                                        errorResId = 0,
                                    ),
                                isLoading = false,
                                isVisible = true,
                                isVisibleSingleChoice = true,
                            ),
                    ),
                onAction = {},
            )
        }
    }
}
