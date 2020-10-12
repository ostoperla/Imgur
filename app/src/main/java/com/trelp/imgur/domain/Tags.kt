package com.trelp.imgur.domain

import com.google.gson.annotations.SerializedName

data class Tags(val items: List<Tag>, val next: String?)

data class Tag(
    val name: String,
    @SerializedName("display_name") val displayName: String,
    val followers: Int,
    @SerializedName("total_items") val totalItems: Int,
    val following: Boolean,
    @SerializedName("background_hash") val backgroundHash: String,
    @SerializedName("thumbnail_hash") val thumbnailHash: String?,
    @SerializedName("accent") val accent: String?,
    @SerializedName("background_is_animated") val backgroundIsAnimated: Boolean,
    @SerializedName("thumbnail_is_animated") val thumbnailIsAnimated: Boolean,
    @SerializedName("is_promoted") val isPromoted: Boolean,
    val description: String,
    @SerializedName("logo_hash") val logoHash: String?,
    @SerializedName("logo_destination_url") val logoDestinationUrl: String?,
    @SerializedName("is_whitelisted") val isWhitelisted: Boolean
) {
    override fun toString() = displayName
}