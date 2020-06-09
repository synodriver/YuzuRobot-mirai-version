package cn.yumetsuki.murasame.feature.superadmin

import cn.yumetsuki.murasame.repo.dao.GroupDao
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.getGroupOrNull
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.asMessageChain

fun GroupMessageSubscribersBuilder.sendToGroup() {

    val groupDao: GroupDao by globalKoin().inject()

    val matchRegex = Regex("\\d+ .+")

    superAdmin() and content {
        it.removePrefix("admin ").startsWith("send") && it.removePrefix("admin send ").matches(matchRegex)
    } quoteReply {
        val arguments = it.removePrefix("admin send ").split(" ", limit = 2)
        if (arguments.size != 2) return@quoteReply "唔...参数好像有误呢...示例: admin send 1 发送一条消息～"
        val groupId = arguments[0].toLong()
        val rMsg = groupDao.queryGroupById(groupId)?.let {
            val firstMsg = message[PlainText]!!.content.removePrefix("admin send $groupId ")
            val msg = PlainText(firstMsg) + message.drop(1).asMessageChain()
            bot.getGroupOrNull(groupId)?.sendMessage(msg)?.let {
                "发送成功啦～"
            }?:"诶？...吾辈好像不在这个群里..."
        }?:"诶？这个群好像没有被授权..."
        quoteReply(rMsg)
        Unit
    }

}