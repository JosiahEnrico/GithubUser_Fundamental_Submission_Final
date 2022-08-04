package com.example.github_user_app2.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.github_user_app2.data.local.FavoriteUser
import com.example.github_user_app2.data.local.FavoriteUserDao
import com.example.github_user_app2.data.local.UserDatabase
import com.example.github_user_app2.data.main.DetailResponse

class FavoriteViewModel (application: Application):AndroidViewModel(application){

    private var userDao: FavoriteUserDao?
    private val userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return userDao?.getFavoriteUser()
    }

}