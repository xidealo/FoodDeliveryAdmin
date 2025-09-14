package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.selectadditiongroup

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationTextCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.SwitcherCard
import com.bunbeauty.fooddeliveryadmin.compose.element.selectableitem.SelectableItem
import com.bunbeauty.fooddeliveryadmin.compose.element.selectableitem.SelectableItemView
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenu
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenuProductViewModel
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup.SelectAdditionGroup
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup.SelectAdditionGroupViewModel
import com.bunbeauty.presentation.feature.menulist.categorylist.SelectCategoryList
import com.bunbeauty.presentation.feature.profile.Profile
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SelectAdditionGroupFragment :
    SingleStateComposeFragment<SelectAdditionGroup.DataState, SelectAdditionGroup.Action, SelectAdditionGroup.Event>() {

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
        }
    }

    @Composable
    fun SelectAdditionGroupScreen(
        state: SelectAdditionGroup.DataState,
        onAction: (SelectAdditionGroup.Action) -> Unit
    ) {
        AdminScaffold(
            title = "Выбор группы",
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
                            //onAction(SelectAdditionGroup.Action)
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
            items(
                items = state.selectableAdditionGroupList,
                key = { selectableAdditionGroup ->
                    selectableAdditionGroup.uuid
                }
            ) { selectableCategory ->
                SelectableItemView(
                    selectableItem = SelectableItem(
                        uuid = selectableCategory.uuid,
                        title = selectableCategory.name,
                        isSelected = selectableCategory.isSelected
                    ),
                    hasDivider = true,
                    onClick = {
//                        onAction(
//                            SelectCategoryList.Action.OnCategoryClick(
//                                uuid = selectableCategory.uuid,
//                                selected = selectableCategory.selected
//                            )
//                        )
                    },
                    elevated = false,
                    isClickable = true
                )
            }
        }
    }


    val selectAdditionGroupViewState = SelectAdditionGroup.DataState(
        state = SelectAdditionGroup.DataState.State.SUCCESS,
        selectableAdditionGroupList = listOf(
            SelectAdditionGroup.DataState.SelectableAdditionGroupItem(
                uuid = "uuid1",
                name = "Roy Faulkner",
                isSelected = false
            ),
            SelectAdditionGroup.DataState.SelectableAdditionGroupItem(
                uuid = "uuid2",
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
