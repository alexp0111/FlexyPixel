package ru.alexp0111.flexypixel.di.modules

import dagger.Binds
import dagger.Module
import ru.alexp0111.flexypixel.business.global_state.convertor.ApiConfigurationConverter
import ru.alexp0111.flexypixel.business.global_state.convertor.IApiConfigurationConverter
import ru.alexp0111.flexypixel.business.global_state.panel_validation.IPanelPositionValidator
import ru.alexp0111.flexypixel.business.global_state.panel_validation.PanelPositionValidator
import ru.alexp0111.flexypixel.business.profile.IProfileInteractor
import ru.alexp0111.flexypixel.business.profile.ProfileInteractor
import ru.alexp0111.flexypixel.business.saved_scheme.ISavedSchemeInteractor
import ru.alexp0111.flexypixel.business.saved_scheme.SavedSchemeInteractor
import ru.alexp0111.flexypixel.business.templates.ITemplatesMessageInteractor
import ru.alexp0111.flexypixel.business.templates.TemplatesMessageInteractor
import ru.alexp0111.flexypixel.media.BitmapProcessor
import ru.alexp0111.flexypixel.media.IBitmapProcessor
import ru.alexp0111.flexypixel.ui.displayLevel.converter.DisplayLevelConverter
import ru.alexp0111.flexypixel.ui.displayLevel.converter.IDisplayLevelConverter
import ru.alexp0111.flexypixel.ui.savedScheme.converter.ISavedSchemeConvertor
import ru.alexp0111.flexypixel.ui.savedScheme.converter.SavedSchemeConvertor
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
    fun bindTemplatesMessageInteractor(interactor: TemplatesMessageInteractor): ITemplatesMessageInteractor

    @Binds
    fun bindProfileInteractor(interactor: ProfileInteractor): IProfileInteractor

    @Binds
    fun bindSavedSchemeConvertor(converter: SavedSchemeConvertor): ISavedSchemeConvertor

    @Binds
    fun bindSavedSchemeInteractor(interactor: SavedSchemeInteractor): ISavedSchemeInteractor

    @Binds
    fun bindApiConfigurationConverter(converter: ApiConfigurationConverter): IApiConfigurationConverter
}