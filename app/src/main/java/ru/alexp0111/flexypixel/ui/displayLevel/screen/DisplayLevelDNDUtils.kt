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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

private class DraggableItemInfo {
    var isDragging: Boolean by mutableStateOf(false)
    var dragStartOffset by mutableStateOf(Offset.Zero)
    var dragCurrentOffset by mutableStateOf(Offset.Zero)
    var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
}

private val LocalDraggableItemInfo = compositionLocalOf { DraggableItemInfo() }

@Composable
internal fun DragAndDropStateHandler(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val state = remember { DraggableItemInfo() }
    CompositionLocalProvider(LocalDraggableItemInfo provides state) {
        Box(modifier = modifier.fillMaxSize()) {
            content()
            if (state.isDragging) {
                var targetSize by remember { mutableStateOf(IntSize.Zero) }
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            val offset = (state.dragStartOffset + state.dragCurrentOffset)
                            scaleX = 1.3f
                            scaleY = 1.3f
                            alpha = if (targetSize == IntSize.Zero) 0f else 0.9f
                            translationX = offset.x.minus(targetSize.width / 2)
                            translationY = offset.y.minus(targetSize.height)
                        }
                        .onGloballyPositioned {
                            targetSize = it.size
                        }
                ) {
                    state.draggableComposable?.invoke()
                }
            }
        }
    }
}

@Composable
internal fun DraggableView(
    modifier: Modifier = Modifier,
    onDragStart: () -> Unit = {},
    onDragEnd: (Offset) -> Unit = {},
    onClick: () -> Unit = {},
    content: @Composable (() -> Unit),
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
                    currentState.dragStartOffset = currentPosition + startOffset
                    currentState.draggableComposable = content
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    currentState.dragCurrentOffset += Offset(dragAmount.x, dragAmount.y)
                },
                onDragEnd = {
                    onDragEnd.invoke(currentState.dragStartOffset + currentState.dragCurrentOffset)
                    currentState.isDragging = false
                    currentState.dragCurrentOffset = Offset.Zero
                },
                onDragCancel = {
                    currentState.dragCurrentOffset = Offset.Zero
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
        content()
    }
}