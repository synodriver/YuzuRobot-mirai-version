package cn.yumetsuki.util

import com.madgag.gif.fmsware.AnimatedGifEncoder
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.imageio.ImageIO

private val mutex = Mutex()

@Suppress("BlockingMethodInNonBlockingContext", "EXPERIMENTAL_API_USAGE")
suspend fun getNetImageStreamFromUrl(url: String, cache: Boolean = true, cachePath: String = "static") : InputStream {
    return if (cache) {
        mutex.withLock {
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
        }
    } else {
        HttpClient(CIO).get<HttpResponse>(url).content.toInputStream()
    }
}

fun List<BufferedImage>.toGif(filePath: String, scaled: Double = 0.3, delay: Int = 150): InputStream = imagesToGif(this, filePath, scaled, delay)

fun imagesToGif(images: List<BufferedImage>, filePath: String, scaled: Double, delay: Int): InputStream {
    val checkFile = File(filePath)
    if (!checkFile.exists()) checkFile.createNewFile()
    val encoder = AnimatedGifEncoder()
    encoder.start(filePath)
    encoder.setRepeat(0)
    images.forEach {
        encoder.setDelay(delay)
        val width = (it.width * scaled).toInt()
        val height = (it.height * scaled).toInt()
        val newImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE)
        val image = it.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        val graphics = newImage.graphics
        graphics.color = Color.WHITE
        graphics.drawImage(image, 0, 0, null)
        encoder.addFrame(newImage)
    }
    encoder.finish()
    val outFile = File(filePath)
    val image = ImageIO.read(outFile)
    ImageIO.write(image, outFile.name, outFile)
    return outFile.inputStream()
}