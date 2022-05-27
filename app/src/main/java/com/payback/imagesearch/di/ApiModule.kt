package com.payback.imagesearch.di

import androidx.viewbinding.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.payback.imagesearch.data.api.digitalcontract.DigitalApiPixabay
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.Interceptor.Companion.invoke
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val apiKey = "27643483-0ea724ef8a6119223cf6a95ee"

    @Provides
    fun providesBaseUrl() = "https://pixabay.com/api/"

    @Provides
    @Singleton
    @Named("HttpLoggingInterceptor")
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    @Provides
    @Singleton
    @Named("BaseInterceptor")
    fun provideBaseInterceptor(): Interceptor = invoke { chain ->
        val newUrl = chain
            .request()
            .url
            .newBuilder()
            .addQueryParameter("key", apiKey)
            .build()

        val request = chain
            .request()
            .newBuilder()
            .url(newUrl)
            .build()

        return@invoke chain.proceed(request)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named("HttpLoggingInterceptor") loggingInterceptor: HttpLoggingInterceptor,
        @Named("BaseInterceptor") baseInterceptor: Interceptor
    ): OkHttpClient = if (BuildConfig.DEBUG) {
        OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(baseInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .addInterceptor(baseInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .setLenient()
            .create()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String, gson: Gson): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideDigitalApiPixabay(retrofit: Retrofit): DigitalApiPixabay =
        retrofit.create(DigitalApiPixabay::class.java)

}