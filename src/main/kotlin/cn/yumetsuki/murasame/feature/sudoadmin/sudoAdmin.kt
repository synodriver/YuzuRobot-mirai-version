package cn.yumetsuki.murasame.feature.sudoadmin

import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.MessageSubscribersBuilder
import net.mamoe.mirai.message.GroupMessageEvent

fun GroupMessageSubscribersBuilder.sudoAdmin() : MessageSubscribersBuilder<GroupMessageEvent, Listener<GroupMessageEvent>, Unit, Unit>.ListeningFilter {
    return sentByOperator() and startsWith("sudo")
}

fun GroupMessageSubscribersBuilder.allSudoAdminFeature() {
    banInstruction()
    listCommand()
    listInstruction()
    openInstruction()
    queryGroupConfig()
    updateGroupConfig()
}