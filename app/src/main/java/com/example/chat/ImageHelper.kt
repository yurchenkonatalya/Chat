package com.example.chat

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Base64
import androidx.core.graphics.scale
import androidx.exifinterface.media.ExifInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.File

class ImageHelper(
    @ApplicationContext
    private val applicationContext: Context
) {
    private val storageDirectory: File =
        ContextWrapper(applicationContext).getDir("imageDir", Context.MODE_PRIVATE)

    fun uriToBitmap(uri: Uri): Bitmap? {
        var stream = applicationContext.contentResolver.openInputStream(uri) ?: return null
        val exif = ExifInterface(stream)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        stream.close()
        stream = applicationContext.contentResolver.openInputStream(uri) ?: return null
        var bitmap = BitmapFactory.decodeStream(stream) ?: return null
        stream.close()
        val rotateDegrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
        if (rotateDegrees != 0) {
            val matrix = Matrix().apply { postRotate(rotateDegrees.toFloat()) }
            bitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width,
                bitmap.height, matrix, true
            )
        }
        return scaleBitmap(bitmap)
    }

    private fun scaleBitmap(bitmap: Bitmap): Bitmap {
        if (bitmap.width > 4000 || bitmap.height > 4000) {
            return if (bitmap.width > bitmap.height)
                bitmap.scale(4000, 3000)
            else
                bitmap.scale(3000, 4000)
        }
        return bitmap
    }

    fun bitmapToBase64(bitmap: Bitmap, compress: Boolean): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        var quality = 100
        if (compress)
            quality = 20
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}