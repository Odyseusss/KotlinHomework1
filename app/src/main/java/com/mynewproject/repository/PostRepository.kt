package com.mynewproject.repository

import androidx.lifecycle.LiveData
import com.mynewproject.dto.Post

interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun share()
}