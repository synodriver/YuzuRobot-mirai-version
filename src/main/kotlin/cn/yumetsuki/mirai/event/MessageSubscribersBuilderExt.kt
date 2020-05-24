package cn.yumetsuki.mirai.event

import net.mamoe.mirai.event.*
import net.mamoe.mirai.message.MessageEvent

fun GroupMessageSubscribersBuilder.sentFromGroups(
        vararg groupIds: Long,
        listeners: GroupMessageSubscribersBuilder.() -> Unit
) {
    return wrapContent({
        this.group.id in groupIds
    }, listeners)
}

fun GroupMessageSubscribersBuilder.sentByUsers(
        vararg userIds: Long,
        listeners: GroupMessageSubscribersBuilder.() -> Unit
) {
    return wrapContent({
        this.sender.id in userIds
    }, listeners)
}

fun <M : MessageEvent, Ret> MessageSubscribersBuilder<M, Ret, Unit, Unit>.wrapContent(
        wrapFilter: M.(String) -> Boolean,
        listeners: MessageSubscribersBuilder<M, Ret, Unit, Unit>.() -> Unit
){
    wrapContent(Unit, wrapFilter, listeners)
}

fun <M : MessageEvent, Ret, R : RR, RR> MessageSubscribersBuilder<M, Ret, R, RR>.wrapContent(
        stub: RR,
        wrapFilter: M.(String) -> Boolean,
        listeners: MessageSubscribersBuilder<M, Ret, R, RR>.() -> R
) : R {
    return MessageSubscribersBuilder<M, Ret, R, RR>(stub) { filter, listener ->
        newListeningFilter(wrapFilter) and newListeningFilter(filter) reply listener
    }.run(listeners)
}