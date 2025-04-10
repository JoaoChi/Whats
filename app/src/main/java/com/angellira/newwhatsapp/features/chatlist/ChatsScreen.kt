package com.angellira.newwhatsapp.features.chatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angellira.newwhatsapp.ui.theme.NewWhatsappTheme
import kotlin.random.Random

@Composable
fun ChatsScreen(
    state: ChatsListScreenState,
    modifier: Modifier = Modifier
) {
    when (state) {
        ChatsListScreenState.Loading -> {
            Box(modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        is ChatsListScreenState.Success -> {
            LazyColumn(
                modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        Modifier
                            .clip(CircleShape)
                            .fillMaxWidth()
                            .background(Color.Gray)
                            .padding(16.dp)

                    ) {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("Search...")
                    }
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        state.filters.forEach { filter ->
                            Box(
                                Modifier
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                                    .padding(16.dp, 8.dp),

                                ) {
                                Text(filter)
                            }
                        }
                    }
                }
                items(state.chats) { chat ->
                    val avatarSize = 54.dp
                    Row(Modifier.fillMaxSize()) {
                        chat.avatar?.let {
                            Box(
                                Modifier
                                    .size(avatarSize)
                                    .clip(CircleShape)
                                    .background(
                                        Color(
                                            Random.nextInt(1, 255),
                                            Random.nextInt(1, 255),
                                            Random.nextInt(1, 255)
                                        )
                                    )
                            )
                        } ?: Box(
                            Modifier
                                .size(avatarSize)
                                .clip(CircleShape)
                                .background(
                                    Color(
                                        Random.nextInt(1, 255),
                                        Random.nextInt(1, 255),
                                        Random.nextInt(1, 255)
                                    )
                                )
                        ) {
                            Text(
                                chat.name.first().toString(),
                                Modifier.align(Alignment.Center),
                                style = TextStyle.Default.copy(
                                    fontSize = 24.sp
                                )
                            )
                        }

                        Spacer(Modifier.size(16.dp))
                        Column(
                            Modifier.heightIn(avatarSize),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    chat.name,
                                    Modifier.weight(1f),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    style = TextStyle.Default.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,

                                        )
                                )
                                Spacer(Modifier.size(8.dp))
                                Text(
                                    chat.lastMessage.date, style = TextStyle.Default.copy(
                                        fontSize = 14.sp,
                                    )
                                )
                            }

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(Modifier.weight(1f)) {
                                    if (state.currentUser != chat.lastMessage.author) {
                                        Icon(Icons.Default.DoneAll, contentDescription = null)
                                    }
                                    Spacer(Modifier.size(4.dp))
                                    Text(
                                        chat.lastMessage.text,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                }
                                Box(
                                    Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(Color.Green)
                                        .padding(4.dp)
                                ) {
                                    Text(
                                        "99",
                                        Modifier.align(Alignment.Center),
                                        style = TextStyle.Default.copy(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun ChatListScreenPreview() {
    NewWhatsappTheme {
        Surface {
            ChatsScreen(
                state = ChatsListScreenState.Success(
                    currentUser = User("Joao"),
                    filters = listOf("All", "Unread", "Groups"),
                    chats = List(10) {
                        Chat(
                            avatar = if (Random.nextBoolean()) "avatar" else null,
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
                )
            )
        }
    }
}

@Preview
@Composable
private fun ChatListScreenWithLoadingStatePreview() {
    NewWhatsappTheme {
        Surface {
            ChatsScreen(
                state = ChatsListScreenState.Loading
            )
        }
    }
}
