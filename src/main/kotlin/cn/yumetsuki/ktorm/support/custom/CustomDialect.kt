package cn.yumetsuki.ktorm.support.custom

import cn.yumetsuki.ktorm.expression.RegexpExpression
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.database.SqlDialect
import me.liuwj.ktorm.expression.SqlExpression
import me.liuwj.ktorm.expression.SqlFormatter

open class CustomDialect : SqlDialect {
    override fun createSqlFormatter(database: Database, beautifySql: Boolean, indentSize: Int): SqlFormatter {
        return CustomFormatter(database, beautifySql, indentSize)
    }
}

class CustomFormatter(database: Database, beautifySql: Boolean, indentSize: Int)
    : SqlFormatter(database, beautifySql, indentSize) {

    override fun visitUnknown(expr: SqlExpression): SqlExpression {
        return createVisitor(expr)?.visit()?:super.visitUnknown(expr)
    }

    private fun createVisitor(expr: SqlExpression): Visitor<*>? {
        return when(expr) {
            is RegexpExpression -> RegexpVisitor(expr)
            else -> null
        }
    }

    abstract inner class Visitor<S: SqlExpression>(
        val expr: S
    ) {

        abstract fun visit(): S

    }

    inner class RegexpVisitor(
        expr: RegexpExpression
    ): Visitor<RegexpExpression>(expr) {
        override fun visit(): RegexpExpression {
            if (expr.left.removeBrackets) {
                visit(expr.left)
            } else {
                write("(")
                visit(expr.left)
                removeLastBlank()
                write(") ")
            }

            write("regexp ")

            if (expr.right.removeBrackets) {
                visit(expr.right)
            } else {
                write("(")
                visit(expr.right)
                removeLastBlank()
                write(") ")
            }
            return expr
        }
    }

}