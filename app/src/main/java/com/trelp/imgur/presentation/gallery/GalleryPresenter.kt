package com.trelp.imgur.presentation.gallery

import com.trelp.imgur.data.SchedulersProvider
import com.trelp.imgur.di.FlowNav
import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.domain.Filter
import com.trelp.imgur.domain.GalleryInteractor
import com.trelp.imgur.domain.GalleryObject
import com.trelp.imgur.presentation.ErrorHandler
import com.trelp.imgur.presentation.FlowRouter
import com.trelp.imgur.presentation.Paginator
import com.trelp.imgur.presentation.global.BasePresenter
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@FragmentScope
class GalleryPresenter @Inject constructor(
    @FlowNav private val flowRouter: FlowRouter,
    private val galleryInteractor: GalleryInteractor,
    private val paginator: Paginator.Store<GalleryObject>,
    private val errorHandler: ErrorHandler,
    private val schedulers: SchedulersProvider
) : BasePresenter<GalleryView>() {

    private var currentFilter: Filter = Filter.MV_POPULAR
    private var pageDisposable: Disposable? = null

    init {
        initPaginator()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshGallery()
    }

    private fun initPaginator() {
        paginator.render = viewState::renderState
        paginator.sideEffects.subscribe {
            when (it) {
                is Paginator.SideEffect.LoadPage -> loadNewPage(it.page)
                is Paginator.SideEffect.ErrorEvent ->
                    errorHandler.proceed(it.error) { msg -> viewState.showErrorMessage(msg) }
            }
        }.connect()
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
        pageDisposable?.connect()
    }

    fun onBackPressed() {
        flowRouter.exit()
    }
}