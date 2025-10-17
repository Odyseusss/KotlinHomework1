package com.mynewproject.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mynewproject.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val likes: Int = 0,
    val likedByMe: Boolean = false,
    val shares: Int = 0,
    val sharedByMe: Boolean = false,
    val video: String? = null,
) {
    fun toDto(): Post = Post(
        id = id,
        author = author,
        published = published,
        content = content,
        likes = likes,
        likedByMe = likedByMe,
        shares = shares,
        sharedByMe = sharedByMe,
        video = video
    )

    companion object {
        fun fromDto(post: Post): PostEntity = with(post) {
            PostEntity(
                id = id,
                author = author,
                published = published,
                content = content,
                likes = likes,
                likedByMe = likedByMe,
                shares = shares,
                sharedByMe = sharedByMe,
                video = video
            )
        }
    }
}