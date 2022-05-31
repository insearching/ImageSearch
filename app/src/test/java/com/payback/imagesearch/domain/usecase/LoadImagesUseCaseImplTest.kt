package com.payback.imagesearch.domain.usecase

import com.google.gson.Gson
import com.payback.imagesearch.data.repository.ImageRepository
import com.payback.imagesearch.domain.entity.Hit
import com.payback.imagesearch.ui.imageDetails.ImageDetailsItem
import com.payback.imagesearch.util.network.Resource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class LoadImagesUseCaseImplTest {

    @Test
    fun testLoadImagesSuccess() = runTest {
        val query = "query"

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
        val expected = Resource.success(listOf(hit))

        val imageRepository: ImageRepository = mock {
            onBlocking { loadImages(query) }.doAnswer { expected }
        }
        val loadImageUseCase = LoadImagesUseCaseImpl(imageRepository)
        assertEquals(expected, loadImageUseCase.loadImages(query))
    }

    @Test
    fun testLoadImagesError() = runTest {
        val query = "query"
        val expected = Resource.error<List<Hit>>("error message")
        val imageRepository: ImageRepository = mock {
            onBlocking { loadImages(query) }.doAnswer { expected }
        }
        val loadImageUseCase = LoadImagesUseCaseImpl(imageRepository)
        assertEquals(expected, loadImageUseCase.loadImages(query))
    }

    @Test
    fun testLoadImageDetailsSuccess() = runTest {
        val id = 3792914L

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
        val imageDetailsItem = ImageDetailsItem(
            "https://pixabay.com/get/g59d891c1f2279c9c31ce91b595abf4937ae4e6ea834c277709e95eb3a34e55854bfafe215bfc06c67018a9e68c4cd59969a3e1957ac7ec09398c72bdf2db1315_1280.jpg",
            "suju-foto",
            "hd wallpaper, nature wallpaper, sunflowers",
            609,
            148840,
            134
        )
        val imageRepository: ImageRepository = mock {
            onBlocking { loadImageDetails(id) }.doAnswer { Resource.success(hit) }
        }
        val loadImageUseCase = LoadImagesUseCaseImpl(imageRepository)
        assertEquals(Resource.success(imageDetailsItem), loadImageUseCase.loadImageDetails(id))
    }

    @Test
    fun testLoadImageDetailsError() = runTest {
        val id = 3792914L
        val expected = Resource.error<Hit>("no image found")
        val imageRepository: ImageRepository = mock {
            onBlocking { loadImageDetails(id) }.doAnswer { expected }
        }
        val loadImageUseCase = LoadImagesUseCaseImpl(imageRepository)
        assertEquals(expected, loadImageUseCase.loadImageDetails(id))
    }
}