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
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.MainButton
import com.bunbeauty.fooddeliveryadmin.compose.element.selectableitem.SelectableItem
import com.bunbeauty.fooddeliveryadmin.compose.element.selectableitem.SelectableItemView
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.presentation.feature.menulist.categorylist.SelectCategoryList
import com.bunbeauty.presentation.feature.menulist.categorylist.SelectCategoryListViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectCategoryListFragment :
    BaseComposeFragment<SelectCategoryList.DataState, SelectCategoryListViewState, SelectCategoryList.Action, SelectCategoryList.Event>() {
    companion object {
        const val CATEGORY_LIST_REQUEST_KEY = "CATEGORY_LIST_REQUEST_KEY"
        const val CATEGORY_LIST_KEY = "CATEGORY_LIST_KEY"
    }

    override val viewModel: SelectCategoryListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(SelectCategoryList.Action.Init)
    }

    @Composable
    override fun Screen(
        state: SelectCategoryListViewState,
        onAction: (SelectCategoryList.Action) -> Unit,
    ) {
        CategoryListScreen(state = state, onAction = onAction)
    }

    @Composable
    fun CategoryListScreen(
        state: SelectCategoryListViewState,
        onAction: (SelectCategoryList.Action) -> Unit,
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_category_list),
            backActionClick = {
                onAction(SelectCategoryList.Action.OnBackClick)
            },
            actionButton = {
                MainButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.action_category_list_save),
                    onClick = {
                        onAction(SelectCategoryList.Action.OnSaveClick)
                    },
                )
            },
        ) {
            Column {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    verticalArrangement = Arrangement.Absolute.spacedBy(8.dp),
                    contentPadding =
                        PaddingValues(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = AdminTheme.dimensions.scrollScreenBottomSpace,
                        ),
                ) {
                    items(
                        items = state.selectableCategoryList,
                        key = { category -> category.uuid },
                    ) { selectableCategory ->
                        SelectableItemView(
                            selectableItem =
                                SelectableItem(
                                    uuid = selectableCategory.uuid,
                                    title = selectableCategory.name,
                                    isSelected = selectableCategory.selected,
                                ),
                            onClick = {
                                onAction(
                                    SelectCategoryList.Action.OnCategoryClick(
                                        uuid = selectableCategory.uuid,
                                        selected = selectableCategory.selected,
                                    ),
                                )
                            },
                            elevated = false,
                            isClickable = true,
                        )
                    }
                }
            }
        }
    }

    @Composable
    override fun mapState(state: SelectCategoryList.DataState): SelectCategoryListViewState =
        SelectCategoryListViewState(
            selectableCategoryList =
                state.selectableCategoryList
                    .map { selectableCategory ->
                        SelectCategoryListViewState.SelectCategoryItem(
                            uuid = selectableCategory.category.uuid,
                            name = selectableCategory.category.name,
                            selected = selectableCategory.selected,
                        )
                    }.toPersistentList(),
        )

    @Preview(showSystemUi = true)
    @Composable
    fun CategoryListScreenPreview() {
        AdminTheme {
            CategoryListScreen(
                state =
                    SelectCategoryListViewState(
                        selectableCategoryList =
                            persistentListOf(
                                SelectCategoryListViewState.SelectCategoryItem(
                                    uuid = "movet",
                                    name = "Roy Faulkner",
                                    selected = false,
                                ),
                            ),
                    ),
                onAction = {},
            )
        }
    }

    override fun handleEvent(event: SelectCategoryList.Event) {
        when (event) {
            SelectCategoryList.Event.Back -> {
                findNavController().popBackStack()
            }

            is SelectCategoryList.Event.Save -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(R.string.msg_category_list_selected),
                )
                setFragmentResult(
                    CATEGORY_LIST_REQUEST_KEY,
                    bundleOf(CATEGORY_LIST_KEY to event.selectedCategoryUuidList),
                )
                findNavController().popBackStack()
            }
        }
    }
}
