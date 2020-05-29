package cn.yumetsuki.murasame.feature.superadmin

import cn.yumetsuki.murasame.repo.dao.BlackUserDao
import cn.yumetsuki.murasame.repo.entity.BlackUser
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.blackUser() {

    val blackUserDao : BlackUserDao by globalKoin().inject()

    superAdmin() and content {
        it.removePrefix("admin ").startsWith("ublack")
    } quoteReply {
        val msg = it.removePrefix("admin ublack ").toLongOrNull()?.let { userId ->
            blackUserDao.queryBlackUserById(userId)?.let {
                "这个人已经在黑名单里啦～"
            }?:blackUserDao.addBlackUser(BlackUser {
                this.userId = userId
            }).run {
                "拉黑成功啦～"
            }
        }?:"诶？执行参数好像不对呢...示例: admin ublack 1"
        quoteReply(msg)
        Unit
    }
}