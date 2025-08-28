package com.mynewproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mynewproject.R
import com.mynewproject.ShortNumberFun.shortNumber
import com.mynewproject.databinding.CardPostBinding
import com.mynewproject.dto.Post

typealias OnItemLikeListener = (post: Post) -> Unit
typealias OnItemShareListener = (post: Post) -> Unit

class PostAdapter(
    private val onItemLikeListener: OnItemLikeListener,
    private val onItemShareListener: OnItemShareListener
) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding, onItemLikeListener,
            onItemShareListener
        )
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onItemLikeListener: OnItemLikeListener,
    private val onItemShareListener: OnItemShareListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
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
            icLikes.setOnClickListener {
                onItemLikeListener(post)
            }
            icShare.setOnClickListener {
                onItemShareListener(post)
            }
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(
        oldItem: Post,
        newItem: Post
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Post,
        newItem: Post
    ): Boolean {
        return oldItem == newItem
    }

}