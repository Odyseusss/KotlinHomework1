package com.mynewproject.repository

import com.mynewproject.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun shareById(id: Long)
    fun likeById(post: Post)
    fun removeById(id: Long)
    fun save(post: Post): Post
}