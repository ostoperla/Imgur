package com.trelp.imgur.domain.session


data class UserAccount(
    val id: Int,
    val username: String,
    val avatar: String,
    val cover: String,
    val accessToken: String
)
