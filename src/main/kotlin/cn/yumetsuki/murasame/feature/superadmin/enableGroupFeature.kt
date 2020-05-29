package cn.yumetsuki.murasame.feature.superadmin

import cn.yumetsuki.murasame.repo.dao.GroupDao
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.enableGroup() {

    val groupDao: GroupDao by globalKoin().inject()

    superAdmin() and content {
        it.removePrefix("admin ").startsWith("enable")
    } quoteReply {
        val msg = it.removePrefix("admin enable ").toLongOrNull()?.let { groupId ->
            groupDao.queryGroupById(groupId)?.let { group ->
                groupDao.updateGroups(group.apply { enable = true })
                "诶？又要吾辈回去说话吗。。真是拿主人没办法呀。。"
            }?:"诶？这个群好像没有被授权..."
        }?:"唔...执行参数好像不够呢...示例: admin enable 1"
        quoteReply(msg)
        Unit
    }

}