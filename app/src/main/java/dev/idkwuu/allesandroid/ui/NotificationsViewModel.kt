package dev.idkwuu.allesandroid.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.models.AllesPost

class NotificationsViewModel : ViewModel() {
    private val repo = Repo()

    fun fetchMentions(): LiveData<MutableList<AllesPost>> {
        val mutableData = MutableLiveData<MutableList<AllesPost>>()
        repo.getMentions().observeForever {
            mutableData.value = it
        }
        return mutableData
    }
}