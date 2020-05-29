package cn.yumetsuki.murasame.feature.superadmin

import cn.yumetsuki.murasame.repo.dao.GroupDao
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.blackGroup() {

    val groupDao: GroupDao by globalKoin().inject()

    superAdmin() and content {
        it.removePrefix("admin ").startsWith("black")
    } quoteReply {
        val msg = it.removePrefix("admin black ").toLongOrNull()?.let { groupId ->
            groupDao.queryGroupById(groupId)?.let { group ->
                groupDao.updateGroups(group.apply { isBlack = true })
                "把群拉黑成功啦～"
            }?:"诶？这个群好像没有被授权..."
        }?:"唔...执行参数好像不够呢...示例: admin black 1"
        quoteReply(msg)
        Unit
    }

}