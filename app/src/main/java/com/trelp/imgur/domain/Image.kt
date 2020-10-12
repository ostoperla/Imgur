package com.trelp.imgur.domain

import com.google.gson.annotations.SerializedName

data class Image(
    val id: String,
    val title: String,
    val description: String,
    val datetime: Int,
    val type: String,
    val animated: Boolean,
    val width: Int,
    val height: Int,
    val size: Int,
    val views: Int,
    val bandwidth: Long,
    val section: String,
    val link: String,
    val favorite: Boolean,
    val nsfw: Boolean,
    val vote: String,
    @SerializedName("in_gallery") val inGallery: Boolean,
    @SerializedName("account_url") val accountUrl: String,
    @SerializedName("account_id") val accountId: Int,
    @SerializedName("is_ad") val isAd: Boolean,
    @SerializedName("in_most_viral") val inMostViral: Boolean,
    @SerializedName("has_sound") val hasSound: Boolean,
    val tags: List<Tag>,
    @SerializedName("ad_type") val adType: Int,
    @SerializedName("ad_url") val adUrl: String,
    val edited: String,
    val hls: String,
    @SerializedName("comment_count") val commentCount: Int,
    @SerializedName("favorite_count") val favoriteCount: Int,
    val ups: Int,
    val downs: Int,
    val points: Int,
    val score: Int,

//    optional
    val deletehash: String,
    val name: String,
    val gifv: String,
    val mp4: String,
    @SerializedName("mp4_size") val mp4Size: Int,
    val looping: Boolean
)