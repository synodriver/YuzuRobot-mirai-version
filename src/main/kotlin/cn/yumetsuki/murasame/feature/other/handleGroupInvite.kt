package cn.yumetsuki.murasame.feature.other

import cn.yumetsuki.murasame.repo.dao.GroupDao
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent

suspend fun BotInvitedJoinGroupRequestEvent.handleGroupInvite(groupDao: GroupDao) {
    groupDao.queryGroupById(groupId)?.also {
        groupDao.updateGroups(it.apply {
            botUserId = invitorId
        })
        accept()
    }?:ignore()
}