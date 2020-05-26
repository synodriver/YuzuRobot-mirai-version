package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.mirai.data.withLine
import cn.yumetsuki.murasame.listeningfilter.tag
import cn.yumetsuki.murasame.repo.dao.QQUserDao
import cn.yumetsuki.murasame.repo.dao.RobMoneyRecordDao
import cn.yumetsuki.murasame.repo.dao.RobotDao
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.data.PlainText
import java.time.LocalDate
import kotlin.math.floor
import kotlin.random.Random
import kotlin.random.nextInt

fun GroupMessageSubscribersBuilder.robMoney() {

    val qqUserDao : QQUserDao by globalKoin().inject()
    val robotDao: RobotDao by globalKoin().inject()
    val robMoneyRecordDao : RobMoneyRecordDao by globalKoin().inject()

    val robotName = "murasame"

    atBot() and content {
        message[PlainText]?.content?.trim() == "抢钱"
    } and tag("robMoney") quoteReply {
        robMoneyRecordDao.findRobMoneyRecordsByUserIdAndGroupIdAndDate(
                sender.id, group.id, LocalDate.now()
        ).takeIf {
            it.isNotEmpty()
        }?.let {
            "主人汝今天都抢了吾辈那么多钱了还想抢吗kora？！！"
        }?:qqUserDao.findQQUserByUserIdAndGroupIdOrNewDefault(
                sender.id, group.id
        ).let { user ->
            val robot = robotDao.findRobotByRobotName(robotName)?:return@let "诶...好像出现了神奇的错误..."
            if (Random.nextInt(1..100) > 10) return@let "这是吾辈的钱kora！主人不可以抢吾辈的钱！！！(诶嘿嘿，吾辈还有${robot.money}元零花钱"
            val robedMoney = floor(robot.money / 100.0).toInt()
            if (robedMoney == 0) return@let "呜呜呜呜...吾辈已经没有钱了...主人好过分..."
            robotDao.updateRobotRecord(robot.apply { money -= robedMoney })
            val favoriteDecrement = (robedMoney * 1.5).toInt()
            qqUserDao.updateQQUser(user.apply {
                money += robedMoney
                favorite -= favoriteDecrement
            })
            PlainText("呜啊啊啊啊啊啊啊啊啊啊！！！！吾辈的点心钱啊啊啊啊啊啊啊！！！").withLine(
                    "(资金+$robedMoney, 好感-$favoriteDecrement"
            ).withLine(
                    "总资金: ${user.money}; 总好感: ${user.favorite}"
            )
        }
    }

}