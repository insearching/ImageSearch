package com.payback.imagesearch.utils

import androidx.lifecycle.LiveData

/**
 * Creates an observer for [LiveData].
 */
fun <T> LiveData<T>.testObserver() = LiveDataTestObserver<T>()
	.also { observeForever(it) }