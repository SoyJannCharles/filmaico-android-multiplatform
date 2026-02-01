package com.jycra.filmaico.data.media.util.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.core.model.stream.StreamDto

class MediaConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromStringMap(value: Map<String, String>?): String {
        return Gson().toJson(value ?: emptyMap<String, String>())
    }

    @TypeConverter
    fun toStringMap(value: String): Map<String, String> {
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(value, mapType) ?: emptyMap()
    }

    @TypeConverter
    fun fromStreamList(value: List<StreamDto>?): String {
        return gson.toJson(value ?: emptyList<StreamDto>())
    }

    @TypeConverter
    fun toStreamList(value: String): List<StreamDto> {
        val listType = object : TypeToken<List<StreamDto>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

}