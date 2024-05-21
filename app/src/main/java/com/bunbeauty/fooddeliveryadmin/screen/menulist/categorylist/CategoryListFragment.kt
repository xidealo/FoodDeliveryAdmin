package com.bunbeauty.fooddeliveryadmin.screen.menulist.categorylist

import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.MainButton
import com.bunbeauty.fooddeliveryadmin.compose.element.selectableitem.SelectableItem
import com.bunbeauty.fooddeliveryadmin.compose.element.selectableitem.SelectableItemView
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.menulist.categorylist.CategoryList
import com.bunbeauty.presentation.feature.menulist.categorylist.CategoryListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@AndroidEntryPoint
class CategoryListFragment :
    BaseComposeFragment<CategoryList.DataState, CategoryListViewState, CategoryList.Action, CategoryList.Event>() {

    companion object {
        const val CATEGORY_LIST_REQUEST_KEY = "CATEGORY_LIST_REQUEST_KEY"
        const val CATEGORY_LIST_KEY = "CATEGORY_LIST_KEY"
    }

    override val viewModel: CategoryListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(CategoryList.Action.Init)
    }

    @Composable
    override fun Screen(state: CategoryListViewState, onAction: (CategoryList.Action) -> Unit) {
        CategoryListScreen(state = state, onAction = onAction)
    }

    @Composable
    fun CategoryListScreen(
        state: CategoryListViewState,
        onAction: (CategoryList.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_category_list),
            backActionClick = {
                onAction(CategoryList.Action.OnBackClick)
            },
            actionButton = {
                MainButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.action_category_list_save),
                    onClick = {
                        onAction(CategoryList.Action.OnSaveClick)
                    }
                )
            }
        ) {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Absolute.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = AdminTheme.dimensions.scrollScreenBottomSpace
                    )
                ) {
                    items(
                        items = state.selectableCategoryList,
                        key = { category -> category.uuid }
                    ) { selectableCategory ->
                        SelectableItemView(
                            selectableItem = SelectableItem(
                                uuid = selectableCategory.uuid,
                                title = selectableCategory.name,
                                isSelected = selectableCategory.selected
                            ),
                            onClick = {
                                onAction(
                                    CategoryList.Action.OnCategoryClick(
                                        uuid = selectableCategory.uuid,
                                        selected = selectableCategory.selected
                                    )
                                )
                            },
                            elevated = false,
                            isClickable = true
                        )
                    }
                }
            }
        }
    }

    @Composable
    override fun mapState(state: CategoryList.DataState): CategoryListViewState {
        return CategoryListViewState(
            selectableCategoryList = state.selectableCategoryList.map { selectableCategory ->
                CategoryListViewState.CategoryItem(
                    uuid = selectableCategory.category.uuid,
                    name = selectableCategory.category.name,
                    selected = selectableCategory.selected
                )
            }.toPersistentList()
        )
    }

    @Preview(showSystemUi = true)
    @Composable
    fun CategoryListScreenPreview() {
        AdminTheme {
            CategoryListScreen(
                state = CategoryListViewState(
                    selectableCategoryList = persistentListOf(
                        CategoryListViewState.CategoryItem(
                            uuid = "movet",
                            name = "Roy Faulkner",
                            selected = false
                        )
                    )
                ),
                onAction = {}
            )
        }
    }

    override fun handleEvent(event: CategoryList.Event) {
        when (event) {
            CategoryList.Event.Back -> {
                findNavController().popBackStack()
            }

            is CategoryList.Event.Save -> {
                setFragmentResult(
                    CATEGORY_LIST_REQUEST_KEY,
                    bundleOf(CATEGORY_LIST_KEY to event.selectedCategoryUuidList)
                )
                findNavController().popBackStack()
            }
        }
    }
}
