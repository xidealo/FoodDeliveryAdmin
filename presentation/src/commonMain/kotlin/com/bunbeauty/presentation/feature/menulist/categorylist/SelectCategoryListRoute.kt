package com.bunbeauty.presentation.feature.menulist.categorylist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.button.MainButton
import com.bunbeauty.presentation.designsystem.compose.element.selectableitem.SelectableItem
import com.bunbeauty.presentation.designsystem.compose.element.selectableitem.SelectableItemView
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_category_list_save
import fooddeliveryadmin.presentation.generated.resources.msg_category_list_selected
import fooddeliveryadmin.presentation.generated.resources.title_category_list
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SelectCategoryListRouteScreen(
    viewModel: SelectCategoryListViewModel = koinViewModel(),
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: SelectCategoryList.Action ->
                viewModel.onAction(event)
            }
        }

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }

    LaunchedEffect(Unit) {
        onAction(SelectCategoryList.Action.Init)
    }

    SelectCategoryListEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        goBack = goBack,
    )

    SelectCategoryListScreen(
        state = viewState.toViewState(),
        onAction = onAction,
    )
}

@Composable
private fun SelectCategoryListEffect(
    effects: List<SelectCategoryList.Event>,
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                SelectCategoryList.Event.Back -> {
                    goBack()
                }

                is SelectCategoryList.Event.Save -> {
                    showInfoMessage(getString(Res.string.msg_category_list_selected), 2000)
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun SelectCategoryListScreen(
    state: SelectCategoryListViewState,
    onAction: (SelectCategoryList.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_category_list),
        backActionClick = {
            onAction(SelectCategoryList.Action.OnBackClick)
        },
        actionButton = {
            MainButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(resource = Res.string.action_category_list_save),
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
