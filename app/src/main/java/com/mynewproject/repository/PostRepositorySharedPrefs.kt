package com.mynewproject.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mynewproject.dto.Post
import androidx.core.content.edit

class PostRepositorySharedPrefs(context: Context) : PostRepository {
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private var posts = listOf<Post>()
        set(value) {
            field = value
            sync()
        }

    private var nextId = posts.maxOfOrNull { it.id }?.plus(1) ?: 1L

    private val data = MutableLiveData(posts)

    init {
        prefs.getString(KEY_POSTS, null)?.let { it ->
            posts = gson.fromJson(it, type)
            nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
            data.value = posts
        }
    }

    private fun sync() {
        prefs.edit {
            putString(KEY_POSTS, gson.toJson(posts))
        }
    }

    override fun get(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {

        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likedByMe = !post.likedByMe,
                    likes = if (post.likedByMe) {
                        post.likes - 1
                    } else {
                        post.likes + 1
                    }
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    sharedByMe = !post.sharedByMe,
                    shares = if (post.sharedByMe) {
                        post.shares - 1
                    } else {
                        post.shares + 1
                    }
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++, author = "Me", published = "now")) + posts
        } else {
            posts.map { if (it.id != post.id) it else it.copy(content = post.content) }
        }
        data.value = posts
    }

    companion object {
        private const val KEY_POSTS = "posts"

        private val gson = Gson()

        private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    }
}