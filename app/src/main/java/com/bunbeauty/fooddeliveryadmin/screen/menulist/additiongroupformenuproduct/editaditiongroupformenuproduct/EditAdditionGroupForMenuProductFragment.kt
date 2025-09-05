package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.editaditiongroupformenuproduct

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenu
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenuProductViewModel
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
                additionGroupForMenuUuid = editAdditionGroupForMenuProductFragmentArgs.additionGroupForMenuUuid
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
        loading = false,
        groupName = "Вкусняхи",
        additionNameList = "Бекон * Страпон * Бурбон",
        isVisible = true,
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
