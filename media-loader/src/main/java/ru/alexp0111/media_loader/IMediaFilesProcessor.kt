package ru.alexp0111.media_loader

import android.graphics.Bitmap
import android.net.Uri
import ru.alexp0111.core.model.main.PanelMetaData
import ru.alexp0111.model.MediaFileType

interface IMediaFilesProcessor {
    fun getMediaType(uri: Uri): MediaFileType?
    fun getBitmapFromUri(uri: Uri, metaData: Set<PanelMetaData>, onBitmapExtracted: (Bitmap) -> Unit)
    fun getBitmapCycleFromUri(
        uri: Uri,
        metaData: Set<PanelMetaData>,
        onBitmapCycleExtracted: (List<Bitmap>, interframeDelay: Int) -> Unit,
    )
}