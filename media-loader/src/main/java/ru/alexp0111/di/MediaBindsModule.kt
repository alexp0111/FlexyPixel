package ru.alexp0111.di

import dagger.Binds
import dagger.Module
import ru.alexp0111.core.di.AppScope
import ru.alexp0111.media_loader.IMediaFilesProcessor
import ru.alexp0111.media_loader.MediaFilesProcessor

@Module
interface MediaBindsModule {
    @AppScope
    @Binds
    fun bindMediaFilesProcessor(processor: MediaFilesProcessor): IMediaFilesProcessor
}