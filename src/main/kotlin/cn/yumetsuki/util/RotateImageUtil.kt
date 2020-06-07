package cn.yumetsuki.util

import java.awt.Dimension
import java.awt.Image
import java.awt.Rectangle
import java.awt.image.BufferedImage

fun Image.rotate(angel: Double): BufferedImage {
    return RotateImageUtil.rotate(this, angel)
}

object RotateImageUtil {
    /**
     * 图像旋转
     * @param src
     * @param angel
     * @return
     */
    fun rotate(src: Image, angel: Double): BufferedImage {
        val src_width = src.getWidth(null)
        val src_height = src.getHeight(null)
        // calculate the new image size
        val rect_des = calcRotatedSize(
            Rectangle(
                Dimension(
                    src_width, src_height
                )
            ), angel
        )
        var res: BufferedImage? = null
        res = BufferedImage(
            rect_des.width, rect_des.height,
            BufferedImage.TYPE_3BYTE_BGR
        )
        val g2 = res.createGraphics()
        // transform
        g2.translate(
            (rect_des.width - src_width) / 2,
            (rect_des.height - src_height) / 2
        )
        g2.rotate(Math.toRadians(angel), src_width / 2.toDouble(), src_height / 2.toDouble())
        g2.drawImage(src, null, null)
        return res
    }

    private fun calcRotatedSize(src: Rectangle, angel: Double): Rectangle {
        // if angel is greater than 90 degree, we need to do some conversion
        var angel = angel
        if (angel >= 90) {
            if (angel / 90 % 2 == 1.0) {
                val temp = src.height
                src.height = src.width
                src.width = temp
            }
            angel = angel % 90
        }
        val r = Math.sqrt(src.height * src.height + src.width * src.width.toDouble()) / 2
        val len = 2 * Math.sin(Math.toRadians(angel) / 2) * r
        val angel_alpha = (Math.PI - Math.toRadians(angel)) / 2
        val angel_dalta_width = Math.atan(src.height.toDouble() / src.width)
        val angel_dalta_height = Math.atan(src.width.toDouble() / src.height)
        var len_dalta_width = (len * Math.cos(
            Math.PI - angel_alpha
                    - angel_dalta_width
        )).toInt()
        len_dalta_width = if (len_dalta_width > 0) len_dalta_width else -len_dalta_width
        var len_dalta_height = (len * Math.cos(
            Math.PI - angel_alpha
                    - angel_dalta_height
        )).toInt()
        len_dalta_height = if (len_dalta_height > 0) len_dalta_height else -len_dalta_height
        var des_width = src.width + len_dalta_width * 2
        var des_height = src.height + len_dalta_height * 2
        des_width = if (des_width > 0) des_width else -des_width
        des_height = if (des_height > 0) des_height else -des_height
        return Rectangle(Dimension(des_width, des_height))
    }
}