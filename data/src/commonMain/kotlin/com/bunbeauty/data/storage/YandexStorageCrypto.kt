package com.bunbeauty.data.storage

internal expect fun sha256(bytes: ByteArray): ByteArray

internal expect fun hmacSha256(
    key: ByteArray,
    data: ByteArray,
): ByteArray

internal expect fun currentAwsDate(): AwsDate

internal data class AwsDate(
    val date: String,
    val dateTime: String,
)
