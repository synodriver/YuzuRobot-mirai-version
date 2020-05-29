package cn.yumetsuki.murasame.feature.other

import cn.yumetsuki.murasame.repo.dao.GroupDao
import cn.yumetsuki.murasame.repo.dao.GroupMessageLimitDao
import cn.yumetsuki.murasame.repo.dao.PersonalMessageLimitDao
import cn.yumetsuki.util.globalKoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.message.GroupMessageEvent
import java.time.Duration
import java.time.LocalDateTime

suspend fun GroupMessageEvent.recordReplyEvent() = coroutineScope {
    launch {
        val groupDao : GroupDao by globalKoin().inject()

        val groupMessageLimitDao: GroupMessageLimitDao by globalKoin().inject()

        val personalMessageLimitDao: PersonalMessageLimitDao by globalKoin().inject()

        val groupConfig = groupDao.queryGroupById(group.id)?:return@launch

        val personalLimit = personalMessageLimitDao.findLimitByUserIdAndGroupIdAsync(
                sender.id, group.id
        ).await()

        val groupLimit = groupMessageLimitDao.findLimitByGroupIdAsync(group.id).await()

        //更新个人消息限制
        val newPersonalLimit = if (Duration.between(personalLimit.firstSendTime, LocalDateTime.now()).seconds > groupConfig.personalLimitTime) {
            personalLimit.copy(firstSendTime = LocalDateTime.now(), messageCount = 1)
        } else {
            personalLimit.copy(messageCount = personalLimit.messageCount + 1)
        }
        personalMessageLimitDao.addOrUpdatePersonalMessageLimitAsync(newPersonalLimit).await()

        //更新群组消息限制
        val newGroupLimit = if (Duration.between(personalLimit.firstSendTime, LocalDateTime.now()).seconds > groupConfig.limitTime) {
            groupLimit.copy(firstSendTime = LocalDateTime.now(), messageCount = 1)
        } else {
            groupLimit.copy(messageCount = personalLimit.messageCount + 1)
        }
        groupMessageLimitDao.addOrUpdatePersonalMessageLimitAsync(newGroupLimit).await()
    }
}