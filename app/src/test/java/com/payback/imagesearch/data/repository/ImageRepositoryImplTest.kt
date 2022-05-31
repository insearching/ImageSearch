package com.payback.imagesearch.data.repository

import com.google.gson.Gson
import com.payback.imagesearch.data.api.digitalcontract.DigitalApiPixabay
import com.payback.imagesearch.data.api.digitalmodel.DigitalImagePhotos
import com.payback.imagesearch.data.local.ImageRecordDao
import com.payback.imagesearch.data.mapper.ImagePhotoMapper.toDaoModel
import com.payback.imagesearch.data.mapper.ImagePhotoMapper.toDomainModel
import com.payback.imagesearch.domain.entity.Hit
import com.payback.imagesearch.util.network.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

@ExperimentalCoroutinesApi
class ImageRepositoryImplTest {

    private val responseJson = """{
            "total": 32248,
            "totalHits": 500,
            "hits": [
            {
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
            },
            {
              "id": 887443,
              "pageURL": "https://pixabay.com/photos/flower-life-yellow-flower-crack-887443/",
              "type": "photo",
              "tags": "flower, life, yellow flower",
              "previewURL": "https://cdn.pixabay.com/photo/2015/08/13/20/06/flower-887443_150.jpg",
              "previewWidth": 150,
              "previewHeight": 116,
              "webformatURL": "https://pixabay.com/get/gbaeed2501765ca8c9c3e5705afaacdd2bcbbde36a51026f3ad510519c637c86b2507cec76f87fe40f32e40cb58d83064_640.jpg",
              "webformatWidth": 640,
              "webformatHeight": 497,
              "largeImageURL": "https://pixabay.com/get/gf0ab912bf91b919de67afa8991b21985f8ceea6d08dc61cb5ed566b3b794b6faa3b2e14fd68b29da5ae98857011640cefdb5720d6df3ac7291c141195d22267b_1280.jpg",
              "imageWidth": 3867,
              "imageHeight": 3005,
              "imageSize": 2611531,
              "views": 294811,
              "downloads": 176725,
              "collections": 5389,
              "likes": 1054,
              "comments": 177,
              "user_id": 1298145,
              "user": "klimkin",
              "userImageURL": "https://cdn.pixabay.com/user/2017/07/18/13-46-18-393_250x250.jpg"
            }
            ]
            } """

    @Test
    fun loadImages_withInternetSuccess() = runTest {
        val query = "tree"

        val digitalImagePhotos = Gson().fromJson(responseJson, DigitalImagePhotos::class.java)
        val apiAccessor: DigitalApiPixabay = mock {
            onBlocking { loadImages(query) }.doAnswer { Response.success(digitalImagePhotos) }
        }
        val imageRecordsDao: ImageRecordDao = mock()
        val imageRepository = ImageRepositoryImpl(apiAccessor, imageRecordsDao)
        assertEquals(
            Resource.success(digitalImagePhotos.toDomainModel.hits),
            imageRepository.loadImages(query)
        )
    }

    @Test
    fun loadImages_withNoInternetSuccess() = runTest {
        val query = "tree"
        val digitalImagePhotos = Gson().fromJson(responseJson, DigitalImagePhotos::class.java)
        val apiAccessor: DigitalApiPixabay = mock {
            onBlocking { loadImages(query) }.doAnswer { Response.success(digitalImagePhotos) }
        }
        val imageRecordsDao: ImageRecordDao = mock {
            onBlocking { findRecordsByTag("%$query%") } doAnswer {
                digitalImagePhotos.toDomainModel.hits.asSequence().map { it.toDaoModel }.toList()
            }
        }

        val imageRepository = ImageRepositoryImpl(apiAccessor, imageRecordsDao)
        assertEquals(
            Resource.success(digitalImagePhotos.toDomainModel.hits),
            imageRepository.loadImages(query)
        )

        whenever(apiAccessor.loadImages(query)).then {
            Resource.error<DigitalImagePhotos>("error")
        }
        assertEquals(
            Resource.success(digitalImagePhotos.toDomainModel.hits),
            imageRepository.loadImages(query)
        )
    }

    @Test
    fun loadImages_withNoInternetNoImagesFound() = runTest {
        val query = "tree"
        val digitalImagePhotos = Gson().fromJson(responseJson, DigitalImagePhotos::class.java)
        val apiAccessor: DigitalApiPixabay = mock {
            onBlocking { loadImages(query) }.doAnswer { Response.success(digitalImagePhotos) }
        }
        val imageRecordsDao: ImageRecordDao = mock {
            onBlocking { findRecordsByTag("%sun%") } doAnswer {
                digitalImagePhotos.toDomainModel.hits.asSequence().map { it.toDaoModel }.toList()
            }
        }

        val imageRepository = ImageRepositoryImpl(apiAccessor, imageRecordsDao)
        assertEquals(
            Resource.success(digitalImagePhotos.toDomainModel.hits),
            imageRepository.loadImages(query)
        )

        whenever(apiAccessor.loadImages(query)).then {
            Resource.error<DigitalImagePhotos>("error")
        }
        assertEquals(
            Resource.success(emptyList<Hit>()),
            imageRepository.loadImages(query)
        )
    }

    @Test
    fun loadImageDetailsSuccess() = runTest {
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
        val apiAccessor: DigitalApiPixabay = mock()
        val imageRecordsDao: ImageRecordDao = mock {
            onBlocking { findById(id) } doAnswer { hit.toDaoModel }
        }
        val imageRepository = ImageRepositoryImpl(apiAccessor, imageRecordsDao)
        assertEquals(
            Resource.success(hit),
            imageRepository.loadImageDetails(id)
        )
    }

    @Test
    fun loadImageDetailsError() = runTest {
        val id = 3792914L

        val apiAccessor: DigitalApiPixabay = mock()
        val imageRecordsDao: ImageRecordDao = mock()
        val imageRepository = ImageRepositoryImpl(apiAccessor, imageRecordsDao)
        assertEquals(
            Resource.error<Hit>("No image found"),
            imageRepository.loadImageDetails(id)
        )
    }
}