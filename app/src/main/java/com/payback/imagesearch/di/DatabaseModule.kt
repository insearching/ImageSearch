package com.payback.imagesearch.di

import android.content.Context
import androidx.room.Room
import androidx.viewbinding.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.payback.imagesearch.data.api.digitalcontract.DigitalApiPixabay
import com.payback.imagesearch.data.local.AppDatabase
import com.payback.imagesearch.data.local.ImageRecordDao
import com.payback.imagesearch.data.repository.ImageRepository
import com.payback.imagesearch.data.repository.ImageRepositoryImpl
import com.payback.imagesearch.domain.usecase.LoadImagesUseCase
import com.payback.imagesearch.domain.usecase.LoadImagesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "photo_db"
        ).build()
    }

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): ImageRecordDao {
        return appDatabase.imageRecordDao()
    }
}