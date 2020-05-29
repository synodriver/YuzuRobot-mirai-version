package cn.yumetsuki

import cn.yumetsuki.murasame.bean.configModule
import cn.yumetsuki.murasame.bean.repoModule
import cn.yumetsuki.murasame.feature.subscribeAllFeature
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.join
import net.mamoe.mirai.utils.BotConfiguration
import org.koin.core.context.startKoin

suspend fun main(args: Array<String>) {
    if (args.size != 2) error("args size error")
    val qq = args[0].toLongOrNull()?: error("qq number error")
    val password = args[1]

    startKoin {
        modules(listOf(configModule, repoModule))
    }

    val bot = Bot(qq, password, BotConfiguration.Default.apply {
        fileBasedDeviceInfo("device-$qq.json")
    })
    bot.alsoLogin()
    bot.subscribeAllFeature()
    bot.join()
}