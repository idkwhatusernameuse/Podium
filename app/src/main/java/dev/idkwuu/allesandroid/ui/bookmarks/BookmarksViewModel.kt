package dev.idkwuu.allesandroid.ui.bookmarks

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.models.AllesPost

class BookmarksViewModel : ViewModel()  {
    private val bookmarksList = mutableListOf<AllesPost>()
    val bookmarks = MutableLiveData<List<AllesPost>>()

    init {
        bookmarks.value = bookmarksList
    }

    fun fetchBookmarks(context: Context) {
        Repo().getBookmarks(context).observeForever {
            bookmarks.value = it
        }
    }
}