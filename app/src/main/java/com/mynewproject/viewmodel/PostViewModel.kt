package com.mynewproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mynewproject.dto.Post
import com.mynewproject.repository.PostRepository
import com.mynewproject.repository.PostRepositorySQLite
import kotlin.String

private val empty = Post(
    id = 0,
    author = "",
    published = "",
    content = "",
    likes = 0,
    likedByMe = false,
    shares = 0,
    sharedByMe = false,
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositorySQLite(application)
    val data: LiveData<List<Post>> = repository.get()
    val edited = MutableLiveData(empty)
    fun like(id: Long) = repository.likeById(id)
    fun share(id: Long) = repository.shareById(id)
    fun remove(id: Long) = repository.removeById(id)
    fun save(text: String) {
        edited.value?.let {
            if (it.content.isNotBlank()) {
                repository.save(it)
            }
        }
        edited.value = Post(id = 0, author = "", content = "", published = "")
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
}