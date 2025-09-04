package com.mynewproject.repository

import androidx.lifecycle.LiveData
import com.mynewproject.dto.Post

interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun shareById(id: Long)
    fun likeById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
}