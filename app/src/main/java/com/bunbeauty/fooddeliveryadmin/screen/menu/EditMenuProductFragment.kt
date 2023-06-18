package com.bunbeauty.fooddeliveryadmin.screen.menu

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentComposeBinding
import com.bunbeauty.fooddeliveryadmin.util.compose
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.view_model.menu.edit_menu_product.EditMenuProductUIState
import com.bunbeauty.presentation.view_model.menu.edit_menu_product.EditMenuProductViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditMenuProductFragment : BaseFragment<FragmentComposeBinding>() {

    @Inject
    lateinit var resourcesProvider: IResourcesProvider

    override val viewModel: EditMenuProductViewModel by viewModels()

//    private val imageLauncher =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            if (uri != null) {
//                binding.fragmentEditMenuProductIvPhoto.setImageURI(uri)
//                viewModel.photo = binding.fragmentEditMenuProductIvPhoto.getBitmap()
//            }
//        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.compose {
            val editMenuProductUIState by viewModel.editMenuProductUiState.collectAsStateWithLifecycle()
            EditMenuProductScreen(editMenuProductUIState = editMenuProductUIState)
        }
    }

    @Composable
    fun EditMenuProductScreen(editMenuProductUIState: EditMenuProductUIState) {
        AdminScaffold(
            title = stringResource(R.string.title_bottom_navigation_menu),
        ) {
            when (editMenuProductUIState.editMenuProductState) {
                EditMenuProductUIState.EditMenuProductState.Error -> TODO()
                EditMenuProductUIState.EditMenuProductState.Loading -> TODO()
                is EditMenuProductUIState.EditMenuProductState.Success -> TODO()
            }
        }
    }


    @Composable
    fun EditMenuProductSuccessScreen(){

    }

    @Composable
    fun EditMenuProductErrorScreen(){

    }

    @Composable
    fun EditMenuProductLoadingScreen(){

    }

    fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_edit_menu_product_alert)
            .setMessage(R.string.msg_edit_menu_product_alert)
            .setPositiveButton(R.string.action_edit_menu_product_alert_yes) { _, _ ->
                viewModel.deleteMenuProduct()
            }
            .setNegativeButton(R.string.action_edit_menu_product_alert_cancel) { _, _ -> }
            .show()
    }
}
