package com.bunbeauty.fooddeliveryadmin.screen.optionlist

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetOptionListBinding
import com.bunbeauty.fooddeliveryadmin.databinding.ElementPrimaryTextBinding
import com.bunbeauty.fooddeliveryadmin.databinding.ElementTextBinding
import com.bunbeauty.fooddeliveryadmin.util.argument
import com.bunbeauty.fooddeliveryadmin.util.setLinearLayoutMargins
import com.bunbeauty.presentation.Option
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Deprecated("Use AdminModalBottomSheet.kt")
@AndroidEntryPoint
class OptionListBottomSheet : BottomSheetDialogFragment() {

    private var title by argument<String>()
    private var options by argument<List<Option>>()
    private var isCenter by argument<Boolean>()
    private var callback: Callback? = null

    private var mutableBinding: BottomSheetOptionListBinding? = null
    private val binding: BottomSheetOptionListBinding
        get() = checkNotNull(mutableBinding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
                val (binding, textView) = if (option.isPrimary) {
                    val elementBinding = ElementPrimaryTextBinding.inflate(
                        layoutInflater,
                        bottomSheetOptionListLlList,
                        false
                    )
                    elementBinding to elementBinding.elementTextTvTitle
                } else {
                    val elementBinding = ElementTextBinding.inflate(
                        layoutInflater,
                        bottomSheetOptionListLlList,
                        false
                    )
                    elementBinding to elementBinding.elementTextTvTitle
                }

                binding.also { elementBinding ->
                    bottomSheetOptionListLlList.addView(elementBinding.root)
                    if (i > 0) {
                        elementBinding.root.setLinearLayoutMargins(
                            top = resources.getDimensionPixelOffset(R.dimen.small_margin)
                        )
                    }
                    if (isCenter) {
                        textView.textAlignment = TEXT_ALIGNMENT_CENTER
                    }
                    textView.text = option.title
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
            fragmentManager: FragmentManager,
            title: String,
            options: List<Option>,
            isCenter: Boolean = false
        ) = suspendCoroutine { continuation ->
            OptionListBottomSheet().apply {
                this.title = title
                this.options = options
                this.isCenter = isCenter
                callback = object : Callback {
                    override fun onClicked(id: String?) {
                        continuation.resume(OptionListResult(id))
                    }

                    override fun onCancel() {
                        continuation.resume(null)
                    }
                }
                show(fragmentManager, TAG)
            }
        }
    }
}
