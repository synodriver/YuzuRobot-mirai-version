package cn.yumetsuki.murasame.util

fun String.removeBlank(): String {
    return replace(Regex("\\s"), "")
}

fun String.removePunctuation(): String {
    return replace(
        Regex("[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]"),
        ""
    )
}