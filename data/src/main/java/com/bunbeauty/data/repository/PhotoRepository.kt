package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PhotoRepository @Inject constructor() : PhotoRepo {

    private val photoListCache: List<Photo>? = null

    override suspend fun getPhotoList(username: String): List<Photo> {
        val imagesRef = FirebaseStorage.getInstance().reference.child("gustopab")

        val storageReferenceList = suspendCoroutine { continuation ->
            imagesRef.listAll()
                .addOnSuccessListener { listResult ->
                    continuation.resume(listResult.items)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
        Log.d("TAG_PhotoRepository", "start load")

        val photoList = storageReferenceList.map { storageReference ->
            coroutineScope {
                async(Dispatchers.IO) {
                    suspendCoroutine { continuation ->
                        storageReference.downloadUrl.addOnSuccessListener { photoUri ->
                            android.util.Log.d(
                                "TAG_PhotoRepository",
                                "getPhotoList: gotPhoto $photoUri"
                            )
                            continuation.resume(com.bunbeauty.domain.model.Photo(photoLink = photoUri.toString()))
                        }
                    }
                }
            }
        }
        return photoList.awaitAll()
    }
}
