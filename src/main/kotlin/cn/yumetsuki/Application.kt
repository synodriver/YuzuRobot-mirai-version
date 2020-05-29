package cn.yumetsuki

import cn.yumetsuki.murasame.bean.configModule
import cn.yumetsuki.murasame.bean.repoModule
import cn.yumetsuki.murasame.feature.subscribeAllFeature
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.join
import net.mamoe.mirai.utils.BotConfiguration
import org.koin.core.context.startKoin

suspend fun main() {

    val koinApp = startKoin {
        modules(listOf(configModule, repoModule))
    }

    val config by koinApp.koin.inject<Config>()

    val bot = Bot(config.qqConfig.qq, config.qqConfig.password, BotConfiguration.Default.apply {
        fileBasedDeviceInfo()
    })
    bot.alsoLogin()
    bot.subscribeAllFeature()
    bot.join()
}