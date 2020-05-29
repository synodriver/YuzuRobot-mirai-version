package cn.yumetsuki.murasame.feature.other

import net.mamoe.mirai.event.events.NewFriendRequestEvent

suspend fun NewFriendRequestEvent.autoAgreeFriendRequest() {
    accept()
}