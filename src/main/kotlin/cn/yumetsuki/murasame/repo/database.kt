package cn.yumetsuki.murasame.repo

import me.liuwj.ktorm.database.Database

fun dataBase(
    host: String = "47.97.201.51",
    port: Int = 3306,
    username: String = "root",
    password: String = "shinku520",
    dbName: String = "robot_data"
): Database {
    return Database.connect(
        url = "jdbc:mysql://$host:$port/$dbName",
        driver = "com.mysql.jdbc.Driver",
        user = username,
        password = password
    )
}