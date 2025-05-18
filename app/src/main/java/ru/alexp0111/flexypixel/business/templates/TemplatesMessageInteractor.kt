package ru.alexp0111.flexypixel.business.templates

import ru.alexp0111.bt_service.business.IMessageHandler
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.model.main.Frame
import ru.alexp0111.core.model.main.FrameCycle
import ru.alexp0111.core.model.main.Panel
import ru.alexp0111.core.model.main.PanelConfiguration
import ru.alexp0111.core.model.main.PanelMetaData
import ru.alexp0111.local_repo.database.templates.model.PanelTemplate
import javax.inject.Inject

internal class TemplatesMessageInteractor @Inject constructor(
    private val messageHandler: IMessageHandler,
) : ITemplatesMessageInteractor {
    override fun sendTemplate(template: PanelTemplate) {
        val frame = Frame(
            panels = MutableList(CommonSizeConstants.PANELS_TOTAL_AMOUNT) {
                Panel(template.pixels.map { it.asString() }.toTypedArray())
            }
        )
        val frameCycle = FrameCycle(
            configuration = getConfiguration(template),
            framesAmount = FrameCycle.DEFAULT_FRAMES_AMOUNT,
            interframeDelay = FrameCycle.DEFAULT_INTERFRAME_DELAY,
            frames = mutableListOf(frame)
        )
        messageHandler.sendFrames(frameCycle)
    }

    private fun getConfiguration(template: PanelTemplate): PanelConfiguration =
        PanelConfiguration(
            listOfMetaData = template.orientations.mapIndexed { index, orientation ->
                PanelMetaData(
                    order = index,
                    absoluteX = -1,
                    absoluteY = -1,
                    orientation = orientation,
                )
            }.toMutableSet()
        )
}