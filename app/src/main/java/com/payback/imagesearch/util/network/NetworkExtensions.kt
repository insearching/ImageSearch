package com.payback.imagesearch.util.network

import retrofit2.Response

fun <T> Response<T>.toResource(): Resource<T> {
    return if (isSuccessful) {
        Resource.success(body())
    } else {
        val message = errorBody()?.string()
        val errorMsg = if (message.isNullOrEmpty()) {
            message()
        } else {
            message
        }
        Resource.error(errorMsg, null)
    }
}