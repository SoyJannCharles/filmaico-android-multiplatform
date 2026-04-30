package com.jycra.filmaico.data.stream.util.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jycra.filmaico.domain.media.model.stream.DrmKeys

class DrmKeysConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromDrmKeys(value: DrmKeys?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDrmKeys(value: String?): DrmKeys? {
        if (value == null) return null
        val type = object : TypeToken<DrmKeys>() {}.type
        return gson.fromJson(value, type)
    }

}