package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.murasame.feature.other.recordReplyEvent
import cn.yumetsuki.murasame.listeningfilter.tag
import cn.yumetsuki.murasame.repo.dao.QQUserDao
import cn.yumetsuki.murasame.repo.dao.RobotDao
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.data.PlainText

fun GroupMessageSubscribersBuilder.giveMoney(intercepted: Boolean = true) {

    val qqUserDao : QQUserDao by globalKoin().inject()
    val robotDao : RobotDao by globalKoin().inject()

    val robotName = "murasame"
    val favoriteAddPerMoney = 1

    atBot() and contains("打钱") and tag("giveMoney") quoteReply {
        recordReplyEvent()
        if (intercepted) intercept()
        message[PlainText]?.let {
            it.content.trim().removePrefix("打钱").toIntOrNull()?.let money@{ money ->
                if (money == 0) return@money "岂可修！这不是没有钱嘛？！！！！狗修金快把钱都交出来！"
                val user = qqUserDao.findQQUserByUserIdAndGroupIdOrNewDefault(sender.id, group.id)
                if (user.money < money) "唔....主人的钱好像不太够呢...剩余资金: ${user.money}(饿"
                robotDao.findRobotByRobotName(robotName)?.let { robot ->
                    robot.money += money
                    robotDao.updateRobotRecord(robot)
                }?:return@money "诶...这是什么秘制情况"
                user.money -= money
                val favoriteAdd = favoriteAddPerMoney * money
                user.favorite += favoriteAdd
                "呜哇！！！主人果然对吾辈最好了！～(拿去买好吃的\n(主人剩余资金: ${user.money}\n好感度增加: ${favoriteAdd}\n好感度: ${user.favorite}"
            }?:"唔姆？主人要给多少钱给吾辈呢...(饿"
        }?:"唔？...好像发生了点奇怪的错误.."
    }

}