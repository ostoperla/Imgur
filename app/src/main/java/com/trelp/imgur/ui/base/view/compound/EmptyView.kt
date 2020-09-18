package com.trelp.imgur.ui.base.view.compound

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.trelp.imgur.R
import com.trelp.imgur.databinding.EmptyViewBinding
import com.trelp.imgur.inflater

class EmptyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: EmptyViewBinding
    private val res = context.resources

    init {
        binding = EmptyViewBinding.inflate(inflater, this, true)
    }

    fun showEmptyData() {
        with(binding) {
            title.text = res.getText(R.string.empty_data_title)
            description.text = res.getText(R.string.empty_data_description)
        }
        visibility = View.VISIBLE
    }

    fun showEmptyError(msg: String? = null) {
        with(binding) {
            title.text = res.getText(R.string.empty_data_error)
            description.text = msg ?: res.getText(R.string.empty_data_error_description)
        }
        visibility = View.VISIBLE
    }

    fun setRefreshListener(listener: () -> Unit) {
        binding.refresh.setOnClickListener { listener() }
    }

    fun hide() {
        visibility = View.GONE
    }
}