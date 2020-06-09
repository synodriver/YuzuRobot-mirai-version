package cn.yumetsuki.murasame.feature.superadmin

import cn.yumetsuki.murasame.repo.dao.GroupDao
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.disableGroup() {

    val groupDao: GroupDao by globalKoin().inject()

    superAdmin() and content {
        it.removePrefix("admin ").startsWith("disable")
    } quoteReply {
        val msg = it.removePrefix("admin disable ").toLongOrNull()?.let { groupId ->
            groupDao.queryGroupById(groupId)?.let { group ->
                groupDao.updateGroups(group.apply { enable = false })
                "哼哼，吾辈不在这里说话了！"
            }?:"诶？这个群好像没有被授权..."
        }?:"唔...执行参数好像不够呢...示例: admin disable 1"
        quoteReply(msg)
        Unit
    }

}