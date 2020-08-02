package dev.idkwuu.allesandroid.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.models.AllesPost

class HomeViewModel : ViewModel() {

    private val timelinePosts = mutableListOf<AllesPost>()
    val timeline = MutableLiveData<List<AllesPost>>()

    init {
        timeline.value = timelinePosts
    }

    fun fetchTimeline(context: Context) {
        Repo().getPosts(context).observeForever {
            timeline.value = it
        }
    }
}