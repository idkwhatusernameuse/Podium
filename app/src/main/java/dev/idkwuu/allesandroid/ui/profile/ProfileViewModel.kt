package dev.idkwuu.allesandroid.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.idkwuu.allesandroid.api.Repo
import dev.idkwuu.allesandroid.models.AllesUser

class ProfileViewModel : ViewModel() {
    private val repo = Repo()

    fun fetchUser(user: String): LiveData<AllesUser> {
        val mutableData = MutableLiveData<AllesUser>()
        repo.getUser(user).observeForever {
            mutableData.value = it
        }
        return mutableData
    }
}