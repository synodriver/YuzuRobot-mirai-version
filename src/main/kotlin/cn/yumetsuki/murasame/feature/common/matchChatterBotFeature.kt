package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.murasame.feature.other.recordReplyEvent
import cn.yumetsuki.murasame.repo.dao.AtStatusDao
import cn.yumetsuki.murasame.repo.entity.AtStatus
import cn.yumetsuki.resource.CALL_BOT_1
import cn.yumetsuki.resource.CALL_BOT_2
import cn.yumetsuki.resource.CALL_BOT_3
import cn.yumetsuki.util.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.header
import io.ktor.client.request.post
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import java.io.File

@Suppress("EXPERIMENTAL_API_USAGE")
fun GroupMessageSubscribersBuilder.matchChatterBot() {

    val atStatusDao : AtStatusDao by globalKoin().inject()

    suspend fun GroupMessageEvent.replyRandom() {
        atStatusDao.findAtStatusByUserIdAndGroupIdAsync(sender.id, group.id).await().also { status ->
            val replies = arrayOf(
                getNetImageStreamFromUrl(CALL_BOT_1).uploadAsImage() + "叫吾辈有什么事吗？主人～",
                getNetImageStreamFromUrl(CALL_BOT_2).uploadAsImage() + "诶？抱歉主人，刚才在稍微想点事情，叫吾辈有什么事吗？",
                getNetImageStreamFromUrl(CALL_BOT_3).uploadAsImage() + "唔姆？？"
            )
            reply(replies.random())
            atStatusDao.addOrUpdateAtStatusAsync(status.copy(atCount = status.atCount + 1)).await()
        }
    }

    atBot {
        recordReplyEvent()
        val request = message[PlainText]?.content?.trim()?.removePunctuation()?.takeIf { it.isNotEmpty() }?:replyRandom().run {
            return@atBot
        }
        try {
            HttpClient(CIO).use { httpClient ->
                httpClient.post<String>(
                    host = "121.36.84.191",
                    port = 8081,
                    path = "/response/generate",
                    body = mapOf("request" to request).toJson()
                ) {
                    header("content-type", "application/json")
                }.fromJson<Map<String, String>>()["response"]?.takeIf { s -> s.isNotEmpty() }
            }
        } catch (e: Exception) {
            println("request chatterbot error: ")
            e.printStackTrace()
            null
        }?.let { response ->
            reply(getMessageChainFromCQMessages(CQMessage(response).toList()))
            atStatusDao.addOrUpdateAtStatusAsync(AtStatus(sender.id, group.id, 0)).await()
        }?:replyRandom()

    }
}