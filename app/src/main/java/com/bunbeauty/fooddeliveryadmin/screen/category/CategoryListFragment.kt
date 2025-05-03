package com.bunbeauty.fooddeliveryadmin.screen.category

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.FloatingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCardDefaults.noCornerCardShape
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
            title = stringResource(R.string.title_categories_list),
            pullRefreshEnabled = true,
            refreshing = state.isRefreshing,
            onRefresh = {
                onAction(CategoryListState.Action.OnRefreshData)
            },
            backActionClick = {
                onAction(CategoryListState.Action.OnBackClicked)
            },
            // TODO(Ваня доделает в обновлении 1.9.0)
//            topActions = listOf(
//                AdminTopBarAction(
//                    iconId = R.drawable.ic_edit,
//                    color = AdminTheme.colors.main.primary,
//                    onClick = {
//                        onAction(CategoryListState.Action.OnPriorityEditClicked)
//                    }
//                )
//            ),
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

                is CategoryListViewState.State.SuccessDragDrop -> TODO()
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
        return CategoryListViewState(
            state = when (state.state) {
                CategoryListState.DataState.State.LOADING -> CategoryListViewState.State.Loading
                CategoryListState.DataState.State.ERROR -> CategoryListViewState.State.Error
                CategoryListState.DataState.State.SUCCESS -> CategoryListViewState.State.Success(
                    categoryList = state.categoryList.map { category ->
                        CategoryListViewState.CategoriesViewItem(
                            uuid = category.uuid,
                            name = category.name,
                            priority = category.priority
                        )
                    }.toPersistentList()
                )
            },
            isLoading = state.isLoading,
            isRefreshing = state.isRefreshing,
            isEditPriority = state.isEditPriority
        )
    }


    @Composable
    private fun CategoryItemDraggable(
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
                Icon(
                    painter = painterResource(id = R.drawable.ic_drad_handle),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
            }
        }
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

//    @Preview(showSystemUi = true)
//    @Composable
//    private fun SettingsScreenPreview() {
//        AdminTheme {
//            CategoriesScreen(
//                state = CategoryListViewState(
//                    state = CategoryListViewState.State.Success(
//                        categoryList = persistentListOf(
//                            CategoryListViewState.CategoriesViewItem(
//                                uuid = "",
//                                name = "Лаваш",
//                                priority = 0
//                            ),
//
//                            CategoryListViewState.CategoriesViewItem(
//                                uuid = "BBB",
//                                name = "Соус",
//                                priority = 1
//                            )
//                        )
//                    ),
//                    isLoading = false,
//                    isRefreshing = false,
//                    isEditPriority = false
//                ),
//                onAction = {}
//            )
//        }
//    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewCategoryItemDraggable() {
        val sampleCategory = CategoryListViewState.CategoriesViewItem(
            uuid = "1",
            priority = 1,
            name = "Категория 1"
        )

        AdminTheme {
            CategoryItemDraggable(
                category = sampleCategory,
                onClick = {},
                isClickable = true
            )
        }
    }
}
