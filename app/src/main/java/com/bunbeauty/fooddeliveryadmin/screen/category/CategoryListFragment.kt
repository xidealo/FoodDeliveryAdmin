package com.bunbeauty.fooddeliveryadmin.screen.category

import android.R.attr.category
import android.R.attr.translationZ
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.fragment.findNavController
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
                        CategoriesListSuccessDragScreen(state = state.state, onAction = onAction)

                        Spacer(modifier = Modifier.weight(1f))

                        LoadingButton(
                            modifier = Modifier.padding(16.dp),
                            text = stringResource(R.string.action_create_category_save),
                            isLoading = state.isLoading,
                            onClick = {
                                onAction(CategoryListState.Action.OnSaveEditPriorityCategoryClick)
                            }
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

//    @OptIn(ExperimentalFoundationApi::class)
//    @SuppressLint("RememberReturnType")
//    @Composable
//    private fun CategoriesListSuccessDragScreen(
//        state: CategoryListViewState.State.SuccessDragDrop,
//        onAction: (CategoryListState.Action) -> Unit
//    ) {
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .dragAndDropTarget(
//                    shouldStartDragAndDrop = { event ->
//                        event.mimeTypes()
//                            .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
//                    },
//                    target = remember {
//                        object : DragAndDropTarget {
//                            override fun onDrop(event: DragAndDropEvent): Boolean {
//                                TODO("Not yet implemented")
//                            }
//                        }
//                    }
//                ),
//        ) {
//            AnimatedVisibility(
//                visible = TODO("УКАЗЫВАЕМ ИНДЕКС ПЕРЕТАСКИВАЕМОГО ПОЛЯ"),
//                enter = scaleIn() + fadeIn(),
//                exit = scaleOut() + fadeOut()
//            ) {
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .dragAndDropSource( // Источник перетаскивания
//                            shouldStartDragAndDrop = { true },
//                            onDragStarted = {
//                                draggingUuid = category.uuid
//                                DragAndDropTransferData(
//                                    clipData = ClipData.newPlainText("uuid", category.uuid)
//                                )
//                            },
//                            onDragEnded = {
//                                draggingUuid = null
//                            }
//                        )
//                ) {
//                    // Элемент списка
//                    CategoryItemDraggable(
//                        category = category,
//                        isDragging = false
//                    )
//                }
//
//            }
//        }
//    }

    @Composable
    private fun CategoriesListSuccessDragScreen(
        state: CategoryListViewState.State.SuccessDragDrop,
        onAction: (CategoryListState.Action) -> Unit
    ) {
        // Список категорий с возможностью мутации (перетаскивание)
        val categoryList = remember { mutableStateListOf(*state.categoryList.toTypedArray()) }

        val hoveredIndex = remember { mutableStateOf<Int?>(null) }

        // UUID текущего перетаскиваемого элемента
        var draggingUuid by remember { mutableStateOf<String?>(null) }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(categoryList, key = { it.uuid }) { category ->
                val isDragging = draggingUuid == category.uuid

                // Обёртка над карточкой с логикой перемещения
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(categoryList) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = {
                                    draggingUuid = category.uuid
                                },
                                onDragEnd = {
                                    draggingUuid = null
                                    // Здесь можно отправить обновлённый список в ViewModel
                                },
                                onDragCancel = {
                                    draggingUuid = null
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()


                                    val fromIndex = categoryList.indexOfFirst { it.uuid == category.uuid }
                                    val toIndex = when {
                                        dragAmount.y > 50 -> fromIndex + 1
                                        dragAmount.y < -50 -> fromIndex - 1
                                        else -> fromIndex
                                    }

                                    hoveredIndex.value = toIndex

                                    if (toIndex in categoryList.indices && fromIndex != toIndex) {
                                        val updatedList = categoryList.toMutableList()
                                        val movedItem = updatedList.removeAt(fromIndex)
                                        updatedList.add(toIndex, movedItem)

                                        val finalList = updatedList.mapIndexed { index, item ->
                                            item.copy(priority = index)
                                        }

                                        categoryList.clear()
                                        categoryList.addAll(finalList)

                                        draggingUuid = movedItem.uuid
                                    }
                                }
                            )
                        }
                ) {
                    // Отображение самой карточки с иконкой для начала drag
                    CategoryItemDraggable(
                        category = category,
                        isDragging = isDragging,
                        isTarget = hoveredIndex.value == categoryList.indexOf(category),
                        onStartDrag = {
                            draggingUuid = category.uuid
                        }
                    )
                }
            }
        }
    }


