package com.bunbeauty.data.storage

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess

private const val AWS_ALGORITHM = "AWS4-HMAC-SHA256"
private const val AWS_SERVICE = "s3"
private const val AWS_REQUEST = "aws4_request"
private const val HEADER_AMZ_CONTENT_SHA256 = "x-amz-content-sha256"
private const val HEADER_AMZ_DATE = "x-amz-date"

class YandexS3KtorClient(
    private val httpClient: HttpClient,
    private val config: YandexStorageConfig,
) {
    suspend fun listObjectKeys(prefix: String): List<String> {
        val query = "list-type=2&prefix=${prefix.awsEncode(encodeSlash = true)}"
        val response =
            httpClient.get("${config.endpoint}/${config.bucket}/?$query") {
                signedHeaders(
                    method = "GET",
                    canonicalUri = "/${config.bucket}/",
                    canonicalQuery = query,
                    payload = ByteArray(0),
                    contentType = null,
                )
            }

        if (!response.status.isSuccess()) return emptyList()

        return response
            .bodyAsText()
            .substringBetweenTags("Key")
            .map { it.xmlUnescape() }
    }

    suspend fun putObject(
        key: String,
        data: ByteArray,
        contentType: String,
    ): Boolean {
        val encodedKey = key.toStoragePath()
        val response =
            httpClient.put("${config.endpoint}/${config.bucket}/$encodedKey") {
                contentType(ContentType.parse(contentType))
                signedHeaders(
                    method = "PUT",
                    canonicalUri = "/${config.bucket}/$encodedKey",
                    canonicalQuery = "",
                    payload = data,
                    contentType = contentType,
                )
                setBody(data)
            }

        return response.status.isSuccess()
    }

    suspend fun deleteObject(key: String): Boolean {
        val encodedKey = key.toStoragePath()
        val response =
            httpClient.delete("${config.endpoint}/${config.bucket}/$encodedKey") {
                signedHeaders(
                    method = "DELETE",
                    canonicalUri = "/${config.bucket}/$encodedKey",
                    canonicalQuery = "",
                    payload = ByteArray(0),
                    contentType = null,
                )
            }

        return response.status == HttpStatusCode.NoContent || response.status.isSuccess()
    }

    fun publicUrl(key: String): String =
        "${config.endpoint.removePrefix("https://")}".let {
            "https://${config.bucket}.$it/${key.toStoragePath()}"
        }

    fun extractKey(photoLink: String): String? {
        val virtualHostPrefix = "https://${config.bucket}.${config.endpoint.removePrefix("https://")}/"
        val pathStylePrefix = "${config.endpoint}/${config.bucket}/"
        val rawKey =
            when {
                photoLink.startsWith(virtualHostPrefix) -> photoLink.removePrefix(virtualHostPrefix)
                photoLink.startsWith(pathStylePrefix) -> photoLink.removePrefix(pathStylePrefix)
                else -> null
            } ?: return null

        return rawKey.percentDecode()
    }

    private fun io.ktor.client.request.HttpRequestBuilder.signedHeaders(
        method: String,
        canonicalUri: String,
        canonicalQuery: String,
        payload: ByteArray,
        contentType: String?,
    ) {
        val awsDate = currentAwsDate()
        val host = config.endpoint.removePrefix("https://")
        val payloadHash = sha256(payload).toHex()
        val canonicalHeaders =
            buildString {
                if (contentType != null) {
                    append("${HttpHeaders.ContentType.lowercase()}:$contentType\n")
                }
                append("${HttpHeaders.Host.lowercase()}:$host\n")
                append("$HEADER_AMZ_CONTENT_SHA256:$payloadHash\n")
                append("$HEADER_AMZ_DATE:${awsDate.dateTime}\n")
            }
        val signedHeaders =
            buildList {
                if (contentType != null) add(HttpHeaders.ContentType.lowercase())
                add(HttpHeaders.Host.lowercase())
                add(HEADER_AMZ_CONTENT_SHA256)
                add(HEADER_AMZ_DATE)
            }.joinToString(";")
        val canonicalRequest =
            listOf(
                method,
                canonicalUri,
                canonicalQuery,
                canonicalHeaders,
                signedHeaders,
                payloadHash,
            ).joinToString("\n")

        val credentialScope = "${awsDate.date}/${config.region}/$AWS_SERVICE/$AWS_REQUEST"
        val stringToSign =
            listOf(
                AWS_ALGORITHM,
                awsDate.dateTime,
                credentialScope,
                sha256(canonicalRequest.encodeToByteArray()).toHex(),
            ).joinToString("\n")

        val signature =
            signingKey(awsDate.date).let { key ->
                hmacSha256(key = key, data = stringToSign.encodeToByteArray()).toHex()
            }

        header(HttpHeaders.Host, host)
        header(HEADER_AMZ_CONTENT_SHA256, payloadHash)
        header(HEADER_AMZ_DATE, awsDate.dateTime)
        header(
            HttpHeaders.Authorization,
            "$AWS_ALGORITHM Credential=${config.accessKey}/$credentialScope, " +
                "SignedHeaders=$signedHeaders, Signature=$signature",
        )
    }

    private fun signingKey(date: String): ByteArray {
        val dateKey = hmacSha256(("AWS4" + config.secretKey).encodeToByteArray(), date.encodeToByteArray())
        val regionKey = hmacSha256(dateKey, config.region.encodeToByteArray())
        val serviceKey = hmacSha256(regionKey, AWS_SERVICE.encodeToByteArray())
        return hmacSha256(serviceKey, AWS_REQUEST.encodeToByteArray())
    }
}

internal fun String.toStoragePath(): String =
    split('/').joinToString(separator = "/") { segment ->
        segment.awsEncode(encodeSlash = true)
    }

private fun String.awsEncode(encodeSlash: Boolean): String =
    encodeToByteArray().joinToString(separator = "") { byte ->
        val value = byte.toInt() and 0xff
        val char = value.toChar()
        when {
            char in 'A'..'Z' ||
                char in 'a'..'z' ||
                char in '0'..'9' ||
                char == '-' ||
                char == '_' ||
                char == '.' ||
                char == '~' -> char.toString()
            char == '/' && !encodeSlash -> "/"
            else -> "%${value.toString(16).uppercase().padStart(2, '0')}"
        }
    }

private fun String.percentDecode(): String {
    val bytes = mutableListOf<Byte>()
    var index = 0
    while (index < length) {
        if (this[index] == '%' && index + 2 < length) {
            bytes += substring(index + 1, index + 3).toInt(16).toByte()
            index += 3
        } else {
            bytes += this[index].code.toByte()
            index++
        }
    }
    return bytes.toByteArray().decodeToString()
}

private fun ByteArray.toHex(): String =
    joinToString(separator = "") { byte ->
        (byte.toInt() and 0xff).toString(16).padStart(2, '0')
    }

private fun String.substringBetweenTags(tag: String): List<String> {
    val regex = Regex("<$tag>(.*?)</$tag>")
    return regex.findAll(this).map { it.groupValues[1] }.toList()
}

private fun String.xmlUnescape(): String =
    replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&quot;", "\"")
        .replace("&apos;", "'")
