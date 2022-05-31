package com.payback.imagesearch.viewmodel

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.payback.imagesearch.domain.entity.Hit
import com.payback.imagesearch.domain.usecase.LoadImagesUseCase
import com.payback.imagesearch.util.network.Resource
import com.payback.imagesearch.utils.testObserver
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class ImageListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun testSearchSuccess(): Unit = runBlocking {
        val query = "tree"
        val jsonHit = """{
                "id": 3792914,
                "pageURL": "https://pixabay.com/photos/hd-wallpaper-nature-wallpaper-3792914/",
                "type": "photo",
                "tags": "hd wallpaper, nature wallpaper, sunflowers",
                "previewURL": "https://cdn.pixabay.com/photo/2018/11/03/21/42/sunflowers-3792914_150.jpg",
                "previewWidth": 150,
                "previewHeight": 69,
                "webformatURL": "https://pixabay.com/get/gf4966ecb630e1a140f0b2667dac49f205a0a61c0385c7ff56a5dd30f98bed83d3b4d90b8d8c8457dc63db9c26da89dc8586548a13173792f4582bb3fc431b1de_640.jpg",
                "webformatWidth": 640,
                "webformatHeight": 296,
                "largeImageURL": "https://pixabay.com/get/g59d891c1f2279c9c31ce91b595abf4937ae4e6ea834c277709e95eb3a34e55854bfafe215bfc06c67018a9e68c4cd59969a3e1957ac7ec09398c72bdf2db1315_1280.jpg",
                "imageWidth": 6000,
                "imageHeight": 2780,
                "imageSize": 3835319,
                "views": 230132,
                "downloads": 148840,
                "collections": 6683,
                "likes": 609,
                "comments": 134,
                "user_id": 165106,
                "user": "suju-foto",
                "userImageURL": "https://cdn.pixabay.com/user/2022/05/25/18-12-48-180_250x250.jpeg"
            }"""
        val hit = Gson().fromJson(jsonHit, Hit::class.java)
        val useCase: LoadImagesUseCase = mock {
            onBlocking { loadImages(query) }.doAnswer { Resource.success(listOf(hit)) }
        }
        val viewModel = ImageListViewModel(useCase)
        val viewObserver = viewModel.viewState.testObserver()

        viewModel.initModel()
        viewModel.loadImages(query)
        viewObserver
            .assertValue { it.progressVisible == View.GONE && it.dataVisible == View.GONE }
            .assertValue { it.progressVisible == View.VISIBLE }
            .assertValue { it.dataVisible == View.VISIBLE && it.errorVisible == View.GONE }
    }

    @Test
    fun testSearchError(): Unit = runBlocking {
        val query = "tree"
        val useCase: LoadImagesUseCase = mock {
            onBlocking { loadImages(query) }.doAnswer { Resource.success(emptyList()) }
        }
        val viewModel = ImageListViewModel(useCase)
        val viewObserver = viewModel.viewState.testObserver()

        viewModel.initModel()
        viewModel.loadImages(query)
        viewObserver
            .assertValue { it.progressVisible == View.GONE && it.dataVisible == View.GONE }
            .assertValue { it.progressVisible == View.VISIBLE }
            .assertValue { it.errorVisible == View.VISIBLE }
    }

    @Test
    fun testEmptyQuery(): Unit = runBlocking {
        val query = ""
        val useCase: LoadImagesUseCase = mock()
        val viewModel = ImageListViewModel(useCase)
        val viewObserver = viewModel.viewState.testObserver()

        viewModel.initModel()
        viewModel.loadImages(query)
        viewObserver
            .assertValue { it.progressVisible == View.GONE && it.dataVisible == View.GONE }
    }
}