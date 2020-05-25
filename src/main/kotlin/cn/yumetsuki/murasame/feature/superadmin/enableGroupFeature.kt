package cn.yumetsuki.murasame.feature.superadmin

import cn.yumetsuki.murasame.repo.dao.GroupDao
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.enableGroup() {

    val groupDao: GroupDao by cn.yumetsuki.util.globalKoin().inject()

    superAdmin() and content {
        it.removePrefix("admin ").startsWith("enable")
    } quoteReply {
        it.removePrefix("admin enable ").toLongOrNull()?.let { groupId ->
            groupDao.queryGroupById(groupId)?.let { group ->
                groupDao.updateGroups(group.apply { enable = true })
            }?:"诶？这个群好像没有被授权..."
        }?:"唔...执行参数好像不够呢...示例: admin enable 1"
    }

}