package com.phoqe.fackla.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.phoqe.fackla.R
import com.phoqe.fackla.databinding.ComponentEmptyStateBinding

class EmptyState @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding = ComponentEmptyStateBinding.inflate(LayoutInflater.from(context), this)

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.EmptyState, 0, 0).apply {
            try {
                binding.ivIllustration.setImageDrawable(getDrawable(R.styleable.EmptyState_image))
                binding.tvTitle.text = getString(R.styleable.EmptyState_title)
                binding.tvBody.text = getString(R.styleable.EmptyState_body)
            } finally {
                recycle()
            }
        }
    }
}