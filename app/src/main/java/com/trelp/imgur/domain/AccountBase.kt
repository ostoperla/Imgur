package com.trelp.imgur.domain


import com.google.gson.annotations.SerializedName

data class AccountBase(
    @SerializedName("avatar") val avatar: String,
    @SerializedName("avatar_name") val avatarName: String,
    @SerializedName("bio") val bio: String?,
    @SerializedName("cover") val cover: String,
    @SerializedName("cover_name") val coverName: String,
    @SerializedName("created") val created: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("is_blocked") val isBlocked: Boolean,
    @SerializedName("pro_expiration") val proExpiration: Boolean,
    @SerializedName("reputation") val reputation: Float,
    @SerializedName("reputation_name") val reputationName: String,
    @SerializedName("url") val url: String,
    @SerializedName("user_follow") val userFollow: UserFollow
)