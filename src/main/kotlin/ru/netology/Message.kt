package ru.netology

data class Message(
    val id: Int,
    val ownerId: Int,
    var text: String,
    var isRead: Boolean = false,
)
