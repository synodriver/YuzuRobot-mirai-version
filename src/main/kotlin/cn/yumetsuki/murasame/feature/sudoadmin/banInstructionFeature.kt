package cn.yumetsuki.murasame.feature.sudoadmin

import cn.yumetsuki.murasame.repo.dao.InstructionDao
import cn.yumetsuki.murasame.repo.dao.InstructionGroupBanRuleDao
import cn.yumetsuki.murasame.repo.entity.InstructionGroupBanRule
import cn.yumetsuki.util.globalKoin
import net.mamoe.mirai.event.GroupMessageSubscribersBuilder

fun GroupMessageSubscribersBuilder.banInstruction() {

    val instructionDao: InstructionDao by globalKoin().inject()

    val instructionGroupBanRuleDao: InstructionGroupBanRuleDao by globalKoin().inject()

    sudoAdmin() and content {
        it.removePrefix("sudo ").startsWith("iban")
    } quoteReply {
        val msg = it.removePrefix("sudo iban ").trim().let { tag ->
            instructionDao.queryInstructionByTag(tag)?.let { instruction ->
                instructionGroupBanRuleDao.queryBanRuleByTagAndGroupId(
                        tag, group.id
                )?:instructionGroupBanRuleDao.addBanRule(InstructionGroupBanRule {
                    this.instructionTag = tag
                    this.groupId = group.id
                })
                "禁用[${instruction.description}]成功啦～～～"
            }?:"诶...这个指令好像不存在诶...可以用: sudo li 看一看哦～"
        }
        quoteReply(msg)
        Unit
    }
}