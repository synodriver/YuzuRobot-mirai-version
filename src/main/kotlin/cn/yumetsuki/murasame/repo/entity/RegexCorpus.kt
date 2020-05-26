package cn.yumetsuki.murasame.repo.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar

interface RegexCorpus : Entity<RegexCorpus> {
    var regex: String
    var response: String
    var startTime: Long?
    var endTime: Long?
}

object RegexCorpuses : Table<RegexCorpus>("regex_corpuses") {
    val regex by varchar("regex").bindTo { it.regex }
    val response by varchar("response").bindTo { it.response }
    val startTime by long("start_time").bindTo { it.startTime }
    val endTime by long("end_time").bindTo { it.endTime }
}