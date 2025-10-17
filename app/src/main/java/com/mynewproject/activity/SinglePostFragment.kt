package com.mynewproject.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mynewproject.R
import com.mynewproject.adapter.OnInteractionListener
import com.mynewproject.adapter.PostViewHolder
import com.mynewproject.databinding.FragmentSinglePostBinding
import com.mynewproject.dto.Post
import com.mynewproject.viewmodel.PostViewModel
import com.mynewproject.util.LongArg
import androidx.fragment.app.activityViewModels

class SinglePostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    companion object {
        var Bundle.postIdArg: Long by LongArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSinglePostBinding.inflate(inflater, container, false)
        val postId = arguments?.postIdArg ?: return binding.root

        val viewHolder = PostViewHolder(binding.postLayout, object : OnInteractionListener {
            override fun edit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(R.id.action_singlePostFragment_to_newPostFragment)
            }

            override fun like(post: Post) = viewModel.likeById(post.id)

            override fun share(post: Post) {
                viewModel.shareById(post.id)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                startActivity(Intent.createChooser(intent, getString(R.string.chooser_share_post)))
            }

            override fun remove(post: Post) {
                viewModel.removeById(post.id)
                findNavController().navigateUp()
            }

            override fun onVideoClick(videoUrl: String) {
                val intent = Intent(Intent.ACTION_VIEW, videoUrl.toUri())
                startActivity(intent)
            }

            override fun onPostClick(post: Post) {}
        })

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            viewHolder.bind(post)
        }

        return binding.root
    }
}