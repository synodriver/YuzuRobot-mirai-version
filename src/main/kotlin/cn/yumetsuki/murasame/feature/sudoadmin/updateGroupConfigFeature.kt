package cn.yumetsuki.murasame.feature.sudoadmin

import cn.yumetsuki.murasame.repo.dao.GroupDao
import cn.yumetsuki.murasame.repo.entity.Group
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.updateGroupConfig() {

    val groupDao : GroupDao by globalKoin().inject()

    sudoAdmin() and content {
        it.removePrefix("sudo gconf ").startsWith("update ")
    } quoteReply {
        val msg = groupDao.queryGroupById(group.id)?.let { group ->
            val items = it.removePrefix("sudo gconf update ").split(" ")
            if (items.all { item -> item.matches(Regex("--(glt|glc|plt|plc)=\\d+")) }) {
                items.groupBy { item ->
                    item.removePrefix("--").split("=")[0]
                }.mapValues { (_, value) ->
                    value.last().split("=")[1].toInt()
                }.forEach { (key, value) ->
                    when(key) {
                        "glt" -> group.limitTime = value
                        "glc" -> group.limitCount = value
                        "plt" -> group.personalLimitTime = value
                        "plc" -> group.personalLimitCount = value
                    }
                }
                groupDao.updateGroups(group)
                "更新群配置成功啦～${formatGroupConfig(group)}"
            } else {
                "诶...格式有点不对哦～示例: sudo gconf update --glc=5 --glt=3600"
            }
        }?:"诶？...这个群好像没有被授权诶.."
        quoteReply(msg)
        Unit
    }
}

private fun formatGroupConfig(group: Group): String {
    return "\n群上限: ${String.format(
            "%.2f",
            group.limitTime / 3600.0
    )}小时${group.limitCount}次\n个人上限: ${String.format(
            "%.2f",
            group.personalLimitTime / 60.0
    )}分钟${group.personalLimitCount}次"
}