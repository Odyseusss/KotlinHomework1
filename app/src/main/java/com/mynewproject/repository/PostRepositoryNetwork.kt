package com.mynewproject.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mynewproject.dto.Post
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
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

    override fun getAll(): List<Post> {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()

        val call = client.newCall(request)

        val response = call.execute()

        val textBody = response.body.string()

        return gson.fromJson(textBody, type)
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

    override fun shareById(id: Long) {
    }

    override fun removeById(id: Long) {
        val request = Request.Builder()
            .url("${BASE_URL}api/posts/$id")
            .delete()
            .build()

        client.newCall(request).execute()
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