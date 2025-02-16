package ru.alexp0111.flexypixel.business.panel_validation

import ru.alexp0111.core_ui.common.model.Coordinate
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.data.model.PanelOrientation
import ru.alexp0111.flexypixel.data.model.PanelStatus
import javax.inject.Inject
import kotlin.math.abs

/**
 * This class goes through panels and updates:
 * 1. **Status** - is panel placed correct or wrong
 * 2. **Orientation** - resolves what orientation panel currently in
 * */
internal class PanelPositionValidator @Inject constructor() : IPanelPositionValidator {

    override fun validate(panels: Set<PanelMetaData>): Set<PanelMetaData> {
        val orderedPanelMap = panels.associateBy { it.order }.toMutableMap()
        var orderOfChainBrokenPanel: Int? = null

        for (order in orderedPanelMap.keys.sorted()) {
            val previousPanel = orderedPanelMap[order - 1]
            val nextPanel = orderedPanelMap[order + 1]

            orderedPanelMap[order] = when {
                previousPanel == null -> handleRootPanel(orderedPanelMap, order)
                nextPanel == null -> handleTailPanel(orderedPanelMap, order)
                else -> handleMidPanel(orderedPanelMap, order)
            }

            // Detect if panel chain is broken
            if (orderedPanelMap[order]!!.status == PanelStatus.PLACED_WRONG) {
                orderOfChainBrokenPanel = order
                break
            }
        }

        // If any one panel is placed wrong -> all other panels with upper order is also placed wrong
        if (orderOfChainBrokenPanel != null) {
            for (order in orderOfChainBrokenPanel..orderedPanelMap.keys.max()) {
                orderedPanelMap[order] = orderedPanelMap[order]!!.copy(
                    status = PanelStatus.PLACED_WRONG,
                    orientation = PanelOrientation.LEFT
                )
            }
        }

        return orderedPanelMap.values.toSet()
    }

    private fun handleRootPanel(orderedPanelMap: Map<Int, PanelMetaData>, order: Int): PanelMetaData {
        val currentPanel = requireNotNull(orderedPanelMap[order])
        val nextPanel = orderedPanelMap[order + 1]

        val newStatus = when(currentPanel.status) {
            PanelStatus.PLACED_WRONG -> PanelStatus.DEFAULT
            else -> currentPanel.status
        }
        val newOrientation = if (nextPanel == null) {
            PanelOrientation.LEFT
        } else {
            resolveOrientation(
                next = Coordinate(nextPanel.absoluteX, nextPanel.absoluteY),
                current = Coordinate(currentPanel.absoluteX, currentPanel.absoluteY)
            )
        }

        return currentPanel.copy(
            status = newStatus,
            orientation = newOrientation
        )
    }

    private fun handleMidPanel(orderedPanelMap: Map<Int, PanelMetaData>, order: Int): PanelMetaData {
        val previousPanel = requireNotNull(orderedPanelMap[order - 1])
        val currentPanel = requireNotNull(orderedPanelMap[order])
        val nextPanel = requireNotNull(orderedPanelMap[order + 1])

        val isConnected = isConnected(
            head = Coordinate(currentPanel.absoluteX, currentPanel.absoluteY),
            tail = Coordinate(previousPanel.absoluteX, previousPanel.absoluteY)
        )
        val newStatus = resolveStatus(
            isConnected = isConnected,
            previousStatus = currentPanel.status
        )
        val newOrientation = resolveOrientation(
            next = Coordinate(nextPanel.absoluteX, nextPanel.absoluteY),
            current = Coordinate(currentPanel.absoluteX, currentPanel.absoluteY)
        )
        return currentPanel.copy(
            status = newStatus,
            orientation = newOrientation
        )
    }

    private fun handleTailPanel(orderedPanelMap: Map<Int, PanelMetaData>, order: Int): PanelMetaData {
        val currentPanel = requireNotNull(orderedPanelMap[order])
        val previousPanel = requireNotNull(orderedPanelMap[order - 1])

        val isConnected = isConnected(
            head = Coordinate(currentPanel.absoluteX, currentPanel.absoluteY),
            tail = Coordinate(previousPanel.absoluteX, previousPanel.absoluteY)
        )
        val newStatus = resolveStatus(
            isConnected = isConnected,
            previousStatus = currentPanel.status
        )
        val newOrientation = resolveTailOrientation(
            current = Coordinate(currentPanel.absoluteX, currentPanel.absoluteY),
            previous = Coordinate(previousPanel.absoluteX, previousPanel.absoluteY)
        )
        return currentPanel.copy(
            status = newStatus,
            orientation = newOrientation
        )
    }

    private fun isConnected(head: Coordinate, tail: Coordinate): Boolean {
        val placedVertical = tail.x == head.x && (abs(head.y - tail.y) == 1)
        val placedHorizontal = tail.y == head.y && (abs(head.x - tail.x) == 1)
        return placedVertical || placedHorizontal
    }

    private fun resolveStatus(isConnected: Boolean, previousStatus: PanelStatus): PanelStatus =
        when {
            !isConnected -> PanelStatus.PLACED_WRONG
            previousStatus == PanelStatus.SELECTED -> PanelStatus.SELECTED
            else -> PanelStatus.DEFAULT
        }

    private fun resolveOrientation(next: Coordinate, current: Coordinate): PanelOrientation =
        when {
            next.y == current.y && next.x < current.x -> PanelOrientation.LEFT
            next.y == current.y && next.x > current.x -> PanelOrientation.RIGHT
            next.x == current.x && next.y < current.y -> PanelOrientation.UP
            next.x == current.x && next.y > current.y -> PanelOrientation.DOWN
            else -> PanelOrientation.LEFT
        }

    private fun resolveTailOrientation(current: Coordinate, previous: Coordinate): PanelOrientation =
        when {
            current.y == previous.y && current.x > previous.x -> PanelOrientation.RIGHT
            else -> PanelOrientation.LEFT
        }
}