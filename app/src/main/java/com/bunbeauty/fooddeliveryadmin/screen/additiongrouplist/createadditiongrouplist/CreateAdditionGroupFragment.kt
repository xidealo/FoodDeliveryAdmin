package com.bunbeauty.fooddeliveryadmin.screen.additiongrouplist.createadditiongrouplist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist.CreateAdditionGroupDataState
import com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist.CreateAdditionGroupViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateAdditionGroupFragment :
    BaseComposeFragment<CreateAdditionGroupDataState.DataState, CreateAdditionGroupViewState, CreateAdditionGroupDataState.Action, CreateAdditionGroupDataState.Event>() {

    override val viewModel: CreateAdditionGroupViewModel by viewModel()

    @Composable
    override fun mapState(state: CreateAdditionGroupDataState.DataState): CreateAdditionGroupViewState {
        return CreateAdditionGroupViewState(
            state = when (state.state) {
                CreateAdditionGroupDataState.DataState.State.SUCCESS -> CreateAdditionGroupViewState.State.Success(
                    isLoading = state.isLoading,
                    nameField = TextFieldUi(
                        value = state.nameField.value,
                        isError = state.nameField.isError,
                        errorResId = when (state.nameStateError) {
                            CreateAdditionGroupDataState.DataState.NameStateError.EMPTY_NAME ->
                                R.string.error_common_create_addition_group_name

                            CreateAdditionGroupDataState.DataState.NameStateError.DUPLICATE_NAME ->
                                R.string.error_common_create_addition_group_duplicate_name

                            CreateAdditionGroupDataState.DataState.NameStateError.NO_ERROR ->
                                R.string.error_common_something_went_wrong
                        }
                    ),
                    singleChoice = state.singleChoice,
                    isShowMenuVisible = state.isShowMenuVisible
                )

                CreateAdditionGroupDataState.DataState.State.ERROR -> CreateAdditionGroupViewState.State.Error
                CreateAdditionGroupDataState.DataState.State.LOADING -> CreateAdditionGroupViewState.State.Loading
            }
        )
    }

    @Composable
    override fun Screen(
        state: CreateAdditionGroupViewState,
        onAction: (CreateAdditionGroupDataState.Action) -> Unit
    ) {
        CreateAdditionGroupScreen(
            state = state,
            onAction = onAction
        )
    }

    @Composable
    private fun CreateAdditionGroupScreen(
        state: CreateAdditionGroupViewState,
        onAction: (CreateAdditionGroupDataState.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_create_addition_group),
            backgroundColor = AdminTheme.colors.main.surface,
            pullRefreshEnabled = true,
            backActionClick = {
                onAction(CreateAdditionGroupDataState.Action.OnBackClick)
            }
        ) {
            when (state.state) {
                CreateAdditionGroupViewState.State.Error -> ErrorScreen(
                    mainTextId = R.string.title_common_can_not_load_data,
                    extraTextId = R.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(CreateAdditionGroupDataState.Action.OnErrorStateClicked)
                    }
                )

                CreateAdditionGroupViewState.State.Loading -> LoadingScreen()

                is CreateAdditionGroupViewState.State.Success -> CreateAdditionGroupScreenSuccess(
                    state = state.state,
                    onAction = onAction
                )
            }
        }
    }

    @Composable
    private fun CreateAdditionGroupScreenSuccess(
        state: CreateAdditionGroupViewState.State.Success,
        onAction: (CreateAdditionGroupDataState.Action) -> Unit
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(R.string.hint_edit_create_addition_group_name),
                value = state.nameField.value,
                onValueChange = { name ->
                    onAction(
                        CreateAdditionGroupDataState.Action.CreateNameAdditionGroupChanged(
                            name
                        )
                    )
                },
                errorText = stringResource(state.nameField.errorResId),
                isError = state.nameField.isError,
                enabled = !state.isLoading
            )
            SwitcherCard(
                modifier = Modifier.padding(vertical = 8.dp),
                elevated = false,
                text = stringResource(R.string.action_addition_list_group_show_in_menu),
                checked = state.isShowMenuVisible,
                onCheckChanged = { isVisible ->
                    onAction(
                        CreateAdditionGroupDataState.Action.OnVisibleClick
                    )
                }
            )
            AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            SwitcherCard(
                hint = stringResource(R.string.msg_addition_list_group_appliances_hint),
                modifier = Modifier.padding(vertical = 8.dp),
                elevated = false,
                text = stringResource(R.string.msg_addition_list_group_switcher_card),
                checked = state.singleChoice,
                onCheckChanged = { isOneVisible ->
                    onAction(
                        CreateAdditionGroupDataState.Action.OnOneAdditionVisibleClick
                    )
                }
            )
            AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.weight(1f))
            LoadingButton(
                text = stringResource(R.string.action_create_category_save),
                isLoading = state.isLoading,
                onClick = {
                    onAction(CreateAdditionGroupDataState.Action.OnSaveAdditionGroupClick)
                }
            )
        }
    }

    override fun handleEvent(event: CreateAdditionGroupDataState.Event) {
        when (event) {
            CreateAdditionGroupDataState.Event.GoBackEvent -> findNavController().navigateUp()
            is CreateAdditionGroupDataState.Event.ShowUpdateAdditionGroupSuccess -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(
                        R.string.msg_create_addition_group_created,
                        event.additionGroupName
                    )
                )
                findNavController().popBackStack()
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun CreateCategoryPreview() {
        AdminTheme {
            CreateAdditionGroupScreen(
                state = CreateAdditionGroupViewState(
                    state = CreateAdditionGroupViewState.State.Success(
                        isLoading = false,
                        nameField = TextFieldUi(
                            value = "",
                            isError = false,
                            errorResId = 0
                        ),
                        singleChoice = true,
                        isShowMenuVisible = true
                    )
                ),
                onAction = {}
            )
        }
    }
}
