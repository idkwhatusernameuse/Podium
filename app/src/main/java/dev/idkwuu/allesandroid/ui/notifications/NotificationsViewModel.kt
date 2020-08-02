package dev.idkwuu.allesandroid.ui.notifications

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.models.AllesPost

class NotificationsViewModel: ViewModel() {

    private val notificationsList = mutableListOf<AllesPost>()
    val notifications = MutableLiveData<List<AllesPost>>()

    init {
        notifications.value = notificationsList
    }

    fun fetchTimeline(context: Context) {
        Repo().getMentions(context).observeForever {
            notifications.value = it
        }
    }

}