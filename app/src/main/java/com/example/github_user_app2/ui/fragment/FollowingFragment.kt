package com.example.github_user_app2.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github_user_app2.R
import com.example.github_user_app2.databinding.FragmentFollowBinding
import com.example.github_user_app2.ui.detail.DetailUserActivity
import com.example.github_user_app2.ui.main.UserAdapter

class FollowingFragment: Fragment(R.layout.fragment_follow) {
    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_USERNAME).toString()

        _binding = FragmentFollowBinding.bind(view)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                rvUsername.layoutManager = GridLayoutManager(activity,2)
            } else{
                rvUsername.layoutManager = LinearLayoutManager(activity)
            }
            rvUsername.setHasFixedSize(true)
            rvUsername.adapter = adapter

        }

        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowingViewModel::class.java)
        viewModel.setListFollowing(username)
        viewModel.getListFollowing().observe(viewLifecycleOwner,{
            if(it!=null){
                adapter.setList(it)
                showLoading(false)
            }
        })


    }
    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}