package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.Photo

interface PhotoRepo {
    suspend fun getPhotoList(username: String): List<Photo>
}