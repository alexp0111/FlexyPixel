package ru.alexp0111.flexypixel.di.modules

import dagger.Binds
import dagger.Module
import ru.alexp0111.flexypixel.bluetooth.business.IMessageConverter
import ru.alexp0111.flexypixel.bluetooth.business.MessageConverter
import ru.alexp0111.flexypixel.business.panel_validation.IPanelPositionValidator
import ru.alexp0111.flexypixel.business.panel_validation.PanelPositionValidator
import ru.alexp0111.flexypixel.business.templates.ITemplatesMessageInteractor
import ru.alexp0111.flexypixel.business.templates.TemplatesMessageInteractor
import ru.alexp0111.flexypixel.media.BitmapProcessor
import ru.alexp0111.flexypixel.media.IBitmapProcessor
import ru.alexp0111.flexypixel.media.IMediaFilesProcessor
import ru.alexp0111.flexypixel.media.MediaFilesProcessor
import ru.alexp0111.flexypixel.ui.displayLevel.converter.DisplayLevelConverter
import ru.alexp0111.flexypixel.ui.displayLevel.converter.IDisplayLevelConverter
import ru.alexp0111.flexypixel.ui.templates.converter.ITemplatesConverter
import ru.alexp0111.flexypixel.ui.templates.converter.TemplatesConverter
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.converter.IUpperAbstractionLevelConverter
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.converter.UpperAbstractionLevelConverter

@Module
internal interface BindModule {
    @Binds
    fun bindUpperAbstractionLevelConverter(converter: UpperAbstractionLevelConverter): IUpperAbstractionLevelConverter

    @Binds
    fun bindTemplateConverter(converter: TemplatesConverter): ITemplatesConverter

    @Binds
    fun bindDisplayLevelConverter(converter: DisplayLevelConverter): IDisplayLevelConverter

    @Binds
    fun bindBitmapProcessor(bitmapProcessor: BitmapProcessor): IBitmapProcessor

    @Binds
    fun bindPanelPositionValidator(validator: PanelPositionValidator): IPanelPositionValidator

    @Binds
    fun bindMediaFilesProcessor(processor: MediaFilesProcessor): IMediaFilesProcessor

    @Binds
    fun bindMessageConverter(messageConverter: MessageConverter): IMessageConverter

    @Binds
    fun bindTemplatesMessageInteractor(interactor: TemplatesMessageInteractor): ITemplatesMessageInteractor
}