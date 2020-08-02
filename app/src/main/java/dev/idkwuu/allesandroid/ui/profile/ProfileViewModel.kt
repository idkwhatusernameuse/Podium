package dev.idkwuu.allesandroid.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.models.AllesPost
import dev.idkwuu.allesandroid.models.AllesUser

class ProfileViewModel : ViewModel() {
    private val postsList = mutableListOf<AllesPost>()
    val posts = MutableLiveData<List<AllesPost>>()

    var userInfo = MutableLiveData<AllesUser>()

    init {
        posts.value = postsList
    }

    fun fetchUserInfo(context: Context, user: String) {
        Repo().getUser(context, user).observeForever {
            userInfo.value = it
        }
    }

    fun fetchUserPosts(context: Context, user: String) {
        Repo().getUserPosts(context, user).observeForever {
            posts.value = it
        }
    }
}