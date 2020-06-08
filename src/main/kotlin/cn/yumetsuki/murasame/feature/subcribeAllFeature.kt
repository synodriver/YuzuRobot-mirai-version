package cn.yumetsuki.murasame.feature

import cn.yumetsuki.mirai.event.wrapSentByOwner
import cn.yumetsuki.murasame.feature.common.*
import cn.yumetsuki.murasame.feature.other.autoAgreeFriendRequest
import cn.yumetsuki.murasame.feature.other.handleGroupInvite
import cn.yumetsuki.murasame.feature.sudoadmin.*
import cn.yumetsuki.murasame.feature.superadmin.*
import cn.yumetsuki.murasame.repo.dao.BlackUserDao
import cn.yumetsuki.murasame.repo.dao.GroupDao
import cn.yumetsuki.murasame.repo.dao.GroupMessageLimitDao
import cn.yumetsuki.murasame.repo.dao.PersonalMessageLimitDao
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.MessageRecallEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import java.time.Duration
import java.time.LocalDateTime

fun Bot.subscribeAllFeature() {

    val groupDao : GroupDao by globalKoin().inject()
    val blackUserDao: BlackUserDao by globalKoin().inject()
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
    
    subscribeAlways<GroupMessageEvent>(priority = Listener.EventPriority.HIGHEST) {
        blackUserDao.queryBlackUserById(sender.id)?.also {
            intercept()
        }
    }

    subscribeAlways<GroupMessageEvent>(priority = Listener.EventPriority.HIGH) {
        val groupConfig = groupDao.queryGroupById(group.id)?:run {
            intercept()
            return@subscribeAlways
        }
        if (message.content.startsWith("sudo") || message.content.startsWith("admin")) {
            return@subscribeAlways
        }
        val personalLimit = personalMessageLimitDao.findLimitByUserIdAndGroupIdAsync(
                sender.id, group.id
        ).await()

        if (personalLimit.messageCount == groupConfig.personalLimitCount) {
            if (Duration.between(personalLimit.firstSendTime, LocalDateTime.now()).seconds > groupConfig.personalLimitTime) {
                personalLimit.copy(firstSendTime = LocalDateTime.now(), messageCount = 1)
            } else {
                intercept()
                return@subscribeAlways
            }
        }

        val groupLimit = groupMessageLimitDao.findLimitByGroupIdAsync(group.id).await()

        if (groupLimit.messageCount == groupConfig.limitCount) {
            if (Duration.between(personalLimit.firstSendTime, LocalDateTime.now()).seconds > groupConfig.limitTime * 60) {
                groupMessageLimitDao.addOrUpdatePersonalMessageLimitAsync(
                        groupLimit.copy(firstSendTime = LocalDateTime.now(), messageCount = 1)
                ).await()
            } else {
                intercept()
                return@subscribeAlways
            }
        }
    }

    subscribeAlways<MessageRecallEvent.GroupRecall> {
        replyRecallMessage()
    }

    //admin指令
    subscribeGroupMessages(priority = Listener.EventPriority.LOWEST) {
        blackGroup()
        blackUser()
        disableGroup()
        enableGroup()
        permitInvite()
        sendToGroup()
        whiteGroup()
        whiteUser()
    }

    //sudo指令
    subscribeGroupMessages(priority = Listener.EventPriority.LOW) {
        banInstruction()
        listCommand()
        listInstruction()
        openInstruction()
        queryGroupConfig()
        updateGroupConfig()
    }

    //大部分用户会触发的事件
    subscribeGroupMessages(priority = Listener.EventPriority.NORMAL) {
        recordMessage()
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

    //一些可能会被拦截的事件
    subscribeGroupMessages(priority = Listener.EventPriority.MONITOR) {
        repeatMachine()
        matchRegexCorpusFeature()
        matchChatterBot()
    }
}