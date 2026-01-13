package com.jycra.filmaico.core.common.extensions

import com.google.gson.Gson

inline fun <reified T> Gson.fromJsonList(json: String): List<T> =
    fromJson(json, Array<T>::class.java)?.toList() ?: emptyList()