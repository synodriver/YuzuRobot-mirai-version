package cn.yumetsuki.murasame.feature.superadmin

import cn.yumetsuki.murasame.repo.dao.GroupDao
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.whiteGroup() {

    val groupDao: GroupDao by globalKoin().inject()

    superAdmin() and content {
        it.removePrefix("admin ").startsWith("white")
    } quoteReply {
        it.removePrefix("admin white ").toLongOrNull()?.let { groupId ->
            groupDao.queryGroupById(groupId)?.let { group ->
                groupDao.updateGroups(group.apply { isBlack = false })
                "把群重新加回白名单啦～"
            }?:"诶？这个群好像没有被授权..."
        }?:"唔...执行参数好像不够呢...示例: admin white 1"
    }

}