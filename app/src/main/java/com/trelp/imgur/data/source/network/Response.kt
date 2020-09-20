package com.trelp.imgur.data.source.network

import com.trelp.imgur.domain.GalleryObject

data class GalleryResponse(val success: Boolean, val status: Int, val data: List<GalleryObject>)