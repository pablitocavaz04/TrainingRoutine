package com.example.trainingroutine_pablocavaz.data.local

data class MemoryCard(
    val id: Int,
    val imageResId: Int,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)