package com.dixitpatel.dchatdemo.feature_chat.presentation.chatlistscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dixitpatel.dchatdemo.feature_chat.data.consts.ALEX_USER_ID
import com.dixitpatel.dchatdemo.feature_chat.domain.models.MessageGroup
import com.dixitpatel.dchatdemo.feature_chat.domain.models.User
import com.dixitpatel.dchatdemo.feature_chat.domain.usecases.MarkMessagesAsReadUseCase
import com.dixitpatel.dchatdemo.feature_chat.domain.usecases.MessagesUseCase
import com.dixitpatel.dchatdemo.feature_chat.domain.usecases.SendMessageUseCase
import com.dixitpatel.dchatdemo.feature_chat.domain.usecases.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Chat List screen.
 *
 * This ViewModel is responsible for managing the state and business logic of the chat screen.
 * It observes changes in users and messages, handles user interactions like sending messages,
 * selecting a chat partner, and manages the overall UI state. It uses various use cases
 * to interact with the data layer.
 *
 * The UI state is exposed via a [StateFlow] of [ChatUIState], which contains all the necessary
 * data for rendering the chat screen, including the list of messages, users, the currently
 * selected user, and loading/error states.
 *
 * @param messagesUseCase Use case for observing and clearing messages.
 * @param sendMessageUseCase Use case for sending a new message.
 * @param userUseCase Use case for initializing and observing the list of users.
 * @param markMessagesAsReadUseCase Use case for marking messages from a specific user as read.
 */
@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val messagesUseCase: MessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val userUseCase: UserUseCase,
    private val markMessagesAsReadUseCase: MarkMessagesAsReadUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUIState(isLoading = true))
    val uiState: StateFlow<ChatUIState> = _uiState.asStateFlow()

    init {
        launchSafe { userUseCase.initialiseUsers() }
        observeUsers()
        observeMessages()
    }

    fun onMessageTextChanged(text: String) {
        _uiState.update { it.copy(messageText = text) }
    }

    fun sendMessage() {
        val text = _uiState.value.messageText.trim()
        val receiverId = _uiState.value.selectedUser?.id ?: DEFAULT_USER_ID

        if (text.isBlank()) return

        launchSafe {
            sendMessageUseCase(text, receiverId)
            _uiState.update { it.copy(messageText = "") }
        }
    }

    fun onUserSelected(selectedUser: User) {
        val alreadySelected = _uiState.value.selectedUser?.id == selectedUser.id
        if (alreadySelected) return

        _uiState.update { it.copy(selectedUser = selectedUser) }
        markMessagesAsRead(selectedUser.id)
    }

    private fun observeUsers() {
        viewModelScope.launch {
            userUseCase()
                .collectLatest { users ->
                    val currentSelected = _uiState.value.selectedUser
                    val newSelected = currentSelected ?: users.firstOrNull()

                    _uiState.update { it.copy(
                            userList = users,
                            selectedUser = newSelected,
                            isLoading = false
                        )
                    }

                    newSelected?.let { markMessagesAsRead(it.id) }
                }
        }
    }

    private fun observeMessages() {
        viewModelScope.launch {
            messagesUseCase()
                .collectLatest { groups ->
                    _uiState.update { it.copy(messages = groups, isLoading = false) }
                }
        }
    }

    fun clearMessages() {
        launchSafe { messagesUseCase.clearMessages() }
    }


    private fun markMessagesAsRead(userId: String) {
        launchSafe { markMessagesAsReadUseCase(userId) }
    }

    data class ChatUIState(
        val messages: List<MessageGroup> = emptyList(),
        val selectedUser: User? = null,
        val userList: List<User> = emptyList(),
        val messageText: String = "",
        val isLoading: Boolean = true,
        val error: String? = null
    )

    private fun launchSafe(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
                clearError()
            } catch (e: Exception) {
                showError(e)
            }
        }
    }

    private fun showError(e: Exception) {
        _uiState.update { it.copy(error = e.localizedMessage ?: "Unknown error") }
    }

    private fun clearError() {
        if (_uiState.value.error != null) {
            _uiState.update { it.copy(error = null) }
        }
    }

    companion object {
        const val DEFAULT_USER_ID = ALEX_USER_ID
    }
}