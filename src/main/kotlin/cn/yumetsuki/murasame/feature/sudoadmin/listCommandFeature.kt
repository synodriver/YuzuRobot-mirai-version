package cn.yumetsuki.murasame.feature.sudoadmin

import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.listCommand() {
    sudoAdmin() and content {
        it.removePrefix("sudo ").trim() == "lcmd"
    } quoteReply {
        """
            sudo iban [指令码] 管理员在群内禁用某条对话指令
            sudo iopen [指令码] 管理员在群内启用某条对话指令
            sudo gconf 管理员查看群内配置
            sudo gconf update [--options=arg] 管理员更新群内配置
                --glt 设置群内总回复上限的时间范围，单位为分钟。例: sudo gconf update --glt=3600
                --glc 设置群内总回复上限的次数限制，单位为次数。例: sudo gconf update --glc=500
                --plt 设置群内个人回复上限的时间范围，单位为分钟。例: sudo gconf update --plt=60
                --plc 设置群内个人回复上限的次数限制，单位为次数。例: sudo gconf update --plc=5
                参数可以累加，例如sudo gconf update --glt=3600 --glc=500。若出现重复参数，以最后的参数为准
            sudo lcmd 列出管理员指令
            sudo li 列出可管理对话
            admin permit [群号码] 超级管理员同意群邀请
            admin black [群号码] 超级管理员设置群黑名单
            admin white [群号码] 超级管理员设置群白名单
            admin enable [群号码] 超级管理员解禁群
            admin disable [群号码] 超级管理员禁群
            admin send [群号码] [消息] 超级管理员发送群消息
            admin ublack [qq号] 超级管理员拉黑某个人
            admin uwhite [qq号] 超级管理员把某个人从黑名单去除
        """.trimIndent()
    }
}