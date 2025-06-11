package com.bunbeauty.fooddeliveryadmin.screen.category

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.FloatingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCardDefaults.noCornerCardShape
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.presentation.feature.category.CategoryListState
import com.bunbeauty.presentation.feature.category.CategoryListViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class CategoryListFragment :
    BaseComposeFragment<CategoryListState.DataState, CategoryListViewState, CategoryListState.Action, CategoryListState.Event>() {

    override val viewModel: CategoryListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAction(CategoryListState.Action.Init)
    }

    @Composable
    override fun Screen(
        state: CategoryListViewState,
        onAction: (CategoryListState.Action) -> Unit
    ) {
        CategoriesScreen(
            state = state,
            onAction = onAction
        )
    }

    @Composable
    private fun CategoriesScreen(
        state: CategoryListViewState,
        onAction: (CategoryListState.Action) -> Unit
    ) {
        AdminScaffold(
            backgroundColor = AdminTheme.colors.main.surface,
            title = if (state.isEditPriority) {
                stringResource(R.string.title_edit_priority)
            } else {
                stringResource(R.string.title_categories_list)
            },
            pullRefreshEnabled = true,
            refreshing = state.isRefreshing,
            onRefresh = {
                onAction(CategoryListState.Action.OnRefreshData)
            },
            backActionClick = {
                onAction(CategoryListState.Action.OnBackClicked)
            },
            topActions = listOf(
                AdminTopBarAction(
                    iconId = if (state.isEditPriority) {
                        R.drawable.ic_clear
                    } else {
                        R.drawable.ic_edit
                    },
                    color = AdminTheme.colors.main.primary,
                    onClick = {
                        if (state.isEditPriority) {
                            onAction(CategoryListState.Action.OnCancelClicked)
                        } else {
                            onAction(CategoryListState.Action.OnPriorityEditClicked)
                        }
                    }
                )
            ),
            actionButton = {
                when (state.state) {
                    is CategoryListViewState.State.Success -> {
                        FloatingButton(
                            iconId = R.drawable.ic_plus,
                            textStringId = R.string.action_menu_list_create,
                            onClick = {
                                onAction(CategoryListState.Action.OnCreateClicked)
                            }
                        )
                    }

                    is CategoryListViewState.State.SuccessDragDrop -> {
                        LoadingButton(
                            text = stringResource(R.string.action_create_category_save),
                            isLoading = state.isLoading,
                            onClick = {
                                onAction(
                                    CategoryListState.Action.OnSaveEditPriorityCategoryClick(
                                        updatedList = state.state.categoryList.toMutableStateList()
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }

                    else -> Unit
                }
            },
            actionButtonPosition = when (state.state) {
                is CategoryListViewState.State.Success -> FabPosition.End
                else -> FabPosition.Center
            }
        ) {
            when (state.state) {
                CategoryListViewState.State.Loading -> {
                    LoadingScreen()
                }

                CategoryListViewState.State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            onAction(CategoryListState.Action.Init)
                        }
                    )
                }

                is CategoryListViewState.State.Success -> {
                    CategoriesListSuccessScreen(state = state.state, onAction = onAction)
                }

                is CategoryListViewState.State.SuccessDragDrop -> {
                    CategoriesListSuccessDragScreen(
                        state = state.state,
                        onAction = onAction
                    )
                }
            }
        }
    }

    @Composable
    private fun CategoriesListSuccessScreen(
        state: CategoryListViewState.State.Success,
        onAction: (CategoryListState.Action) -> Unit
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                items = state.categoryList,
                key = { category -> category.uuid }
            ) { category ->
                CategoryItemView(
                    category = category,
                    onClick = {
                        onAction(
                            CategoryListState.Action.OnCategoryClick(
                                categoryUuid = category.uuid
                            )
                        )
                    },
                    isClickable = true
                )
            }
        }
    }

    @Composable
    private fun CategoriesListSuccessDragScreen(
        state: CategoryListViewState.State.SuccessDragDrop,
        onAction: (CategoryListState.Action) -> Unit
    ) {
        var draggingIndex by remember { mutableStateOf<Int?>(null) }
        var fromIndex by remember { mutableIntStateOf(0) }
        var toIndex by remember { mutableIntStateOf(0) }
        var dragOffset by remember { mutableStateOf(Offset.Zero) }
        var itemHeight by remember { mutableStateOf(0f) }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            itemsIndexed(state.categoryList, key = { _, item -> item.uuid }) { index, item ->
                val isDragging = draggingIndex == index

                val offsetModifier = if (isDragging) {
                    Modifier.offset { IntOffset(0, dragOffset.y.roundToInt()) }
                } else {
                    Modifier
                }

                Box(
                    modifier = Modifier
                        .then(offsetModifier)
                        .fillMaxWidth()
                        .animateItem()
                ) {
                    CategoryItemDraggable(
                        category = item,
                        onDragStart = {
                            draggingIndex = state.categoryList.indexOf(item)
                            dragOffset = Offset.Zero

                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragOffset += dragAmount
                            fromIndex = draggingIndex ?: return@CategoryItemDraggable

                            toIndex = when {
                                dragOffset.y > itemHeight / 2 -> fromIndex + 1
                                dragOffset.y < -itemHeight / 2 -> fromIndex - 1
                                else -> fromIndex
                            }

                            Log.d(
                                "MYYRTT",
                                "onDrag fromIndex ${fromIndex} toIndex ${toIndex}"
                            )
                        },
                        onDragEnd = {

                            Log.d(
                                "MYYRTT",
                                "onDragEnd fromIndex ${fromIndex} toIndex ${toIndex}"
                            )

                            if (toIndex in state.categoryList.indices && fromIndex != toIndex) {
                                onAction(
                                    CategoryListState.Action.SwapItem(
                                        fromIndex = fromIndex,
                                        toIndex = toIndex
                                    )
                                )
                                draggingIndex = toIndex
                                dragOffset = Offset.Zero
                            }
                        },
                        onDragCancel = {
                            dragOffset = Offset.Zero
                            Log.d(
                                "MYYRTT",
                                "onDragCancel"
                            )
                        },
                        onHeightMeasured = { height -> itemHeight = height }
                    )
                }
            }
        }
    }

    @Composable
    fun CategoryItemDraggable(
        category: Category,
        onDragStart: () -> Unit,
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
            modifier = modifier.padding(vertical = 4.dp),
            shape = noCornerCardShape
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    text = category.name,
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
                                onDragStart = { onDragStart() },
                                onDragEnd = { onDragEnd() },
                                onDragCancel = { onDragCancel() },
                                onDrag = onDrag
                            )
                        }
                )
            }
        }
    }

    override fun handleEvent(event: CategoryListState.Event) {
        when (event) {
            CategoryListState.Event.GoBackEvent -> {
                findNavController().navigateUp()
            }

            CategoryListState.Event.CreateCategoryEvent -> {
                findNavController().navigateSafe(CategoryListFragmentDirections.toCreateCategoryFragment())
            }

            is CategoryListState.Event.OnCategoryClick -> {
                findNavController().navigateSafe(
                    CategoryListFragmentDirections.toEditCategoryListFragment(
                        event.categoryUuid
                    )
                )
            }

            CategoryListState.Event.ShowUpdateCategoryListSuccess -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(R.string.msg_update_category_priority_list)
                )
            }
        }
    }

    @Composable
    override fun mapState(state: CategoryListState.DataState): CategoryListViewState {
        val categoryItems = state.categoryList.map { category ->
            CategoryListViewState.CategoriesViewItem(
                uuid = category.uuid,
                name = category.name,
                priority = category.priority
            )
        }.toPersistentList()
        val category = state.categoryList.map { category ->
            Category(
                uuid = category.uuid,
                name = category.name,
                priority = category.priority
            )
        }.toPersistentList()

        return CategoryListViewState(
            state = when (state.state) {
                CategoryListState.DataState.State.LOADING -> CategoryListViewState.State.Loading
                CategoryListState.DataState.State.ERROR -> CategoryListViewState.State.Error
                CategoryListState.DataState.State.SUCCESS -> {
                    if (state.isEditPriority) {
                        CategoryListViewState.State.SuccessDragDrop(category)
                    } else {
                        CategoryListViewState.State.Success(categoryItems)
                    }
                }
            },
            isLoading = state.isLoading,
            isRefreshing = state.isRefreshing,
            isEditPriority = state.isEditPriority
        )
    }

    @Composable
    private fun CategoryItemView(
        modifier: Modifier = Modifier,
        category: CategoryListViewState.CategoriesViewItem,
        onClick: () -> Unit,
        isClickable: Boolean
    ) {
        AdminCard(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            clickable = isClickable,
            shape = noCornerCardShape
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    text = category.name,
                    style = AdminTheme.typography.bodyLarge,
                    color = AdminTheme.colors.main.onSurface
                )
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun SettingsScreenPreview() {
        AdminTheme {
            CategoriesScreen(
                state = CategoryListViewState(
                    state = CategoryListViewState.State.Success(
                        categoryList = persistentListOf(
                            CategoryListViewState.CategoriesViewItem(
                                uuid = "",
                                name = "Лаваш",
                                priority = 0
                            ),

                            CategoryListViewState.CategoriesViewItem(
                                uuid = "BBB",
                                name = "Соус",
                                priority = 1
                            )
                        )
                    ),
                    isLoading = false,
                    isRefreshing = false,
                    isEditPriority = false
                ),
                onAction = {}
            )
        }
    }
}
