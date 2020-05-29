package cn.yumetsuki.murasame.bean

import cn.yumetsuki.Config
import cn.yumetsuki.ktorm.support.custom.CustomDialect
import cn.yumetsuki.murasame.repo.dao.*
import me.liuwj.ktorm.database.Database
import org.koin.dsl.module

val repoModule = module {
    single {
        val config = get<Config>().databaseConfig
        Database.connect(
                url = "jdbc:mysql://${config.host}:${config.port}/${config.databaseName}?useSSL=${config.useSsl}",
                driver = config.driverClass,
                user = config.username,
                password = config.password,
                dialect = CustomDialect()
        )
    }
    single<AtStatusDao> { AtStatusDaoImpl() }
    single<BlackUserDao> { BlackUserDaoImpl(get()) }
    single<ExecRecordDao> { ExecRecordDaoImpl(get()) }
    single<GoHanRecordDao> { GoHanRecordDaoImpl() }
    single<GroupDao> { GroupDaoImpl(get()) }
    single<GroupMessageLimitDao> { GroupMessageLimitDaoImpl() }
    single<ImageRotateDao> { ImageRotateDaoImpl(get()) }
    single<InstructionDao> { InstructionDaoImpl(get()) }
    single<InstructionGroupBanRuleDao> { InstructionGroupBanRuleDaoImpl(get()) }
    single<LotRecordDao> { LotRecordDaoImpl(get()) }
    single<PersonalMessageLimitDao> { PersonalMessageLimitDaoImpl() }
    single<QQUserDao> { QQUserDaoImpl(get()) }
    single<RegexCorpusDao> { RegexCorpusDaoImpl(get()) }
    single<RobMoneyRecordDao> { RobMoneyRecordDaoImpl(get()) }
    single<RobotDao> { RobotDaoImpl(get()) }
    single<RudeSpeakDao> { RudeSpeakDaoImpl(get()) }
    single<TodoRecordDao> { TodoRecordDaoImpl(get()) }
    single<UserDao> { UserDaoImpl(get()) }
}