package com.mynewproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mynewproject.dto.Post
import com.mynewproject.repository.PostRepository
import com.mynewproject.repository.PostRepositoryInMemory

class PostViewModel: ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemory()
    val data: LiveData<Post> = repository.get()
    fun like() = repository.like()
    fun share() = repository.share()

}