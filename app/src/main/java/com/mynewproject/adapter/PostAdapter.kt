package com.mynewproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mynewproject.R
import com.mynewproject.ShortNumberFun.shortNumber
import com.mynewproject.databinding.CardPostBinding
import com.mynewproject.dto.Post

interface OnInteractionListener {

    fun like(post: Post)
    fun share(post: Post)
    fun remove(post: Post)
    fun edit(post: Post)
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding, onInteractionListener
        )
    }

    override fun onBindViewHolder(
        holder: PostViewHolder, position: Int
    ) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding, private val onInteractionListener: OnInteractionListener
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
                onInteractionListener.like(post)
            }
            icShare.setOnClickListener {
                onInteractionListener.share(post)
            }
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.remove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.edit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()

            }
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(
        oldItem: Post, newItem: Post
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Post, newItem: Post
    ): Boolean {
        return oldItem == newItem
    }

}