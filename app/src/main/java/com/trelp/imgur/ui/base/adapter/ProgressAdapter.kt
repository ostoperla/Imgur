package com.trelp.imgur.ui.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.trelp.imgur.databinding.ProgressItemBinding
import com.trelp.imgur.inflater
import timber.log.Timber

abstract class ProgressAdapterDelegate : AdapterDelegate<MutableList<Any>>() {
    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is ProgressItem
}

class SimpleProgressAdapterDelegate : ProgressAdapterDelegate() {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        SimpleProgressViewHolder(
            ProgressItemBinding.inflate(parent.inflater, parent, false)
        )

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as SimpleProgressViewHolder).bind()

    private inner class SimpleProgressViewHolder(
        binding: ProgressItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            Timber.tag("bind").d("Progress")
        }
    }
}

class StaggeredGridProgressAdapterDelegate : ProgressAdapterDelegate() {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ProgressViewHolder(
            ProgressItemBinding.inflate(parent.inflater, parent, false)
        )

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ProgressViewHolder).bind()

    private inner class ProgressViewHolder(
        binding: ProgressItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val layoutParams =
            itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams

        fun bind() {
            Timber.tag("bind").d("Progress")
            layoutParams.isFullSpan = true
        }
    }
}
