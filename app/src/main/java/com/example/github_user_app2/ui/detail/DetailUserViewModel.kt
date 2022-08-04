package com.example.github_user_app2.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github_user_app2.api.Client
import com.example.github_user_app2.data.local.FavoriteUser
import com.example.github_user_app2.data.local.FavoriteUserDao
import com.example.github_user_app2.data.local.UserDatabase
import com.example.github_user_app2.data.main.DetailResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application): AndroidViewModel(application){
    val user = MutableLiveData<DetailResponse>()

    private var userDao: FavoriteUserDao?
    private val userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun setUserDetail(username: String) {
        Client.apiInstance
            .getUserDetail(username)
            .enqueue(object : Callback<DetailResponse> {
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if(response.isSuccessful){
                    user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    Log.d("Failure", t.message!!)
                }
            })
    }

    fun getUserDetail(): LiveData<DetailResponse>{
        return user
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String){
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(
                username,
                id,
                avatarUrl
            )
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id:Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }
}