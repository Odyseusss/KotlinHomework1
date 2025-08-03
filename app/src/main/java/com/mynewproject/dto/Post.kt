package com.mynewproject.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likes: Int = 0,
    var likedByMe: Boolean = false,
    var shares: Int = 0,
    var sharedByMe: Boolean = false,
)
