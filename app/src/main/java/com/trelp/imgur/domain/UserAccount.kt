package com.trelp.imgur.domain

data class UserAccount(
    val username: String,
    val id: Int,
    val accessToken: String,
    val expiresIn: Long,
    val tokenType: String,
    val refreshToken: String,
    val isAuth: Boolean
)
