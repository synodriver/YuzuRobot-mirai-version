package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.ImageRotate
import cn.yumetsuki.murasame.repo.entity.ImageRotates
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.entity.toList

interface ImageRotateDao {

    suspend fun findAllImageRotate(): List<ImageRotate>

}

class ImageRotateDaoImpl(
    private val database: Database
): ImageRotateDao {
    override suspend fun findAllImageRotate(): List<ImageRotate> = withContext(Dispatchers.IO) {
        database.sequenceOf(ImageRotates).toList()
    }
}