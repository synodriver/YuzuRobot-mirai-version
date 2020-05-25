package cn.yumetsuki.mirai.data

import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.at

fun Message.withEmpty() = this + " "

fun Member.atWithEmpty() = at().withEmpty()