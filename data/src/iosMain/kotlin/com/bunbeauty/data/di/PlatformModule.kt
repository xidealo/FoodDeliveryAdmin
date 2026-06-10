package com.bunbeauty.data.di

import com.bunbeauty.data.repository.IosPhotoRepository
import com.bunbeauty.data.storage.YandexS3KtorClient
import com.bunbeauty.data.storage.YandexStorageConfig
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.PhotoRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import io.ktor.client.HttpClient
import org.koin.dsl.module
import platform.Foundation.NSBundle
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual fun platformDataModule() =
    module {
        single<HttpClient> { createIosHttpClient(get()) }
        single<DataStoreRepo> { IosDataStoreRepository() }
        single<UserAuthorizationRepo> {
            IosUserAuthorizationRepository(
                networkConnector = get(),
                dataStoreRepo = get(),
            )
        }
        single {
            YandexStorageConfig(
                accessKey = requireInfoPlistValue("YANDEX_STORAGE_ACCESS_KEY"),
                secretKey = requireInfoPlistValue("YANDEX_STORAGE_SECRET_KEY"),
                bucket = requireInfoPlistValue("YANDEX_STORAGE_BUCKET"),
            )
        }
        single {
            YandexS3KtorClient(
                httpClient = get(),
                config = get(),
            )
        }
        single<PhotoRepo> {
            IosPhotoRepository(
                yandexS3Client = get(),
            )
        }
    }

private fun requireInfoPlistValue(key: String): String =
    (NSBundle.mainBundle.objectForInfoDictionaryKey(key) as? String)
        ?.takeIf { value -> value.isNotBlank() && !value.startsWith("$(") }
        ?: error("Missing $key in iOS Info.plist")
