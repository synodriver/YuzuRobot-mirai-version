package cn.yumetsuki.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.jvm.javaio.toInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream

@Suppress("BlockingMethodInNonBlockingContext", "EXPERIMENTAL_API_USAGE")
suspend fun getNetImageStreamFromUrl(url: String, cache: Boolean = true, cachePath: String = "static") : InputStream {
    return if (cache) {
        val dic = File(cachePath)
        if (!dic.exists()) dic.mkdir()
        val filePath = "$cachePath/${url.substring(url.lastIndexOf("/") + 1)}"
        var file = File(filePath)
        if (!file.exists()) {
            file.createNewFile()
            file = File(filePath)
            file.writeBytes(HttpClient(CIO).get<HttpResponse>(url).content.toInputStream().readAllBytes())
        }
        file.inputStream()
    } else {
        HttpClient(CIO).get<HttpResponse>(url).content.toInputStream()
    }
}