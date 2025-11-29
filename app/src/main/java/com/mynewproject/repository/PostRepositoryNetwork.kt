package com.mynewproject.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mynewproject.dto.Post
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryNetwork() : PostRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999/"
        val jsonType = "application/json".toMediaType()
    }

    override fun getAllAsync(callback: GetAllCallBack) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val posts =
                            response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(posts, type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            })
    }

    override fun getAll(): List<Post> {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let { gson.fromJson(it, type) }
    }

    override fun likeByIdAsync(post: Post, callback: PostCallback) {
        val request = (if (!post.likedByMe) {
            Request.Builder()
                .url("${BASE_URL}api/posts/${post.id}/likes")
                .post("".toRequestBody())
        } else {
            Request.Builder()
                .url("${BASE_URL}api/posts/${post.id}/likes")
                .delete()
        }).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) =
                callback.onError(e)

            override fun onResponse(call: Call, response: Response) =
                callback.onSuccess()
        })
    }

    override fun likeById(post: Post) {
        val request = if (!post.likedByMe) {
            Request.Builder()
                .url("${BASE_URL}api/posts/${post.id}/likes")
                .post("".toRequestBody())
                .build()
        } else {
            Request.Builder()
                .url("${BASE_URL}api/posts/${post.id}/likes")
                .delete()
                .build()
        }

        client.newCall(request).execute()
    }

    override fun shareByIdAsync(id: Long, callback: PostCallback) {
        callback.onSuccess()
    }

    override fun shareById(id: Long) {
    }

    override fun removeByIdAsync(id: Long, callback: PostCallback) {
        val request = Request.Builder()
            .url("${BASE_URL}api/posts/$id")
            .delete()
            .build()

        client.newCall(request).enqueue((object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                callback.onSuccess()
            }
        }))
    }

    override fun removeById(id: Long) {
        val request = Request.Builder()
            .url("${BASE_URL}api/posts/$id")
            .delete()
            .build()

        client.newCall(request).execute()
    }

    override fun saveAsync(post: Post, callback: SaveCallback) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val json =
                        response.body?.string() ?: throw RuntimeException("body is null")
                    callback.onSuccess(gson.fromJson(json, Post::class.java))
                } catch (e: Exception) {
                    callback.onError(e)
                }
            }
        })
    }

    override fun save(post: Post): Post {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()

        val call = client.newCall(request)

        val response = call.execute()

        val textBody = response.body.string()

        response.close()

        return gson.fromJson(textBody, Post::class.java)
    }
}