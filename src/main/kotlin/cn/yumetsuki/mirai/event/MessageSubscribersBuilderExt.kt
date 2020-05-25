package cn.yumetsuki.mirai.event

import net.mamoe.mirai.event.*
import net.mamoe.mirai.message.MessageEvent

fun GroupMessageSubscribersBuilder.sentFromGroups(
        vararg groupIds: Long,
        listeners: GroupMessageSubscribersBuilder.() -> Unit
) {
    return withFilter({
        this.group.id in groupIds
    }, listeners)
}

fun GroupMessageSubscribersBuilder.sentByUsers(
        vararg userIds: Long,
        listeners: GroupMessageSubscribersBuilder.() -> Unit
) {
    return withFilter({
        this.sender.id in userIds
    }, listeners)
}

fun <M : MessageEvent, Ret> MessageSubscribersBuilder<M, Ret, Unit, Unit>.withFilter(
        wrapFilter: M.(String) -> Boolean,
        listeners: MessageSubscribersBuilder<M, Ret, Unit, Unit>.() -> Unit
){
    withFilter(Unit, wrapFilter, listeners)
}

fun <M : MessageEvent, Ret, R : RR, RR> MessageSubscribersBuilder<M, Ret, R, RR>.withFilter(
        stub: RR,
        wrapFilter: M.(String) -> Boolean,
        listeners: MessageSubscribersBuilder<M, Ret, R, RR>.() -> R
) : R {
    return withFilter(stub, newListeningFilter(wrapFilter), listeners)
}

fun <M : MessageEvent, Ret> MessageSubscribersBuilder<M, Ret, Unit, Unit>.withFilter(
        listeningFilter: MessageSubscribersBuilder<M, Ret, Unit, Unit>.ListeningFilter,
        listeners: MessageSubscribersBuilder<M, Ret, Unit, Unit>.() -> Unit
){
    withFilter(Unit, listeningFilter, listeners)
}

fun <M : MessageEvent, Ret, R : RR, RR> MessageSubscribersBuilder<M, Ret, R, RR>.withFilter(
        stub: RR,
        listeningFilter: MessageSubscribersBuilder<M, Ret, R, RR>.ListeningFilter,
        listeners: MessageSubscribersBuilder<M, Ret, R, RR>.() -> R
) : R {
    return MessageSubscribersBuilder<M, Ret, R, RR>(stub) { filter, listener ->
        listeningFilter and newListeningFilter(filter) reply listener
    }.run(listeners)
}