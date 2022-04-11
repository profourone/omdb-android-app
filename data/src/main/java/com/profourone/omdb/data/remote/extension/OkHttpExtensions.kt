package com.profourone.omdb.data.remote.extension

import okhttp3.Request

const val API_KEY_PARAMETER = "apikey"
const val API_KEY = "PASTE_KEY_HERE"

const val MEDIA_TYPE_PARAMETER = "r"
const val MEDIA_TYPE = "json"

fun Request.withApiKeyAndMediaType(): Request {
    val url = url.newBuilder().apply {
        addQueryParameter(API_KEY_PARAMETER, API_KEY)
        addQueryParameter(MEDIA_TYPE_PARAMETER, MEDIA_TYPE)
    }.build()

    return newBuilder().url(url).build()
}
