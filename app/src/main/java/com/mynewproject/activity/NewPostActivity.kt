package com.mynewproject.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mynewproject.R
import com.mynewproject.databinding.ActivityNewPostBinding
import com.mynewproject.dto.Post
import com.mynewproject.util.AndroidUtils

class NewPostActivity : AppCompatActivity() {
    var currentPostId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val postId = intent.getLongExtra("POST_ID", 0L)
        val postContent = intent.getStringExtra("POST_CONTENT")

        if (postId != 0L && postContent != null) {
            currentPostId = postId
            binding.content.setText(postContent)
            binding.add.setImageResource(R.drawable.ic_save_48)
            supportActionBar?.title = getString(R.string.edit_post)
        } else {
            supportActionBar?.title = getString(R.string.new_post)
        }

        AndroidUtils.showKeyboard(binding.add)

        binding.add.setOnClickListener {
            val text = binding.content.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                val resultIntent = Intent().apply {
                    putExtra(Intent.EXTRA_TEXT, text)
                    if (currentPostId != 0L) {
                        putExtra("EDITED_POST_ID", currentPostId)
                    }
                }
                setResult(RESULT_OK, resultIntent)
            }
            finish()
        }
    }
}

object NewPostContract : ActivityResultContract<Post?, String?>() {
    override fun createIntent(context: Context, input: Post?) =
        Intent(context, NewPostActivity::class.java).apply {
            input?.let { post ->
                putExtra("POST_ID", post.id)
                putExtra("POST_CONTENT", post.content)
            }
        }

    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.getStringExtra(Intent.EXTRA_TEXT)
}
