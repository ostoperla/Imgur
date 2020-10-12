package com.trelp.imgur.data.source.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.trelp.imgur.domain.Tag
import com.trelp.imgur.domain.Tags
import java.lang.reflect.Type

class TagsDeserializer : JsonDeserializer<Tags> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ) = if (json != null && context != null) {
        val data = json.asJsonObject
        val itemsArray = data.get("items").asJsonArray
        if (itemsArray.size() == 0)
            Tags(emptyList(), null)
        else {
            val tags = mutableListOf<Tag>()
            itemsArray.forEach {
                tags.add(
                    context.deserialize(
                        it.asJsonObject.get("primary").asJsonObject.get("tag"),
                        Tag::class.java
                    )
                )
            }
            data.get("next").run {
                if (isJsonNull) Tags(tags, null) else Tags(tags, asString)
            }
        }
    } else {
        throw JsonParseException("Configure Gson")
    }
}