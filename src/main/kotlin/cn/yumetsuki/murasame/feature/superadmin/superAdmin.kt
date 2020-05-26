package cn.yumetsuki.murasame.feature.superadmin

import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.MessageSubscribersBuilder
import net.mamoe.mirai.message.GroupMessageEvent

fun GroupMessageSubscribersBuilder.superAdmin() : MessageSubscribersBuilder<GroupMessageEvent, Listener<GroupMessageEvent>, Unit, Unit>.ListeningFilter {
    return sentByOperator() and startsWith("admin") and sentFrom(873010542)
}

fun GroupMessageSubscribersBuilder.allSuperAdminFeature() {
    permitInvite()
    whiteGroup()
    blackGroup()
    enableGroup()
    disableGroup()
    sendToGroup()
    whiteUser()
    blackUser()
}
