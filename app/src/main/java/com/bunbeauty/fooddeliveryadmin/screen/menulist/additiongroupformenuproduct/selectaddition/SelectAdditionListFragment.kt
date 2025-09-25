package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.selectaddition

import android.os.Bundle
import android.view.View
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition.SelectAdditionList
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition.SelectAdditionListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

private const val TITLE_POSITION_VISIBLE_KEY = "title_position_visible"
private const val TITLE_POSITION_HIDDEN_KEY = "title_position_hidden"
private const val LIST_ANIMATION_DURATION = 500

class SelectAdditionListFragment :
    SingleStateComposeFragment<SelectAdditionList.DataState, SelectAdditionList.Action, SelectAdditionList.Event>() {

    companion object {
        const val SELECT_ADDITION_KEY = "SELECT_ADDITION_GROUP_KEY"
        const val ADDITION_KEY = "ADDITION_GROUP_KEY"
    }

    override val viewModel: SelectAdditionListViewModel by viewModel()
    private val selectAdditionFragmentArgs: SelectAdditionListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAction(
            SelectAdditionList.Action.Init(
                menuProductUuid = selectAdditionFragmentArgs.additionGroupUuid,
                additionGroupUuid = selectAdditionFragmentArgs.menuProductUuid
            )
        )
    }

    @Composable
    override fun Screen(
        state: SelectAdditionList.DataState,
        onAction: (SelectAdditionList.Action) -> Unit
    ) {
        SelectAdditionScreen(state = state, onAction = onAction)
    }

    override fun handleEvent(event: SelectAdditionList.Event) {
        when (event) {
            SelectAdditionList.Event.Back -> {
                findNavController().popBackStack()
            }

//            is SelectAddition.Event.SelectAdditionGroupClicked -> {
//                (activity as? MessageHost)?.showInfoMessage(
//                    resources.getString(
//                        R.string.msg_select_addition_group_selected,
//                        event.additionGroupName
//                    )
//                )
//                setFragmentResult(
//                    requestKey = SELECT_ADDITION_KEY,
//                    result = bundleOf(ADDITION_KEY to event.additionGroupUuid)
//                )
//                findNavController().popBackStack()
//            }
        }
    }

    @Composable
    fun SelectAdditionScreen(
        state: SelectAdditionList.DataState,
        onAction: (SelectAdditionList.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(id = R.string.title_select_addition_group),
            backActionClick = {
                onAction(SelectAdditionList.Action.OnBackClick)
            },
            backgroundColor = AdminTheme.colors.main.surface,
            actionButton = {
                LoadingButton(
                    text = stringResource(R.string.action_order_details_save),
                    isLoading = false,
                    onClick = {
                        // onAction(SelectAdditionList.Action.SaveMenuProductClick)
                    }
                )
            }
        ) {
            when (state.state) {
                SelectAdditionList.DataState.State.LOADING -> LoadingScreen()
                SelectAdditionList.DataState.State.ERROR -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            // onAction(SelectAdditionGroup.Action)
                        }
                    )
                }

                SelectAdditionList.DataState.State.SUCCESS -> SelectAdditionSuccessScreen(
                    state = state,
                    onAction = onAction
                )
            }
        }
    }

    @Composable
    fun SelectAdditionSuccessScreen(
        state: SelectAdditionList.DataState,
        onAction: (SelectAdditionList.Action) -> Unit
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
                    text = stringResource(
                        id = R.string.action_select_addition_list_title_group,
                        state.groupName
                    ),
                    style = AdminTheme.typography.titleMedium.bold
                )
            }
        }
    }

    private val selectAdditionListViewState = SelectAdditionList.DataState(
        state = SelectAdditionList.DataState.State.SUCCESS,
        groupName = "Some group"
    )

    @Composable
    @Preview
    fun SelectAdditionScreenPreview() {
        AdminTheme {
            SelectAdditionScreen(
                state = selectAdditionListViewState,
                onAction = {}
            )
        }
    }
}
