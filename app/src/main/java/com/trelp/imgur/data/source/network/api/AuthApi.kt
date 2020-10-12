package com.trelp.imgur.data.source.network.api

import com.google.gson.Gson
import com.trelp.imgur.data.source.network.AccountBaseResponse
import com.trelp.imgur.data.source.network.exception.ServerError
import com.trelp.imgur.domain.session.TokenData
import com.trelp.imgur.domain.session.UserAccount
import io.reactivex.Single
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthApi @Inject constructor(private val gson: Gson) {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().build()

    fun getUserAccount(
        serverPath: String,
        clientId: String,
        tokenData: TokenData
    ): Single<UserAccount> =
        Single
            .defer {
                val request = requestAccountBase(clientId, serverPath, tokenData)
                try {
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        val account = response.fromCharStream<AccountBaseResponse>().data

                        Timber.d("$account")

                        return@defer Single.just(
                            UserAccount(
                                account.id,
                                account.url,
                                account.avatar,
                                account.cover,
                                tokenData.accessToken
                            )
                        )
                    } else {
                        checkHttpStatusCodeAndThrow(response.code)
                    }
                } catch (e: Exception) {
                    return@defer Single.error(e)
                }
            }


    fun refreshToken(
        serverPath: String,
        refreshToken: String,
        clientId: String,
        clientSecret: String
    ): Single<TokenData> =
        Single
            .defer {
                val request = requestTokenData(serverPath, refreshToken, clientId, clientSecret)
                try {
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) return@defer Single.just(response.fromCharStream<TokenData>())
                    else checkHttpStatusCodeAndThrow(response.code)
                } catch (e: Exception) {
                    return@defer Single.error(e)
                }
            }

    private fun requestAccountBase(
        clientId: String,
        serverPath: String,
        tokenData: TokenData
    ) = Request.Builder()
        .addHeader(AUTHORIZATION, "$CLIENT_ID $clientId")
        .url("${serverPath}3/account/${tokenData.username}")
        .build()


    private fun requestTokenData(
        serverPath: String,
        refreshToken: String,
        clientId: String,
        clientSecret: String
    ): Request {
        val body = FormBody.Builder()
            .add(REFRESH_TOKEN, refreshToken)
            .add(CLIENT_ID, clientId)
            .add(CLIENT_SECRET, clientSecret)
            .add(GRANT_TYPE_NAME, GRANT_TYPE_VALUE)
            .build()
        return Request.Builder()
            .url("${serverPath}oauth2/token")
            .post(body)
            .build()
    }

    private inline fun <reified T> Response.fromCharStream(): T =
        gson.fromJson(this.body?.charStream(), T::class.java)

    private fun checkHttpStatusCodeAndThrow(resultCode: Int): Nothing {
        Timber.d("$resultCode")

        if (resultCode in 400..500) {
            throw ServerError(resultCode)
        } else {
            throw RuntimeException("Get token data error: $resultCode")
        }
    }

    companion object {
        private const val REFRESH_TOKEN = "refresh_token"
        private const val CLIENT_ID = "Client-ID"
        private const val CLIENT_SECRET = "client_secret"
        private const val GRANT_TYPE_NAME = "grant_type"
        private const val GRANT_TYPE_VALUE = "refresh_token"
        private const val AUTHORIZATION = "Authorization"
    }
}