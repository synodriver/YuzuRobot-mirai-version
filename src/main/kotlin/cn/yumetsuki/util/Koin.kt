package cn.yumetsuki.util

import org.koin.core.Koin
import org.koin.core.context.GlobalContext

fun globalKoin(): Koin = GlobalContext.get().koin