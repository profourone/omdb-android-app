package com.profourone.omdb.data.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.profourone.omdb.data.BuildConfig
import com.profourone.omdb.data.R
import com.profourone.omdb.data.remote.OMDbApi
import com.profourone.omdb.data.remote.interceptor.AuthorizationInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    private val JSON_MEDIA_TYPE = "application/json".toMediaType()

    @Provides
    @Singleton
    fun providesAuthorizationInterceptor() = AuthorizationInterceptor()

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        @ApplicationContext context: Context,
        authorizationInterceptor: AuthorizationInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(
                context.resources.getInteger(R.integer.api_connect_timeout_seconds).toLong(),
                TimeUnit.SECONDS
            )
            .readTimeout(
                context.resources.getInteger(R.integer.api_read_timeout_seconds).toLong(),
                TimeUnit.SECONDS
            )
            .writeTimeout(
                context.resources.getInteger(R.integer.api_write_timeout_seconds).toLong(),
                TimeUnit.SECONDS
            )
            .addInterceptor(authorizationInterceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun providesJson() = Json {
        prettyPrint = BuildConfig.DEBUG
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.api_url))
            .addConverterFactory(json.asConverterFactory(JSON_MEDIA_TYPE))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesJSONPlaceholderApi(retrofit: Retrofit): OMDbApi =
        retrofit.create(OMDbApi::class.java)

}
