package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.Robot
import cn.yumetsuki.murasame.repo.entity.Robots
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.sequenceOf

interface RobotDao {

    suspend fun findRobotByRobotName(robotName: String): Robot?

    suspend fun updateRobotRecord(robot: Robot)

}

class RobotDaoImpl(
    private val database: Database
): RobotDao {

    override suspend fun findRobotByRobotName(
        robotName: String
    ): Robot? = withContext(Dispatchers.IO) {
        database.sequenceOf(Robots).find {
            it.name eq robotName
        }
    }

    override suspend fun updateRobotRecord(
        robot: Robot
    ) = withContext(Dispatchers.IO) {
        robot.flushChanges()
        Unit
    }

}