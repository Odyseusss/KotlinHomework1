package com.mynewproject.util

import android.content.Context
import androidx.core.content.edit

object DraftManager {
    private const val PREFS_NAME = "draft_prefs"
    private const val KEY_DRAFT = "post_draft"

    fun saveDraft(context: Context, text: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit { putString(KEY_DRAFT, text) }
    }

    fun getDraft(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_DRAFT, null)
    }

    fun clearDraft(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit { remove(KEY_DRAFT) }
    }
}