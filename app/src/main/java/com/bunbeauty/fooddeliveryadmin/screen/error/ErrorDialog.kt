package com.bunbeauty.fooddeliveryadmin.screen.error

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.DialogErrorBinding
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class ErrorDialog : DialogFragment() {
    private lateinit var binding: DialogErrorBinding

    private var callback: DialogCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogFragmentStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            retryBtn.setOnClickListener {
                callback?.onRetry()
                dismiss()
            }
            closeIv.setOnClickListener {
                callback?.onDismiss()
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback?.onDismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        callback?.onDismiss()
    }

    interface DialogCallback {
        fun onRetry()
        fun onDismiss()
    }

    companion object {
        suspend fun show(fragmentManager: FragmentManager): Unit =
            suspendCancellableCoroutine { continuation ->
                val dlg = ErrorDialog().apply {
                    this.callback = object : DialogCallback {
                        override fun onRetry() {
                            continuation.resume(Unit)
                        }

                        override fun onDismiss() {
                            continuation.cancel()
                        }
                    }
                }
                if (!fragmentManager.isStateSaved) {
                    dlg.show(fragmentManager, "warningDialog")
                }
            }
    }
}
