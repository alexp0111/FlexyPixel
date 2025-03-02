package ru.alexp0111.flexypixel.media

import android.graphics.Bitmap
import android.net.Uri
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.media.model.MediaFileType

internal interface IMediaFilesProcessor {
    fun getMediaType(uri: Uri): MediaFileType?
    fun getBitmapFromUri(uri: Uri, metaData: Set<PanelMetaData>, onBitmapExtracted: (Bitmap) -> Unit)
}