package com.bunbeauty.fooddeliveryadmin.screen.cafe_list

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.bottom_sheet.AdminBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.card.SelectableCard
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.core_ui.ComposeBottomSheet
import com.bunbeauty.fooddeliveryadmin.util.argument
import com.bunbeauty.presentation.feature.cafe_list.SelectableCafeItem
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CafeListBottomSheet : ComposeBottomSheet<SelectableCafeItem>() {

    private var cafeList by argument<List<SelectableCafeItem>>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setContentWithTheme {
            CafeListScreen(
                cafeList = cafeList,
                onCafeClicked = { cafeItem ->
                    callback?.onResult(cafeItem)
                    dismiss()
                },
            )
        }
    }

    companion object {
        private const val TAG = "UserAddressListBottomSheet"

        suspend fun show(
            fragmentManager: FragmentManager,
            addressList: List<SelectableCafeItem>,
        ) = suspendCoroutine { continuation ->
            CafeListBottomSheet().apply {
                this.cafeList = addressList
                callback = object : Callback<SelectableCafeItem> {
                    override fun onResult(result: SelectableCafeItem?) {
                        continuation.resume(result)
                    }
                }
                show(fragmentManager, TAG)
            }
        }
    }
}

@Composable
private fun CafeListScreen(
    cafeList: List<SelectableCafeItem>,
    onCafeClicked: (SelectableCafeItem) -> Unit,
) {
    AdminBottomSheet(titleStringId = R.string.title_cafe_list) {
        Column(verticalArrangement = spacedBy(8.dp)) {
            cafeList.forEach { selectableCafeItem ->
                SelectableCard(
                    label = selectableCafeItem.address,
                    selected = selectableCafeItem.isSelected,
                    onClick = {
                        onCafeClicked(selectableCafeItem)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CafeListScreenPreview() {
    AdminTheme {
        CafeListScreen(
            cafeList = listOf(
                SelectableCafeItem(
                    uuid = "1",
                    address = "Адрес 1",
                    isSelected = true
                ),
                SelectableCafeItem(
                    uuid = "2",
                    address = "Оооооооооооооооооооооооочень длинный адрес 2",
                    isSelected = false
                ),
            ),
            onCafeClicked = {},
        )
    }
}
