package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationTextCard
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.selectaddition.SelectAdditionListFragment.Companion.ADDITION_LIST_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.selectaddition.SelectAdditionListFragment.Companion.SELECT_ADDITION_LIST_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.selectadditiongroup.SelectAdditionGroupFragment.Companion.ADDITION_GROUP_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.selectadditiongroup.SelectAdditionGroupFragment.Companion.SELECT_ADDITION_GROUP_KEY
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenu
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenuProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditAdditionGroupForMenuProductFragment :
    SingleStateComposeFragment<EditAdditionGroupForMenu.DataState, EditAdditionGroupForMenu.Action, EditAdditionGroupForMenu.Event>() {
    override val viewModel: EditAdditionGroupForMenuProductViewModel by viewModel()
    private val editAdditionGroupForMenuProductFragmentArgs: EditAdditionGroupForMenuProductFragmentArgs by navArgs()

    companion object {
        const val EDIT_ADDITION_GROUP = "EDIT_ADDITION_GROUP"
        const val EDIT_ADDITION_GROUP_KEY = "ADDITION_GROUP_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(
            EditAdditionGroupForMenu.Action.Init(
                additionGroupForMenuUuid =
                    editAdditionGroupForMenuProductFragmentArgs
                        .additionGroupForMenuUuid,
                menuProductUuid = editAdditionGroupForMenuProductFragmentArgs.menuProductUuid,
            ),
        )

        setFragmentResultListener(SELECT_ADDITION_GROUP_KEY) { _, bundle ->
            viewModel.onAction(
                EditAdditionGroupForMenu.Action.SelectAdditionGroup(
                    additionGroupUuid = bundle.getString(ADDITION_GROUP_KEY).orEmpty(),
                ),
            )
        }

        setFragmentResultListener(SELECT_ADDITION_LIST_KEY) { _, bundle ->
            viewModel.onAction(
                EditAdditionGroupForMenu.Action.SelectAdditionList(
                    additionListUuid =
                        bundle.getStringArrayList(ADDITION_LIST_KEY)
                            ?: emptyList(),
                ),
            )
        }
    }

    @Composable
    override fun Screen(
        state: EditAdditionGroupForMenu.DataState,
        onAction: (EditAdditionGroupForMenu.Action) -> Unit,
    ) {
        EditAdditionGroupForMenuProductScreen(state = state, onAction = onAction)
    }

    @Composable
    fun EditAdditionGroupForMenuProductScreen(
        state: EditAdditionGroupForMenu.DataState,
        onAction: (EditAdditionGroupForMenu.Action) -> Unit,
    ) {
        AdminScaffold(
            title = state.groupName ?: stringResource(R.string.title_common_loading),
            backActionClick = {
                onAction(EditAdditionGroupForMenu.Action.OnBackClick)
            },
            backgroundColor = AdminTheme.colors.main.surface,
        ) {
            when (state.state) {
                EditAdditionGroupForMenu.DataState.State.LOADING -> LoadingScreen()
                EditAdditionGroupForMenu.DataState.State.ERROR -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            // todo refresh data
                            // onAction(OrderList.Action.RetryClick)
                        },
                    )
                }

                EditAdditionGroupForMenu.DataState.State.SUCCESS ->
                    EditAdditionGroupForMenuProductSuccessScreen(
                        state = state,
                        onAction = onAction,
                    )
            }
        }
    }

    @Composable
    fun EditAdditionGroupForMenuProductSuccessScreen(
        state: EditAdditionGroupForMenu.DataState,
        onAction: (EditAdditionGroupForMenu.Action) -> Unit,
    ) {
        Column {
            NavigationTextCard(
                valueText = state.groupName,
                onClick = {
                    onAction(
                        EditAdditionGroupForMenu.Action.OnAdditionGroupClick(
                            uuid = state.additionGroupForMenuProductUuid,
                        ),
                    )
                },
                elevated = false,
                labelText =
                    stringResource(
                        id = R.string.title_edit_addition_group_for_menu_product_group,
                    ),
                hasDivider = true,
            )

            NavigationTextCard(
                valueText = state.additionNameList,
                onClick = {
                    onAction(
                        EditAdditionGroupForMenu.Action.OnAdditionListClick(
                            uuid = state.additionGroupForMenuProductUuid,
                        ),
                    )
                },
                elevated = false,
                labelText =
                    stringResource(
                        id = R.string.title_edit_addition_group_for_menu_product_addition,
                    ),
                hasDivider = true,
            )

            Spacer(modifier = Modifier.weight(1f))
            LoadingButton(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.action_edit_addition_group_for_menu_product_save),
                isLoading = state.state == EditAdditionGroupForMenu.DataState.State.LOADING,
                onClick = {
                    onAction(EditAdditionGroupForMenu.Action.OnSaveClick)
                },
            )
        }
    }

    override fun handleEvent(event: EditAdditionGroupForMenu.Event) {
        when (event) {
            EditAdditionGroupForMenu.Event.Back -> {
                findNavController().popBackStack()
            }

            EditAdditionGroupForMenu.Event.SaveAndBack -> {
                setFragmentResult(
                    requestKey = EDIT_ADDITION_GROUP,
                    result = bundleOf(EDIT_ADDITION_GROUP_KEY to true),
                )
                findNavController().popBackStack()
            }

            is EditAdditionGroupForMenu.Event.OnAdditionGroupClick -> {
                findNavController().navigate(
                    directions =
                        EditAdditionGroupForMenuProductFragmentDirections.toSelectAdditionGroupFragment(
                            additionGroupUuid = event.editedAdditionGroupUuid,
                            menuProductUuid = event.menuProductUuid,
                            mainEditedAdditionGroupUuid = event.mainEditedAdditionGroupUuid,
                        ),
                )
            }

            is EditAdditionGroupForMenu.Event.OnAdditionListClick -> {
                findNavController().navigate(
                    directions =
                        EditAdditionGroupForMenuProductFragmentDirections.toSelectAdditionListFragment(
                            additionGroupUuid = event.additionGroupUuid,
                            menuProductUuid = event.menuProductUuid,
                            additionGroupForMenuName = event.additionGroupName,
                            editedAdditionListUuid =
                                event.editedAdditionListUuid.toTypedArray(),
                        ),
                )
            }
        }
    }

    val editAdditionGroupForMenuProductViewState =
        EditAdditionGroupForMenu.DataState(
            state = EditAdditionGroupForMenu.DataState.State.SUCCESS,
            groupName = "Вкусняхи",
            additionNameList = "Бекон * Страпон * Бурбон",
            isVisible = true,
            additionGroupForMenuProductUuid = "",
            menuProductUuid = "",
            editedAdditionGroupUuid = "",
            editedAdditionListUuid = null,
        )

    @Composable
    @Preview
    fun EditAdditionGroupForMenuProductScreenPreview() {
        AdminTheme {
            EditAdditionGroupForMenuProductScreen(
                state = editAdditionGroupForMenuProductViewState,
                onAction = {},
            )
        }
    }
}
