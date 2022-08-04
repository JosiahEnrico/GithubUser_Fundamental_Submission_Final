package com.example.github_user_app2.ui.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github_user_app2.R
import com.example.github_user_app2.data.main.User
import com.example.github_user_app2.databinding.ActivityMainBinding
import com.example.github_user_app2.ui.SettingPreferences
import com.example.github_user_app2.ui.ViewModelFactory
import com.example.github_user_app2.ui.detail.DetailUserActivity
import com.example.github_user_app2.ui.favorite.FavoriteActivity
import com.google.android.material.switchmaterial.SwitchMaterial

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        showEmpty(true)
        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also{
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvUsername.layoutManager = GridLayoutManager(this@MainActivity, 2)
            } else {
                rvUsername.layoutManager = LinearLayoutManager(this@MainActivity)
            }
            rvUsername.setHasFixedSize(true)
            rvUsername.adapter = adapter

            btnSearch.setOnClickListener {
                searchUser()
            }
            etQuery.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

        }
        viewModel.getSearchUsers().observe(this,{
            if(it != null){
                adapter.setList(it)
                showLoading(false)
            }
        })
    }

    private fun searchUser(){
        binding.apply {
            val query = etQuery.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            showEmpty(false)
            viewModel.setSearchUser(query)
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showEmpty(state: Boolean){
        if (state){
            binding.tvEmptyText.visibility = View.VISIBLE
        }else{
            binding.tvEmptyText.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu,menu)
        val itemswitch = menu!!.findItem(R.id.switch_theme2)
        itemswitch.setActionView(R.layout.switch_item)

        val sw = menu.findItem(R.id.switch_theme2).actionView.findViewById(R.id.switch_theme1) as SwitchMaterial
        viewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sw.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sw.isChecked = false
                }
            })

        sw.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
            if(isChecked){
                Toast.makeText(this@MainActivity, "Night Theme Applied", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@MainActivity, "Day Theme Applied", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite_menu ->{
                Intent(this,FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
