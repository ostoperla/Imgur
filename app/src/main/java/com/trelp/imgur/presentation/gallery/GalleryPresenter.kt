package com.trelp.imgur.presentation.gallery

import com.trelp.imgur.data.SchedulersProvider
import com.trelp.imgur.di.FlowNav
import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.domain.Filter
import com.trelp.imgur.domain.GalleryInteractor
import com.trelp.imgur.domain.GalleryObject
import com.trelp.imgur.presentation.FlowRouter
import com.trelp.imgur.presentation.Paginator
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import moxy.MvpPresenter
import javax.inject.Inject

@FragmentScope
class GalleryPresenter @Inject constructor(
    @FlowNav private val flowRouter: FlowRouter,
    private val galleryInteractor: GalleryInteractor,
    private val schedulers: SchedulersProvider
) : MvpPresenter<GalleryView>() {
    private var currentFilter: Filter = Filter.MV_POPULAR
    private var pageDisposable: Disposable? = null
    private val compositeDisposable = CompositeDisposable()

    private val paginator = Paginator.Store<GalleryObject>().apply {
        render = { viewState.renderState(it) }
        sideEffects = {
            when (it) {
                is Paginator.SideEffect.LoadPage -> loadNewPage(it.page)
                is Paginator.SideEffect.ErrorEvent -> viewState.showErrorMessage(it.error.toString())   // TODO: 20.09.2020 Сделать нормальную обработку
            }
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshGallery()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.dispose()
    }

    fun refreshGallery() = paginator.proceed(Paginator.Action.Refresh)

    fun loadNextGalleryPage() = paginator.proceed(Paginator.Action.LoadMore)

    fun onFilterChanged(filter: Filter) {
        if (currentFilter != filter) {
            currentFilter = filter
            paginator.proceed(Paginator.Action.Restart)
        }
    }

    private fun loadNewPage(page: Int) {
        pageDisposable?.dispose()
        pageDisposable = galleryInteractor.getGallery(currentFilter, page)
            .observeOn(schedulers.ui())
            .subscribe(
                {
                    if (it.isNotEmpty()) paginator.proceed(Paginator.Action.NewPage(page, it))
                    else paginator.proceed(Paginator.Action.NewEmptyPage(page, it))
                },
                { paginator.proceed(Paginator.Action.PageError(it)) }
            )
        pageDisposable?.let { compositeDisposable.add(it) }
    }

    fun onBackPressed() {
        flowRouter.exit()
    }
}