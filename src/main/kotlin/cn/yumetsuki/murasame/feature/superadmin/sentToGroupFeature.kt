package cn.yumetsuki.murasame.feature.superadmin

import cn.yumetsuki.murasame.repo.dao.GroupDao
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.sentToGroup() {

    val groupDao: GroupDao by cn.yumetsuki.util.globalKoin().inject()

    superAdmin() and content {
        it.removePrefix("admin ").startsWith("send") && it.removePrefix("admin send ").matches(
                Regex("")
        )
    } quoteReply {
        it.removePrefix("admin black ").toLongOrNull()?.let { groupId ->
            groupDao.queryGroupById(groupId)?.let { group ->
                groupDao.updateGroups(group.apply { isBlack = true })
            }?:"诶？这个群好像没有被授权..."
        }?:"唔...执行参数好像不够呢...示例: admin black 1"
    }

}