//    @Composable
//    private fun CategoriesListSuccessDragScreen(
//        state: CategoryListViewState.State.SuccessDragDrop,
//        onAction: (CategoryListState.Action) -> Unit
//    ) {
//        // Список категорий с mutableState
//        val categoryList = remember { mutableStateListOf(*state.categoryList.toTypedArray()) }
//
//        // UUID текущего перетаскиваемого элемента
//        var draggingUuid by remember { mutableStateOf<String?>(null) }
//
//        LazyColumn(modifier = Modifier.fillMaxSize()) {
//            items(categoryList, key = { it.uuid }) { category ->
//                val isDragging = draggingUuid == category.uuid
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .graphicsLayer {
//                            // Визуальный эффект для перетаскиваемого элемента
//                            alpha = if (isDragging) 0.5f else 1f
//                            translationZ
//                        }
//                        .pointerInput(Unit) {
//                            detectDragGestures(
//                                onDragStart = {
//                                    draggingUuid = category.uuid
//                                },
//                                onDragEnd = {
//                                    draggingUuid = null
//                                },
//                                onDragCancel = {
//                                    draggingUuid = null
//                                },
//                                onDrag = { change, dragAmount ->
//                                    change.consume()
//
//                                    val fromIndex =
//                                        categoryList.indexOfFirst { it.uuid == category.uuid }
//                                    val toIndex = when {
//                                        dragAmount.y > 20 -> fromIndex + 1
//                                        dragAmount.y < -20 -> fromIndex - 1
//                                        else -> fromIndex
//                                    }
//
//                                    if (toIndex in categoryList.indices && fromIndex != toIndex) {
//                                        val newList = categoryList.toMutableList()
//
//                                        // Перемещаем элемент
//                                        val movedItem = newList.removeAt(fromIndex)
//                                        newList.add(toIndex, movedItem)
//
//                                        // Пересчитываем приоритеты
//                                        val updatedList = newList.mapIndexed { index, item ->
//                                            item.copy(priority = index)
//                                        }
//
//                                        // Обновляем UI
//                                        categoryList.clear()
//                                        categoryList.addAll(updatedList)
//
//                                        // Обновляем UUID перетаскиваемого
//                                        draggingUuid = movedItem.uuid
//                                    }
//                                }
//                            )
//                        }
//                ) {
//                    CategoryItemDraggable(
//                        category = category,
//                        isDragging = isDragging
//                    )
//                }
//            }
//        }
//    }


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

        return CategoryListViewState(
            state = when (state.state) {
                CategoryListState.DataState.State.LOADING -> CategoryListViewState.State.Loading
                CategoryListState.DataState.State.ERROR -> CategoryListViewState.State.Error
                CategoryListState.DataState.State.SUCCESS -> {
                    if (state.isEditPriority) {
                        CategoryListViewState.State.SuccessDragDrop(categoryItems)
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

    @Composable
    fun CategoryItemDraggable(
        category: CategoryListViewState.CategoriesViewItem,
        isDragging: Boolean = false,
        isTarget: Boolean = false,
        onStartDrag: (() -> Unit)? = null
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isDragging) Modifier
                    .alpha(0.5f)
                    .scale(1.05f)
                else Modifier
            )
            .then(
                if (isTarget) Modifier
                    .zIndex(1f)       // Поверх других
                else Modifier
            )

        AdminCard(
            modifier = modifier
                .padding(vertical = 4.dp),
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
                            detectTapGestures(
                                onLongPress = {
                                    onStartDrag?.invoke()
                                }
                            )
                        }
                )
            }
        }
    }


    private fun DragAndDropEvent.clipData(): ClipData? {
        return this.toAndroidDragEvent().clipData
    }
}
