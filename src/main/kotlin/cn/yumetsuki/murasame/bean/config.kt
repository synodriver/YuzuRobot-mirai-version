package cn.yumetsuki.murasame.bean

import cn.yumetsuki.Config
import cn.yumetsuki.util.fromJson
import org.koin.dsl.module
import java.io.File

val configModule = module {
    single<Config> { File("config.json").readText().fromJson() }
}