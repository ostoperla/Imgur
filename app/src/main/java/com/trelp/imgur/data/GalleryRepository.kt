package com.trelp.imgur.data

import com.trelp.imgur.data.source.network.api.ImgurApi
import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.domain.GalleryObject
import com.trelp.imgur.domain.Sort
import com.trelp.imgur.domain.Window
import io.reactivex.Single
import javax.inject.Inject

@FragmentScope
class GalleryRepository @Inject constructor(
    private val api: ImgurApi,
    private val schedulers: SchedulersProvider
) {
    fun getMostViralGallery(sort: Sort, page: Int): Single<List<GalleryObject>> =
        api.getMostViralGallery(sort, page, showViral = true)
            .map { it.data }
            .subscribeOn(schedulers.io())

    fun getUserSubmittedGallery(
        sort: Sort,
        page: Int,
        showViral: Boolean = true
    ): Single<List<GalleryObject>> =
        api.getUserSubmittedGallery(sort, page, showViral)
            .map { it.data }
            .subscribeOn(schedulers.io())

    fun getHighestScoringGallery(window: Window, page: Int): Single<List<GalleryObject>> =
        api.getHighestScoringGallery(window, page)
            .map { it.data }
            .subscribeOn(schedulers.io())
}