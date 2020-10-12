package com.trelp.imgur.data.source.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.trelp.imgur.domain.GalleryAlbum
import com.trelp.imgur.domain.GalleryImage
import com.trelp.imgur.domain.GalleryObject
import java.lang.reflect.Type

class GalleryObjectDeserializer : JsonDeserializer<GalleryObject> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GalleryObject = if (json != null && context != null) {
        val jsonObject = json.asJsonObject
        if (jsonObject.get("is_album").asBoolean) {
            context.deserialize(jsonObject, GalleryAlbum::class.java)
        } else {
            context.deserialize(jsonObject, GalleryImage::class.java)
        }
    } else {
        throw JsonParseException("Configure Gson")
    }
}