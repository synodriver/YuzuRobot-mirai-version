package cn.yumetsuki.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

@Suppress("ObjectPropertyName")
val _utilObjectMapper = ObjectMapper().registerKotlinModule().apply {
    propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
    enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
    enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
}

inline fun <reified T> String.fromJson(): T {
    return _utilObjectMapper.readValue(this)
}

inline fun <reified T> Map<String, Any?>.fromJson(): T {
    return _utilObjectMapper.convertValue(this)
}

fun <T> T.toJson(): String {
    return _utilObjectMapper.writeValueAsString(this)
}