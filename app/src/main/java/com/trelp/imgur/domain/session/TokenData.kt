package com.trelp.imgur.domain.session

import com.google.gson.annotations.SerializedName

data class TokenData(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("account_id") val id: Int,
    @SerializedName("account_username") val username: String,
    @SerializedName("expires_in") val expiresIn: Long,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("scope") val scope: String,
    @SerializedName("token_type") val tokenType: String
)