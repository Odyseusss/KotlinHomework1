package com.mynewproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mynewproject.dto.Post
import com.mynewproject.model.FeedModel
import com.mynewproject.repository.PostRepository
import com.mynewproject.repository.PostRepositoryNetwork
import com.mynewproject.util.SingleLiveEvent
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likes = 0,
    likedByMe = false
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryNetwork()

    private val _data: MutableLiveData<FeedModel> = MutableLiveData(FeedModel())

    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)

    private val _postCreated = SingleLiveEvent<Unit>()

    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        load()
    }

    fun save() {
        thread {
            edited.value?.let {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
            edited.postValue(empty)
        }
    }

    fun load() {
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val posts = repository.getAll()
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            } catch (_: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        }
    }

    fun edit(post: Post) {
        thread {
            edited.postValue(post)
        }
    }

    fun shareById(id: Long) {
        thread {
            repository.shareById(id)
        }
    }

    fun likeById(id: Long) {
        thread {
            repository.likeById(id)
        }
    }

    fun removeById(id: Long) {
        thread {
            repository.removeById(id)
        }
    }

    fun changeContent(content: String) {
        thread {
            edited.value?.let {
                val trimmed = content.trim()
                if (it.content != trimmed) {
                    edited.postValue(it.copy(content = trimmed))
                }
            }
        }
    }

    fun cancelEdit() {
        thread {
            edited.postValue(empty)
        }
    }
}