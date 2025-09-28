package com.mynewproject.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mynewproject.R
import com.mynewproject.adapter.OnInteractionListener
import com.mynewproject.adapter.PostAdapter
import com.mynewproject.databinding.ActivityMainBinding
import com.mynewproject.dto.Post
import com.mynewproject.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val newPostLauncher = registerForActivityResult(NewPostContract) { result ->
            result ?: return@registerForActivityResult
            viewModel.save(result)
        }

        val editPostLauncher = registerForActivityResult(NewPostContract) { result ->
            result ?: return@registerForActivityResult
            viewModel.save(result)
        }

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun like(post: Post) {
                viewModel.like(post.id)
            }

            override fun share(post: Post) {
                viewModel.share(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(chooser)
            }

            override fun remove(post: Post) {
                viewModel.remove(post.id)
            }

            override fun edit(post: Post) {
                editPostLauncher.launch(post)
            }

        })
        binding.list.adapter = adapter

        viewModel.data.observe(this) { posts ->
            val new = posts.size > adapter.currentList.size && adapter.currentList.isNotEmpty()
            adapter.submitList(posts) {
                if (new) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

//        viewModel.edited.observe(this) {
//            if (it.id != 0L) {
//                binding.content.setText(it.content)
//                binding.editGroup.visibility = View.VISIBLE
//                AndroidUtils.showKeyboard(binding.content)
//            } else {
//                binding.editGroup.visibility = View.GONE
//                binding.content.setText("")
//                binding.content.clearFocus()
//                AndroidUtils.hideKeyboard(binding.content)
//            }
//        }
//
//        binding.deleteEditButton.setOnClickListener {
//            viewModel.cancelEdit()
//        }
//
//        binding.save.setOnClickListener {
//            val text = binding.content.text.toString()
//            if (text.isBlank()) {
//                Toast.makeText(this@MainActivity, R.string.error_empty_content, Toast.LENGTH_LONG)
//                    .show()
//                return@setOnClickListener
//            }
//            viewModel.save(text)
//
//            binding.content.setText("")
//            binding.content.clearFocus()
//            AndroidUtils.hideKeyboard(binding.content)
//        }
        binding.add.setOnClickListener {
            newPostLauncher.launch(null)
        }
    }
}