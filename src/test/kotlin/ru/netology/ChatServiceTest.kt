package ru.netology

import org.junit.Test

import org.junit.Assert.*

class ChatServiceTest {

    @Test
    fun createMessage_newChat() {
        val service = ChatService()

        val result = service.createMessage(1, 1, "test")

        assertEquals(1, result)

    }

    @Test
    fun createMessage_addedToTheExistingChat() {
        val service = ChatService()

        service.createMessage(1, 1, "test")
        service.createMessage(2, 2, "test")
        service.createMessage(3, 3, "test")

        val result = service.createMessage(1, 1, "test")

        assertEquals(3, result)

    }

    @Test(expected = MessageNotFoundException::class)
    fun getListOfChats_throwException() {
        val service = ChatService()

        service.getListOfChats(1)
    }

    @Test
    fun getListOfChats_success() {
        val service = ChatService()

        service.createMessage(1, 1, "test")
        service.createMessage(1, 2, "test")
        service.createMessage(1, 3, "test")
        service.createMessage(5, 3, "test")

        val actual = listOf(
            Chat(1, listOf(1, 1), listOf(Message(1, 1, "test"))),
            Chat(2, listOf(2, 1), listOf(Message(2, 1, "test"))),
            Chat(3, listOf(3, 1), listOf(Message(3, 1, "test")))
        )

        val result = service.getListOfChats(1)

        assertEquals(actual, result)

    }

    @Test
    fun deleteChat_false() {
        val service = ChatService()

        val result = service.deleteChat(1)

        assertFalse(result)
    }

    @Test
    fun deleteChat_true() {
        val service = ChatService()

        service.createMessage(1, 1, "test")

        val result = service.deleteChat(1)

        assertTrue(result)

    }

    @Test
    fun deleteChat_other() {
        val service = ChatService()
        service.createMessage(1, 1, "test")
        val result = service.deleteChat(4)

        assertFalse(result)

    }

    @Test
    fun deleteMessage_true() {
        val service = ChatService()

        service.createMessage(1, 1, "test")

        val result = service.deleteMessage(1)

        assertTrue(result)
    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessage_false() {
        val service = ChatService()

        service.deleteMessage(1)
    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessage_false2() {
        val service = ChatService()

        service.createMessage(1, 1, "test")
        service.createMessage(2, 1, "test")
        service.createMessage(5, 5, "test")

        service.deleteMessage(54)
    }

    @Test
    fun deleteMessage_true_ChatNotDeleted() {
        val service = ChatService()

        service.createMessage(1, 1, "test")
        service.createMessage(1, 1, "test")

        val result = service.deleteMessage(1)

        assertTrue(result)
    }


    @Test
    fun getUnreadChats_true() {
        val service = ChatService()

        service.createMessage(1, 1, "test")
        service.createMessage(1, 2, "test")
        service.createMessage(1, 2, "test")
        service.createMessage(1, 2, "test")
        service.getExactCountMessages(2, 1, 1)
        service.createMessage(1, 2, "test")
        service.createMessage(2, 1, "test")
        service.createMessage(5, 5, "test")

        val result = service.getUnreadChats(1)

        assertEquals(2, result)
    }

    @Test(expected = ChatsNotFoundException::class)
    fun getUnreadChats_false() {
        val service = ChatService()

        service.getUnreadChats(1)
    }

    @Test(expected = ChatsNotFoundException::class)
    fun getExactCountMessages_emptyList() {
        val service = ChatService()

        service.getExactCountMessages(1, 1, 1)


    }

    @Test(expected = MessageNotFoundException::class)
    fun getExactCountMessages_emptyList2() {
        val service = ChatService()

        service.createMessage(5, 3, "test")
        service.createMessage(7, 22, "test")


        service.getExactCountMessages(1, 1, 1)


    }

    @Test
    fun getExactCountMessages_fullList() {
        val service = ChatService()

        service.createMessage(1, 2, "test")
        service.createMessage(1, 2, "test")
        service.createMessage(1, 2, "test")
        service.createMessage(1, 2, "test")
        service.createMessage(5, 3, "test")
        service.createMessage(5, 3, "test")
        service.createMessage(7, 22, "test")


        val actual = listOf(
            Message(
                id = 2,
                ownerId = 1,
                text = "test",
                isRead = true
            ), Message(
                id = 3,
                ownerId = 1,
                text = "test",
                isRead = true
            ), Message(
                id = 4,
                ownerId = 1,
                text = "test",
                isRead = true
            )
        )

        val result = service.getExactCountMessages(1, 1, 3)

        assertEquals(actual, result)
    }

}