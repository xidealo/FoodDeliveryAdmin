package com.bunbeauty.fooddeliveryadmin.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.core.view.setPadding
import com.bunbeauty.presentation.utils.ResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NavigationCardView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attributeSet, defStyleAttr), Customizable {

    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    var cardText = getString(
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
        strokeWidth = resourcesProvider.getDimension(R.dimen.button_stroke_width)
        strokeColor = resourcesProvider.getColor(R.color.colorPrimary)
        radius = resourcesProvider.getDimensionFloat(R.dimen.medium_radius)
        cardElevation = 0f

        val constraintLayout = createConstraintLayout(context)
        constraintLayout.addView(createImageView(context))
        textView = createTextView(context)
        constraintLayout.addView(textView)
        addView(constraintLayout)
    }

    private fun createConstraintLayout(context: Context): ConstraintLayout {
        return ConstraintLayout(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setPadding(resourcesProvider.getDimension(R.dimen.small_padding))
        }
    }

    private fun createImageView(context: Context): ImageView {
        return ImageView(context).apply {
            id = imageViewId
            layoutParams = ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                topToTop = PARENT_ID
                bottomToBottom = PARENT_ID
                endToEnd = PARENT_ID
            }
            setColorFilter(resourcesProvider.getColor(R.color.colorPrimary))
            setImageDrawable(icon)
        }
    }

    private fun createTextView(context: Context): TextView {
        return TextView(context).apply {
            textSize = 16f
            layoutParams = ConstraintLayout.LayoutParams(0, 0).apply {
                topToTop = PARENT_ID
                startToStart = PARENT_ID
                bottomToBottom = PARENT_ID
                endToEnd = imageViewId
            }
            text = cardText
        }
    }


}