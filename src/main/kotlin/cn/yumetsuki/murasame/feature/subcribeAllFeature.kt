package cn.yumetsuki.murasame.feature

import cn.yumetsuki.mirai.event.wrapSentByOwner
import cn.yumetsuki.murasame.feature.common.*
import cn.yumetsuki.murasame.feature.other.autoAgreeFriendRequest
import cn.yumetsuki.murasame.feature.other.handleGroupInvite
import cn.yumetsuki.murasame.feature.sudoadmin.*
import cn.yumetsuki.murasame.feature.superadmin.*
import cn.yumetsuki.murasame.repo.dao.GroupDao
import cn.yumetsuki.murasame.repo.dao.GroupMessageLimitDao
import cn.yumetsuki.murasame.repo.dao.PersonalMessageLimitDao
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.message.GroupMessageEvent

fun Bot.subscribeAllFeature() {

    val groupDao : GroupDao by globalKoin().inject()
    val groupMessageLimitDao : GroupMessageLimitDao by globalKoin().inject()
    val personalMessageLimitDao : PersonalMessageLimitDao by globalKoin().inject()

    subscribeAlways<BotInvitedJoinGroupRequestEvent> {
        handleGroupInvite(groupDao)
    }

    subscribeAlways<NewFriendRequestEvent> {
        autoAgreeFriendRequest()
    }

    subscribeAlways<GroupMessageEvent>(priority = Listener.EventPriority.HIGHEST) {
        groupDao.queryGroupById(group.id)?.also {
            if (!it.enable || it.isBlack || it.botUserId != bot.id) intercept()
        }?:run {
            intercept()
            return@subscribeAlways
        }
    }

    subscribeAlways<GroupMessageEvent>(priority = Listener.EventPriority.HIGH) {
        val groupConfig = groupDao.queryGroupById(group.id)?:run {
            intercept()
            return@subscribeAlways
        }
        if (groupMessageLimitDao.findLimitByGroupIdAsync(group.id).await().messageCount == groupConfig.limitCount) {
            intercept()
            return@subscribeAlways
        }
        if (personalMessageLimitDao.findLimitByUserIdAndGroupIdAsync(sender.id, group.id).await().messageCount == groupConfig.personalLimitCount) {
            intercept()
            return@subscribeAlways
        }
    }

    //admin指令
    subscribeGroupMessages(priority = Listener.EventPriority.LOWEST) {
        wrapSentByOwner {
            blackGroup()
            blackUser()
            disableGroup()
            enableGroup()
            permitInvite()
            sendToGroup()
            whiteGroup()
            whiteUser()
        }
    }

    //sudo指令
    subscribeGroupMessages(priority = Listener.EventPriority.LOW) {
        wrapSentByOwner {
            banInstruction()
            listCommand()
            listInstruction()
            openInstruction()
            queryGroupConfig()
            updateGroupConfig()
        }
    }

    //大部分用户会触发的事件
    subscribeGroupMessages(priority = Listener.EventPriority.NORMAL) {
        wrapSentByOwner {
            lot()
            eatTogether()
            giveMoney()
            ownInfo()
            ownStatus()
            queryFeature()
            robMoney()
            todo()
            touchOpai()
            rotateImageFeature()
        }
    }

    //一些可能会被拦截的事件
    subscribeGroupMessages(priority = Listener.EventPriority.MONITOR) {
        wrapSentByOwner {
            repeatMachine()
            matchRegexCorpusFeature()
            matchChatterBot()
        }
    }
}