package ru.alexp0111.flexypixel.ui.displayLevel.screen

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import ru.alexp0111.core_ui.common.model.Coordinate

private const val DRAG_AND_DROP_SCALE_RATIO = 1.2f
private const val DRAG_AND_DROP_ALPHA = 0.8f

private class DraggableItemInfo {
    var dndObject by mutableStateOf<(@Composable (Int) -> Unit)?>(null)
    var dndObjectSize by mutableIntStateOf(0)
    var isDragging: Boolean by mutableStateOf(false)
    var dndStartOffset by mutableStateOf(Offset.Zero)
    var dndCurrentOffset by mutableStateOf(Offset.Zero)
    var targetOffsetMap = mutableStateMapOf<Coordinate, Rect>()
}

private val LocalDraggableItemInfo = compositionLocalOf { DraggableItemInfo() }

@Composable
internal fun DragAndDropStateHandler(
    content: @Composable BoxScope.() -> Unit,
) {
    val state = remember { DraggableItemInfo() }
    CompositionLocalProvider(LocalDraggableItemInfo provides state) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
            if (state.isDragging) {
                DraggableComposableDecoration(state)
            }
        }
    }
}

@Composable
private fun DraggableComposableDecoration(state: DraggableItemInfo) {
    var targetSize by remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = Modifier
            .graphicsLayer {
                val offset = (state.dndStartOffset + state.dndCurrentOffset)
                scaleX = DRAG_AND_DROP_SCALE_RATIO
                scaleY = DRAG_AND_DROP_SCALE_RATIO
                alpha = DRAG_AND_DROP_ALPHA
                translationX = offset.x - (targetSize.width / 2)
                translationY = offset.y - targetSize.height
            }
            .onGloballyPositioned {
                targetSize = it.size
            }
    ) {
        state.dndObject?.invoke(state.dndObjectSize)
    }
}

@Composable
internal fun DragAndDropTarget(
    modifier: Modifier = Modifier,
    coordinate: Coordinate,
    content: @Composable (() -> Unit),
) {
    val currentState = LocalDraggableItemInfo.current
    Box(
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                currentState.dndObjectSize = layoutCoordinates.size.height
                layoutCoordinates
                    .boundsInWindow()
                    .let { rect -> currentState.targetOffsetMap[coordinate] = rect }
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
internal fun DraggableView(
    modifier: Modifier = Modifier,
    onDragStart: () -> Unit = {},
    onDragEnd: (Coordinate?) -> Unit = {},
    onClick: () -> Unit = {},
    content: @Composable ((Int) -> Unit),
) {
    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    val currentState = LocalDraggableItemInfo.current

    Box(modifier = modifier
        .onGloballyPositioned { layoutCoordinates ->
            currentPosition = layoutCoordinates.localToWindow(Offset.Zero)
        }
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = { startOffset ->
                    onDragStart.invoke()
                    currentState.isDragging = true
                    currentState.dndStartOffset = currentPosition + startOffset
                    currentState.dndObject = content
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    currentState.dndCurrentOffset += Offset(dragAmount.x, dragAmount.y)
                },
                onDragEnd = {
                    val offset = currentState.dndStartOffset + currentState.dndCurrentOffset
                    val targetOffsetMap = currentState.targetOffsetMap.toMap()
                    val targetCoordinate = extractCoordinateByOffset(targetOffsetMap, offset)
                    onDragEnd.invoke(targetCoordinate)
                    currentState.isDragging = false
                    currentState.dndCurrentOffset = Offset.Zero
                },
                onDragCancel = {
                    currentState.dndCurrentOffset = Offset.Zero
                    currentState.isDragging = false
                }
            )
        }
        .pointerInput(Unit) {
            detectTapGestures {
                onClick.invoke()
            }
        }
    ) {
        content(currentState.dndObjectSize)
    }
}

private fun extractCoordinateByOffset(offsetMap: Map<Coordinate, Rect>, offset: Offset): Coordinate? =
    offsetMap.filter { (_, rect) -> rect.contains(offset) }.toList().firstOrNull()?.first