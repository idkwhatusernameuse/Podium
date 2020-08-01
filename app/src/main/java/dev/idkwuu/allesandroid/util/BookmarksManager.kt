package dev.idkwuu.allesandroid.util

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BookmarksManager {
    companion object {
        @Suppress("RemoveExplicitTypeArguments")
        val data: MutableLiveData<MutableList<String>> by lazy {
            val type = object : TypeToken<MutableList<String>>() {}.type
            MutableLiveData(Gson().fromJson<MutableList<String>>(SharedPreferences.bookmarks, type))
        }
    }

    fun save(slug: String) {
        data.value?.add(slug)
        SharedPreferences.bookmarks = Gson().toJson(data.value)
    }

    fun remove(slug: String) {
        data.value?.remove(slug)
        SharedPreferences.bookmarks = Gson().toJson(data.value)
    }
}