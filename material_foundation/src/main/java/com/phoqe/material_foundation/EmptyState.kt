package com.phoqe.material_foundation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.phoqe.material_foundation.databinding.EmptyStateBinding

class EmptyState @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding = EmptyStateBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.EmptyState, 0, 0).apply {
            try {
                binding.ivImage.setImageDrawable(getDrawable(R.styleable.EmptyState_image))
                binding.ivImage.contentDescription = getString(R.styleable.EmptyState_image_cd)
                binding.tvTitle.text = getString(R.styleable.EmptyState_title)
                binding.tvBody.text = getString(R.styleable.EmptyState_body)
            } finally {
                recycle()
            }
        }
    }
}