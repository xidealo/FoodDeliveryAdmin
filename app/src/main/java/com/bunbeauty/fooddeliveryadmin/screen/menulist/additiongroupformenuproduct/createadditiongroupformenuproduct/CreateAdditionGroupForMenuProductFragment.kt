package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct

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
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.CreateAdditionGroupForMenu
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.CreateAdditionGroupForMenuProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateAdditionGroupForMenuProductFragment :
    SingleStateComposeFragment<
        CreateAdditionGroupForMenu.DataState,
        CreateAdditionGroupForMenu.Action,
        CreateAdditionGroupForMenu.Event,
    >() {
    override val viewModel: CreateAdditionGroupForMenuProductViewModel by viewModel()
    private val createAdditionGroupForMenuProductFragmentArgs:
        CreateAdditionGroupForMenuProductFragmentArgs by navArgs()

    companion object {
        const val CREATE_ADDITION_GROUP = "CREATE_ADDITION_GROUP"
        const val CREATE_ADDITION_GROUP_KEY = "ADDITION_GROUP_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(
            CreateAdditionGroupForMenu.Action.Init(
                menuProductUuid = createAdditionGroupForMenuProductFragmentArgs.menuProductUuid,
            ),
        )

        setFragmentResultListener(SELECT_ADDITION_GROUP_KEY) { _, bundle ->
            viewModel.onAction(
                CreateAdditionGroupForMenu.Action.SelectAdditionGroup(
                    additionGroupUuid = bundle.getString(ADDITION_GROUP_KEY).orEmpty(),
                ),
            )
        }

        setFragmentResultListener(SELECT_ADDITION_LIST_KEY) { _, bundle ->
            viewModel.onAction(
                CreateAdditionGroupForMenu.Action.SelectAdditionList(
                    additionListUuid =
                        bundle.getStringArrayList(ADDITION_LIST_KEY)
                            ?: emptyList(),
                ),
            )
        }
    }

    @Composable
    override fun Screen(
        state: CreateAdditionGroupForMenu.DataState,
        onAction: (CreateAdditionGroupForMenu.Action) -> Unit,
    ) {
        CreateAdditionGroupForMenuProductScreen(state = state, onAction = onAction)
    }

    @Composable
    fun CreateAdditionGroupForMenuProductScreen(
        state: CreateAdditionGroupForMenu.DataState,
        onAction: (CreateAdditionGroupForMenu.Action) -> Unit,
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_create_addition_group_for_menu_product),
            backActionClick = {
                onAction(CreateAdditionGroupForMenu.Action.OnBackClick)
            },
            backgroundColor = AdminTheme.colors.main.surface,
        ) {
            when (state.state) {
                CreateAdditionGroupForMenu.DataState.State.LOADING -> LoadingScreen()
                CreateAdditionGroupForMenu.DataState.State.ERROR -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            // todo click
                        },
                    )
                }

                CreateAdditionGroupForMenu.DataState.State.SUCCESS ->
                    CreateAdditionGroupForMenuProductSuccessScreen(
                        state = state,
                        onAction = onAction,
                    )
            }
        }
    }

    @Composable
    fun CreateAdditionGroupForMenuProductSuccessScreen(
        state: CreateAdditionGroupForMenu.DataState,
        onAction: (CreateAdditionGroupForMenu.Action) -> Unit,
    ) {
        Column {
            NavigationTextCard(
                valueText = state.groupName,
                onClick = {
                    onAction(
                        CreateAdditionGroupForMenu.Action.OnAdditionGroupClick,
                    )
                },
                elevated = false,
                labelText =
                    stringResource(
                        id = R.string.title_create_addition_group_for_menu_product_group,
                    ),
                isError = state.groupHasError,
                errorText =
                    stringResource(
                        id = R.string.error_create_addition_group_for_menu_product_group,
                    ),
                hasDivider = true,
            )

            NavigationTextCard(
                valueText = state.additionNameList,
                onClick = {
                    onAction(
                        CreateAdditionGroupForMenu.Action.OnAdditionListClick,
                    )
                },
                elevated = false,
                labelText =
                    stringResource(
                        id = R.string.title_create_addition_group_for_menu_product_addition,
                    ),
                isError = state.additionListHasError,
                errorText =
                    stringResource(
                        id = R.string.error_create_addition_group_for_menu_product_addition,
                    ),
                hasDivider = true,
            )

            Spacer(modifier = Modifier.weight(1f))
            LoadingButton(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.action_create_addition_group_for_menu_product_save),
                isLoading = state.isSaveLoading,
                onClick = {
                    onAction(CreateAdditionGroupForMenu.Action.OnSaveClick)
                },
            )
        }
    }

    override fun handleEvent(event: CreateAdditionGroupForMenu.Event) {
        when (event) {
            CreateAdditionGroupForMenu.Event.Back -> {
                findNavController().popBackStack()
            }

            CreateAdditionGroupForMenu.Event.SaveAndBack -> {
                setFragmentResult(
                    requestKey = CREATE_ADDITION_GROUP,
                    result = bundleOf(CREATE_ADDITION_GROUP_KEY to true),
                )
                findNavController().popBackStack()
            }

            is CreateAdditionGroupForMenu.Event.OnAdditionGroupClick -> {
                findNavController().navigate(
                    directions =
                        CreateAdditionGroupForMenuProductFragmentDirections
                            .toSelectAdditionGroupFragment(
                                additionGroupUuid = event.uuid,
                                menuProductUuid = event.menuProductUuid,
                                mainEditedAdditionGroupUuid = null,
                            ),
                )
            }

            is CreateAdditionGroupForMenu.Event.OnAdditionListClick -> {
                findNavController().navigate(
                    directions =
                        CreateAdditionGroupForMenuProductFragmentDirections
                            .toSelectAdditionListFragment(
                                additionGroupUuid = null,
                                menuProductUuid = event.menuProductUuid,
                                additionGroupForMenuName = event.additionGroupName,
                                editedAdditionListUuid = null,
                                ),
                )
            }
        }
    }

    val createAdditionGroupForMenuProductViewState =
        CreateAdditionGroupForMenu.DataState(
            state = CreateAdditionGroupForMenu.DataState.State.SUCCESS,
            groupName = "Вкусняхи",
            additionNameList = "Бекон * Страпон * Бурбон",
            menuProductUuid = "",
            additionGroupForMenuProductUuid = "",
            editedAdditionGroupUuid = "",
            editedAdditionListUuid = listOf(),
            isVisible = false,
            groupHasError = false,
            isSaveLoading = false,
            additionListHasError = false,
        )

    @Composable
    @Preview
    fun CreateAdditionGroupForMenuProductScreenPreview() {
        AdminTheme {
            CreateAdditionGroupForMenuProductScreen(
                state = createAdditionGroupForMenuProductViewState,
                onAction = {},
            )
        }
    }
}
