package com.ascri.failtok.data.models

data class Fail(
    val postId: Long = 0,
    val title: String = "",
    val streamer: String = "",
    val game: String = "",
    val points: Int = 0,
    val nsfw: Boolean = false,
    val thumbnailUrl: String = "",
    val videoUrl: String = "",
    val sourceUrl: String = ""
)