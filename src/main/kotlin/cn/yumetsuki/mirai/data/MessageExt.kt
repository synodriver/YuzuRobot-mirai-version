package cn.yumetsuki.mirai.data

import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.at
import net.mamoe.mirai.message.data.PlainText

fun Message.withEmpty() = this + " "

fun Message.withLine(message: Message) = this + "\n" + message

fun Message.withLine(text: String) = withLine(PlainText(text))

fun Member.atWithEmpty() = at().withEmpty()