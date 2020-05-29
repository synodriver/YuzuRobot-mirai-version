package cn.yumetsuki.murasame.feature.sudoadmin

import cn.yumetsuki.murasame.repo.dao.InstructionDao
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.listInstruction() {

    val instructionDao: InstructionDao by globalKoin().inject()

    sudoAdmin() and content {
        it.removePrefix("sudo ").trim() == "li"
    } quoteReply {
        quoteReply(
            instructionDao.queryInstructions().joinToString("\n") {
                "${it.tag}: ${it.description}"
            }.let { format ->
                "可管理功能: \n$format"
            }
        )
        Unit
    }
}