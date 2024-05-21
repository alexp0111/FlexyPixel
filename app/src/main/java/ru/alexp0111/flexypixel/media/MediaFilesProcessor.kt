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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.alexp0111.flexypixel.data.model.PanelMetaData
import javax.inject.Inject

private const val TAG = "MediaFilesProcessor"
private const val MIME_TYPE_GIF = "image/gid"

class MediaFilesProcessor @Inject constructor(
    private val context: Context,
) {

    private var _bitmaps = MutableSharedFlow<List<Bitmap>>(replay = 1)
    val bitmaps: SharedFlow<List<Bitmap>>
        get() = _bitmaps.asSharedFlow()

    fun handleIncomingMedia(
        fileUri: Uri,
        goalPanels: List<PanelMetaData>,
    ) {
        val mimeType = getMimeType(fileUri)
        if (mimeType == null) {
            Log.d(TAG, "mime type cannot be defined")
            return
        }
        val bitmapWidthToHeight: Pair<Int, Int> = getBitmapSize(goalPanels) ?: return
        when (mimeType) {
            MIME_TYPE_GIF -> {
                // extractGif()
            }

            else -> {
                extractImageAsBitmap(
                    fileUri,
                    bitmapWidthToHeight.first,
                    bitmapWidthToHeight.second,
                )
            }
        }
    }

    private fun getBitmapSize(goalPanels: List<PanelMetaData>): Pair<Int, Int>? {
        val panelsInUnifiedFormat: List<Pair<Int, Int>> = unify(goalPanels)

        val minX = panelsInUnifiedFormat.minOfOrNull { it.first } ?: return null
        val maxX = panelsInUnifiedFormat.maxOfOrNull { it.first } ?: return null
        val minY = panelsInUnifiedFormat.minOfOrNull { it.second } ?: return null
        val maxY = panelsInUnifiedFormat.maxOfOrNull { it.second } ?: return null

        val widthInPixels = (maxX - minX + 1) * 8
        val heightInPixels = (maxY - minY + 1) * 8

        return Pair(widthInPixels, heightInPixels)
    }

    /**
     * Handling TYPE_256 panels. They represented as 4 panels with coordinates:
     * [x, y]   [x+1, y]
     * [x, y+1] [x+1, y+1]
     * */
    private fun unify(goalPanels: List<PanelMetaData>): List<Pair<Int, Int>> {
        return mutableListOf<Pair<Int, Int>>().apply {
            goalPanels.forEach {
                if (it.type == PanelMetaData.TYPE_64) {
                    add(Pair(it.absoluteX, it.absoluteY))
                } else {
                    add(Pair(it.absoluteX, it.absoluteY))
                    add(Pair(it.absoluteX + 1, it.absoluteY))
                    add(Pair(it.absoluteX, it.absoluteY + 1))
                    add(Pair(it.absoluteX + 1, it.absoluteY + 1))
                }
            }
        }
    }

    private fun extractImageAsBitmap(uri: Uri, width: Int, height: Int) {
        // TODO: Check if we not loading data on UI thread
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>(width, height) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?,
                ) {
                    _bitmaps.tryEmit(listOf(resource))
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }
            })
    }

    private fun getMimeType(uri: Uri): String? {
        return if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            context.contentResolver.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.lowercase())
        }
    }
}