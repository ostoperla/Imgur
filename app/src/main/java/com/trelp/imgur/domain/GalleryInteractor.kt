package com.trelp.imgur.domain

import com.trelp.imgur.data.GalleryRepository
import com.trelp.imgur.di.FragmentScope
import com.trelp.imgur.domain.Filter.*
import com.trelp.imgur.domain.Sort.*
import com.trelp.imgur.domain.Window.*
import javax.inject.Inject

@FragmentScope
class GalleryInteractor @Inject constructor(
    private val galleryRepository: GalleryRepository
) {

    fun getGallery(filter: Filter, page: Int) = when (filter) {
        MV_POPULAR -> galleryRepository.getMostViralGallery(VIRAL, page)
        MV_NEWEST -> galleryRepository.getMostViralGallery(TIME, page)
        MV_BEST -> galleryRepository.getMostViralGallery(TOP, page)
        US_POPULAR -> galleryRepository.getUserSubmittedGallery(VIRAL, page)
        US_NEWEST -> galleryRepository.getUserSubmittedGallery(TIME, page, showViral = false)
        US_RISING -> galleryRepository.getUserSubmittedGallery(RISING, page)
        HS_DAY -> galleryRepository.getHighestScoringGallery(DAY, page)
        HS_WEEK -> galleryRepository.getHighestScoringGallery(WEEK, page)
        HS_MONTH -> galleryRepository.getHighestScoringGallery(MONTH, page)
        HS_YEAR -> galleryRepository.getHighestScoringGallery(YEAR, page)
        HS_ALL_TIME -> galleryRepository.getHighestScoringGallery(ALL, page)
    }
}