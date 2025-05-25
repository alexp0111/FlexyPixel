package ru.alexp0111.flexypixel.business.global_state.convertor

import ru.alexp0111.core.model.main.FrameCycle
import ru.alexp0111.remote_repo.scheme.model.ApiConfiguration

interface IApiConfigurationConverter {
    fun convertApiToCycle(apiConfiguration: ApiConfiguration): FrameCycle
    fun convertCycleToApi(cycle: FrameCycle): ApiConfiguration
}