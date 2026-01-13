package com.jycra.filmaico.core.network.typeconverter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.jycra.filmaico.domain.stream.model.Stream
import java.lang.reflect.Type

class StreamTypeAdapter : JsonDeserializer<Stream> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Stream? {

        val jsonObject = json?.asJsonObject ?: throw JsonParseException("Json a deserializar es nulo")
        context ?: throw JsonParseException("Contexto de deserialización es nulo")

        // 2. Busca la propiedad "type" que nos dirá qué clase concreta es.
        val type = jsonObject.get("type")?.asString

        // 3. Usa un `when` para decidir qué clase crear.
        return when (type) {
            "direct" -> context.deserialize(jsonObject, Stream.Direct::class.java)
            "webview_scrap" -> context.deserialize(jsonObject, Stream.RegexWebView::class.java)
            "regex_scrap" -> context.deserialize(jsonObject, Stream.RegexScrap::class.java)
            else -> throw JsonParseException("Tipo de Stream desconocido: $type")
        }

    }

}