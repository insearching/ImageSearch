package com.payback.imagesearch.util.network

import com.insearching.revolutrate.utils.network.Status

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T? = null): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }
        
        fun <T> error(message: String, data: T? = null) = Resource(Status.ERROR, data, message)
        
        val LOADING = Resource(Status.LOADING, null, null)
    }
    
    val isLoading = status == Status.LOADING
}
