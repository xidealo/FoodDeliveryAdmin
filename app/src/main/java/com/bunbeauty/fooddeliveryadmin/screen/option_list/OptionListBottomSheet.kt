package com.bunbeauty.fooddeliveryadmin.screen.option_list

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.fragment.app.FragmentManager
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetOptionListBinding
import com.bunbeauty.fooddeliveryadmin.databinding.ElementTextBinding
import com.bunbeauty.fooddeliveryadmin.util.argument
import com.bunbeauty.fooddeliveryadmin.util.setLinearLayoutMargins
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@AndroidEntryPoint
class OptionListBottomSheet : BottomSheetDialogFragment() {

    private var title by argument<String>()
    private var options by argument<List<Option>>()
    private var callback: Callback? = null

    private var mutableBinding: BottomSheetOptionListBinding? = null
    private val binding: BottomSheetOptionListBinding
        get() = checkNotNull(mutableBinding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mutableBinding = BottomSheetOptionListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.DialogFragmentStyle
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            bottomSheetOptionListTvTitle.text = title
            options.onEachIndexed { i, option ->
                ElementTextBinding.inflate(layoutInflater, bottomSheetOptionListLlList, false)
                    .also { elementBinding ->
                        bottomSheetOptionListLlList.addView(elementBinding.root)
                        if (i > 0) {
                            elementBinding.root.setLinearLayoutMargins(
                                top = resources.getDimensionPixelOffset(R.dimen.small_margin)
                            )
                        }
                        elementBinding.elementTextTvTitle.text = option.title
                        elementBinding.root.setOnClickListener {
                            callback?.onClicked(option.id)
                            dismiss()
                        }
                    }
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        callback?.onCancel()
    }

    private interface Callback {
        fun onClicked(id: String?)
        fun onCancel()
    }

    companion object {
        private const val TAG = "OptionListBottomSheet"

        suspend fun show(
            fragmentManager: FragmentManager, title: String, options: List<Option>
        ) = suspendCancellableCoroutine<String?> { continuation ->
            OptionListBottomSheet().apply {
                this.title = title
                this.options = options
                callback = object : Callback {
                    override fun onClicked(id: String?) {
                        continuation.resume(id)
                    }

                    override fun onCancel() {
                        continuation.cancel()
                    }
                }
                show(fragmentManager, TAG)
            }
        }
    }
}