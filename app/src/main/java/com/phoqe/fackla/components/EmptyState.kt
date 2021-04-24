package com.phoqe.fackla.components

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.phoqe.fackla.R
import java.security.KeyStore

class EmptyState @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.component_empty_state, this)

        attrs?.let {
            
        }
    }
}