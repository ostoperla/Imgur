package com.trelp.imgur.data.source.network

import com.trelp.imgur.domain.Sort
import com.trelp.imgur.domain.Window
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImgurApi {
    //region Gallery
    @GET("gallery/hot/{sort}/{page}")
    fun getMostViralGallery(
        @Path("sort") sort: Sort,
        @Path("page") page: Int,
        @Query("showViral") showViral: Boolean
    ): Single<GalleryResponse>

    @GET("gallery/hot/{sort}/{page}")
    fun getUserSubmittedGallery(
        @Path("sort") sort: Sort,
        @Path("page") page: Int,
        @Query("showViral") showViral: Boolean
    ): Single<GalleryResponse>

    @GET("gallery/top/viral/{window}/{page}")
    fun getHighestScoringGallery(
        @Path("window") window: Window,
        @Path("page") page: Int
    ): Single<GalleryResponse>
    //endregion
}