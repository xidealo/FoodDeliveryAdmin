package com.bunbeauty.fooddeliveryadmin.screen.selectcafe

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.card.SelectableCard
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.ComposeBottomSheet
import com.bunbeauty.fooddeliveryadmin.util.argument
import com.bunbeauty.presentation.feature.selectcafe.SelectableCafeItem
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SelectCafeBottomSheet : ComposeBottomSheet<SelectableCafeItem>() {

    private var cafeList by argument<List<SelectableCafeItem>>()

    @Composable
    override fun Content() {
        SelectCafeScreen(
            cafeList = cafeList,
            onCafeClicked = { cafeItem ->
                callback?.onResult(cafeItem)
                dismiss()
            }
        )
    }

    companion object {
        private const val TAG = "CafeListBottomSheet"

        suspend fun show(
            fragmentManager: FragmentManager,
            addressList: List<SelectableCafeItem>,
        ) = suspendCoroutine { continuation ->
            SelectCafeBottomSheet().apply {
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
private fun SelectCafeScreen(
    cafeList: List<SelectableCafeItem>,
    onCafeClicked: (SelectableCafeItem) -> Unit,
) {
    AdminBottomSheet(titleStringId = R.string.msg_common_cafe) {
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
private fun SelectCafeScreenPreview() {
    AdminTheme {
        SelectCafeScreen(
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
                )
            ),
            onCafeClicked = {}
        )
    }
}
