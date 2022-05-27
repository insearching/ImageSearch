package com.payback.imagesearch.viewmodel

import androidx.lifecycle.*
import com.insearching.revolutrate.utils.network.Status
import com.payback.imagesearch.domain.entity.ImagePhotos
import com.payback.imagesearch.domain.usecase.LoadImagesUseCase
import com.payback.imagesearch.ui.imageList.ImageListViewState
import com.payback.imagesearch.util.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageListViewModel @Inject constructor(private val loadImagesUseCase: LoadImagesUseCase) :
    ViewModel() {

    var searchText: String = ""

    private val initialState = MutableLiveData<Resource<ImagePhotos>>()

    private val searchQuery = MutableLiveData<String>()

    private var cachedData: ImagePhotos? = null

    private val data = searchQuery.switchMap {
        liveData {
            emit(Resource.LOADING)
            emit(loadImagesUseCase.loadImages(it))
        }
    }

    val viewState: LiveData<ImageListViewState> = MediatorLiveData<ImageListViewState>().apply {
        var resource: Resource<ImagePhotos> = Resource.LOADING

        fun error() =
            if (resource.status == Status.ERROR) {
                ImageListViewState.Error.BaseError(resource.message ?: "Error occurred")
            } else {
                null
            }

        fun updateState() {
            value = ImageListViewState(
                isLoading = resource.isLoading,
                error = error(),
                imagePhotos = resource.data
            )
        }

        addSource(data) {
            resource = it
            cachedData = it.data
            updateState()
        }

        addSource(initialState) {
            resource = it
            updateState()
        }
    }

    fun initModel() {
        initialState.value = Resource.success(cachedData)
    }

    fun loadImages() {
        viewModelScope.launch {
            searchQuery.value = searchText
        }
    }
}