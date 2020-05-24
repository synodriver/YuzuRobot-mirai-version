package cn.yumetsuki.murasame.repo.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.varchar

interface ImageRotate: Entity<ImageRotate> {

    companion object: Entity.Factory<ImageRotate>() {
        fun new(
            characterName: String,
            imgUrl: String
        ): ImageRotate = ImageRotate {
            this@ImageRotate.characterName = characterName
            this@ImageRotate.imgUrl = imgUrl
        }
    }

    var characterName: String
    var imgUrl: String
}

object ImageRotates: Table<ImageRotate>("image_rotate") {

    val characterName by varchar("character_name").bindTo { it.characterName }
    val imgUrl by varchar("img_url").bindTo { it.imgUrl }
}