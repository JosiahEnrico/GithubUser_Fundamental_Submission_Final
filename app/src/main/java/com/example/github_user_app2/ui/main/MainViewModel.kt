package com.example.github_user_app2.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.example.github_user_app2.api.Client
import com.example.github_user_app2.data.main.User
import com.example.github_user_app2.data.main.UserResponse
import com.example.github_user_app2.ui.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences): ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setSearchUser(query: String){
        Client.apiInstance
            .getSearchUsers(query)
            .enqueue(object: Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful){
                        listUsers.postValue(response.body()?.items)
                    }
                }
                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("Failure", t.message!!)
                }

            })
    }

    fun getSearchUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

}