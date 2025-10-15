package com.mynewproject.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DB_NAME = "posts.db"
private const val DB_VERSION = 1

class PostsDbHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE ${PostColumns.TABLE} (
                ${PostColumns.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${PostColumns.AUTHOR} TEXT NOT NULL,
                ${PostColumns.PUBLISHED} TEXT NOT NULL,
                ${PostColumns.CONTENT} TEXT,
                ${PostColumns.LIKES} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.LIKED_BY_ME} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.SHARES} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.SHARED_BY_ME} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.VIDEO} TEXT
            );
        """.trimIndent()
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${PostColumns.TABLE}")
        onCreate(db)
    }
}