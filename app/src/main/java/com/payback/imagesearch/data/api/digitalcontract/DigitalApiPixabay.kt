package com.payback.imagesearch.data.api.digitalcontract

import com.payback.imagesearch.data.api.digitalmodel.DigitalImagePhotos
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DigitalApiPixabay {

    @GET(".")
    suspend fun loadImages(
        @Query("q", encoded = true) searchQuery: String,
        @Query("image_type") selectedOfferId: String = "photo",
    ): Response<DigitalImagePhotos>
}