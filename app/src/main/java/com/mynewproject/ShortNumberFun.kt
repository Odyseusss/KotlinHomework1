package com.mynewproject

object ShortNumberFun {
    fun shortNumber(number: Int): String {
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