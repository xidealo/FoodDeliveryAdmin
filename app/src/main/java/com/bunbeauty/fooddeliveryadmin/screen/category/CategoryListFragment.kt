package com.bunbeauty.fooddeliveryadmin.screen.category

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.presentation.feature.category.CategoryListState
import com.bunbeauty.presentation.feature.category.CategoryListViewModel
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
//            title = stringResource(R.string.title_categories_list),
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
                if (state.isEditPriority) {
                    AdminTopBarAction(
                        iconId = R.drawable.ic_clear,
                        color = AdminTheme.colors.main.primary,
                        onClick = {
                            onAction(CategoryListState.Action.OnCancelClicked)
                        }
                    )
                } else {
                    AdminTopBarAction(
                        iconId = R.drawable.ic_edit,
                        color = AdminTheme.colors.main.primary,
                        onClick = {
                            onAction(CategoryListState.Action.OnPriorityEditClicked)
                        }
                    )
                }
            ),
            actionButton = {
                if (state.state is CategoryListViewState.State.Success) {
                    FloatingButton(
                        iconId = R.drawable.ic_plus,
                        textStringId = R.string.action_menu_list_create,
                        onClick = {
                            onAction(CategoryListState.Action.OnCreateClicked)
                        }
                    )
                }
            },
            actionButtonPosition = FabPosition.End
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

                    Column {
                        CategoriesListSuccessDragScreen(
                            state = state.state,
                            stateLoading = state,
                            onAction = onAction
                        )
                    }
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
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun CategoriesListSuccessDragScreen(
        state: CategoryListViewState.State.SuccessDragDrop,
        stateLoading: CategoryListViewState,
        onAction: (CategoryListState.Action) -> Unit
    ) {
        val itemList = remember { state.categoryList.toMutableStateList() }
        var draggingIndex by remember { mutableStateOf<Int?>(null) }
        var dragOffset by remember { mutableStateOf(Offset.Zero) }
        var itemHeight by remember { mutableStateOf(0f) }

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                itemsIndexed(itemList, key = { _, item -> item.uuid }) { index, item ->
                    val isDragging = draggingIndex == index

                    val offsetModifier = if (isDragging) {
                        Modifier.offset { IntOffset(0, dragOffset.y.roundToInt()) }
                    } else Modifier

                    Box(
                        modifier = Modifier
                            .then(offsetModifier)
                            .fillMaxWidth()
                            .animateItemPlacement()
                    ) {
                        CategoryItemDraggable(
                            category = item,
                            onDragStart = {
                                draggingIndex = itemList.indexOf(item)
                                dragOffset = Offset.Zero
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffset += dragAmount

                                val fromIndex = draggingIndex ?: return@CategoryItemDraggable
                                val toIndex = when {
                                    dragOffset.y > itemHeight / 2 -> fromIndex + 1
                                    dragOffset.y < -itemHeight / 2 -> fromIndex - 1
                                    else -> fromIndex
                                }

                                if (toIndex in itemList.indices && fromIndex != toIndex) {
                                    itemList.swap(fromIndex, toIndex)
                                    draggingIndex = toIndex
                                    dragOffset = Offset.Zero
                                }
                            },
                            onDragEnd = { dragOffset = Offset.Zero },
                            onDragCancel = { dragOffset = Offset.Zero },
                            onHeightMeasured = { height -> itemHeight = height }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LoadingButton(
                text = stringResource(R.string.action_create_category_save),
                isLoading = stateLoading.isLoading,
                onClick = {
                    onAction(
                        CategoryListState.Action.OnSaveEditPriorityCategoryClick(
                            updatedList = itemList)
                        )
                    println("ЗАЛУПА$itemList")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }


    fun <T> MutableList<T>.swap(fromIndex: Int, toIndex: Int) {
        val tmp = this[fromIndex]
        this[fromIndex] = this[toIndex]
        this[toIndex] = tmp
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

            CategoryListState.Event.OnEditPriorityCategoryEvent -> {
                // TODO()
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
    //    @OptIn(ExperimentalFoundationApi::class)
//    @Composable
//    private fun CategoriesListSuccessDragScreen(
//        state: CategoryListViewState.State.SuccessDragDrop,
//        stateLoading: CategoryListViewState,
//        onAction: (CategoryListState.Action) -> Unit
//    ) {
//        val itemList = remember { state.categoryList.toMutableStateList() }
//        var draggingIndex by remember { mutableStateOf<Int?>(null) }
//        var dragOffset by remember { mutableStateOf(Offset.Zero) }
//        var itemHeight by remember { mutableStateOf(0f) }
//
//        Column(modifier = Modifier.fillMaxSize()) {
//            LazyColumn(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxWidth()
//            ) {
//                itemsIndexed(itemList, key = { _, item -> item.uuid }) { index, item ->
//                    val isDragging = draggingIndex == index
//
//                    val offsetModifier = if (isDragging) {
//                        Modifier.offset { IntOffset(0, dragOffset.y.roundToInt()) }
//                    } else Modifier
//
//                    Box(
//                        modifier = Modifier
//                            .then(offsetModifier)
//                            .fillMaxWidth()
//                            .animateItemPlacement()
//                    ) {
//                        CategoryItemDraggable(
//                            category = item,
//                            onDragStart = {
//                                draggingIndex = itemList.indexOf(item)
//                                dragOffset = Offset.Zero
//                            },
//                            onDrag = { change, dragAmount ->
//                                change.consume()
//                                dragOffset += dragAmount
//
//                                val fromIndex = draggingIndex ?: return@CategoryItemDraggable
//                                val toIndex = when {
//                                    dragOffset.y > itemHeight / 2 -> fromIndex + 1
//                                    dragOffset.y < -itemHeight / 2 -> fromIndex - 1
//                                    else -> fromIndex
//                                }
//
//                                if (toIndex in state.categoryList.indices && fromIndex != toIndex) {
//                                    itemList.swap(fromIndex, toIndex)
//                                    draggingIndex = toIndex
//                                    dragOffset = Offset.Zero
//                                }
//                            },
//                            onDragEnd = { dragOffset = Offset.Zero },
//                            onDragCancel = { dragOffset = Offset.Zero },
//                            onHeightMeasured = { height -> itemHeight = height }
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            LoadingButton(
//                text = stringResource(R.string.action_create_category_save),
//                isLoading = stateLoading.isLoading,
//                onClick = {
//                    val updatedList = itemList.mapIndexed { index, item ->
//                        item.toDomain(priority = index + 1)
//                    }
//                    onAction(
//                        CategoryListState.Action.OnSaveEditPriorityCategoryClick(
//                            updatedList = updatedList
//                        )
//                    )
//                    println(updatedList)
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            )
//        }
//    }

//    @Composable
//    fun CategoryItemDraggable(
//        category: CategoryListViewState.CategoriesViewItem,
//        onDragStart: () -> Unit,
//        onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit,
//        onDragEnd: () -> Unit,
//        onDragCancel: () -> Unit
//    ) {
//        val modifier = Modifier.fillMaxWidth()
//
//        AdminCard(
//            modifier = modifier.padding(vertical = 4.dp),
//            shape = noCornerCardShape
//        ) {
//            Row(
//                modifier = Modifier
//                    .padding(horizontal = 16.dp, vertical = 16.dp)
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(start = 16.dp),
//                    text = category.name,
//                    style = AdminTheme.typography.bodyLarge,
//                    color = AdminTheme.colors.main.onSurface
//                )
//
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_drad_handle),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .padding(start = 8.dp)
//                        .pointerInput(Unit) {
//                            detectDragGestures(
//                                onDragStart = { onDragStart() },
//                                onDragEnd = onDragEnd,
//                                onDragCancel = onDragCancel,
//                                onDrag = onDrag
//                            )
//                        }
//                )
//            }
//        }
//    }
}

