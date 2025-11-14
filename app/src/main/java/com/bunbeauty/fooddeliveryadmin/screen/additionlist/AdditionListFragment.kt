package com.bunbeauty.fooddeliveryadmin.screen.additionlist

import android.os.Bundle
import android.view.View
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
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
import androidx.navigation.fragment.findNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.FloatingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.presentation.feature.additionlist.AdditionList
import com.bunbeauty.presentation.feature.additionlist.AdditionListViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TITLE_POSITION_VISIBLE_KEY = "title_position_visible"
private const val TITLE_POSITION_HIDDEN_KEY = "title_position_hidden"
private const val LIST_ANIMATION_DURATION = 500

class AdditionListFragment :
    BaseComposeFragment<AdditionList.DataState, AdditionListViewState, AdditionList.Action, AdditionList.Event>() {
    override val viewModel: AdditionListViewModel by viewModel()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAction(AdditionList.Action.Init)
    }

    @Composable
    override fun Screen(
        state: AdditionListViewState,
        onAction: (AdditionList.Action) -> Unit,
    ) {
        AdditionListScreen(state = state, onAction = onAction)
    }

    @Composable
    fun AdditionListScreen(
        state: AdditionListViewState,
        onAction: (AdditionList.Action) -> Unit,
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_addition_list),
            pullRefreshEnabled = true,
            refreshing = state.isRefreshing,
            onRefresh = {
                onAction(AdditionList.Action.RefreshData)
            },
            backActionClick = {
                onAction(AdditionList.Action.OnBackClick)
            },
            actionButton = {
                if (!state.isLoading && !state.hasError) {
                    FloatingButton(
                        iconId = R.drawable.ic_plus,
                        textStringId = R.string.action_addition_list_create,
                        onClick = {
                            findNavController().navigateSafe(
                                AdditionListFragmentDirections
                                    .actionAdditionListFragmentToCreateAdditionFragment(),
                            )
                        },
                    )
                }
            },
            actionButtonPosition = FabPosition.End,
        ) {
            when {
                state.hasError -> {
                    ErrorScreen(
                        mainTextId = R.string.error_common_loading_failed,
                        isLoading = state.isLoading,
                    ) {
                        onAction(AdditionList.Action.Init)
                    }
                }

                state.isLoading -> LoadingScreen()

                else -> AdditionListSuccessScreen(state = state, onAction = onAction)
            }
        }
    }

    @Composable
    private fun AdditionListSuccessScreen(
        state: AdditionListViewState,
        onAction: (AdditionList.Action) -> Unit,
    ) {
        LazyColumn(
            contentPadding =
                PaddingValues(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = AdminTheme.dimensions.scrollScreenBottomSpace,
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (state.visibleAdditionItems.isNotEmpty()) {
                item(
                    key = TITLE_POSITION_VISIBLE_KEY,
                ) {
                    Text(
                        modifier =
                            Modifier
                                .animateItem()
                                .animateContentSize(
                                    animationSpec = tween(LIST_ANIMATION_DURATION),
                                ),
                        text = stringResource(id = R.string.title_menu_list_position_visible),
                        style = AdminTheme.typography.titleMedium.bold,
                    )
                }
                items(
                    items = state.visibleAdditionItems,
                    key = { additionItem ->
                        additionItem.key
                    },
                ) { visibleAddition ->
                    when (visibleAddition) {
                        is AdditionListViewState.AdditionFeedViewItem.AdditionItem ->
                            AdditionCard(
                                modifier =
                                    Modifier
                                        .animateItem()
                                        .animateContentSize(
                                            animationSpec = tween(LIST_ANIMATION_DURATION),
                                        ),
                                additionItem = visibleAddition.addition,
                                onAction = onAction,
                            )

                        is AdditionListViewState.AdditionFeedViewItem.Title ->
                            Text(
                                modifier =
                                    Modifier
                                        .animateItem()
                                        .animateContentSize(
                                            animationSpec = tween(LIST_ANIMATION_DURATION),
                                        ),
                                text = visibleAddition.title,
                                style = AdminTheme.typography.titleSmall.bold,
                            )
                    }
                }
            }

            if (state.hiddenAdditionItems.isNotEmpty()) {
                item(
                    key = TITLE_POSITION_HIDDEN_KEY,
                ) {
                    Text(
                        modifier =
                            Modifier
                                .padding(top = 8.dp)
                                .animateItem()
                                .animateContentSize(
                                    animationSpec = tween(LIST_ANIMATION_DURATION),
                                ),
                        text = stringResource(id = R.string.title_menu_list_position_hidden),
                        style = AdminTheme.typography.titleMedium.bold,
                    )
                }
                items(
                    items = state.hiddenAdditionItems,
                    key = { additionGroupItem ->
                        additionGroupItem.key
                    },
                ) { hiddenAddition ->
                    when (hiddenAddition) {
                        is AdditionListViewState.AdditionFeedViewItem.AdditionItem ->
                            AdditionCard(
                                modifier =
                                    Modifier
                                        .animateItem()
                                        .animateContentSize(
                                            animationSpec = tween(LIST_ANIMATION_DURATION),
                                        ),
                                additionItem = hiddenAddition.addition,
                                onAction = onAction,
                            )

                        is AdditionListViewState.AdditionFeedViewItem.Title ->
                            Text(
                                modifier =
                                    Modifier
                                        .animateItem()
                                        .animateContentSize(
                                            animationSpec = tween(LIST_ANIMATION_DURATION),
                                        ),
                                text = hiddenAddition.title,
                                style = AdminTheme.typography.titleSmall.bold,
                            )
                    }
                }
            }
        }
    }

    @Composable
    private fun AdditionCard(
        modifier: Modifier,
        additionItem: AdditionListViewState.AdditionViewItem,
        onAction: (AdditionList.Action) -> Unit,
    ) {
        AdminCard(
            modifier = modifier.fillMaxWidth(),
            onClick = {
                onAction(AdditionList.Action.OnAdditionClick(additionUuid = additionItem.uuid))
            },
            elevated = false,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                AsyncImage(
                    modifier =
                        Modifier
                            .width(AdminTheme.dimensions.productImageSmallWidth)
                            .height(AdminTheme.dimensions.productImageSmallHeight),
                    model =
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(additionItem.photoLink)
                            .crossfade(true)
                            .build(),
                    placeholder = painterResource(R.drawable.default_product),
                    contentDescription = stringResource(R.string.description_product),
                    contentScale = ContentScale.FillWidth,
                )

                Text(
                    text = additionItem.name,
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(top = AdminTheme.dimensions.smallSpace)
                            .padding(horizontal = AdminTheme.dimensions.smallSpace),
                )

                IconButton(
                    modifier =
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = AdminTheme.dimensions.smallSpace),
                    onClick = {
                        onAction(
                            AdditionList.Action.OnVisibleClick(
                                isVisible = additionItem.isVisible,
                                uuid = additionItem.uuid,
                            ),
                        )
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_visible),
                        contentDescription = null,
                        tint = additionItem.iconColor,
                    )
                }
            }
        }
    }

    @Composable
    override fun mapState(state: AdditionList.DataState): AdditionListViewState =
        AdditionListViewState(
            visibleAdditionItems =
                state.visibleAdditions
                    .map { additionFeedItem ->
                        additionFeedItem.toItem()
                    }.toPersistentList(),
            hiddenAdditionItems =
                state.hiddenAdditions
                    .map { additionFeedItem ->
                        additionFeedItem.toItem()
                    }.toPersistentList(),
            isRefreshing = state.isRefreshing,
            isLoading = state.isLoading,
            hasError = state.hasError,
        )

    override fun handleEvent(event: AdditionList.Event) {
        when (event) {
            is AdditionList.Event.Back -> {
                findNavController().popBackStack()
            }

            is AdditionList.Event.OnAdditionClick -> {
                findNavController().navigate(
                    AdditionListFragmentDirections.toEditAdditionFragment(
                        event.additionUuid,
                    ),
                )
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun AdditionListScreenPreview() {
        AdminTheme {
            AdditionListScreen(
                state =
                    AdditionListViewState(
                        visibleAdditionItems =
                            persistentListOf(
                                AdditionListViewState
                                    .AdditionFeedViewItem
                                    .Title(key = "1", "Заголовок"),
                                AdditionListViewState
                                    .AdditionFeedViewItem
                                    .AdditionItem(
                                        key = "2",
                                        AdditionListViewState.AdditionViewItem(
                                            uuid = "1",
                                            name = "additio1",
                                            photoLink = "",
                                            iconColor = AdminTheme.colors.main.primary,
                                            isVisible = true,
                                        ),
                                    ),
                            ),
                        hiddenAdditionItems =
                            persistentListOf(
                                AdditionListViewState
                                    .AdditionFeedViewItem
                                    .Title(
                                        key = "3",
                                        title = "Заголовок",
                                    ),
                                AdditionListViewState
                                    .AdditionFeedViewItem
                                    .AdditionItem(
                                        key = "4",
                                        AdditionListViewState.AdditionViewItem(
                                            uuid = "2",
                                            name = "additio2",
                                            photoLink = "",
                                            iconColor = AdminTheme.colors.main.primary,
                                            isVisible = false,
                                        ),
                                    ),
                            ),
                        isRefreshing = false,
                        isLoading = false,
                        hasError = false,
                    ),
                onAction = {
                },
            )
        }
    }
}
