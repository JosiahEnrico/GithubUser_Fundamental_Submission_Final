package com.example.github_user_app2.ui.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github_user_app2.api.Client
import com.example.github_user_app2.data.main.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel(){
    val listFollowing = MutableLiveData<ArrayList<User>>()

    fun setListFollowing(username: String){
        Client.apiInstance
            .getFollowing(username)
            .enqueue(object : Callback<ArrayList<User>>{
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if(response.isSuccessful){
                        listFollowing.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d("Failure", t.message!!)
                }

            })
    }

    fun getListFollowing(): LiveData<ArrayList<User>>{
        return listFollowing
    }


}