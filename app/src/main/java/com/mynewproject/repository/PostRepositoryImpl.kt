package com.mynewproject.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.mynewproject.dao.PostDao
import com.mynewproject.dto.Post
import com.mynewproject.entity.PostEntity

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {

    override fun getAll(): LiveData<List<Post>> =
        dao.getAll().map { entities -> entities.map(PostEntity::toDto) }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        val posts = dao.getAll().value ?: return
        val post = posts.find { it.id == id } ?: return

        val newSharedByMe = !post.sharedByMe
        val newShares = if (newSharedByMe) post.shares + 1 else (post.shares - 1).coerceAtLeast(0)

        dao.updateContentById(
            id = id,
            content = post.content
        )

    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }
}