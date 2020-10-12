package com.trelp.imgur.data.source.network

import com.trelp.imgur.domain.AccountBase
import com.trelp.imgur.domain.GalleryObject

data class AccountBaseResponse(val success: Boolean, val status: Int, val data: AccountBase)

data class GalleryResponse(val success: Boolean, val status: Int, val data: List<GalleryObject>)