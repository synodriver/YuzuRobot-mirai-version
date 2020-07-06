package cn.yumetsuki.util

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.uploadAsImage
import java.awt.Font
import java.awt.RenderingHints
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream
import javax.imageio.ImageIO

class ResponseImageBuilder(
    val backgroundPath: String,
    val characterPath: String,
    val windowPath: String,
    val title: String
): ArrayList<String>() {

    companion object {
        fun murasame(): ResponseImageBuilder {
            return ResponseImageBuilder(
                    "static/甜心屋.png",
                    "static/丛雨.png",
                    "static/window.png",
                    "【丛雨】"
            )
        }
    }

    fun build(): ByteArrayOutputStream {
        val result = ByteArrayOutputStream()
        val workBackground = ImageIO.read(File(backgroundPath))
        val murasame = ImageIO.read(File(characterPath))
        val window = ImageIO.read(File(windowPath))
        val root = workBackground.createGraphics()
        root.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB)
        root.drawImage(murasame, (workBackground.width - murasame.width) / 2, workBackground.height - murasame.height, null)
        root.drawImage(window, 0, workBackground.height - window.height, null)
        root.font = Font("微软雅黑", Font.PLAIN, 12)
        root.drawString(title, 20, workBackground.height - window.height + 20)
        forEachIndexed { index, s ->
            root.drawString(s, 30, workBackground.height - window.height + 20 * (index + 2))
        }
        ImageIO.write(workBackground, "png", result)
        return result
    }

}

suspend fun ByteArrayOutputStream.uploadImage(contact: Contact): Image {
    return ByteArrayInputStream(this.toByteArray()).uploadAsImage(contact)
}