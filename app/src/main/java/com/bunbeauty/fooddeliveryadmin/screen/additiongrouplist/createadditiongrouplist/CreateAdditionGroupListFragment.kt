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
import com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist.CreateAdditionGroupListDataState
import com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist.CreateAdditionGroupListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateAdditionGroupListFragment :
    BaseComposeFragment<CreateAdditionGroupListDataState.DataState, CreateAdditionGroupListViewState, CreateAdditionGroupListDataState.Action, CreateAdditionGroupListDataState.Event>() {

    override val viewModel: CreateAdditionGroupListViewModel by viewModel()

    @Composable
    override fun mapState(state: CreateAdditionGroupListDataState.DataState): CreateAdditionGroupListViewState {
        return CreateAdditionGroupListViewState(
            state = when (state.state) {
                CreateAdditionGroupListDataState.DataState.State.SUCCESS -> CreateAdditionGroupListViewState.State.Success(
                    isLoading = state.isLoading,
                    nameField = TextFieldUi(
                        value = state.nameField.value,
                        isError = state.nameField.isError,
                        errorResId = when (state.nameStateError) {
                            CreateAdditionGroupListDataState.DataState.NameStateError.EMPTY_NAME ->
                                R.string.error_common_create_category_empty_name

                            CreateAdditionGroupListDataState.DataState.NameStateError.DUPLICATE_NAME ->
                                R.string.error_common_create_category_duplicate_name

                            CreateAdditionGroupListDataState.DataState.NameStateError.NO_ERROR ->
                                R.string.error_common_something_went_wrong
                        }
                    ),
                    singleChoice = state.singleChoice,
                    isShowMenuVisible = state.isShowMenuVisible
                )

                CreateAdditionGroupListDataState.DataState.State.ERROR -> CreateAdditionGroupListViewState.State.Error
                CreateAdditionGroupListDataState.DataState.State.LOADING -> CreateAdditionGroupListViewState.State.Loading
            }
        )
    }

    @Composable
    override fun Screen(
        state: CreateAdditionGroupListViewState,
        onAction: (CreateAdditionGroupListDataState.Action) -> Unit
    ) {
        CreateAdditionGroupListScreen(
            state = state,
            onAction = onAction
        )
    }

    @Composable
    private fun CreateAdditionGroupListScreen(
        state: CreateAdditionGroupListViewState,
        onAction: (CreateAdditionGroupListDataState.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_addition_group_list),
            backgroundColor = AdminTheme.colors.main.surface,
            pullRefreshEnabled = true,
            backActionClick = {
                onAction(CreateAdditionGroupListDataState.Action.OnBackClick)
            }
        ) {
            when (state.state) {
                CreateAdditionGroupListViewState.State.Error -> ErrorScreen(
                    mainTextId = R.string.title_common_can_not_load_data,
                    extraTextId = R.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(CreateAdditionGroupListDataState.Action.OnErrorStateClicked)
                    }
                )

                CreateAdditionGroupListViewState.State.Loading -> LoadingScreen()

                is CreateAdditionGroupListViewState.State.Success -> CreateAdditionGroupListScreenSuccess(
                    state = state.state,
                    onAction = onAction
                )
            }
        }
    }

    @Composable
    private fun CreateAdditionGroupListScreenSuccess(
        state: CreateAdditionGroupListViewState.State.Success,
        onAction: (CreateAdditionGroupListDataState.Action) -> Unit
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AdminTextField(
                modifier = Modifier.fillMaxWidth(),
                labelText = stringResource(R.string.hint_edit_create_addition_list_group_name),
                value = state.nameField.value,
                onValueChange = { name ->
                    onAction(
                        CreateAdditionGroupListDataState.Action.CreateNameAdditionGroupListChanged(
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
                        CreateAdditionGroupListDataState.Action.OnVisibleClick
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
                        CreateAdditionGroupListDataState.Action.OnOneAdditionVisibleClick
                    )
                }
            )
            AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.weight(1f))
            LoadingButton(
                text = stringResource(R.string.action_create_category_save),
                isLoading = state.isLoading,
                onClick = {
                    onAction(CreateAdditionGroupListDataState.Action.OnSaveAdditionGroupListClick)
                }
            )
        }
    }

    override fun handleEvent(event: CreateAdditionGroupListDataState.Event) {
        when (event) {
            CreateAdditionGroupListDataState.Event.GoBackEvent -> findNavController().navigateUp()
            is CreateAdditionGroupListDataState.Event.ShowUpdateAdditionGroupSuccess -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(
                        R.string.msg_create_addition_list_group_created,
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
            CreateAdditionGroupListScreen(
                state = CreateAdditionGroupListViewState(
                    state = CreateAdditionGroupListViewState.State.Success(
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
