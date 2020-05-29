package cn.yumetsuki.murasame.repo.dao

import cn.yumetsuki.murasame.repo.entity.ImageRotate
import cn.yumetsuki.murasame.repo.entity.ImageRotates
import kotlinx.coroutines.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.entity.find
import me.liuwj.ktorm.entity.sequenceOf
import me.liuwj.ktorm.entity.toList

interface ImageRotateDao {

    suspend fun findAllImageRotate(): List<ImageRotate>

    suspend fun queryImageRotateByCharacterName(name: String) : ImageRotate?

}

class ImageRotateDaoImpl(
    private val database: Database
): ImageRotateDao {
    override suspend fun findAllImageRotate(): List<ImageRotate> = withContext(Dispatchers.IO) {
        database.sequenceOf(ImageRotates).toList()
    }

    override suspend fun queryImageRotateByCharacterName(name: String): ImageRotate? = withContext(Dispatchers.IO) {
        database.sequenceOf(ImageRotates).find {
            it.characterName eq name
        }
    }
}