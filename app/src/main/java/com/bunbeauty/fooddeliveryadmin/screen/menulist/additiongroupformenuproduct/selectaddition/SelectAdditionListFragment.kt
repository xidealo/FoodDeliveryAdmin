package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.selectaddition

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCardDefaults.noCornerCardShape
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition.SelectAdditionList
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition.SelectAdditionListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAction(
            SelectAdditionList.Action.Init(
                menuProductUuid = selectAdditionFragmentArgs.menuProductUuid,
                additionGroupUuid = selectAdditionFragmentArgs.additionGroupUuid,
                additionGroupName = selectAdditionFragmentArgs.additionGroupForMenuName
            )
        )
    }

    @Composable
    override fun Screen(
        state: SelectAdditionList.DataState,
        onAction: (SelectAdditionList.Action) -> Unit
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
                        R.string.action_select_addition_list_title_selected
                    )
                )
                setFragmentResult(
                    requestKey = SELECT_ADDITION_LIST_KEY,
                    result = bundleOf(ADDITION_LIST_KEY to event.additionUuidList)
                )
                findNavController().popBackStack()
            }
        }
    }

    @Composable
    fun SelectAdditionScreen(
        state: SelectAdditionList.DataState,
        onAction: (SelectAdditionList.Action) -> Unit
    ) {
        AdminScaffold(
            title = if (state.isEditPriority) {
                stringResource(R.string.title_edit_priority)
            } else {
                stringResource(id = R.string.title_select_addition_group)
            },
            backActionClick = {
                if (state.isEditPriority) {
                    onAction(SelectAdditionList.Action.OnCancelClicked)
                } else {
                    onAction(SelectAdditionList.Action.OnBackClick)
                }
            },
            backgroundColor = AdminTheme.colors.main.surface,
            actionButton = {
                if (state.isEditPriority) {
                    null
                } else {
                    LoadingButton(
                        text = stringResource(R.string.action_order_details_save),
                        isLoading = false,
                        onClick = {
                            onAction(SelectAdditionList.Action.SelectAdditionListClick)
                        },
                        modifier = Modifier
                            .padding(horizontal = AdminTheme.dimensions.mediumSpace)
                    )
                }
            },
            topActions = when (state.state) {
                SelectAdditionList.DataState.State.LOADING -> emptyList()
                SelectAdditionList.DataState.State.ERROR -> emptyList()
                SelectAdditionList.DataState.State.SUCCESS -> listOf(
                    AdminTopBarAction(
                        iconId = R.drawable.ic_edit,
                        color = AdminTheme.colors.main.primary,
                        onClick = {
                            onAction(SelectAdditionList.Action.OnPriorityEditClicked)
                        }
                    )
                )

                SelectAdditionList.DataState.State.SUCCESS_DRAG_DROP -> {
                    listOf(
                        AdminTopBarAction(
                            iconId = R.drawable.ic_check,
                            color = AdminTheme.colors.main.primary,
                            onClick = {
                                onAction(
                                    SelectAdditionList.Action.OnSaveEditPriorityClick
                                )
                            }
                        )
                    )
                }
            }
        ) {
            when (state.state) {
                SelectAdditionList.DataState.State.LOADING -> LoadingScreen()
                SelectAdditionList.DataState.State.ERROR -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            // onAction(SelectAdditionList.Action.SelectAdditionListClick)
                        }
                    )
                }

                SelectAdditionList.DataState.State.SUCCESS -> SelectAdditionSuccessScreen(
                    state = state,
                    onAction = onAction
                )

                SelectAdditionList.DataState.State.SUCCESS_DRAG_DROP -> SelectAdditionSuccessDragScreen(
                    state = state,
                    onAction = onAction
                )
            }
        }
    }

    @Composable
    fun SelectAdditionSuccessScreen(
        state: SelectAdditionList.DataState,
        onAction: (SelectAdditionList.Action) -> Unit
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace
            )
        ) {
            item(
                key = TITLE_POSITION_VISIBLE_KEY
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            bottom = 16.dp
                        )
                        .fillMaxWidth()
                        .animateItem()
                        .animateContentSize(
                            animationSpec = tween(LIST_ANIMATION_DURATION)
                        ),
                    text = stringResource(
                        id = R.string.action_select_addition_list_title_group,
                        state.groupName
                    ),
                    style = AdminTheme.typography.titleMedium.bold
                )
            }

            items(
                items = state.selectedAdditionList,
                key = { additions -> additions.uuid }
            ) { additionItem ->
                SelectAdditionCard(
                    additionItem = additionItem,
                    icon = R.drawable.ic_minus,
                    iconColor = AdminTheme.colors.main.onSurfaceVariant,
                    onClick = {
                        onAction(
                            SelectAdditionList.Action.RemoveAdditionClick(
                                uuid = additionItem.uuid
                            )
                        )
                    }
                )
            }

            item(
                key = TITLE_POSITION_ADDITIONS_KEY
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            all = 16.dp
                        )
                        .fillMaxWidth()
                        .animateItem()
                        .animateContentSize(
                            animationSpec = tween(LIST_ANIMATION_DURATION)
                        ),
                    text = stringResource(
                        id = R.string.action_select_addition_list_title_additions,
                        state.groupName
                    ),
                    style = AdminTheme.typography.titleMedium.bold
                )
            }

            items(
                items = state.notSelectedAdditionList,
                key = { additions -> additions.uuid }
            ) { additionItem ->
                SelectAdditionCard(
                    additionItem = additionItem,
                    icon = R.drawable.ic_plus,
                    iconColor = AdminTheme.colors.main.primary,
                    onClick = {
                        onAction(
                            SelectAdditionList.Action.SelectAdditionClick(
                                uuid = additionItem.uuid
                            )
                        )
                    }
                )
            }
        }
    }

    @Composable
    private fun SelectAdditionCard(
        additionItem: SelectAdditionList.DataState.AdditionItem,
        @DrawableRes icon: Int,
        iconColor: Color,
        onClick: () -> Unit
    ) {
        AdminCard(
            modifier = Modifier.fillMaxWidth(),
            clickable = false,
            elevated = false
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = additionItem.name,
                        modifier = Modifier.weight(1f),
                        style = AdminTheme.typography.bodyLarge
                    )

                    IconButton(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(vertical = 8.dp),
                        onClick = onClick
                    ) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                AdminHorizontalDivider()
            }
        }
    }

    @Composable
    private fun SelectAdditionSuccessDragScreen(
        state: SelectAdditionList.DataState,
        onAction: (SelectAdditionList.Action) -> Unit
    ) {
        var draggingIndex by remember { mutableStateOf<Int?>(null) }
        var fromIndex by remember { mutableIntStateOf(0) }
        var toIndex by remember { mutableIntStateOf(0) }
        var dragOffset by remember { mutableStateOf(Offset.Zero) }
        var itemHeight by remember { mutableFloatStateOf(0f) }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace
            )
        ) {
            item(key = TITLE_POSITION_VISIBLE_KEY) {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                        .animateItem(),
                    text = stringResource(
                        id = R.string.action_select_addition_list_title_group,
                        state.groupName
                    ),
                    style = AdminTheme.typography.titleMedium.bold
                )
            }
            itemsIndexed(
                items = state.selectedAdditionList
            ) { index, additionItem ->
                val isDragging = draggingIndex == index

                val offsetModifier = if (isDragging) {
                    Modifier
                        .offset { IntOffset(0, dragOffset.y.roundToInt()) }
                        .zIndex(1f)
                } else {
                    Modifier
                }

                Box(
                    modifier = Modifier
                        .then(offsetModifier)
                        .fillMaxWidth()
                        .zIndex(0.9f)
                        .animateItem()
                ) {
                    AdditionItemDraggable(
                        additionItem = additionItem,
                        onDragStart = {
                            draggingIndex = index
                            dragOffset = Offset.Zero
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragOffset += dragAmount
                            fromIndex = draggingIndex ?: return@AdditionItemDraggable

                            val delta = (dragOffset.y / itemHeight).roundToInt()
                            toIndex = (fromIndex + delta).coerceIn(
                                0,
                                state.selectedAdditionList.lastIndex
                            )
                        },
                        onDragEnd = {
                            if (fromIndex != toIndex && toIndex in state.selectedAdditionList.indices) {
                                onAction(
                                    SelectAdditionList.Action.MoveSelectedItem(
                                        fromIndex = fromIndex,
                                        toIndex = toIndex
                                    )
                                )
                            }
                            dragOffset = Offset.Zero
                            draggingIndex = null
                        },
                        onDragCancel = {
                            dragOffset = Offset.Zero
                            draggingIndex = null
                        },
                        onHeightMeasured = { height ->
                            itemHeight = height
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun AdditionItemDraggable(
        additionItem: SelectAdditionList.DataState.AdditionItem,
        onDragStart: (Offset) -> Unit,
        onDrag: (PointerInputChange, Offset) -> Unit,
        onDragEnd: () -> Unit,
        onDragCancel: () -> Unit,
        onHeightMeasured: (Float) -> Unit
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                onHeightMeasured(coordinates.size.height.toFloat())
            }

        AdminCard(
            modifier = modifier
                .padding(vertical = 4.dp),
            shape = noCornerCardShape,
            elevated = false
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = additionItem.name,
                    style = AdminTheme.typography.bodyLarge,
                    color = AdminTheme.colors.main.onSurface
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_drad_handle),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = onDragStart,
                                onDragEnd = onDragEnd,
                                onDragCancel = onDragCancel,
                                onDrag = onDrag
                            )
                        }
                )
            }
        }
    }

    private val selectAdditionListViewState = SelectAdditionList.DataState(
        state = SelectAdditionList.DataState.State.SUCCESS,
        groupName = "Some group",
        isEditPriority = false,
        selectedAdditionList = listOf(
            SelectAdditionList.DataState.AdditionItem(
                uuid = "1",
                name = "Картошка"
            ),
            SelectAdditionList.DataState.AdditionItem(
                uuid = "2",
                name = "Крошка"
            )
        ),
        notSelectedAdditionList = listOf(
            SelectAdditionList.DataState.AdditionItem(
                uuid = "4",
                name = "Подношка"
            ),
            SelectAdditionList.DataState.AdditionItem(
                uuid = "5",
                name = "Ложка"
            )
        )
    )

    @Composable
    @Preview
    fun SelectAdditionScreenPreview() {
        AdminTheme {
            SelectAdditionScreen(
                state = selectAdditionListViewState,
                onAction = {}
            )
        }
    }
}
