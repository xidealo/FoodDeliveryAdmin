package com.bunbeauty.data.storage

data class YandexStorageConfig(
    val accessKey: String,
    val secretKey: String,
    val bucket: String,
    val endpoint: String = "https://storage.yandexcloud.net",
    val region: String = "ru-central1",
)
