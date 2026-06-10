package com.bunbeauty.data.storage

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CCHmac
import platform.CoreCrypto.CC_SHA256
import platform.CoreCrypto.CC_SHA256_DIGEST_LENGTH
import platform.CoreCrypto.kCCHmacAlgSHA256
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.localeWithLocaleIdentifier
import platform.Foundation.timeZoneForSecondsFromGMT

@OptIn(ExperimentalForeignApi::class)
internal actual fun sha256(bytes: ByteArray): ByteArray {
    val result = ByteArray(CC_SHA256_DIGEST_LENGTH)
    val input = if (bytes.isEmpty()) byteArrayOf(0) else bytes
    input.usePinned { pinnedBytes ->
        result.usePinned { pinnedResult ->
            CC_SHA256(
                pinnedBytes.addressOf(0),
                bytes.size.convert(),
                pinnedResult.addressOf(0).reinterpret(),
            )
        }
    }
    return result
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun hmacSha256(
    key: ByteArray,
    data: ByteArray,
): ByteArray {
    val result = ByteArray(CC_SHA256_DIGEST_LENGTH)
    val input = if (data.isEmpty()) byteArrayOf(0) else data
    key.usePinned { pinnedKey ->
        input.usePinned { pinnedData ->
            result.usePinned { pinnedResult ->
                CCHmac(
                    kCCHmacAlgSHA256,
                    pinnedKey.addressOf(0),
                    key.size.convert(),
                    pinnedData.addressOf(0),
                    data.size.convert(),
                    pinnedResult.addressOf(0),
                )
            }
        }
    }
    return result
}

internal actual fun currentAwsDate(): AwsDate {
    val date = NSDate()
    val shortDateFormat =
        NSDateFormatter().apply {
            locale = NSLocale.localeWithLocaleIdentifier("en_US_POSIX")
            timeZone = NSTimeZone.timeZoneForSecondsFromGMT(0)
            dateFormat = "yyyyMMdd"
        }
    val dateTimeFormat =
        NSDateFormatter().apply {
            locale = NSLocale.localeWithLocaleIdentifier("en_US_POSIX")
            timeZone = NSTimeZone.timeZoneForSecondsFromGMT(0)
            dateFormat = "yyyyMMdd'T'HHmmss'Z'"
        }

    return AwsDate(
        date = shortDateFormat.stringFromDate(date),
        dateTime = dateTimeFormat.stringFromDate(date),
    )
}
