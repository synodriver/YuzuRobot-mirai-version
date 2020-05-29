package cn.yumetsuki.util

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.uploadAsImage
import java.io.File

open class CQMessage(
        val rawMessage: String
) {
    companion object {
        fun of(rawMessage: String): CQMessage {
            return CQMessage(rawMessage)
        }

        fun empty(): CQMessage = CQMessage("")
        fun blank(): CQMessage = CQMessage(" ")
        fun newLine(): CQMessage = CQMessage("\n")
        fun withNewLine(text: String): CQMessage = CQMessage(text) + newLine()
        fun withPrefixNewLine(text: String): CQMessage = newLine() + CQMessage(text)
    }

    operator fun <T: CQMessage> plus(cqMessage: T): CQMessage {
        return CQMessage(rawMessage + cqMessage.rawMessage)
    }

    operator fun plus(message: String): CQMessage {
        return CQMessage(rawMessage + message)
    }
}

sealed class CQCode(
        private val type: String,
        private val valueType: String,
        val content: String
): CQMessage("[CQ:$type,$valueType=$content]") {

    companion object {
        val matchRegex = Regex("\\[CQ:.+,\\s*.+=((?!]).)*]")
        fun getContent(message: String): String? {
            return if (matchRegex.matches(message)) {
                return message.substring(1, message.length - 1).split("=")[1]
            } else {
                null
            }
        }
    }

    fun test(message: String, withContent: Boolean = false): Boolean {
        return Regex(
                "\\[CQ:$type,\\s*$valueType=${
                if (withContent) content else "((?!]).)*"
                }]"
        ).matches(message)
    }

    fun contains(message: String, withContent: Boolean = false): Boolean {
        return message.contains(
                Regex(
                        "\\[CQ:$type,\\s*$valueType=${
                        if (withContent) content else "((?!]).)*"
                        }]"
                )
        )
    }

}

class CQImage(
        url: String
): CQCode(TYPE, VALUE_TYPE, url) {

    companion object {
        private const val TYPE = "image"
        private const val VALUE_TYPE = "file"
        val matchRegex = Regex("\\[CQ:$TYPE,\\s*$VALUE_TYPE=((?!]).)*]")
    }

}


class CQAt(
        content: String = ""
): CQCode(TYPE, VALUE_TYPE, content) {

    companion object {

        private const val TYPE = "at"
        private const val VALUE_TYPE = "qq"
        val matchRegex = Regex("\\[CQ:${TYPE},\\s*${VALUE_TYPE}=((?!]).)*]")

        fun of(userId: Long): CQAt {
            return CQAt(userId.toString())
        }
    }

}

class CQRecord(
        content: String = ""
) : CQCode(TYPE, VALUE_TYPE, content) {

    companion object {
        private const val TYPE = "record"
        private const val VALUE_TYPE = "file"
        val matchRegex = Regex("\\[CQ:${TYPE},\\s*${VALUE_TYPE}=((?!]).)*]")
    }

}

suspend fun CQMessage.toList() : List<CQMessage> {
    val result = arrayListOf<CQMessage>()
    var message = rawMessage
    while (message.isNotEmpty()) {
        val firstStartIndex = message.indexOfFirst {
            it == '['
        }
        if (firstStartIndex == -1) {
            result.add(CQMessage(message))
            break
        }
        val text = message.substring(0, firstStartIndex)
        message = if (text.isNotEmpty()) {
            result.add(CQMessage(text))
            message.substring(firstStartIndex)
        } else {
            val firstEndIndex = message.indexOfFirst {
                it == ']'
            }
            val cqCodeMessage = message.substring(firstStartIndex, firstEndIndex + 1)
            if (CQImage.matchRegex.matches(cqCodeMessage)) {
                result.add(CQImage(CQCode.getContent(cqCodeMessage)!!))
            }
            if (CQAt.matchRegex.matches(cqCodeMessage)) {
                result.add(CQAt(CQCode.getContent(cqCodeMessage)!!))
            }
            message.substring(firstEndIndex + 1)
        }
    }
    return result
}

suspend fun GroupMessageEvent.getMessageChainFromCQMessages(messages: List<CQMessage>): MessageChain {
    var result = PlainText("").asMessageChain()
    messages.forEach {
        when(it) {
            is CQImage -> result += getNetImageStreamFromUrl(it.content).uploadAsImage()
            is CQAt -> result += group[it.content.toLong()].at()
            is CQRecord -> result += "[语音消息]"
            else -> result += it.rawMessage
        }
    }
    return result
}