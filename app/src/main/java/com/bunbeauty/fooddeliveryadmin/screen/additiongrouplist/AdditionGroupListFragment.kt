package com.bunbeauty.fooddeliveryadmin.screen.additiongrouplist

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.presentation.feature.additiongrouplist.AdditionGroupList
import com.bunbeauty.presentation.feature.additiongrouplist.AdditionGroupListViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TITLE_POSITION_VISIBLE_KEY = "title_position_visible"
private const val TITLE_POSITION_HIDDEN_KEY = "title_position_hidden"

class AdditionGroupListFragment :
    BaseComposeFragment<AdditionGroupList.DataState, AdditionGroupListViewState, AdditionGroupList.Action, AdditionGroupList.Event>() {

    override val viewModel: AdditionGroupListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAction(AdditionGroupList.Action.Init)
    }

    @Composable
    override fun Screen(
        state: AdditionGroupListViewState,
        onAction: (AdditionGroupList.Action) -> Unit
    ) {
        AdditionGroupListScreen(state = state, onAction = onAction)
    }

    @Composable
    fun AdditionGroupListScreen(
        state: AdditionGroupListViewState,
        onAction: (AdditionGroupList.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_addition_group_list),
            pullRefreshEnabled = true,
            refreshing = state.isRefreshing,
            onRefresh = {
                onAction(AdditionGroupList.Action.RefreshData)
            },
            backActionClick = {
                onAction(AdditionGroupList.Action.OnBackClick)
            }
        ) {
            when {
                state.isLoading -> LoadingScreen()
                else -> AdditionGroupListSuccess(state = state, onAction = onAction)
            }
        }
    }

    @Composable
    private fun AdditionGroupListSuccess(
        state: AdditionGroupListViewState,
        onAction: (AdditionGroupList.Action) -> Unit
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.visibleAdditionItems.isNotEmpty()) {
                item(
                    key = TITLE_POSITION_VISIBLE_KEY
                ) {
                    Text(
                        text = stringResource(id = R.string.title_menu_list_position_visible),
                        style = AdminTheme.typography.titleMedium.bold
                    )
                }
                items(
                    items = state.visibleAdditionItems,
                    key = { additionItem ->
                        additionItem.uuid
                    }
                ) { visibleAddition ->
                    AdditionGroupCard(
                        additionItem = visibleAddition,
                        onAction = onAction
                    )
                }
            }
            if (state.hiddenAdditionItems.isNotEmpty()) {
                item(
                    key = TITLE_POSITION_HIDDEN_KEY
                ) {
                    Text(
                        text = stringResource(id = R.string.title_menu_list_position_hidden),
                        style = AdminTheme.typography.titleMedium.bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                items(
                    items = state.hiddenAdditionItems,
                    key = { additionGroupItem ->
                        additionGroupItem.uuid
                    }
                ) { hiddenAddition ->
                    AdditionGroupCard(
                        additionItem = hiddenAddition,
                        onAction = onAction
                    )
                }
            }
        }
    }

    @Composable
    private fun AdditionGroupCard(
        additionItem: AdditionGroupListViewState.AdditionGroupItem,
        onAction: (AdditionGroupList.Action) -> Unit
    ) {
        AdminCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onAction(AdditionGroupList.Action.OnAdditionClick(
                    additionUuid = additionItem.uuid
                ))
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = additionItem.name,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        onAction(
                            AdditionGroupList.Action.OnVisibleClick(
                                isVisible = additionItem.isVisible,
                                uuid = additionItem.uuid
                            )
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_visible),
                        contentDescription = null,
                        tint = additionItem.iconColor
                    )
                }
            }
        }
    }

    @Composable
    override fun mapState(state: AdditionGroupList.DataState): AdditionGroupListViewState {
        return AdditionGroupListViewState(
            visibleAdditionItems = state.visibleAdditionGroups.map { additionGroup ->
                additionGroup.toItem()
            }.toPersistentList(),
            hiddenAdditionItems = state.hiddenAdditionGroups.map { additionGroup ->
                additionGroup.toItem()
            }.toPersistentList(),
            isRefreshing = state.isRefreshing,
            isLoading = state.isLoading
        )
    }

    override fun handleEvent(event: AdditionGroupList.Event) {
        when (event) {
            AdditionGroupList.Event.Back -> findNavController().popBackStack()
            is AdditionGroupList.Event.OnAdditionGroupClick -> {
                findNavController().navigateSafe(
                    AdditionGroupListFragmentDirections.toEditAdditionGroupFragment(
                        event.additionUuid
                    )
                )
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun AdditionListScreenPreview() {
        AdminTheme {
            AdditionGroupListScreen(
                state = AdditionGroupListViewState(
                    visibleAdditionItems = persistentListOf(
                        AdditionGroupListViewState.AdditionGroupItem(
                            uuid = "1",
                            name = "additio1",
                            iconColor = AdminTheme.colors.main.primary,
                            isVisible = true
                        )
                    ),
                    hiddenAdditionItems = persistentListOf(
                        AdditionGroupListViewState.AdditionGroupItem(
                            uuid = "2",
                            name = "additio2",
                            iconColor = AdminTheme.colors.main.onSecondary,
                            isVisible = false
                        )
                    ),
                    isRefreshing = false,
                    isLoading = false
                ),
                onAction = {}
            )
        }
    }
}
