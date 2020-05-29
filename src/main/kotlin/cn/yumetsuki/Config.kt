package cn.yumetsuki

class Config(
    val databaseConfig: DatabaseConfig,
    val chatterbotServiceConfig: ChatterbotServiceConfig
) {
    class DatabaseConfig(
        val host: String,
        val port: Int,
        val username: String,
        val password: String,
        val databaseName: String,
        val driverClass: String,
        val useSsl: Boolean
    )

    class ChatterbotServiceConfig(
        val host: String,
        val port: Int
    )
}