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
    "https://randomuser.me/api/portraits/men/32.jpg",
)