package cn.yumetsuki.murasame.feature.sudoadmin

import cn.yumetsuki.murasame.repo.dao.GroupDao
import cn.yumetsuki.murasame.repo.entity.Group
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.queryGroupConfig() {

    val groupDao : GroupDao by globalKoin().inject()

    sudoAdmin() and content {
        it.removePrefix("sudo ").trim() == "gconf"
    } quoteReply {
        groupDao.queryGroupById(group.id)?.let {
            formatGroupConfig(it)
        }?:"诶？...这个群好像没有被授权诶.."
    }
}

private fun formatGroupConfig(group: Group): String {
    return "群上限: ${String.format(
            "%.2f",
            group.limitTime / 3600.0
    )}小时${group.limitCount}次\n个人上限: ${String.format(
            "%.2f",
            group.personalLimitTime / 60.0
    )}分钟${group.personalLimitCount}次"
}