package com.bunbeauty.data.repository

import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PhotoRepository @Inject constructor() : PhotoRepo {

    private var photoListCache: List<Photo>? = null

    override suspend fun getPhotoList(username: String): List<Photo> = coroutineScope {
        photoListCache ?: fetchPhotoList(username = username)
    }

    override suspend fun fetchPhotoList(username: String): List<Photo> = coroutineScope {
        val imagesRef = FirebaseStorage.getInstance().reference.child(username)

        val referenceListResult = imagesRef.listAll().await()

        val referenceListResult = imagesRef.listAll().await()

        val photoList = referenceListResult.items.map { reference ->
            async {
                Photo(link = reference.downloadUrl.await().toString())
            }
        }.awaitAll()
        photoListCache = photoList
        photoList
    }
}
