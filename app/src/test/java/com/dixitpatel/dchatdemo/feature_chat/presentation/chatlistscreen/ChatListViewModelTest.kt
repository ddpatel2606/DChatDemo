package com.dixitpatel.dchatdemo.feature_chat.presentation.chatlistscreen

import app.cash.turbine.test
import com.dixitpatel.dchatdemo.feature_chat.domain.models.Message
import com.dixitpatel.dchatdemo.feature_chat.domain.models.User
import com.dixitpatel.dchatdemo.feature_chat.domain.repository.MessageRepository
import com.dixitpatel.dchatdemo.feature_chat.domain.repository.UserRepository
import com.dixitpatel.dchatdemo.feature_chat.domain.usecases.MarkMessagesAsReadUseCase
import com.dixitpatel.dchatdemo.feature_chat.domain.usecases.MessagesUseCase
import com.dixitpatel.dchatdemo.feature_chat.domain.usecases.SendMessageUseCase
import com.dixitpatel.dchatdemo.feature_chat.domain.usecases.UserUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class ChatListViewModelTest {

    // Mocks for use cases and repositories
    private lateinit var messageRepository: MessageRepository
    private lateinit var userRepository: UserRepository
    private lateinit var sendMessageUseCase: SendMessageUseCase
    private lateinit var markMessagesAsReadUseCase: MarkMessagesAsReadUseCase
    private lateinit var messagesUseCase: MessagesUseCase
    private lateinit var userUseCase: UserUseCase
    private lateinit var chatViewModel: ChatListViewModel

    // Sample test data
    private val sampleUser = User("1", "Alex", 1)
    private val anotherUser = User("2", "Sara", 2)
    private val sampleMessage = Message(
        id = "m1", content = "Hello", senderId = "1",
        timestamp = LocalDateTime.now(), isRead = false
    )

    @Before
    fun setup() {
        // Repository mocks
        messageRepository = mockk()
        userRepository = mockk()

        // Use case mocks
        sendMessageUseCase = SendMessageUseCase(messageRepository)
        markMessagesAsReadUseCase = MarkMessagesAsReadUseCase(messageRepository)
        messagesUseCase = MessagesUseCase(messageRepository)
        userUseCase = UserUseCase(userRepository)

        // Mock repository behavior
        coEvery { messageRepository.sendMessage(any(), any()) } just Runs
        coEvery { messageRepository.markMessagesAsRead(any()) } just Runs
        coEvery { messageRepository.clearChatMessages() } just Runs
        every { messageRepository.getAllMessages() } returns flowOf(listOf(sampleMessage))
        every { userRepository.getAllUsers() } returns flowOf(listOf(sampleUser))
        coEvery { userRepository.initializeUsers() } just Runs

        // Initialize ViewModel
        chatViewModel = ChatListViewModel(
            messagesUseCase,
            sendMessageUseCase,
            userUseCase,
            markMessagesAsReadUseCase
        )
    }

    @Test
    fun `SendMessageUseCase sends non-blank message`() = runTest {
        val content = "Hello"
        val senderId = "1"
        sendMessageUseCase(content, senderId)
        coVerify { messageRepository.sendMessage(content, senderId) }
    }

    @Test
    fun `SendMessageUseCase ignores blank message`() = runTest {
        val blanks = listOf("", "   ", "\n")
        blanks.forEach { sendMessageUseCase(it, "1") }
        coVerify(exactly = 0) { messageRepository.sendMessage(any(), any()) }
    }

    @Test
    fun `MarkMessagesAsReadUseCase calls repository`() = runTest {
        val userId = "1"
        markMessagesAsReadUseCase(userId)
        coVerify { messageRepository.markMessagesAsRead(userId) }
    }

    @Test
    fun `MessagesUseCase invokes repository and returns grouped messages`() = runTest {
        messagesUseCase().test {
            val groups = awaitItem()
            assertThat(groups).isNotEmpty()
            assertThat(groups[0].messages).containsExactly(sampleMessage)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `MessagesUseCase clearMessages calls repository`() = runTest {
        messagesUseCase.clearMessages()
        coVerify { messageRepository.clearChatMessages() }
    }

    @Test
    fun `UserUseCase invoke returns users`() = runTest {
        userUseCase().test {
            val users = awaitItem()
            assertThat(users).containsExactly(sampleUser)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `UserUseCase initialiseUsers calls repository`() = runTest {
        userUseCase.initialiseUsers()
        coVerify { userRepository.initializeUsers() }
    }

    @Test
    fun `ChatListViewModel uiState emits initial loading and updates`() = runTest {
        chatViewModel.uiState.test {
            val updated = awaitItem()
            assertThat(updated.isLoading).isFalse()
            assertThat(updated.userList).containsExactly(sampleUser)
            assertThat(updated.selectedUser).isEqualTo(sampleUser)
            assertThat(updated.messages[0].messages).containsExactly(sampleMessage)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ChatListViewModel onMessageTextChanged updates messageText`() = runTest {
        chatViewModel.onMessageTextChanged("Hi")
        chatViewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.messageText).isEqualTo("Hi")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ChatListViewModel sendMessage calls use case and clears text`() = runTest {
        chatViewModel.onMessageTextChanged("Hi")
        chatViewModel.sendMessage()
        coVerify { sendMessageUseCase("Hi", sampleUser.id) }

        chatViewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.messageText).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ChatListViewModel onUserSelected updates selected user and marks messages read`() = runTest {
        chatViewModel.onUserSelected(anotherUser)
        coVerify { markMessagesAsReadUseCase(anotherUser.id) }

        chatViewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.selectedUser).isEqualTo(anotherUser)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ChatListViewModel clearMessages calls messagesUseCase`() = runTest {
        chatViewModel.clearMessages()
        coVerify { messagesUseCase.clearMessages() }
    }

}