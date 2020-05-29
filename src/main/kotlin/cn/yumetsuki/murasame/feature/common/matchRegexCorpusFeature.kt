package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.murasame.feature.other.recordReplyEvent
import cn.yumetsuki.murasame.repo.dao.RegexCorpusDao
import cn.yumetsuki.util.CQMessage
import cn.yumetsuki.util.getMessageChainFromCQMessages
import cn.yumetsuki.util.globalKoin
import cn.yumetsuki.util.toList
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.data.At

fun GroupMessageSubscribersBuilder.matchRegexCorpusFeature() {

    val regexCorpusDao : RegexCorpusDao by globalKoin().inject()

    content {
        message[At] == null
    } quoteReply {
        recordReplyEvent()
        regexCorpusDao.findRegexCorpusMatchRegex(it).takeIf { corpuses ->
            corpuses.isNotEmpty()
        }?.let { corpuses ->
            getMessageChainFromCQMessages(CQMessage(corpuses.random().response).toList())
        }?:Unit
    }

}