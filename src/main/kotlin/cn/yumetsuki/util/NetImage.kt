package cn.yumetsuki.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.jvm.javaio.toInputStream
import java.io.InputStream

suspend fun getNetImageStreamFromUrl(url: String) : InputStream {
    @Suppress("EXPERIMENTAL_API_USAGE")
    return HttpClient(CIO).get<HttpResponse>(url).content.toInputStream()
}