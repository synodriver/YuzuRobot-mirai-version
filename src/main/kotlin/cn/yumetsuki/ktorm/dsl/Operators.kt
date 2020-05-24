package cn.yumetsuki.ktorm.dsl

import cn.yumetsuki.ktorm.expression.RegexpExpression
import me.liuwj.ktorm.expression.ArgumentExpression
import me.liuwj.ktorm.expression.SqlFormatter
import me.liuwj.ktorm.schema.BooleanSqlType
import me.liuwj.ktorm.schema.ColumnDeclaring
import me.liuwj.ktorm.schema.VarcharSqlType

infix fun ColumnDeclaring<*>.regexp(argument: String): RegexpExpression {
    return this regexp ArgumentExpression(argument, VarcharSqlType)
}

infix fun ColumnDeclaring<*>.regexp(expr: ColumnDeclaring<String>): RegexpExpression {
    return RegexpExpression(asExpression(), expr.asExpression(), BooleanSqlType)
}

infix fun String.regexp(expr: ColumnDeclaring<String>): RegexpExpression {
    return ArgumentExpression(this, VarcharSqlType) regexp expr
}