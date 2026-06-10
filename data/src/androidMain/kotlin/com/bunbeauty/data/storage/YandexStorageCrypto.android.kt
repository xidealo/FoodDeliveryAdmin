package com.bunbeauty.data.storage

import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

internal actual fun sha256(bytes: ByteArray): ByteArray = MessageDigest.getInstance("SHA-256").digest(bytes)

internal actual fun hmacSha256(
    key: ByteArray,
    data: ByteArray,
): ByteArray {
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(SecretKeySpec(key, "HmacSHA256"))
    return mac.doFinal(data)
}

internal actual fun currentAwsDate(): AwsDate {
    val date = Date()
    val timeZone = TimeZone.getTimeZone("UTC")
    val shortDateFormat =
        SimpleDateFormat("yyyyMMdd", Locale.US).apply {
            this.timeZone = timeZone
        }
    val dateTimeFormat =
        SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US).apply {
            this.timeZone = timeZone
        }

    return AwsDate(
        date = shortDateFormat.format(date),
        dateTime = dateTimeFormat.format(date),
    )
}
