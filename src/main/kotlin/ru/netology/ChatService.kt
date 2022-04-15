package ru.netology

class ChatService {
    private val chats = mutableListOf<Chat>()
    private var messageId = 1
    private var chatId = 1

    fun createMessage(senderId: Int, receiverId: Int, text: String): Int {
        val message = Message(
            id = messageId++,
            ownerId = senderId,
            text = text
        )

        val newChat = chats.firstOrNull { chat -> chat.users.containsAll(listOf(receiverId, senderId)) }
            ?.let { chat -> chat.copy(messages = chat.messages + message) } ?: Chat(
            id = chatId++,
            users = listOf(receiverId, senderId),
            messages = mutableListOf(message)
        )

        chats.removeIf { newChat.id == it.id }
        chats.add(newChat)

        return chats.size
    }

    fun getExactCountMessages(id: Int, lastMessageId: Int, messageCount: Int): List<Message> {
        val chat = chats.firstOrNull { it.id == id } ?: throw ChatsNotFoundException("Чат не найден")

        val updateMessages = chat.messages.asSequence()
            .filter { it.id > lastMessageId }
            .take(messageCount)
            .map { it.copy(isRead = true) }
            .ifEmpty { throw MessageNotFoundException("Сообщения не найдены") }
            .toList()

        val updatedChat = chat.copy(messages = updateMessages)
        chats.removeIf { updatedChat.id == it.id }
        chats.add(updatedChat)

        return updateMessages
    }

    fun getListOfChats(senderId: Int): List<Chat> {
        return chats.filter { chat -> chat.users.containsAll(listOf(senderId)) }
            .ifEmpty { throw MessageNotFoundException("Нет сообщений") }
    }

    fun getUnreadChats(userId: Int): Int {
        return chats.filter { chat -> chat.users.containsAll(listOf(userId)) }
            .onEach { chat -> chat.messages.onEach { it.ownerId == userId && !it.isRead } }
            .ifEmpty { throw ChatsNotFoundException("Непрочитанных чатов не найдено") }.size
    }

    fun deleteChat(id: Int): Boolean {
        return chats.removeIf { it.id == id }
    }

    fun deleteMessage(id: Int): Boolean {
        for (chat in chats) {
            for (message in chat.messages) {
                if (message.id == id) {
                    chat.messages -= message
                    if (chat.messages.isEmpty()) {
                        chats.remove(chat)
                    }
                    return true
                } else {
                    throw MessageNotFoundException("Сообщение не найдено!")
                }
            }
        }
        throw MessageNotFoundException("Сообщение не найдено!")
    }


}






