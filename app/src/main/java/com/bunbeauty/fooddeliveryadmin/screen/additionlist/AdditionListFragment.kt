package com.bunbeauty.fooddeliveryadmin.screen.additionlist

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.additionlist.AdditionList
import com.bunbeauty.presentation.feature.additionlist.AdditionListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@AndroidEntryPoint
class AdditionListFragment :
    BaseComposeFragment<AdditionList.ViewDataState, AdditionListViewState, AdditionList.Action, AdditionList.Event>() {

    companion object {
        private const val TITLE_POSITION_VISIBLE_KEY = "title_position_visible"
        private const val TITLE_POSITION_HIDDEN_KEY = "title_position_hidden"
    }

    override val viewModel: AdditionListViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAction(AdditionList.Action.Init)
    }
    @Composable
    override fun Screen(state: AdditionListViewState, onAction: (AdditionList.Action) -> Unit) {
        AdditionListScreen(state = state, onAction = onAction)
    }

    @Composable
    fun AdditionListScreen(state: AdditionListViewState, onAction: (AdditionList.Action) -> Unit) {
        AdminScaffold(
            title = stringResource(R.string.title_addition_list),
            pullRefreshEnabled = true,
            refreshing = state.isRefreshing,
            onRefresh = {
                onAction(AdditionList.Action.RefreshData)
            },
            backActionClick = {
                onAction(AdditionList.Action.OnBackClick)
            }
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
                            text = stringResource(id = R.string.title_position_visible),
                            style = AdminTheme.typography.titleMedium.bold
                        )
                    }
                    items(
                        items = state.visibleAdditionItems,
                        key = { additionItem ->
                            additionItem.uuid
                        }
                    ) { visibleAddition ->
                        AdditionCard(
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
                            text = stringResource(id = R.string.title_position_hidden),
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
                        AdditionCard(
                            additionItem = hiddenAddition,
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun AdditionCard(
        additionItem: AdditionListViewState.AdditionItem,
        onAction: (AdditionList.Action) -> Unit
    ) {
        AdminCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onAction(AdditionList.Action.OnAdditionClick(additionItem.uuid))
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .width(AdminTheme.dimensions.productImageSmallWidth)
                        .height(AdminTheme.dimensions.productImageSmallHeight),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(additionItem.photoLink)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.default_product),
                    contentDescription = stringResource(R.string.description_product),
                    contentScale = ContentScale.FillWidth
                )

                Text(
                    text = additionItem.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = AdminTheme.dimensions.smallSpace)
                        .padding(horizontal = AdminTheme.dimensions.smallSpace)
                )

                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = AdminTheme.dimensions.smallSpace),
                    onClick = {
                        onAction(
                            AdditionList.Action.OnVisibleClick(
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
    override fun mapState(state: AdditionList.ViewDataState): AdditionListViewState {
        return AdditionListViewState(
            visibleAdditionItems = state.visibleAdditions.map { addition ->
                addition.toItem()
            }.toPersistentList(),
            hiddenAdditionItems = state.hiddenAdditions.map { addition ->
                addition.toItem()
            }.toPersistentList(),
            isRefreshing = state.isRefreshing,
            isLoading = state.isLoading
        )
    }

    override fun handleEvent(event: AdditionList.Event) {
        when (event) {
            AdditionList.Event.Back -> findNavController().popBackStack()
            is AdditionList.Event.OnAdditionClick -> {
                // TODO (implement)
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun AdditionListScreenPreview() {
        AdminTheme {
            AdditionListScreen(
                state = AdditionListViewState(
                    visibleAdditionItems = persistentListOf(
                        AdditionListViewState.AdditionItem(
                            uuid = "1",
                            name = "additio1",
                            photoLink = "",
                            iconColor = AdminTheme.colors.main.primary,
                            isVisible = true
                        )
                    ),
                    hiddenAdditionItems = persistentListOf(
                        AdditionListViewState.AdditionItem(
                            uuid = "2",
                            name = "additio2",
                            photoLink = "",
                            iconColor = AdminTheme.colors.main.primary,
                            isVisible = false
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
