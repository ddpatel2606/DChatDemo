package com.dixitpatel.dchatdemo.feature_chat.presentation.chatlistscreen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.dixitpatel.dchatdemo.HiltTestActivity
import com.dixitpatel.dchatdemo.feature_chat.domain.models.Message
import com.dixitpatel.dchatdemo.feature_chat.domain.models.MessageGroup
import com.dixitpatel.dchatdemo.feature_chat.domain.models.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

@HiltAndroidTest
class ChatListScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    private lateinit var fakeViewModel: ChatListViewModel
    private lateinit var uiStateFlow: MutableStateFlow<ChatListViewModel.ChatUIState>

    private val user1 = User("1", "Alex", profilePic = android.R.drawable.ic_menu_view)
    private val user2 = User("2", "Sara", profilePic = android.R.drawable.ic_menu_view)

    @Before
    fun setUp() {
        hiltRule.inject()
        fakeViewModel = mockk(relaxUnitFun = true)

        uiStateFlow = MutableStateFlow(
            ChatListViewModel.ChatUIState(
                userList = listOf(user1, user2),
                selectedUser = user1
            )
        )

        every { fakeViewModel.uiState } returns uiStateFlow
        every { fakeViewModel.onUserSelected(any()) } just Runs
        every { fakeViewModel.sendMessage() } just Runs
        every { fakeViewModel.clearMessages() } just Runs
        every { fakeViewModel.onMessageTextChanged(any()) } just Runs

        composeTestRule.setContent {
            ChatListScreen(viewModel = fakeViewModel)
        }
    }

    @Test
    fun givenInitialState_whenScreenIsLaunched_thenCorrectUserAndContentAreDisplayed() {
        // Assert
        composeTestRule.onNodeWithTag("chatListScreen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Alex").assertIsDisplayed() // Toolbar shows selected user
    }

    @Test
    fun whenBackArrowIsClicked_thenNavigateBack() {
        // Act
        composeTestRule.onNodeWithTag("backBtn").performClick()

        // Check if activity finished (exited)
        composeTestRule.activityRule.scenario.onActivity { activity ->
            assert(activity.isFinishing)
        }
    }

    @Test
    fun givenUserSheetIsHidden_whenUserSelectionArrowIsClicked_thenUserSheetIsDisplayed() {
        uiStateFlow.value = uiStateFlow.value.copy(isLoading = false)
        every { fakeViewModel.uiState } returns uiStateFlow

        // Act
        composeTestRule.onNodeWithTag("arrowBtn").performClick()

        // Assert
        composeTestRule.onNodeWithText("Select User").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sara").assertIsDisplayed()
    }

    @Test
    fun givenUserSheetIsDisplayed_whenNewUserIsSelected_thenViewModelIsNotifiedAndSheetIsHidden() {
        uiStateFlow.value = uiStateFlow.value.copy(isLoading = false)
        every { fakeViewModel.uiState } returns uiStateFlow

        // Arrange
        composeTestRule.onNodeWithTag("arrowBtn").performClick()

        // Act
        composeTestRule.onAllNodesWithTag("userSheetRow")[1].performClick()

        // Assert
        verify { fakeViewModel.onUserSelected(user2) }
        composeTestRule.onNodeWithTag("userSheet").assertDoesNotExist()
    }

    @Test
    fun givenUserSheetIsDisplayed_whenClickingOutsideSheet_thenSheetIsHidden() {
        // Arrange
        composeTestRule.onNodeWithTag("arrowBtn").performClick()

        // Act
        composeTestRule.onNodeWithTag("chatListScreen").performClick() // Click on the main screen

        // Assert
        composeTestRule.onNodeWithTag("userSheet").assertDoesNotExist()
    }

    @Test //
    fun givenMessagesExist_whenScreenIsDisplayed_thenMessagesAndTimestampsAreVisible() {
        // Arrange
        val messages = listOf(
            MessageGroup(
                timestamp = "2025-11-07T13:08:59.991471",
                isTimestampShow = true,
                messages = listOf(
                    Message(
                        "1",
                        "Hi",
                        senderId = "1",
                        timestamp = LocalDateTime.now()
                    ),
                    Message(
                        "2",
                        "Hello!",
                        senderId = "2",
                        timestamp = LocalDateTime.now()
                    )
                )
            )
        )
        uiStateFlow.value = uiStateFlow.value.copy(messages = messages, isLoading = false)
        every { fakeViewModel.uiState } returns uiStateFlow

        composeTestRule.onNodeWithText("Hi").assertIsDisplayed()
        composeTestRule.onNode(hasText("13:08", substring = true, ignoreCase = true)).assertIsDisplayed()
        composeTestRule.onNodeWithText("Hello!").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("message_delivered_desc").assertIsDisplayed()


        // Arrange
        composeTestRule.onNodeWithTag("arrowBtn").performClick()

        // Act
        composeTestRule.onAllNodesWithTag("userSheetRow")[1].performClick()

        // Assert
        verify { fakeViewModel.onUserSelected(user2) }
        composeTestRule.onNodeWithTag("userSheet").assertDoesNotExist()

        // Arrange
        val messageText = "This is a test message"
        composeTestRule.onNodeWithTag("messageChatInput").performTextInput(messageText)

        // Act
        composeTestRule.onNodeWithTag("sendBtn").performClick()

        // Arrange
        composeTestRule.onNodeWithTag("arrowBtn").performClick()

        // Act
        composeTestRule.onAllNodesWithTag("userSheetRow")[0].performClick()

        // Assert
        verify { fakeViewModel.onUserSelected(user2) }
        composeTestRule.onNodeWithTag("userSheet").assertDoesNotExist()

    }

    @Test
    fun whenClearChatIsClickedFromMenu_thenViewModelIsNotified() {
        // Act
        composeTestRule.onNodeWithContentDescription("menu_options").performClick() // Assuming a menu button with this tag
        composeTestRule.onNodeWithText("Clear Chat").assertIsDisplayed().performClick()

        // Assert
        verify { fakeViewModel.clearMessages() }

        val messages = listOf(
            MessageGroup(
                timestamp = "10:00 AM",
                isTimestampShow = true,
                messages = listOf(
                    Message(
                        "1",
                        "Hi",
                        senderId = "me",
                        timestamp = LocalDateTime.now()
                    )
                )
            ),
            MessageGroup(
                timestamp = "10:00 AM",
                isTimestampShow = false,
                messages = listOf(
                    Message(
                        "2",
                        "Hi There",
                        senderId = "me",
                        timestamp = LocalDateTime.now()
                    )
                )
            )
        )
        uiStateFlow.value = uiStateFlow.value.copy(messages = messages)
        every { fakeViewModel.uiState } returns uiStateFlow

        // Act
        composeTestRule.onNodeWithContentDescription("menu_options").performClick() // Assuming a menu button with this tag
        composeTestRule.onNodeWithText("Clear Chat").assertIsDisplayed().performClick()

        // Assert
        verify { fakeViewModel.clearMessages() }
    }

    @Test
    fun givenTextInInput_whenSendButtonIsClicked_thenViewModelIsNotified() {
        uiStateFlow.value = uiStateFlow.value.copy(isLoading = false)
        every { fakeViewModel.uiState } returns uiStateFlow

        // Arrange
        val messageText = "This is a test message"
        composeTestRule.onNodeWithTag("messageChatInput").performTextInput(messageText)

        // Act
        composeTestRule.onNodeWithTag("sendBtn").performClick()

        // Assert
        verify { fakeViewModel.onMessageTextChanged(messageText) }
        verify { fakeViewModel.sendMessage() }
    }

    @Test
    fun givenMessageInputIsEmpty_whenSendButtonIsClicked_thenActionIsIgnored() {
        // Arrange
        uiStateFlow.value = uiStateFlow.value.copy(messageText = "", isLoading = false)
        every { fakeViewModel.uiState } returns uiStateFlow

        // Act
        composeTestRule.onNodeWithTag("sendBtn").performClick()

        // Assert
        verify(exactly = 1) { fakeViewModel.sendMessage() }
    }

    @Test
    fun givenMessageInputWithText_whenSendButtonIsClicked_thenMessageIsSent() {
        uiStateFlow.value = uiStateFlow.value.copy(isLoading = false)
        every { fakeViewModel.uiState } returns uiStateFlow

        // Arrange
        composeTestRule.onNodeWithTag("messageChatInput").performTextInput("This is a test message")

        // Act
        composeTestRule.onNodeWithTag("sendBtn").performClick()

        // Assert
        verify(exactly = 1) { fakeViewModel.sendMessage() }
    }
    
    @Test
    fun givenMessageInputIsNotEmpty_whenSendButtonIsEnabledAndClicked_thenSendsMessage() {
        // Arrange
        uiStateFlow.value = uiStateFlow.value.copy(messageText = "Hi there", isLoading = false)
        every { fakeViewModel.uiState } returns uiStateFlow

        // Act
        composeTestRule.onNodeWithTag("sendBtn").assertIsEnabled().performClick()
        
        // Assert
        verify { fakeViewModel.sendMessage() }
    }

    @Test
    fun givenErrorState_whenScreenIsDisplayed_thenSnackbarWithErrorIsShown() {
        // Arrange
        val errorMessage = "Error sending message"
        uiStateFlow.value = uiStateFlow.value.copy(error = errorMessage)
        every { fakeViewModel.uiState } returns uiStateFlow

        // Assert
        composeTestRule.waitForIdle() // waits until Compose is done with recomposition
        composeTestRule.onNodeWithText(errorMessage, useUnmergedTree = true).assertExists()
    }

    @Test
    fun givenNoSelectedUser_whenScreenIsDisplayed_thenToolbarShowsDefaultName() {
        // Arrange
        uiStateFlow.value = uiStateFlow.value.copy(selectedUser = null, userList = emptyList())
        every { fakeViewModel.uiState } returns uiStateFlow

        // Assert
        composeTestRule.onNodeWithText("Unknown User").assertIsDisplayed()
    }

    @Test
    fun givenNoMessages_whenScreenIsDisplayed_thenEmptyMessageListIsShown() {
        // Arrange
        uiStateFlow.value = uiStateFlow.value.copy(messages = emptyList(), isLoading = false)
        every { fakeViewModel.uiState } returns uiStateFlow

        // Assert
        composeTestRule.onNodeWithText("Looks like you don\'t have any chat").assertIsDisplayed()
    }
}
