package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.selectaddition

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.DragDropList
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition.SelectAdditionList
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition.SelectAdditionList.DataState.AdditionItem
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition.SelectAdditionListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TITLE_POSITION_VISIBLE_KEY = "title_position_visible"
private const val TITLE_POSITION_ADDITIONS_KEY = "title_position_additions"
private const val LIST_ANIMATION_DURATION = 500

class SelectAdditionListFragment :
    SingleStateComposeFragment<SelectAdditionList.DataState, SelectAdditionList.Action, SelectAdditionList.Event>() {
    companion object {
        const val SELECT_ADDITION_LIST_KEY = "SELECT_ADDITION_LIST_KEY"
        const val ADDITION_LIST_KEY = "ADDITION_LIST_KEY"
    }

    override val viewModel: SelectAdditionListViewModel by viewModel()
    private val selectAdditionFragmentArgs: SelectAdditionListFragmentArgs by navArgs()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAction(
            SelectAdditionList.Action.Init(
                menuProductUuid = selectAdditionFragmentArgs.menuProductUuid,
                additionGroupUuid = selectAdditionFragmentArgs.additionGroupUuid,
                additionGroupName = selectAdditionFragmentArgs.additionGroupForMenuName,
            ),
        )
    }

    @Composable
    override fun Screen(
        state: SelectAdditionList.DataState,
        onAction: (SelectAdditionList.Action) -> Unit,
    ) {
        SelectAdditionScreen(state = state, onAction = onAction)
    }

    override fun handleEvent(event: SelectAdditionList.Event) {
        when (event) {
            SelectAdditionList.Event.Back -> {
                findNavController().popBackStack()
            }

            is SelectAdditionList.Event.SelectAdditionListBack -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(
                        R.string.action_select_addition_list_title_selected,
                    ),
                )
                setFragmentResult(
                    requestKey = SELECT_ADDITION_LIST_KEY,
                    result = bundleOf(ADDITION_LIST_KEY to event.additionUuidList),
                )
                findNavController().popBackStack()
            }
        }
    }

    @Composable
    fun SelectAdditionScreen(
        state: SelectAdditionList.DataState,
        onAction: (SelectAdditionList.Action) -> Unit,
    ) {
        AdminScaffold(
            title =
                when (state.state) {
                    SelectAdditionList.DataState.State.LOADING -> null
                    SelectAdditionList.DataState.State.ERROR -> null
                    SelectAdditionList.DataState.State.SUCCESS -> stringResource(id = R.string.title_select_addition_group)
                    SelectAdditionList.DataState.State.SUCCESS_DRAG_DROP -> stringResource(R.string.title_edit_priority)
                },
            backActionClick = {
                when (state.state) {
                    SelectAdditionList.DataState.State.LOADING -> Unit
                    SelectAdditionList.DataState.State.ERROR -> Unit
                    SelectAdditionList.DataState.State.SUCCESS -> onAction(SelectAdditionList.Action.OnBackClick)
                    SelectAdditionList.DataState.State.SUCCESS_DRAG_DROP ->
                        onAction(
                            SelectAdditionList.Action.OnCancelClicked,
                        )
                }
            },
            backgroundColor = AdminTheme.colors.main.surface,
            actionButton = {
                when (state.state) {
                    SelectAdditionList.DataState.State.LOADING -> Unit
                    SelectAdditionList.DataState.State.ERROR -> Unit
                    SelectAdditionList.DataState.State.SUCCESS -> Unit
                    SelectAdditionList.DataState.State.SUCCESS_DRAG_DROP -> {
                        LoadingButton(
                            text = stringResource(R.string.action_order_details_save),
                            isLoading = false,
                            onClick = {
                                onAction(SelectAdditionList.Action.SelectAdditionListClick)
                            },
                            modifier =
                                Modifier
                                    .padding(horizontal = AdminTheme.dimensions.mediumSpace),
                        )
                    }
                }
            },
            topActions = when (state.state) {
                SelectAdditionList.DataState.State.LOADING -> emptyList()
                SelectAdditionList.DataState.State.ERROR -> emptyList()
                SelectAdditionList.DataState.State.SUCCESS -> {
                    if (state.emptySelectedList) {
                        emptyList()
                    } else {
                        listOf(
                            AdminTopBarAction(
                                iconId = R.drawable.ic_edit,
                                color = AdminTheme.colors.main.primary,
                                onClick = {
                                    onAction(SelectAdditionList.Action.OnPriorityEditClicked)
                                }
                            )
                        )
                    }
                }

                    SelectAdditionList.DataState.State.SUCCESS_DRAG_DROP -> {
                        listOf(
                            AdminTopBarAction(
                                iconId = R.drawable.ic_check,
                                color = AdminTheme.colors.main.primary,
                                onClick = {
                                    onAction(
                                        SelectAdditionList.Action.OnSaveEditPriorityClick,
                                    )
                                },
                            ),
                        )
                    }
                },
        ) {
            when (state.state) {
                SelectAdditionList.DataState.State.LOADING -> LoadingScreen()
                SelectAdditionList.DataState.State.ERROR -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            // onAction(SelectAdditionList.Action.SelectAdditionListClick)
                        },
                    )
                }

                SelectAdditionList.DataState.State.SUCCESS ->
                    SelectAdditionSuccessScreen(
                        state = state,
                        onAction = onAction,
                    )

                SelectAdditionList.DataState.State.SUCCESS_DRAG_DROP ->
                    SelectAdditionSuccessDragScreen(
                        state = state,
                        onAction = onAction,
                    )
            }
        }
    }

    @Composable
    fun SelectAdditionSuccessScreen(
        state: SelectAdditionList.DataState,
        onAction: (SelectAdditionList.Action) -> Unit,
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize(),
            contentPadding =
                PaddingValues(
                    bottom = AdminTheme.dimensions.scrollScreenBottomSpace,
                ),
        ) {
            item(
                key = TITLE_POSITION_VISIBLE_KEY,
            ) {
                Text(
                    modifier =
                        Modifier
                            .padding(
                                start = 16.dp,
                                bottom = 16.dp,
                            ).fillMaxWidth()
                            .animateItem()
                            .animateContentSize(
                                animationSpec = tween(LIST_ANIMATION_DURATION),
                            ),
                    text =
                        stringResource(
                            id = R.string.action_select_addition_list_title_group,
                            state.groupName,
                        ),
                    style = AdminTheme.typography.titleMedium.bold,
                )
            }

            items(
                items = state.selectedAdditionList,
                key = { additions -> additions.uuid },
            ) { additionItem ->
                SelectAdditionCard(
                    additionItem = additionItem,
                    icon = R.drawable.ic_minus,
                    iconColor = AdminTheme.colors.main.onSurfaceVariant,
                    onClick = {
                        onAction(
                            SelectAdditionList.Action.RemoveAdditionClick(
                                uuid = additionItem.uuid,
                            ),
                        )
                    },
                )
            }

            item(
                key = TITLE_POSITION_ADDITIONS_KEY,
            ) {
                Text(
                    modifier =
                        Modifier
                            .padding(
                                all = 16.dp,
                            ).fillMaxWidth()
                            .animateItem()
                            .animateContentSize(
                                animationSpec = tween(LIST_ANIMATION_DURATION),
                            ),
                    text =
                        stringResource(
                            id = R.string.action_select_addition_list_title_additions,
                            state.groupName,
                        ),
                    style = AdminTheme.typography.titleMedium.bold,
                )
            }

            items(
                items = state.notSelectedAdditionList,
                key = { additions -> additions.uuid },
            ) { additionItem ->
                SelectAdditionCard(
                    additionItem = additionItem,
                    icon = R.drawable.ic_plus,
                    iconColor = AdminTheme.colors.main.primary,
                    onClick = {
                        onAction(
                            SelectAdditionList.Action.SelectAdditionClick(
                                uuid = additionItem.uuid,
                            ),
                        )
                    },
                )
            }
        }
    }

    @Composable
    private fun SelectAdditionCard(
        additionItem: AdditionItem,
        @DrawableRes icon: Int,
        iconColor: Color,
        onClick: () -> Unit,
    ) {
        AdminCard(
            modifier = Modifier.fillMaxWidth(),
            clickable = false,
            elevated = false,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = additionItem.name,
                        modifier = Modifier.weight(1f),
                        style = AdminTheme.typography.bodyLarge,
                    )

                    IconButton(
                        modifier =
                            Modifier
                                .align(Alignment.CenterVertically)
                                .padding(vertical = 8.dp),
                        onClick = onClick,
                    ) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
                AdminHorizontalDivider()
            }
        }
    }

    @Composable
    fun SelectAdditionSuccessDragScreen(
        state: SelectAdditionList.DataState,
        onAction: (SelectAdditionList.Action) -> Unit,
    ) {
        DragDropList(
            title = state.groupName,
            items = state.selectedAdditionList,
            itemKey = { it.uuid },
            onMove = { fromIndex, toIndex ->
                onAction(
                    SelectAdditionList.Action.MoveSelectedItem(
                        fromIndex = fromIndex,
                        toIndex = toIndex,
                    ),
                )
            },
            itemLabel = { it.name },
        )
    }

    private val selectAdditionListViewState =
        SelectAdditionList.DataState(
            state = SelectAdditionList.DataState.State.SUCCESS,
            groupName = "Some group",
            selectedAdditionList =
                listOf(
                    AdditionItem(
                        uuid = "1",
                        name = "Картошка",
                    ),
                    AdditionItem(
                        uuid = "2",
                        name = "Крошка",
                    ),
                ),
            notSelectedAdditionList =
                listOf(
                    AdditionItem(
                        uuid = "4",
                        name = "Подношка",
                    ),
                    AdditionItem(
                        uuid = "5",
                        name = "Ложка",
                    ),
                ),
            emptySelectedList = true
        )

    @Composable
    @Preview
    fun SelectAdditionScreenPreview() {
        AdminTheme {
            SelectAdditionScreen(
                state = selectAdditionListViewState,
                onAction = {},
            )
        }
    }
}
