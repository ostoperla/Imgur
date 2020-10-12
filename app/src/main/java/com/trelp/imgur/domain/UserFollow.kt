package com.trelp.imgur.domain


import com.google.gson.annotations.SerializedName

data class UserFollow(
    @SerializedName("status") val status: Boolean
)