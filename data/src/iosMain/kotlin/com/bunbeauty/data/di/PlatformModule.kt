package com.bunbeauty.data.di

import com.bunbeauty.data.YandexStorageBuildConfig
import com.bunbeauty.data.repository.IosPhotoRepository
import com.bunbeauty.data.storage.YandexS3KtorClient
import com.bunbeauty.data.storage.YandexStorageConfig
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.PhotoRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module
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
                accessKey = YandexStorageBuildConfig.YC_ACCESS_KEY,
                secretKey = YandexStorageBuildConfig.YC_SECRET_KEY,
                bucket = YandexStorageBuildConfig.YC_BUCKET,
            )
        }
        single {
            YandexS3KtorClient(
                httpClient = HttpClient(Darwin.create()),
                config = get(),
            )
        }
        single<PhotoRepo> {
            IosPhotoRepository(
                yandexS3Client = get(),
            )
        }
    }
