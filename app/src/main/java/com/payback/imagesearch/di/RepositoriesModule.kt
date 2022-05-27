package com.payback.imagesearch.di

import com.payback.imagesearch.data.api.digitalcontract.DigitalApiPixabay
import com.payback.imagesearch.data.local.ImageRecordDao
import com.payback.imagesearch.data.repository.ImageRepository
import com.payback.imagesearch.data.repository.ImageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Provides
    @Singleton
    fun provideImageRepository(api: DigitalApiPixabay, dao: ImageRecordDao): ImageRepository =
        ImageRepositoryImpl(api, dao)

}