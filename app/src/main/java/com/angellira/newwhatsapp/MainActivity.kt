 package com.angellira.newwhatsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CircleNotifications
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angellira.newwhatsapp.features.chatlist.Chat
import com.angellira.newwhatsapp.features.chatlist.ChatsListScreenState
import com.angellira.newwhatsapp.features.chatlist.ChatsScreen
import com.angellira.newwhatsapp.features.chatlist.Message
import com.angellira.newwhatsapp.features.chatlist.User
import com.angellira.newwhatsapp.ui.theme.NewWhatsappTheme
import kotlin.random.Random

 class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewWhatsappTheme {
                App()
            }
        }
    }
}

 class BottomAppBarItem(
     val icon: ImageVector,
     val label: String,
 )

 class TopAppBarItem(
     val title: String,
     val icons: List<ImageVector> = emptyList()
 )

sealed class ScreenItem(
    val topAppItem: TopAppBarItem,
    val bottomAppItem: BottomAppBarItem,
 ) {
    data object Chats :  ScreenItem(
        topAppItem = TopAppBarItem (
            title = "Whatsapp 2",
            icons = listOf(
                Icons.Default.CameraAlt,
                        Icons.Default.MoreVert
            )
        ),
        bottomAppItem = BottomAppBarItem(
            icon = Icons.AutoMirrored.Filled.Message,
            label = "Chats"
        )
    )
    data object Updates : ScreenItem(

        topAppItem = TopAppBarItem (
            title = "Updates",
            icons = listOf(
                Icons.Default.CameraAlt,
                Icons.Default.Search,
                Icons.Default.MoreVert
            )
        ),
        bottomAppItem = BottomAppBarItem(
            icon = Icons.Default.CircleNotifications,
            label = "Updates"
        )
    )
    data object Communities :  ScreenItem(
        topAppItem = TopAppBarItem (
            title = "Communities",
            icons = listOf(
                Icons.Default.CameraAlt,
                Icons.Default.MoreVert
            )
        ),
        bottomAppItem = BottomAppBarItem(
            icon = Icons.Default.People,
            label = "Communitties"
        )
    )
    data object Calls : ScreenItem(
        topAppItem = TopAppBarItem (
            title = "Calls",
            icons = listOf(
                Icons.Default.CameraAlt,
                Icons.Default.MoreVert
            )
        ),
        bottomAppItem = BottomAppBarItem(
            icon = Icons.Default.Call,
            label = "Calls"
        )
    )
}

 @Composable
 @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
 fun App() {
     val screens = remember {
         listOf(
             ScreenItem.Chats,
             ScreenItem.Updates,
             ScreenItem.Communities,
             ScreenItem.Calls
         )
     }

     var currentScreen by remember {
         mutableStateOf(screens.first())
     }

     val pagerState = rememberPagerState {
         screens.size
     }

     LaunchedEffect(currentScreen) {
         pagerState.animateScrollToPage(screens.indexOf(currentScreen))
     }

     LaunchedEffect(pagerState.targetPage) {
         currentScreen = screens[pagerState.targetPage]
     }

     Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
         TopAppBar(title = {
             Text(currentScreen.topAppItem.title)
         }, actions = {
             Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                 currentScreen.topAppItem.icons.forEach { icon ->
                     Icon(icon, contentDescription = null)
                 }
             }
         })
     },
         bottomBar = {
             BottomAppBar {
                 screens.forEach { screen ->
                     with(screen.bottomAppItem) {
                         NavigationBarItem(
                             selected = screen == currentScreen,
                             onClick = {
                                 currentScreen = screen
                             },
                             icon = {
                                 Icon(icon, contentDescription = null)
                             },
                             label = {
                                 Text(label)
                             }
                         )
                     }
                 }
             }
         }

     ) { innerPadding ->
         HorizontalPager(pagerState, Modifier.padding(innerPadding)) { page ->
             val item = screens[page]
             when(item) {
                 ScreenItem.Calls -> CallsScreen()
                 ScreenItem.Chats -> ChatsScreen(ChatsListScreenState(
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

                 ))
                 ScreenItem.Communities -> CommunitiesScreen()
                 ScreenItem.Updates -> UpdatesScreen()
             }
         }
     }
 }


 @Composable
 fun UpdatesScreen(modifier: Modifier = Modifier) {
     Box(modifier.fillMaxSize()) {
         Text("Updates", Modifier.align(Alignment.Center), style = TextStyle.Default.copy(
             fontSize = 32.sp
         ))
     }
 }

 @Composable
 fun CommunitiesScreen(modifier: Modifier = Modifier) {
     Box(modifier.fillMaxSize()) {
         Text("Communities", Modifier.align(Alignment.Center), style = TextStyle.Default.copy(
             fontSize = 32.sp
         ))
     }
 }

 @Composable
 fun CallsScreen(modifier: Modifier = Modifier) {
     Box(modifier.fillMaxSize()) {
         Text("Calls", Modifier.align(Alignment.Center), style = TextStyle.Default.copy(
             fontSize = 32.sp
         ))
     }
 }

 @Preview
 @Composable
 private fun AppPreview() {
     NewWhatsappTheme {
        App()
     }
 }