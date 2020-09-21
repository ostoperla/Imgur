package com.trelp.imgur.ui.gallery

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.trelp.imgur.R
import com.trelp.imgur.databinding.FragmentGalleryBinding
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.flow.drawer.DrawerFlowComponent
import com.trelp.imgur.di.gallery.GalleryComponent
import com.trelp.imgur.domain.Filter
import com.trelp.imgur.domain.GalleryObject
import com.trelp.imgur.presentation.Paginator
import com.trelp.imgur.presentation.gallery.GalleryPresenter
import com.trelp.imgur.presentation.gallery.GalleryView
import com.trelp.imgur.ui.base.BaseFragment
import com.trelp.imgur.ui.base.adapter.PaginalAdapter
import com.trelp.imgur.ui.base.adapter.StaggeredGridProgressAdapterDelegate
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

class GalleryFragment : BaseFragment<GalleryComponent>(R.layout.fragment_gallery), GalleryView {

    private val binding
        get() = viewBinding!! as FragmentGalleryBinding

    @Inject
    lateinit var presenterProvider: Provider<GalleryPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    private val adapter by lazy {
        PaginalAdapter(
            { old, new ->
                if (old is GalleryObject && new is GalleryObject) old.id == new.id else false
            },
            { presenter.loadNextGalleryPage() },
            StaggeredGridProgressAdapterDelegate(),
            AlbumAdapterDelegate(),
            ImageAdapterDelegate()
        )
    }

    //region LifeCycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentGalleryBinding.bind(view)

        with(binding.toolbar) {
            inflateMenu(R.menu.gallery_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_mv_popular -> presenter.onFilterChanged(Filter.MV_POPULAR)
                    R.id.menu_mv_newest -> presenter.onFilterChanged(Filter.MV_NEWEST)
                    R.id.menu_mv_best -> presenter.onFilterChanged(Filter.MV_BEST)
                    R.id.menu_us_popular -> presenter.onFilterChanged(Filter.US_POPULAR)
                    R.id.menu_us_rising -> presenter.onFilterChanged(Filter.US_RISING)
                    R.id.menu_us_newest -> presenter.onFilterChanged(Filter.US_NEWEST)
                    R.id.menu_hs_today -> presenter.onFilterChanged(Filter.HS_DAY)
                    R.id.menu_hs_week -> presenter.onFilterChanged(Filter.HS_WEEK)
                    R.id.menu_hs_month -> presenter.onFilterChanged(Filter.HS_MONTH)
                    R.id.menu_hs_year -> presenter.onFilterChanged(Filter.HS_YEAR)
                    R.id.menu_hs_all_time -> presenter.onFilterChanged(Filter.HS_ALL_TIME)
                }
                return@setOnMenuItemClickListener true
            }
        }

        binding.pagingView.initialize(
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL),
            adapter
        ) { presenter.refreshGallery() }
    }
    //endregion

    //region GalleryView
    override fun renderState(state: Paginator.State) {
        binding.pagingView.render(state)
    }

    // SideEffects запускаются не на UI Thread, а результат нужен на UI.
    // В случае c SideEffect.LoadPage, имеется rx chain в presenter.loadNewPage(page), в котором также
    // происходит отписка от предыдущего SideEffect.LoadPage.
    // В случае с SideEffect.ErrorEvent сообщение летит с другого треда на View и никакой обработки
    // SideEffect нету.
    override fun showErrorMessage(msg: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
        }
    }
    //endregion

    //region Dagger
    override fun setupComponent(componentKey: String) {
        Injector.getComponent(this, componentKey).inject(this)
    }

    override fun createComponent() =
        Injector.findComponent<DrawerFlowComponent>().galleryComponentFactory().create()
    //endregion
}