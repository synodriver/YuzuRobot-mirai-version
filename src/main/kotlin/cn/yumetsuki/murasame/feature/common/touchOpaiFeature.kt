package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.murasame.feature.other.recordReplyEvent
import cn.yumetsuki.murasame.repo.dao.GroupDao
import cn.yumetsuki.resource.TOUCH_OPAI_BACK_IMG
import cn.yumetsuki.resource.TOUCH_OPAI_IMG
import cn.yumetsuki.util.getNetImageStreamFromUrl
import cn.yumetsuki.util.globalKoin
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.data.PlainText

fun GroupMessageSubscribersBuilder.touchOpai(intercepted: Boolean = true) {

    val groupDao : GroupDao by globalKoin().inject()

    atBot() and content {
        message[PlainText]?.content?.trim() == "摸胸"
    } reply {
        recordReplyEvent()
        if (intercepted) intercept()
        val groupConfig = groupDao.queryGroupById(group.id)?:return@reply "诶...这个群好像没被授权..."
        reply(getNetImageStreamFromUrl(TOUCH_OPAI_IMG).uploadAsImage() + "hen....hentai啊啊啊啊啊！！！！！梦月妈妈快来救吾辈哇呜呜呜呜！！！")
        coroutineScope {
            launch {
                delay(2000)
                reply("梦月月: 带走小丛雨10分钟...")
                groupDao.updateGroups(groupConfig.apply { enable = false })
                delay(60 * 1000 * 10)
                groupDao.updateGroups(groupConfig.apply { enable = true })
                reply(getNetImageStreamFromUrl(TOUCH_OPAI_BACK_IMG).uploadAsImage() + "下。。。下次可不要再对吾辈做这种无理的事情哦。。。")
            }
        }
        Unit
    }

}