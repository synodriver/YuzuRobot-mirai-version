package cn.yumetsuki.murasame.repo.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

interface RegexCorpus : Entity<RegexCorpus> {
    var regex: String
    var response: String
}

object RegexCorpuses : Table<RegexCorpus>("regex_corpus") {
    val regex by varchar("regex").bindTo { it.regex }
    val response by varchar("response").bindTo { it.response }
}