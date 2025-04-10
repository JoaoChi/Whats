package com.angellira.newwhatsapp.features.chatlist

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextLong

sealed class ChatsListScreenState {
    data object Loading : ChatsListScreenState()
    data class Success(
        val currentUser: User,
        val filters: List<String> = emptyList(),
        val chats: List<Chat> = emptyList(),
    ) : ChatsListScreenState()
}


class Chat(
    val avatar: String?,
    val name: String,
    val lastMessage: Message,
    val unreadMessages: Int
)

class Message(
    val text: String,
    val date: String,
    val isRead: Boolean,
    val author: User,
)

data class User(
    val name: String,
)

class ChatsListViewModel : ViewModel() {
    private val _state = MutableStateFlow<ChatsListScreenState>(ChatsListScreenState.Loading)
    val state = _state.asStateFlow()

    init {

        viewModelScope.launch {
            delay(Random.nextLong(1000, 3000))

            val user = fetchUser()
            val filters = fetchFilters()
            val chats = fetchChats()

            _state.update {
                ChatsListScreenState.Success(
                    currentUser = user,
                    filters = filters,
                    chats = chats,
                )
            }
        }
    }

    private fun fetchFilters(): List<String> {
        return listOf("All", "Unread", "Groups")
    }

    private fun fetchUser(): User {
        return User("Joao")
    }

    private fun fetchChats(): List<Chat> {
        val avatarInterator = avatars.shuffled().listIterator()
        return List(10) {
            Chat(
                avatar = if (Random.nextBoolean()) avatarInterator.next() else null,
                name = LoremIpsum(Random.nextInt(1, 10)).values.first(),
                lastMessage = Message(
                    text = LoremIpsum(Random.nextInt(1, 15)).values.first(),
                    date = "21/21/4241",
                    isRead = true,
                    author = if (Random.nextBoolean()) User("Joao") else User("Pepzin")
                ),
                unreadMessages = 2
            )
        }

    }
}

private val avatars = mutableListOf(
    "https://firebasestorage.googleapis.com/v0/b/imagepets-82fe7.appspot.com/o/WhatsApp%20Image%202025-03-24%20at%2014.27.06.jpeg?alt=media&token=feaf84ac-3ba7-4752-8e23-0d1f7b7b0a05",
    "https://firebasestorage.googleapis.com/v0/b/imagepets-82fe7.appspot.com/o/WhatsApp%20Image%202025-03-24%20at%2014.27.07%20(1).jpeg?alt=media&token=88e790bd-8155-4d03-915e-4c74ce69f73e",
    "https://firebasestorage.googleapis.com/v0/b/imagepets-82fe7.appspot.com/o/WhatsApp%20Image%202025-03-24%20at%2014.27.07.jpeg?alt=media&token=855b8357-1102-45d5-b5af-bafc0a912348",
    "https://firebasestorage.googleapis.com/v0/b/imagepets-82fe7.appspot.com/o/pastor-alemao-filhote.png?alt=media&token=1e01c1bd-ae19-46e5-b66e-7747f7d0c200",
    "https://firebasestorage.googleapis.com/v0/b/imagepets-82fe7.appspot.com/o/scottish-fold-cat-kitten-sitting-isolated-transparent-photo-png.webp?alt=media&token=008bd8c6-8cfc-4fe1-971a-841f5a9e811a",
    "https://firebasestorage.googleapis.com/v0/b/imagepets-82fe7.appspot.com/o/scottish-fold-cat-kitten-sitting-isolated-transparent-photo-png.webp?alt=media&token=008bd8c6-8cfc-4fe1-971a-841f5a9e811a",
    "https://firebasestorage.googleapis.com/v0/b/imagepets-82fe7.appspot.com/o/scottish-fold-cat-kitten-sitting-isolated-transparent-photo-png.webp?alt=media&token=008bd8c6-8cfc-4fe1-971a-841f5a9e811a",
)