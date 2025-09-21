package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.selectadditiongroup

import android.os.Bundle
import android.view.View
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectadditiongroup.SelectableAdditionGroup
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.selectableitem.SelectableItem
import com.bunbeauty.fooddeliveryadmin.compose.element.selectableitem.SelectableItemView
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.fooddeliveryadmin.screen.menulist.categorylist.SelectCategoryListFragment.Companion.CATEGORY_LIST_KEY
import com.bunbeauty.fooddeliveryadmin.screen.menulist.categorylist.SelectCategoryListFragment.Companion.CATEGORY_LIST_REQUEST_KEY
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup.SelectAdditionGroup
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup.SelectAdditionGroupViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

private const val TITLE_POSITION_VISIBLE_KEY = "title_position_visible"
private const val TITLE_POSITION_HIDDEN_KEY = "title_position_hidden"
private const val LIST_ANIMATION_DURATION = 500

class SelectAdditionGroupFragment :
    SingleStateComposeFragment<SelectAdditionGroup.DataState, SelectAdditionGroup.Action, SelectAdditionGroup.Event>() {

    companion object {
        const val SELECT_ADDITION_GROUP_KEY = "SELECT_ADDITION_GROUP_KEY"
        const val ADDITION_GROUP_KEY = "ADDITION_GROUP_KEY"
    }

    override val viewModel: SelectAdditionGroupViewModel by viewModel()
    private val selectAdditionGroupFragmentArgs: SelectAdditionGroupFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAction(
            SelectAdditionGroup.Action.Init(
                selectedAdditionGroupUuid = selectAdditionGroupFragmentArgs.additionGroupUuid
            )
        )
    }

    @Composable
    override fun Screen(
        state: SelectAdditionGroup.DataState,
        onAction: (SelectAdditionGroup.Action) -> Unit
    ) {
        SelectAdditionGroupScreen(state = state, onAction = onAction)
    }

    override fun handleEvent(event: SelectAdditionGroup.Event) {
        when (event) {
            SelectAdditionGroup.Event.Back -> {
                findNavController().popBackStack()
            }

            is SelectAdditionGroup.Event.SelectAdditionGroupClicked -> {
                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(
                        R.string.msg_select_addition_group_selected,
                        event.additionGroupName
                    )
                )
                setFragmentResult(
                    requestKey = SELECT_ADDITION_GROUP_KEY,
                    result = bundleOf(ADDITION_GROUP_KEY to event.additionGroupUuid)
                )
                findNavController().popBackStack()
            }
        }
    }

    @Composable
    fun SelectAdditionGroupScreen(
        state: SelectAdditionGroup.DataState,
        onAction: (SelectAdditionGroup.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(id = R.string.title_select_addition_group),
            backActionClick = {
                onAction(SelectAdditionGroup.Action.OnBackClick)
            },
            backgroundColor = AdminTheme.colors.main.surface
        ) {
            when (state.state) {
                SelectAdditionGroup.DataState.State.LOADING -> LoadingScreen()
                SelectAdditionGroup.DataState.State.ERROR -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            // onAction(SelectAdditionGroup.Action)
                        }
                    )
                }

                SelectAdditionGroup.DataState.State.SUCCESS -> SelectAdditionGroupSuccessScreen(
                    state = state,
                    onAction = onAction
                )
            }
        }
    }

    @Composable
    fun SelectAdditionGroupSuccessScreen(
        state: SelectAdditionGroup.DataState,
        onAction: (SelectAdditionGroup.Action) -> Unit
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace
            )
        ) {
            item(
                key = TITLE_POSITION_VISIBLE_KEY
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            bottom = 16.dp
                        )
                        .animateItem()
                        .animateContentSize(
                            animationSpec = tween(LIST_ANIMATION_DURATION)
                        ),
                    text = stringResource(id = R.string.title_select_addition_group_position_visible),
                    style = AdminTheme.typography.titleMedium.bold
                )
            }

            items(
                items = state.visibleSelectableAdditionGroupList,
                key = { selectableAdditionGroup ->
                    selectableAdditionGroup.uuid
                }
            ) { selectableCategory ->
                SelectableItemView(
                    modifier = Modifier
                        .animateItem(),
                    selectableItem = SelectableItem(
                        uuid = selectableCategory.uuid,
                        title = selectableCategory.name,
                        isSelected = selectableCategory.isSelected
                    ),
                    hasDivider = true,
                    onClick = {
                        onAction(
                            SelectAdditionGroup.Action.SelectAdditionGroupClick(
                                uuid = selectableCategory.uuid,
                                name = selectableCategory.name
                            )
                        )
                    },
                    elevated = false,
                    isClickable = true
                )
            }

            if (state.hiddenSelectableAdditionGroupList.isNotEmpty()) {
                item(
                    key = TITLE_POSITION_HIDDEN_KEY
                ) {
                    Text(
                        modifier = Modifier
                            .padding(
                                all = 16.dp
                            )
                            .animateItem()
                            .animateContentSize(
                                animationSpec = tween(LIST_ANIMATION_DURATION)
                            ),
                        text = stringResource(id = R.string.title_menu_list_position_hidden),
                        style = AdminTheme.typography.titleMedium.bold
                    )
                }
                items(
                    items = state.hiddenSelectableAdditionGroupList,
                    key = { hiddenAdditionGroup ->
                        hiddenAdditionGroup.uuid
                    }
                ) { hiddenAdditionGroup ->
                    SelectableItemView(
                        modifier = Modifier
                            .animateItem(),
                        selectableItem = SelectableItem(
                            uuid = hiddenAdditionGroup.uuid,
                            title = hiddenAdditionGroup.name,
                            isSelected = hiddenAdditionGroup.isSelected
                        ),
                        hasDivider = true,
                        onClick = {
                            onAction(
                                SelectAdditionGroup.Action.SelectAdditionGroupClick(
                                    uuid = hiddenAdditionGroup.uuid,
                                    name = hiddenAdditionGroup.name
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

    val selectAdditionGroupViewState = SelectAdditionGroup.DataState(
        state = SelectAdditionGroup.DataState.State.SUCCESS,
        visibleSelectableAdditionGroupList = listOf(
            SelectableAdditionGroup(
                uuid = "uuid1",
                name = "Roy Faulkner",
                isSelected = false
            ),
            SelectableAdditionGroup(
                uuid = "uuid2",
                name = "Roy Faulkner",
                isSelected = false
            )
        ),
        hiddenSelectableAdditionGroupList = listOf(
            SelectableAdditionGroup(
                uuid = "uuid3",
                name = "Roy Faulkner",
                isSelected = false
            )
        )
    )

    @Composable
    @Preview
    fun SelectAdditionGroupScreenPreview() {
        AdminTheme {
            SelectAdditionGroupScreen(
                state = selectAdditionGroupViewState,
                onAction = {}
            )
        }
    }
}
