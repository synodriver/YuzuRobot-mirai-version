package cn.yumetsuki.murasame.feature.superadmin

import cn.yumetsuki.murasame.repo.dao.BlackUserDao
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.whiteUser() {

    val blackUserDao : BlackUserDao by globalKoin().inject()

    superAdmin() and content {
        it.removePrefix("admin ").startsWith("uwhite")
    } quoteReply {
        val msg = it.removePrefix("admin uwhite ").toLongOrNull()?.let { userId ->
            blackUserDao.queryBlackUserById(userId)?.let { user ->
                blackUserDao.deleteBlackUser(user)
                "添加用户白名单成功啦～"
            }?:"诶...这个人没有被拉黑啦～"
        }?:"诶？执行参数好像不对呢...示例: admin uwhite 1"
        quoteReply(msg)
        Unit
    }
}