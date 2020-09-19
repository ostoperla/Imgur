package com.trelp.imgur.ui.base.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class PaginalAdapter(
    private val itemDiff: (old: Any, new: Any) -> Boolean,
    private val nextPageCallback: () -> Unit,
    vararg delegates: AdapterDelegate<MutableList<Any>>
) : AsyncListDifferDelegationAdapter<Any>(
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            if (oldItem === newItem) return true
            return itemDiff.invoke(oldItem, newItem)
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any) = oldItem == newItem

        /**
         * Disable default animation.
         * @see [RecyclerView.Adapter.onBindViewHolder] with payloads;
         * @see [RecyclerView.ViewHolder.addChangePayload].
         */
        override fun getChangePayload(oldItem: Any, newItem: Any) = Any()

    }
) {
    var fullData = false

    init {
        items = mutableListOf()
        delegates.withIndex().forEach { (index, delegate) ->
            if (index != 0) delegatesManager.addDelegate(delegate)
            else delegatesManager.addDelegate(0, delegate)
        }
    }

    fun update(data: List<Any>, isPageProgress: Boolean) {
        items = mutableListOf<Any>().apply {
            addAll(data)
            if (isPageProgress) add(ProgressItem)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (!fullData && position >= items.size - 15) nextPageCallback.invoke()
    }
}