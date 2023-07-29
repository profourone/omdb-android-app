package com.profourone.omdb.data.remote.interceptor

import com.profourone.omdb.data.remote.extension.withApiKeyAndMediaType
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().withApiKeyAndMediaType())
    }
}
