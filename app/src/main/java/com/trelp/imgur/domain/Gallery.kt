package com.trelp.imgur.domain

import com.google.gson.annotations.SerializedName

interface GalleryObject {
    val id: String
    val title: String
    val description: String
    val datetime: Long
    val views: Int

    // null if not signed in or if the user hasn't voted on it.
    val vote: String?
    val favorite: Boolean

    // Defaults to null if information is not available.
    val nsfw: Boolean?
    val ups: Int
    val downs: Int
    val points: Int
    val score: Int
    val isAlbum: Boolean
    val link: String

    // The account username or null if it's anonymous.
    val accountUrl: String?

    // The account ID of the account that uploaded it or null.
    val accountId: Int?
    val commentCount: Int

    //  val topic: String
    //  val topicId: Int
    val inMostViral: Boolean
}

data class GalleryImage(
    override val id: String,
    override val title: String,
    override val description: String,
    override val datetime: Long,
    override val views: Int,
    override val vote: String?,
    override val favorite: Boolean,
    override val nsfw: Boolean?,
    override val ups: Int,
    override val downs: Int,
    override val points: Int,
    override val score: Int,
    @SerializedName("is_album") override val isAlbum: Boolean,
    override val link: String,
    @SerializedName("account_url") override val accountUrl: String?,
    @SerializedName("account_id") override val accountId: Int?,
    @SerializedName("comment_count") override val commentCount: Int,
    //  override val topic: String,
    //  @SerializedName("topic_id") override val topicId: Int,
    @SerializedName("in_most_viral") override val inMostViral: Boolean,

    val type: String,
    val animated: Boolean,
    val width: Int,
    val height: Int,
    val size: Int,
    val bandwidth: Long,
    val section: String,
    //  optional
    val deletehash: String,
    val gifv: String,
    val mp4: String,
    @SerializedName("mp4_size") val mp4Size: Int,
    val looping: Boolean
) : GalleryObject

data class GalleryAlbum(
    override val id: String,
    override val title: String,
    override val description: String,
    override val datetime: Long,
    override val views: Int,
    override val vote: String?,
    override val favorite: Boolean,
    override val nsfw: Boolean?,
    override val ups: Int,
    override val downs: Int,
    override val points: Int,
    override val score: Int,
    @SerializedName("is_album") override val isAlbum: Boolean,
    override val link: String,
    @SerializedName("account_url") override val accountUrl: String?,
    @SerializedName("account_id") override val accountId: Int?,
    @SerializedName("comment_count") override val commentCount: Int,
    //  override val topic: String,
    //  @SerializedName("topic_id") override val topicId: Int,
    @SerializedName("in_most_viral") override val inMostViral: Boolean,

    val cover: String,
    @SerializedName("cover_width") val coverWidth: Int,
    @SerializedName("cover_height") val coverHeight: Int,
    val privacy: String,
    val layout: String,
    val images: List<Image>,
    @SerializedName("images_count") val imagesCount: Int
) : GalleryObject