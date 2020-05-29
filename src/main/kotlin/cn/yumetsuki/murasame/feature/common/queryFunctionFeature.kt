package cn.yumetsuki.murasame.feature.common

import cn.yumetsuki.mirai.data.withLine
import cn.yumetsuki.murasame.feature.other.recordReplyEvent
import cn.yumetsuki.murasame.listeningfilter.tag
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder
import net.mamoe.mirai.message.data.PlainText

fun GroupMessageSubscribersBuilder.queryFeature(intercepted: Boolean = true) {
    case("./功能") quoteReply {
        recordReplyEvent()
        if (intercepted) intercept()
        PlainText("说明：部分标注限定的功能为特定QQ号/群定制，不在列表中").withLine(
                "1. 抽签功能: @抽签/[at小丛雨]抽签"
        ).withLine(
                "2. 转[角色名], 例: 转七海"
        ).withLine(
                "3. 正则匹配语料库"
        ).withLine(
                "4. [at小丛雨]匹配chatterbot语料库(不触发语料库情况下超过3次不摸头生气，超过8次不回话)(输入 ./对话 查询相关对话"
        ).withLine(
                "5. 资料系统功能：资料、一起吃早饭/午饭/晚饭、打工/锻炼(例如: 打工1 即为打工1小时)、抢钱、打钱(例如: 打钱100 即为给小丛雨打100元钱哦)"
        ).withLine(
                "6. 列出管理员可用指令: sudo lcmd"
        ).withLine(
                "7. [at小丛雨] 状态 查看当前打工、锻炼等事件状态"
        )
    }
}