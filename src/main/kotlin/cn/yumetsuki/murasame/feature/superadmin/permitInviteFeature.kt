package cn.yumetsuki.murasame.feature.superadmin

import cn.yumetsuki.murasame.repo.dao.GroupDao
import cn.yumetsuki.murasame.repo.entity.Group
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.permitInvite() {

    val groupDao: GroupDao by globalKoin().inject()

    superAdmin() and content {
        it.removePrefix("admin ").startsWith("permit")
    } quoteReply {
        val msg = it.removePrefix("admin permit ").toLongOrNull()?.let { groupId ->
            groupDao.queryGroupById(groupId)?.let {
                "这个群已经被允许过啦～"
            }?:groupDao.insertGroups(Group.new(groupId)).run {
                "授权成功啦～"
            }
        }?:"唔...执行参数好像不够呢...示例: admin permit 1"
        quoteReply(msg)
        Unit
    }

}