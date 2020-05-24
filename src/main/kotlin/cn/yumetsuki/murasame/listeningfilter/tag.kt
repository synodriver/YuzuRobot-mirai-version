package cn.yumetsuki.murasame.listeningfilter

import cn.yumetsuki.mirai.event.wrapContent
import cn.yumetsuki.murasame.repo.dao.InstructionGroupBanRuleDao
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.MessageSubscribersBuilder
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import org.koin.core.KoinComponent
import org.koin.core.inject

fun GroupMessageSubscribersBuilder.tag(text: String) : MessageSubscribersBuilder<GroupMessageEvent, Listener<GroupMessageEvent>, Unit, Unit>.ListeningFilter {
    val instructionGroupBanRuleDao = object : KoinComponent {
        val instructionGroupBanRuleDao by inject<InstructionGroupBanRuleDao>()
    }.instructionGroupBanRuleDao
    return content { runBlocking { instructionGroupBanRuleDao.queryBanRuleByTagAndGroupId(text, group.id) } == null }
}

fun GroupMessageSubscribersBuilder.tag(
        text: String,
        listeners: GroupMessageSubscribersBuilder.() -> Unit
) {
    wrapContent(tag(text).filter, listeners)
}

