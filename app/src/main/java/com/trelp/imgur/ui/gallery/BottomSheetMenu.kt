package com.trelp.imgur.ui.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.trelp.imgur.R
import com.trelp.imgur.domain.Filter

class BottomSheetMenu : BottomSheetDialogFragment() {
    private lateinit var listener: OnClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as OnClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.layout_container, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }

    interface OnClickListener {
        val onMenuItemClick: (filter: Filter) -> Unit
    }
}