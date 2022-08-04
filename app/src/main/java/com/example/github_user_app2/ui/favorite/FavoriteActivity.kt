package com.example.github_user_app2.ui.favorite

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github_user_app2.R
import com.example.github_user_app2.data.local.FavoriteUser
import com.example.github_user_app2.data.main.User
import com.example.github_user_app2.databinding.ActivityFavoriteBinding
import com.example.github_user_app2.ui.detail.DetailUserActivity
import com.example.github_user_app2.ui.main.MainViewModel
import com.example.github_user_app2.ui.main.UserAdapter

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //showEmpty(true)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: User) {
                Intent(this@FavoriteActivity, DetailUserActivity::class.java).also{
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvUsername.setHasFixedSize(true)
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvUsername.layoutManager = GridLayoutManager(this@FavoriteActivity, 2)
            } else {
                rvUsername.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            }
            rvUsername.adapter = adapter
        }

        viewModel.getFavoriteUser()?.observe(this,{
            if(it!=null){
                val list = mapList(it)
                adapter.setList(list)
            }
        })
    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<User> {
        val listUsers = ArrayList<User>()
        for (user in users){
            showEmpty(false)
            val userMapped = User(
                user.login,
                user.id,
                user.avatar_url
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }

    private fun showEmpty(state: Boolean){
        if (state){
            binding.tvEmptyText.visibility = View.VISIBLE
        }else{
            binding.tvEmptyText.visibility = View.GONE
        }
    }
}