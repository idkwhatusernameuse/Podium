package dev.idkwuu.allesandroid.util

import android.content.Context
import com.google.gson.Gson
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.models.License
import dev.idkwuu.allesandroid.models.LicensesList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter

class LicenseExtractor {
    fun getResource(context: Context, id: Int): String {
        val inputStream = context.resources.openRawResource(id)
        val writer = StringWriter()
        val buffer = CharArray(1024)
        inputStream.use { iS ->
            val reader = BufferedReader(InputStreamReader(iS, "UTF-8"))
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        }

        return writer.toString()
    }

    fun getLicenses(context: Context): List<License> {
        return Gson().fromJson(getResource(context, R.raw.licenses), LicensesList::class.java).list
    }
}
