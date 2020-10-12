package com.trelp.imgur.di.app

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.trelp.imgur.data.AppSchedulers
import com.trelp.imgur.data.SchedulersProvider
import com.trelp.imgur.data.source.network.deserializer.GalleryObjectDeserializer
import com.trelp.imgur.data.source.network.deserializer.TagsDeserializer
import com.trelp.imgur.domain.GalleryObject
import com.trelp.imgur.domain.Tags
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(GalleryObject::class.java, GalleryObjectDeserializer())
        .registerTypeAdapter(Tags::class.java, TagsDeserializer())
        .create()

    @Provides
    @Singleton
    fun provideSchedulers(): SchedulersProvider = AppSchedulers()
}