import cn.yumetsuki.util.ResponseImageBuilder
import org.junit.Test
import java.awt.Font
import java.awt.RenderingHints
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

class ImageTest {

    @Test
    fun test() {
        val a = ResponseImageBuilder(
                "static/甜心屋.png",
                "static/丛雨.png",
                "static/window.png",
                "【丛雨】"
        ).apply {
            add("唔....这段时间的话...现在芦花姐的田心屋没在营业了呢....")
            add("正常营业时间: 8:00 ~ 21:00")
        }.build()
        File("test.png").writeBytes(a.toByteArray())
    }
}