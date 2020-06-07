package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.murasame.feature.other.recordReplyEvent
import cn.yumetsuki.murasame.listeningfilter.tag
import cn.yumetsuki.murasame.repo.dao.ImageRotateDao
import cn.yumetsuki.util.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.GroupMessageEvent
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO

fun GroupMessageSubscribersBuilder.rotateImageFeature(intercepted: Boolean = true) {

    val mutex = Mutex()

    val imageRotateDao : ImageRotateDao by globalKoin().inject()

    fun cachedPath(qq: Long): String = "static/avatar-$qq.gif"

    fun getCachedAvatar(qq: Long): InputStream? {
        val file = File(cachedPath(qq))
        return file.takeIf {
                it.exists()
            }?.inputStream()
    }

    suspend fun memberAvatarToGif(member: Member, cache: Boolean): InputStream? {
        val originImage = ImageIO.read(getNetImageStreamFromUrl(member.avatarUrl, cache))

        if (originImage.width != originImage.height) {
            return null
        }

        val images = arrayListOf<BufferedImage>()
        images.add(originImage)
        for (i in 1..3) {
            val newImage = originImage.rotate(i * 90.0)
            images.add(newImage)
        }
        return images.toGif("static/avatar-${member.id}.gif")
    }

    //第二个参数不能有默认参数，有毒
    suspend fun GroupMessageEvent.rotateMember(member: Member, specialName: String?) {
        recordReplyEvent()
        if (intercepted) intercept()

        val textMsg = "转呀转呀转${specialName?:member.nameCard.takeIf { s -> s.isNotEmpty() }?:member.nick}"

        mutex.withLock {
            //直接利用缓存好的文件
            val avatarStream = getCachedAvatar(member.id)
            if (avatarStream != null) {
                reply(avatarStream.uploadAsImage() + textMsg)
                return
            }

            reply("没有缓存好的头像...处理中..")

            memberAvatarToGif(member, true)?.also { gifStream ->
                val image = gifStream.uploadAsImage()
                val msg = image + textMsg
                reply(msg)
            }?:apply {
                reply("诶？？..你的头像怎么长宽不一样诶..")
            }
            Unit
        }
    }

    suspend fun GroupMessageEvent.refreshMemberAvatar(member: Member) {
        mutex.withLock {
            reply("刷新中...狗修金撒嘛稍等哦...")
            memberAvatarToGif(member, false)?.also { gifStream ->
                val imageFile = File(cachedPath(member.id))

                if (!imageFile.exists()) imageFile.createNewFile()
                imageFile.writeBytes(gifStream.readAllBytes())
                quoteReply("刷新成功啦～")
            }
        }
    }

    case("刷新我") and tag("rotateMe") reply {
        refreshMemberAvatar(sender)
    }

    case("转群主") and tag("rotateOwner") reply {
        rotateMember(group.owner, "群主")
    }

    case("转我") and tag("rotateMe") reply {
        rotateMember(sender, null)
    }

    matching(Regex("转.+")) quoteReply {
        recordReplyEvent()
        if (intercepted) intercept()
        imageRotateDao.queryImageRotateByCharacterName(it.removePrefix("转"))?.let {
            quoteReply(getNetImageStreamFromUrl(it.imgUrl).uploadAsImage() + "转呀转呀转${it.characterName}")
        }
        Unit
    }

}