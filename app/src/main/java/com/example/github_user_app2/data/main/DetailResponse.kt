package com.example.github_user_app2.data.main

data class DetailResponse(
    val login : String,
    val id: Int,
    val avatar_url: String,
    val followers: Int,
    val following: Int,
    val public_repos: Int,
    val name: String,
    val followers_url: String,
    val following_url: String,
    val company: String,
    val location: String
)
