package com.bunbeauty.fooddeliveryadmin.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.core_ui.Customizable
import com.bunbeauty.presentation.utils.ResourcesProvider
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Deprecated("Use MaterialCardView or compose")
@AndroidEntryPoint
class NavigationCardView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attributeSet, defStyleAttr), Customizable {

    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    var cardText: String? = getString(
        context,
        attributeSet,
        R.styleable.NavigationCardView_android_text,
        R.styleable.NavigationCardView,
        ""
    )
        set(value) {
            field = value
            textView?.text = value
        }

    private val icon = getDrawable(
        context,
        attributeSet,
        R.styleable.NavigationCardView_android_icon,
        R.styleable.NavigationCardView
    )

    private val imageViewId = generateViewId()

    private var textView: TextView? = null

    init {
        radius = resourcesProvider.getDimensionFloat(R.dimen.medium_radius)
        cardElevation = 0f

        textView = createTextView(context)
        addView(textView)
    }

    private fun createLayout(context: Context): LinearLayout {
        return LinearLayout(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
    }

    private fun createImageView(context: Context): ImageView {
        return ImageView(context).apply {
            id = imageViewId
            layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            setColorFilter(resourcesProvider.getColor(R.color.colorPrimary))
            setImageDrawable(icon)
        }
    }

    private fun createTextView(context: Context): TextView {
        return TextView(context).apply {
            textSize = 16f
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            text = cardText
            setCompoundDrawables(
                null,
                null,
                icon,
                null,
            )
        }
    }
}
