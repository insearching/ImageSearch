package com.payback.imagesearch.di

import com.payback.imagesearch.data.repository.ImageRepository
import com.payback.imagesearch.domain.usecase.LoadImagesUseCase
import com.payback.imagesearch.domain.usecase.LoadImagesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsecasesModule {

    @Provides
    @Singleton
    fun provideLoadImageUseCase(repository: ImageRepository): LoadImagesUseCase =
        LoadImagesUseCaseImpl(repository)

}