package com.bunbeauty.fooddeliveryadmin.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import com.bunbeauty.fooddeliveryadmin.R
import com.google.android.material.card.MaterialCardView

class NavigationCardView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attributeSet, defStyleAttr), Customizable {

    var cardText = getString(
        context,
        attributeSet,
        R.styleable.NavigationCardView_android_text,
        R.styleable.NavigationCardView,
        ""
    )
    set(value) {
        field = value
        textView.text = value
        //invalidate()
    }

    private val icon = getDrawable(
        context,
        attributeSet,
        R.styleable.NavigationCardView_android_icon,
        R.styleable.NavigationCardView
    )

    private val imageViewId = generateViewId()
    //private val textViewId = generateViewId()

    private val constraintLayout = createConstraintLayout(context)
    private val imageView = createImageView(context)
    private val textView = createTextView(context)

    init {
        constraintLayout.addView(imageView)
        constraintLayout.addView(textView)
        addView(constraintLayout)
    }

    private fun createConstraintLayout(context: Context): ConstraintLayout {
        return ConstraintLayout(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
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
            setImageDrawable(icon)
        }
    }

    private fun createTextView(context: Context): TextView {
        return TextView(context).apply {
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