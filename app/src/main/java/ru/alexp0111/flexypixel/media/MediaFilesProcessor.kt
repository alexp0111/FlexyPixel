package ru.alexp0111.flexypixel.media

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import ru.alexp0111.flexypixel.data.model.PanelStatus
import ru.alexp0111.flexypixel.media.model.MediaFileType
import javax.inject.Inject

private const val TAG = "MediaFilesProcessor"

internal class MediaFilesProcessor @Inject constructor(
    private val context: Context,
) : IMediaFilesProcessor {

    override fun getBitmapFromUri(uri: Uri, metaData: Set<PanelMetaData>, onBitmapExtracted: (Bitmap) -> Unit) {
        val bitmapWidth: Int = getBitmapWidth(metaData) ?: return
        val bitmapHeight: Int = getBitmapHeight(metaData) ?: return
        extractImageAsBitmap(uri, bitmapWidth, bitmapHeight, onBitmapExtracted)
    }

    private fun getBitmapWidth(metaData: Set<PanelMetaData>): Int? {
        val selectedPanels = metaData.filter { it.status == PanelStatus.SELECTED }
        val minX = selectedPanels.minOfOrNull { it.absoluteX } ?: return null
        val maxX = selectedPanels.maxOfOrNull { it.absoluteX } ?: return null
        return (maxX - minX + 1) * 8
    }

    private fun getBitmapHeight(metaData: Set<PanelMetaData>): Int? {
        val selectedPanels = metaData.filter { it.status == PanelStatus.SELECTED }
        val minY = selectedPanels.minOfOrNull { it.absoluteY } ?: return null
        val maxY = selectedPanels.maxOfOrNull { it.absoluteY } ?: return null
        return (maxY - minY + 1) * 8
    }

    private fun extractImageAsBitmap(uri: Uri, width: Int, height: Int, onBitmapExtracted: (Bitmap) -> Unit) =
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>(width, height) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    onBitmapExtracted(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) = Unit
            })

    override fun getMediaType(uri: Uri): MediaFileType? {
        val mimeType = getMimeType(uri)
        if (mimeType == null) {
            Log.d(TAG, "mime type cannot be defined")
            return null
        }
        return when (mimeType) {
            MIME_TYPE_GIF -> MediaFileType.GIF
            MIME_TYPE_JPEG -> MediaFileType.IMAGE
            MIME_TYPE_JPG -> MediaFileType.IMAGE
            MIME_TYPE_BMP -> MediaFileType.IMAGE
            MIME_TYPE_PNG -> MediaFileType.IMAGE
            else -> null
        }
    }

    private fun getMimeType(uri: Uri): String? {
        return if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            context.contentResolver.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.lowercase())
        }
    }

    private companion object {
        const val MIME_TYPE_GIF = "image/gif"
        const val MIME_TYPE_JPEG = "image/jpeg"
        const val MIME_TYPE_JPG = "image/jpg"
        const val MIME_TYPE_BMP = "image/bmp"
        const val MIME_TYPE_PNG = "image/png"
    }
}