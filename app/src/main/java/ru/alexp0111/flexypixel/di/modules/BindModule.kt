package ru.alexp0111.flexypixel.di.modules

import dagger.Binds
import dagger.Module
import ru.alexp0111.flexypixel.media.BitmapProcessor
import ru.alexp0111.flexypixel.media.IBitmapProcessor
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.converter.IUpperAbstractionLevelConverter
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.converter.UpperAbstractionLevelConverter

@Module
interface BindModule {
    @Binds
    fun bindUpperAbstractionLevelConverter(converter: UpperAbstractionLevelConverter): IUpperAbstractionLevelConverter

    @Binds
    fun bindBitmapProcessor(bitmapProcessor: BitmapProcessor): IBitmapProcessor
}