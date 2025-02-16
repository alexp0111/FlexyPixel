package ru.alexp0111.flexypixel.di.modules

import dagger.Binds
import dagger.Module
import ru.alexp0111.flexypixel.business.panel_validation.IPanelPositionValidator
import ru.alexp0111.flexypixel.business.panel_validation.PanelPositionValidator
import ru.alexp0111.flexypixel.media.BitmapProcessor
import ru.alexp0111.flexypixel.media.IBitmapProcessor
import ru.alexp0111.flexypixel.ui.displayLevel.converter.DisplayLevelConverter
import ru.alexp0111.flexypixel.ui.displayLevel.converter.IDisplayLevelConverter
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.converter.IUpperAbstractionLevelConverter
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.converter.UpperAbstractionLevelConverter

@Module
internal interface BindModule {
    @Binds
    fun bindUpperAbstractionLevelConverter(converter: UpperAbstractionLevelConverter): IUpperAbstractionLevelConverter

    @Binds
    fun bindDisplayLevelConverter(converter: DisplayLevelConverter): IDisplayLevelConverter

    @Binds
    fun bindBitmapProcessor(bitmapProcessor: BitmapProcessor): IBitmapProcessor

    @Binds
    fun bindPanelPositionValidator(validator: PanelPositionValidator): IPanelPositionValidator
}