package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.editaditiongroupformenuproduct

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenu
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenuProductViewModel
import com.bunbeauty.presentation.feature.settings.state.SettingsState
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class EditAdditionGroupForMenuProductFragment :
    SingleStateComposeFragment<EditAdditionGroupForMenu.DataState, EditAdditionGroupForMenu.Action, EditAdditionGroupForMenu.Event>() {

    override val viewModel: EditAdditionGroupForMenuProductViewModel by viewModel()
    private val editAdditionGroupForMenuProductFragmentArgs: EditAdditionGroupForMenuProductFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAction(
            EditAdditionGroupForMenu.Action.Init(
                additionGroupForMenuUuid = editAdditionGroupForMenuProductFragmentArgs
                    .additionGroupForMenuUuid,
                menuProductUuid = editAdditionGroupForMenuProductFragmentArgs.menuProductUuid
            )
        )
    }

    @Composable
    override fun Screen(
        state: EditAdditionGroupForMenu.DataState,
        onAction: (EditAdditionGroupForMenu.Action) -> Unit
    ) {
        EditAdditionGroupForMenuProductScreen(state = state, onAction = onAction)
    }

    @Composable
    fun EditAdditionGroupForMenuProductScreen(
        state: EditAdditionGroupForMenu.DataState,
        onAction: (EditAdditionGroupForMenu.Action) -> Unit
    ) {
        AdminScaffold(
            title = state.groupName,
            backActionClick = {
                onAction(EditAdditionGroupForMenu.Action.OnBackClick)
            },
            backgroundColor = AdminTheme.colors.main.surface
        ) {
            when (state.state) {
                EditAdditionGroupForMenu.DataState.State.Loading -> LoadingScreen()
                EditAdditionGroupForMenu.DataState.State.Error -> TODO()
                EditAdditionGroupForMenu.DataState.State.Success -> EditAdditionGroupForMenuProductSuccessScreen(
                    state = state,
                    onAction = onAction
                )
            }
        }
    }

    @Composable
    fun EditAdditionGroupForMenuProductSuccessScreen(
        state: EditAdditionGroupForMenu.DataState,
        onAction: (EditAdditionGroupForMenu.Action) -> Unit
    ) {
        Column {
            NavigationTextCard(
                valueText = state.groupName,
                onClick = {

                },
                elevated = false,
                labelText = stringResource(
                    id = R.string.title_edit_addition_group_for_menu_product_group
                ),
                hasDivider = true
            )

            NavigationTextCard(
                valueText = state.additionNameList,
                onClick = {

                },
                elevated = false,
                labelText = stringResource(
                    id = R.string.title_edit_addition_group_for_menu_product_addition
                ),
                hasDivider = true
            )

            SwitcherCard(
                modifier = Modifier.padding(vertical = 8.dp),
                elevated = false,
                text = stringResource(R.string.title_edit_addition_group_for_menu_product_visible),
                checked = state.isVisible,
                onCheckChanged = { isVisible ->

                }
            )

        }
    }

    override fun handleEvent(event: EditAdditionGroupForMenu.Event) {
        when (event) {
            EditAdditionGroupForMenu.Event.Back -> {
                findNavController().popBackStack()
            }

            is EditAdditionGroupForMenu.Event.OnAdditionGroupClick -> TODO()
        }
    }

    val editAdditionGroupForMenuProductViewState = EditAdditionGroupForMenu.DataState(
        state = EditAdditionGroupForMenu.DataState.State.Success,
        groupName = "Вкусняхи",
        additionNameList = "Бекон * Страпон * Бурбон",
        isVisible = true,
        additionUuid = ""
    )

    @Composable
    @Preview
    fun EditAdditionGroupForMenuProductScreenPreview() {
        AdminTheme {
            EditAdditionGroupForMenuProductScreen(
                state = editAdditionGroupForMenuProductViewState,
                onAction = {}
            )
        }
    }
}
