package com.bunbeauty.data.repository

import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PhotoRepository @Inject constructor() : PhotoRepo {
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

        val photoList = storageReferenceList.map { storageReference ->
            suspendCoroutine { continuation ->
                storageReference.downloadUrl.addOnSuccessListener { photoUri ->
                    continuation.resume(Photo(photoLink = photoUri.toString()))
                }
            }
        }

        return photoList
    }
}