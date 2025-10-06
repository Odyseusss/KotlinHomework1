package com.mynewproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mynewproject.dto.Post
import com.mynewproject.repository.PostRepository
import com.mynewproject.repository.PostRepositoryInMemory
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

class PostViewModel: ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemory()
    val data: LiveData<List<Post>> = repository.get()
    val edited = MutableLiveData(empty)
    fun like(id: Long) = repository.likeById(id)
    fun share(id: Long) = repository.shareById(id)
    fun remove(id: Long) = repository.removeById(id)
    fun save(text: String) {
            val content = text.trim()
            if (content.isNotEmpty()) {
                repository.save(empty.copy(content = content))
            }
    }
    fun edit(post: Post) {
        edited.value = post
    }
    fun cancelEdit() {
        edited.value = empty
    }

    fun saveAfterEdit(content: String, postId: Long) {
        val trimmedContent = content.trim()
        if (trimmedContent.isNotEmpty() && postId != 0L) {
            val updatedPost = edited.value?.copy(
                id = postId,
                content = trimmedContent
            )
            updatedPost?.let {
                repository.save(it)
            }
        }
        edited.value = empty
    }
}