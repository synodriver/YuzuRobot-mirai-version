package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.murasame.feature.other.recordReplyEvent
import cn.yumetsuki.murasame.repo.dao.ImageRotateDao
import cn.yumetsuki.util.getNetImageStreamFromUrl
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.rotateImageFeature(intercepted: Boolean = true) {
    val imageRotateDao : ImageRotateDao by globalKoin().inject()

    matching(Regex("转.+")) quoteReply {
        recordReplyEvent()
        if (intercepted) intercept()
        imageRotateDao.queryImageRotateByCharacterName(it.removePrefix("转"))?.let {
            getNetImageStreamFromUrl(it.imgUrl).uploadAsImage() + "转呀转呀转${it.characterName}"
        }?:Unit
    }
}