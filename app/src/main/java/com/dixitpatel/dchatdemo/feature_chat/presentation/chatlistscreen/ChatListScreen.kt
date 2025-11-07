package com.dixitpatel.dchatdemo.feature_chat.presentation.chatlistscreen

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dixitpatel.dchatdemo.R
import com.dixitpatel.dchatdemo.feature_chat.domain.models.User
import com.dixitpatel.dchatdemo.feature_chat.presentation.components.ChatTopBar
import com.dixitpatel.dchatdemo.feature_chat.presentation.components.EmptyView
import com.dixitpatel.dchatdemo.feature_chat.presentation.components.LoadingComponent
import com.dixitpatel.dchatdemo.feature_chat.presentation.components.MessageGroupItem
import com.dixitpatel.dchatdemo.feature_chat.presentation.components.MessageChatInput
import com.dixitpatel.dchatdemo.feature_chat.presentation.components.UserSelectionSheet
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.dimens
import kotlinx.coroutines.delay

@Composable
fun ChatListScreen(
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ChatListContent(
        uiState = uiState,
        onBackClick = {
            (context as Activity).finish()
        },
        onUserSelected = viewModel::onUserSelected,
        sendMessage = viewModel::sendMessage,
        onClearChat = viewModel::clearMessages,
        onMessageTextChanged = viewModel::onMessageTextChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListContent(
    onBackClick: () -> Unit,
    uiState: ChatListViewModel.ChatUIState,
    sendMessage: () -> Unit,
    onClearChat: () -> Unit,
    onMessageTextChanged: (String) -> Unit,
    onUserSelected: (User) -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    var showUserSheet by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val snackBarHostState = remember { SnackbarHostState() }

    /**
     * AUTO-SCROLL TO BOTTOM
     * Works reliably even when message list height changes
     */
    LaunchedEffect(uiState.messages) {
        if (listState.layoutInfo.totalItemsCount > 0) {
            // Wait next frame so LazyColumn is measured
            delay(50)
            listState.animateScrollToItem(listState.layoutInfo.totalItemsCount - 1)
        }
    }


    /**
     * Error Snack bar handler
     */
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            snackBarHostState.showSnackbar(error, duration = SnackbarDuration.Short)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(MaterialTheme.colorScheme.background)
            .testTag("chatListScreen")
            .clickable { showUserSheet = false },
        topBar = {
            ChatTopBar(
                uiState = uiState,
                onBackClick = onBackClick,
                onToggleUserSheet = { showUserSheet = !showUserSheet },
                onShowMenu = { showMenu = true },
                showMenu = showMenu,
                onClearChat = {
                    showMenu = false
                    onClearChat()
                },
                onDismiss = {
                    showMenu = false
                }
            )
        }, snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(
                    horizontal = MaterialTheme.dimens.xs,
                    vertical = MaterialTheme.dimens.xl5
                ), hostState = snackBarHostState
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .clickable { showUserSheet = false }
        ) {
            if (uiState.isLoading) {
                LoadingComponent()
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (uiState.messages.isEmpty()) {
                            EmptyView(
                                modifier = Modifier.weight(1f),
                                stringResource(R.string.no_chat_to_show)
                            )
                    } else {
                        // Messages List
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.messages) { messageGroup ->
                                MessageGroupItem(
                                    messageGroup = messageGroup,
                                    currentUserId = uiState.selectedUser?.id ?: ""
                                )
                            }
                        }
                    }

                    // Message Input TextField
                    MessageChatInput(
                        messageText = uiState.messageText,
                        onMessageTextChanged = onMessageTextChanged,
                        onSendMessage = sendMessage
                    )
                }

                // TOP SHEET
                UserSelectionSheet(
                    modifier = Modifier.align(Alignment.TopCenter),
                    visible = showUserSheet,
                    users = uiState.userList,
                    selected = uiState.selectedUser?.id,
                    onUserClick = {
                        showUserSheet = false
                        onUserSelected(it)
                    }
                )
            }
        }
    }
}

