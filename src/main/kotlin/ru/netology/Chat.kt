package ru.netology

data class Chat(
    val id: Int,
    val users: List<Int>,
    var messages: List<Message>,
)



