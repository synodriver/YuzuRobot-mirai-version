package cn.yumetsuki.murasame.repo.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar

enum class RobotType(
    val qqNumber: Long,
    val botName: String
) {
    MurasameFirst(3530816021, "murasameFirst"), MurasameSecond(3039744519, "murasameSecond")
}

interface Robot: Entity<Robot> {

    companion object: Entity.Factory<Robot>() {
        fun new(name: String, money: Int = 0): Robot = Robot {
            this@Robot.name = name
            this@Robot.money = money
        }
    }

    var name: String

    var money: Int

}

object Robots: Table<Robot>("robot") {
    val name by varchar("robot_name").primaryKey().bindTo { it.name }
    val money by int("money").bindTo { it.money }
}