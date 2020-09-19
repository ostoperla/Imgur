package com.trelp.imgur.ui.base.view.compound

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.trelp.imgur.databinding.PaginalViewBinding
import com.trelp.imgur.message
import com.trelp.imgur.presentation.Paginator
import com.trelp.imgur.ui.base.adapter.PaginalAdapter
import com.trelp.imgur.visible

class PaginalRenderStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: PaginalViewBinding
    private lateinit var adapter: PaginalAdapter
    private lateinit var refreshCallback: () -> Unit

    init {
        binding = PaginalViewBinding.inflate(LayoutInflater.from(context), this).apply {
            swipeToRefresh.setOnRefreshListener { refreshCallback() }
            emptyView.setRefreshListener { refreshCallback() }
        }
    }

    fun initialize(
        manager: RecyclerView.LayoutManager,
        pagAdapter: PaginalAdapter,
        refreshCallback: () -> Unit
    ) {
        adapter = pagAdapter
        this.refreshCallback = refreshCallback
        binding.recyclerView.apply {
            layoutManager = manager
            adapter = pagAdapter
            setHasFixedSize(true)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun render(state: Paginator.State) {
        when (state) {
            is Paginator.State.Empty -> binding.apply {
                swipeToRefresh.isRefreshing = false
                swipeToRefresh.visible(true)
                fullScreenProgress.root.visible(false)
                adapter.fullData = false
                adapter.update(emptyList(), false)
                emptyView.showEmptyData()
            }
            is Paginator.State.EmptyProgress -> binding.apply {
                swipeToRefresh.isRefreshing = false
                swipeToRefresh.visible(false)
                fullScreenProgress.root.visible(true)
                adapter.fullData = false
                adapter.update(emptyList(), false)
                emptyView.hide()
            }
            is Paginator.State.NewPageProgress<*> -> binding.apply {
                swipeToRefresh.isRefreshing = false
                swipeToRefresh.visible(true)
                fullScreenProgress.root.visible(false)
                adapter.fullData = false
                adapter.update(state.data as List<Any>, true)
                emptyView.hide()
            }
            is Paginator.State.Data<*> -> binding.apply {
                swipeToRefresh.isRefreshing = false
                swipeToRefresh.visible(true)
                fullScreenProgress.root.visible(false)
                adapter.fullData = false
                adapter.update(state.data as List<Any>, false)
                emptyView.hide()
            }
            is Paginator.State.FullData<*> -> binding.apply {
                swipeToRefresh.isRefreshing = false
                swipeToRefresh.visible(true)
                fullScreenProgress.root.visible(false)
                adapter.fullData = true
                adapter.update(state.data as List<Any>, false)
                emptyView.hide()
            }
            is Paginator.State.Refresh<*> -> binding.apply {
                swipeToRefresh.isRefreshing = true
                swipeToRefresh.visible(true)
                fullScreenProgress.root.visible(false)
                adapter.fullData = false
                adapter.update(state.data as List<Any>, false)
                emptyView.hide()
            }
            is Paginator.State.FullDataRefresh<*> -> binding.apply {
                swipeToRefresh.isRefreshing = true
                swipeToRefresh.visible(true)
                fullScreenProgress.root.visible(false)
                adapter.fullData = true
                adapter.update(state.data as List<Any>, false)
                emptyView.hide()
            }
            is Paginator.State.EmptyError -> binding.apply {
                swipeToRefresh.isRefreshing = false
                swipeToRefresh.visible(true)
                fullScreenProgress.root.visible(false)
                adapter.fullData = false
                adapter.update(emptyList(), false)
                emptyView.showEmptyError(state.error.message(context))
            }
        }
    }
}