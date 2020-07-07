package dev.idkwuu.allesandroid.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.models.AllesPost

class HomeViewModel : ViewModel() {
    private val repo = Repo()

    fun fetchPosts(): LiveData<MutableList<AllesPost>> {
        val mutableData = MutableLiveData<MutableList<AllesPost>>()
        repo.getPosts().observeForever {
            mutableData.value = it
        }
        return mutableData
    }
}