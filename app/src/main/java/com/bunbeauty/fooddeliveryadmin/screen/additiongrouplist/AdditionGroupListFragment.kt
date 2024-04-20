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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.additiongrouplist.AdditionGroupList
import com.bunbeauty.presentation.feature.additiongrouplist.AdditionGroupListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@AndroidEntryPoint
class AdditionGroupListFragment :
    BaseComposeFragment<AdditionGroupList.ViewDataState, AdditionGroupListViewState, AdditionGroupList.Action, AdditionGroupList.Event>() {

    override val viewModel: AdditionGroupListViewModel by viewModels()

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
            backActionClick = {
                onAction(AdditionGroupList.Action.OnBackClick)
            }
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (state.visibleAdditionItems.isNotEmpty()) {
                    item(
                        key = R.string.title_position_visible
                    ) {
                        Text(
                            text = stringResource(id = R.string.title_position_visible),
                            style = AdminTheme.typography.titleMedium.bold
                        )
                    }
                    items(
                        state.visibleAdditionItems,
                        key = { additionItem ->
                            additionItem.uuid
                        }
                    ) { visibleAddition ->
                        AdditionCard(
                            additionItem = visibleAddition,
                            visible = true,
                            onAction = onAction
                        )
                    }
                }
                if (state.hiddenAdditionItems.isNotEmpty()) {
                    item(
                        key = R.string.title_position_hidden
                    ) {
                        Text(
                            text = stringResource(id = R.string.title_position_hidden),
                            style = AdminTheme.typography.titleMedium.bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    items(state.hiddenAdditionItems) { hiddenAddition ->
                        AdditionCard(
                            additionItem = hiddenAddition,
                            visible = false,
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun AdditionCard(
        additionItem: AdditionGroupListViewState.AdditionGroupItem,
        visible: Boolean,
        onAction: (AdditionGroupList.Action) -> Unit
    ) {
        AdminCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onAction(AdditionGroupList.Action.OnAdditionClick)
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
                    modifier = Modifier
                        .weight(1f)
                )

                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    onClick = {
                        onAction(AdditionGroupList.Action.OnVisibleClick(visible))
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
    override fun mapState(state: AdditionGroupList.ViewDataState): AdditionGroupListViewState {
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
            is AdditionGroupList.Event.OnAdditionClick -> {
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
                            iconColor = AdminTheme.colors.main.primary
                        )
                    ),
                    hiddenAdditionItems = persistentListOf(
                        AdditionGroupListViewState.AdditionGroupItem(
                            uuid = "2",
                            name = "additio2",
                            iconColor = AdminTheme.colors.main.onSecondary
                        )
                    ),
                    isRefreshing = false,
                    isLoading = false
                ),
                onAction = {
                }
            )
        }
    }
}
