package cn.yumetsuki.ktorm.expression

import me.liuwj.ktorm.expression.ScalarExpression
import me.liuwj.ktorm.schema.BooleanSqlType
import me.liuwj.ktorm.schema.SqlType

data class RegexpExpression(
    val left: ScalarExpression<*>,
    val right: ScalarExpression<*>,
    override val sqlType: SqlType<Boolean> = BooleanSqlType,
    override val isLeafNode: Boolean = false,
    override val extraProperties: Map<String, Any> = emptyMap()
) : ScalarExpression<Boolean>()