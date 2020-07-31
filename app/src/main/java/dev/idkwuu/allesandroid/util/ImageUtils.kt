@file:Suppress("DEPRECATION")

package dev.idkwuu.allesandroid.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import java.io.*

object ImageUtils {
    @Throws(IllegalArgumentException::class)
    fun convertToBitmap(base64Str: String): Bitmap {
        val decodedBytes = Base64.decode(
            base64Str.substring(base64Str.indexOf(",") + 1),
            Base64.DEFAULT
        )
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    fun convertToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return Base64.encodeToString(
            outputStream.toByteArray(),
            Base64.DEFAULT
        )
    }

    fun saveImage(context: Context, bitmap: Bitmap, name: String? = System.currentTimeMillis().toString()): Boolean {
        try {
            lateinit var fileOutputStream: OutputStream
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues()
                contentValues.run {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/Podium")
                }
                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fileOutputStream = resolver.openOutputStream(imageUri!!)!!
            } else {
                val imagesDir = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}/Podium"
                val dir = File(imagesDir)
                if (!dir.exists()) dir.mkdir()
                val image = File(imagesDir, "$name.png")
                fileOutputStream = FileOutputStream(image)
            }
            val saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            return saved
        } catch (e: IOException) {
            return false
        }
    }
}