package com.mynewproject.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mynewproject.R
import com.mynewproject.ShortNumberFun.shortNumber
import com.mynewproject.databinding.ActivityMainBinding
import com.mynewproject.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("MainActivity", "onCreate")
        Log.d("MainActivity", "MainActivity hashcode ${this.hashCode()}")

        val viewModel: PostViewModel by viewModels()
        Log.d("MainActivity", "viewModel hashcode ${viewModel.hashCode()}")
        viewModel.data.observe(this) {post ->

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likesCount.text = shortNumber(post.likes)
            shareCount.text = shortNumber(post.shares)
            if (post.likedByMe) {
                icLikes.setImageResource(R.drawable.ic_liked_24)
            } else {
                icLikes.setImageResource(R.drawable.ic_favorite_24)
            }
            if (post.sharedByMe) {
                icShare.setImageResource(R.drawable.ic_shared_24)
            } else {
                icShare.setImageResource(R.drawable.ic_share_24)
            }
        }
            binding.icLikes.setOnClickListener {
                viewModel.like()
            }
            binding.icShare.setOnClickListener {
                viewModel.share()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("Main Activity", "OnStart")
    }
    override fun onRestart() {
        super.onRestart()
        Log.d("Main Activity", "onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Main Activity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Main Activity", "onPause")
    }
    override fun onStop() {
        super.onStop()
        Log.d("Main Activity", "OnStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Main Activity", "OnDestroy")
    }
}