package com.mynewproject

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mynewproject.databinding.ActivityMainBinding
import com.mynewproject.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likes = 999_999,
            shares = 999_999,
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likesCount.text = shortNumber(post.likes)
            shareCount.text = shortNumber(post.shares)
            icLikes.setImageResource(R.drawable.ic_favorite_24)
            icLikes.setOnClickListener {
                post.likedByMe = !post.likedByMe
                if (post.likedByMe) {
                    icLikes.setImageResource(R.drawable.ic_liked_24)
                    post.likes++
                } else {
                    icLikes.setImageResource(R.drawable.ic_favorite_24)
                    post.likes--
                }
                likesCount.text = shortNumber(post.likes)
            }
            icShare.setOnClickListener {
                post.sharedByMe = !post.sharedByMe
                if (post.sharedByMe) {
                    icShare.setImageResource(R.drawable.ic_shared_24)
                    post.shares++
                } else {
                    icShare.setImageResource(R.drawable.ic_share_24)
                    post.shares--
                }
                shareCount.text = shortNumber(post.shares)
            }
        }
    }
    private fun shortNumber(number: Int): String {
        return when {
            number < 1_000 -> number.toString()
            number < 10_000 -> {
                val thousands = number / 1000
                val hundreds = (number % 1000) / 100
                if (hundreds > 0) "${thousands}.${hundreds}K" else "${thousands}K"
            }
            number < 1_000_000 -> "${number / 1000}K"
            else -> {
                val millions = number / 1_000_000
                val hundredThousands = (number % 1_000_000) / 100_000
                if (hundredThousands > 0) "${millions}.${hundredThousands}M" else "${millions}M"
            }
        }
    }
}