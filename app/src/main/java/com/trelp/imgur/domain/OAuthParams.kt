package com.trelp.imgur.domain

data class OAuthParams(
    val serverPath: String,
    val clientId: String,
    val oAuthCallback: String,
    val responseType: String
)