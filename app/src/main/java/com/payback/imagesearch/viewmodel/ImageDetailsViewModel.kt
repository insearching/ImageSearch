package com.payback.imagesearch.viewmodel

import androidx.lifecycle.*
import com.insearching.revolutrate.utils.network.Status
import com.payback.imagesearch.domain.usecase.LoadImagesUseCase
import com.payback.imagesearch.ui.imageDetails.ImageDetailsItem
import com.payback.imagesearch.ui.imageDetails.ImageDetailsViewState
import com.payback.imagesearch.util.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageDetailsViewModel @Inject constructor(private val loadImagesUseCase: LoadImagesUseCase) :
    ViewModel() {

    private val imageItemData = MutableLiveData<Long>()

    private val data = imageItemData.switchMap {
        liveData {
            emit(Resource.LOADING)
            emit(loadImagesUseCase.loadImageData(it))
        }
    }

    val viewState: LiveData<ImageDetailsViewState> =
        MediatorLiveData<ImageDetailsViewState>().apply {
            var imageData: Resource<ImageDetailsItem> = Resource.LOADING

            val error =
                if (imageData.status == Status.ERROR) {
                    ImageDetailsViewState.Error.BaseError(
                        imageData.message ?: "Some error occurred"
                    )
                } else {
                    null
                }

            fun updateState() {
                val item = imageData.data

                value = ImageDetailsViewState(
                    isLoading = imageData.isLoading,
                    error = error,
                    item = item
                )
            }

            addSource(data) {
                imageData = it
                updateState()
            }
        }

    fun init(photoId: Long) {
        viewModelScope.launch {
            imageItemData.value = photoId
        }
    }
}