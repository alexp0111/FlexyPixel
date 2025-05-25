package ru.alexp0111.flexypixel.business.global_state.convertor

import ru.alexp0111.core.model.DrawingColor
import ru.alexp0111.core.model.main.Frame
import ru.alexp0111.core.model.main.FrameCycle
import ru.alexp0111.core.model.main.Panel
import ru.alexp0111.core.model.main.PanelConfiguration
import ru.alexp0111.core.model.main.PanelMetaData
import ru.alexp0111.core.model.main.PanelOrientation
import ru.alexp0111.local_repo.database.schemes.data.UserSavedScheme
import ru.alexp0111.remote_repo.scheme.model.ApiConfiguration
import ru.alexp0111.remote_repo.scheme.model.ApiFrameInfo
import ru.alexp0111.remote_repo.scheme.model.ApiPaletteItem
import ru.alexp0111.remote_repo.scheme.model.ApiPanelsInfo
import java.util.UUID
import javax.inject.Inject

class ApiConfigurationConverter @Inject constructor() : IApiConfigurationConverter {

    override fun convertApiToUserSavedScheme(
        apiConfiguration: ApiConfiguration,
        createdAt: String,
        preview: String?
    ): UserSavedScheme = UserSavedScheme(
        id = apiConfiguration.publicId ?: UUID.randomUUID().toString(),
        title = apiConfiguration.name ?: "",
        createdAt = createdAt,
        preview = preview,
        frameCycle = apiConfiguration.let(::convertApiToCycle)
    )

    override fun convertApiToCycle(apiConfiguration: ApiConfiguration): FrameCycle {
        val panelConfiguration = toPanelConfig(apiConfiguration.panelsInfo)
        val frames = toPanelFrames(apiConfiguration.frameInfo)

        return FrameCycle(
            configuration = panelConfiguration,
            frames = frames,
            framesAmount = frames.size,
            interframeDelay = apiConfiguration.interFrameDelay ?: 100,
        )
    }

    private fun toPanelConfig(apiPanelInfo: List<ApiPanelsInfo>): PanelConfiguration =
        PanelConfiguration(
            buildSet {
                apiPanelInfo.forEach {
                    val panelMetaData = PanelMetaData(
                        order = it.uid.toInt() - 1,
                        absoluteX = it.x,
                        absoluteY = it.y,
                        orientation = directionToOrientation(it.direction),
                        palette = if (it.palette.isNotEmpty()) cyclePaletteFromApi(it.palette) else PanelMetaData.getDefaultPalette()
                    )
                    add(panelMetaData)
                }
            }.toMutableSet()
        )

    private fun directionToOrientation(direction: String): PanelOrientation =
        when (direction) {
            "left" -> PanelOrientation.LEFT
            "right" -> PanelOrientation.RIGHT
            "top" -> PanelOrientation.UP
            else -> PanelOrientation.DOWN
        }

    private fun cyclePaletteFromApi(apiPalette: List<ApiPaletteItem>): MutableList<DrawingColor> =
        buildList {
            apiPalette.forEach {
                val drawingColor = DrawingColor(it.r, it.g, it.b)
                add(drawingColor)
            }
        }.toMutableList()

    private fun toPanelFrames(apiFrameList: List<ApiFrameInfo>): MutableList<Frame> =
        buildList {
            apiFrameList.forEach {
                val frame = toPanelFrame(it)
                add(frame)
            }
        }.toMutableList()

    private fun toPanelFrame(apiFrame: ApiFrameInfo): Frame {
        val cleanString = apiFrame.panelPixelColors
            .replace("\\", "")
            .replace("\"", "")
            .replace("{", "")
            .replace("}", "")
            .replace("[", "")
            .replace("]", "")
        var pixelString = ""
        cleanString.forEach {
            pixelString = if (it == ':') pixelString.dropLast(1) else pixelString + it
        }
        val panels = pixelString.split(',').windowed(64, 64) {
            Panel(it.toTypedArray())
        }.toMutableList()

        return Frame(panels)
    }

    override fun convertCycleToApi(cycle: FrameCycle): ApiConfiguration {
        TODO("Not yet implemented")
    }
}