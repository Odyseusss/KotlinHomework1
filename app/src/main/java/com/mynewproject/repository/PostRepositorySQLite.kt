package com.mynewproject.repository

import android.content.ContentValues
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mynewproject.dto.Post
import android.database.Cursor

class PostRepositorySQLite(context: Context) : PostRepository {

    private val dbHelper = PostsDbHelper(context)

    private var posts = listOf<Post>()
        set(value) {
            field = value
            data.value = field
        }

    private val data = MutableLiveData(posts)

    init {
        refreshPostsFromDb()
    }

    override fun get(): LiveData<List<Post>> = data

    private fun refreshPostsFromDb() {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            PostColumns.TABLE,
            null,
            null,
            null,
            null,
            null,
            "${PostColumns.ID} DESC"
        )
        posts = mapCursorToList(cursor)
        cursor.close()
    }

    private fun mapCursorToList(cursor: Cursor): List<Post> {
        val list = mutableListOf<Post>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(PostColumns.ID))
            val author = cursor.getString(cursor.getColumnIndexOrThrow(PostColumns.AUTHOR))
            val published = cursor.getString(cursor.getColumnIndexOrThrow(PostColumns.PUBLISHED))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(PostColumns.CONTENT)) ?: ""
            val likes = cursor.getInt(cursor.getColumnIndexOrThrow(PostColumns.LIKES))
            val likedByMe =
                cursor.getInt(cursor.getColumnIndexOrThrow(PostColumns.LIKED_BY_ME)) != 0
            val shares = cursor.getInt(cursor.getColumnIndexOrThrow(PostColumns.SHARES))
            val sharedByMe =
                cursor.getInt(cursor.getColumnIndexOrThrow(PostColumns.SHARED_BY_ME)) != 0
            val video = cursor.getString(cursor.getColumnIndexOrThrow(PostColumns.VIDEO))

            val post = Post(
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
            list += post
        }
        return list
    }

    override fun likeById(id: Long) {
        val db = dbHelper.writableDatabase
        val post = findPostById(db, id) ?: return
        val newLiked = !post.likedByMe
        val newLikes = if (newLiked) post.likes + 1 else (post.likes - 1).coerceAtLeast(0)

        val values = ContentValues().apply {
            put(PostColumns.LIKED_BY_ME, if (newLiked) 1 else 0)
            put(PostColumns.LIKES, newLikes)
        }
        db.update(PostColumns.TABLE, values, "${PostColumns.ID} = ?", arrayOf(id.toString()))
        refreshPostsFromDb()
    }

    override fun shareById(id: Long) {
        val db = dbHelper.writableDatabase
        val post = findPostById(db, id) ?: return
        val newShared = !post.sharedByMe
        val newShares = if (newShared) post.shares + 1 else (post.shares - 1).coerceAtLeast(0)

        val values = ContentValues().apply {
            put(PostColumns.SHARED_BY_ME, if (newShared) 1 else 0)
            put(PostColumns.SHARES, newShares)
        }
        db.update(PostColumns.TABLE, values, "${PostColumns.ID} = ?", arrayOf(id.toString()))
        refreshPostsFromDb()
    }

    override fun removeById(id: Long) {
        val db = dbHelper.writableDatabase
        db.delete(PostColumns.TABLE, "${PostColumns.ID} = ?", arrayOf(id.toString()))
        refreshPostsFromDb()
    }

    override fun save(post: Post) {
        val db = dbHelper.writableDatabase
        if (post.id == 0L) {
            val values = ContentValues().apply {
                put(PostColumns.AUTHOR, post.author.ifBlank { "Me" })
                put(PostColumns.PUBLISHED, post.published.ifBlank { "now" })
                put(PostColumns.CONTENT, post.content)
                put(PostColumns.LIKES, post.likes)
                put(PostColumns.LIKED_BY_ME, if (post.likedByMe) 1 else 0)
                put(PostColumns.SHARES, post.shares)
                put(PostColumns.SHARED_BY_ME, if (post.sharedByMe) 1 else 0)
                put(PostColumns.VIDEO, post.video)
            }
            db.insert(PostColumns.TABLE, null, values)
        } else {
            val values = ContentValues().apply {
                put(PostColumns.CONTENT, post.content)
                put(PostColumns.LIKES, post.likes)
                put(PostColumns.LIKED_BY_ME, if (post.likedByMe) 1 else 0)
                put(PostColumns.SHARES, post.shares)
                put(PostColumns.SHARED_BY_ME, if (post.sharedByMe) 1 else 0)
                put(PostColumns.VIDEO, post.video)
            }
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.ID} = ?",
                arrayOf(post.id.toString())
            )
        }
        refreshPostsFromDb()
    }

    private fun findPostById(db: android.database.sqlite.SQLiteDatabase, id: Long): Post? {
        val cursor = db.query(
            PostColumns.TABLE,
            null,
            "${PostColumns.ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        val list = mapCursorToList(cursor)
        cursor.close()
        return list.firstOrNull()
    }
}