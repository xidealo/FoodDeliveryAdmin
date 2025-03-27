package com.bunbeauty.fooddeliveryadmin.screen.category

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.FloatingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCardDefaults.noCornerCardShape
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.presentation.feature.category.CategoryState
import com.bunbeauty.presentation.feature.category.CategoryViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryListFragment :
    BaseComposeFragment<CategoryState.DataState, CategoryViewState, CategoryState.Action, CategoryState.Event>() {

    override val viewModel: CategoryViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAction(CategoryState.Action.Init)
    }

    @Composable
    override fun Screen(
        state: CategoryViewState,
        onAction: (CategoryState.Action) -> Unit
    ) {
        CategoriesScreen(
            state = state,
            onAction = onAction
        )
    }

    @Composable
    private fun CategoriesScreen(
        state: CategoryViewState,
        onAction: (CategoryState.Action) -> Unit
    ) {
        AdminScaffold(
            backgroundColor = AdminTheme.colors.main.surface,
            title = stringResource(R.string.title_categories_list),
            pullRefreshEnabled = true,
            refreshing = state.isRefreshing,
            onRefresh = {
                onAction(CategoryState.Action.OnRefreshData)
            },
            backActionClick = {
                onAction(CategoryState.Action.OnBackClicked)
            },
            topActions = listOf(
                AdminTopBarAction(
                    iconId = R.drawable.ic_edit,
                    color = AdminTheme.colors.main.primary,
                    onClick = {
                        onAction(CategoryState.Action.OnPriorityEditClicked)
                    }
                )
            ),
            actionButton = {
                if (state.state is CategoryViewState.State.Success) {
                    FloatingButton(
                        iconId = R.drawable.ic_plus,
                        textStringId = R.string.action_menu_list_create,
                        onClick = {
                            onAction(CategoryState.Action.OnCreateClicked)
                        }
                    )
                }
            },
            actionButtonPosition = FabPosition.End
        ) {
            when (state.state) {
                CategoryViewState.State.Loading -> {
                    LoadingScreen()
                }

                CategoryViewState.State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            onAction(CategoryState.Action.Init)
                        }
                    )
                }

                is CategoryViewState.State.Success -> {
                    CategoriesListSuccessScreen(state = state.state, onAction = onAction)
                }
            }
        }
    }

    @Composable
    private fun CategoriesListSuccessScreen(
        state: CategoryViewState.State.Success,
        onAction: (CategoryState.Action) -> Unit
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(
                items = state.categoryList,
                key = { _, category -> category.uuid }
            ) { index, category ->
                CategoryItemView(
                    category = CategoryViewState.CategoriesViewItem(
                        uuid = category.uuid,
                        name = category.name,
                        priority = category.priority
                    ),
                    onClick = {
                        onAction(
                            CategoryState.Action.OnCategoryClick(
                                categoryUuid = category.uuid
                            )
                        )
                    },
                    isClickable = true
                )
            }
        }
    }

    override fun handleEvent(event: CategoryState.Event) {
        when (event) {
            CategoryState.Event.GoBackEvent -> {
                findNavController().navigateUp()
            }

            CategoryState.Event.OnEditPriorityCategoryEvent -> {
                TODO()
            }

            CategoryState.Event.CreateCategoryEvent -> {
                findNavController().navigateSafe(CategoryListFragmentDirections.toCreateCategoryFragment())
            }

            is CategoryState.Event.OnCategoryClick -> {
                findNavController().navigateSafe(
                    CategoryListFragmentDirections.toEditCategoryListFragment(
                        event.categoryUuid
                    )
                )
            }
        }
    }

    @Composable
    override fun mapState(state: CategoryState.DataState): CategoryViewState {
        return CategoryViewState(
            state = when (state.state) {
                CategoryState.DataState.State.LOADING -> CategoryViewState.State.Loading
                CategoryState.DataState.State.ERROR -> CategoryViewState.State.Error
                CategoryState.DataState.State.SUCCESS -> CategoryViewState.State.Success(
                    categoryList = state.categoryList.map { category ->
                        CategoryViewState.CategoriesViewItem(
                            uuid = category.uuid,
                            name = category.name,
                            priority = category.priority
                        )
                    }.toPersistentList()
                )
            },
            isLoading = state.isLoading,
            isRefreshing = state.isRefreshing
        )
    }

    @Composable
    private fun CategoryItemView(
        modifier: Modifier = Modifier,
        category: CategoryViewState.CategoriesViewItem,
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
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 16.dp)
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
                state = CategoryViewState(
                    state = CategoryViewState.State.Success(
                        categoryList = persistentListOf(
                            CategoryViewState.CategoriesViewItem(
                                uuid = "",
                                name = "Лаваш",
                                priority = 0
                            ),

                            CategoryViewState.CategoriesViewItem(
                                uuid = "BBB",
                                name = "Соус",
                                priority = 1
                            )
                        )
                    ),
                    isLoading = false,
                    isRefreshing = false
                ),
                onAction = {}
            )
        }
    }
}
