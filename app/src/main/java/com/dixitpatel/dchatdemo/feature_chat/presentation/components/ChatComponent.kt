package com.dixitpatel.dchatdemo.feature_chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.dixitpatel.dchatdemo.R
import com.dixitpatel.dchatdemo.feature_chat.data.consts.ALEX_USER_ID
import com.dixitpatel.dchatdemo.feature_chat.data.consts.SARA_USER_ID
import com.dixitpatel.dchatdemo.feature_chat.domain.models.Message
import com.dixitpatel.dchatdemo.feature_chat.domain.models.MessageGroup
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.DChatDemoTheme
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.DarkGray
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.LightGray
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.MessageBubbleReceived
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.MessageBubbleSent
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.MessageSentDoubleTick
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.MessageSentSingleTick
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.PrimaryPink
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.SecondaryPink
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.dimens
import java.time.LocalDateTime

/**
 * A Composable that displays a group of messages from the same sender in a chat.
 *
 * This component arranges messages within a `Column`. It optionally displays a timestamp
 * at the top of the group if `isTimestampShow` is true. It then iterates through the
 * list of messages in the group, rendering each one using the `MessageBubbleItem` composable.
 *
 * @param messageGroup The [MessageGroup] object containing the list of messages and an optional timestamp.
 * @param currentUserId The ID of the currently logged-in user, used to determine if a message
 *                      is sent by them (for alignment and styling purposes).
 */
@Composable
fun MessageGroupItem(
    messageGroup: MessageGroup, currentUserId: String
) {
    Column(Modifier.testTag("messageList")) {
        // Timestamp centered at top of group
        messageGroup.timestamp?.takeIf { messageGroup.isTimestampShow }?.let { label ->
            TimestampLabel(text = label)
            Spacer(Modifier.height(6.dp))
        }

        messageGroup.messages.forEach { message ->
            MessageBubbleItem(
                message = message, isFromCurrentUser = message.senderId == currentUserId
            )
            Spacer(Modifier.height(4.dp))
        }
    }
}

/**
 * A composable that displays a centered timestamp label within a full-width box.
 * This is typically used to show dates or times as separators between groups of messages.
 *
 * @param text The string content of the timestamp to be displayed.
 */
@Composable
private fun TimestampLabel(text: String) {
    Box(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

/**
 * Displays a single chat message bubble.
 *
 * This composable function is responsible for rendering an individual message. It aligns
 * the bubble to the start or end of the row based on whether the message is from the
 * current user. The bubble's shape, color, and text color are also styled accordingly.
 *
 * For messages sent by the current user, it includes a read/delivered status icon
 * (a single or double tick) neatly positioned below the message text. This is achieved
 * using a `SubcomposeLayout` to ensure the icon row has the same width as the text and
 * is placed correctly without disrupting the bubble's size calculation.
 *
 * @param message The [Message] object containing the content and metadata of the message.
 * @param isFromCurrentUser A boolean flag indicating if the message was sent by the
 *                          currently logged-in user. This determines the bubble's alignment
 *                          and styling.
 */
@Composable
fun MessageBubbleItem(
    message: Message, isFromCurrentUser: Boolean
) {
    val bubbleColor = if (isFromCurrentUser) MessageBubbleSent else MessageBubbleReceived
    val textColor =
        if (isFromCurrentUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
    ) {

        Card(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .padding(
                    start = if (isFromCurrentUser) MaterialTheme.dimens.xl3 else 0.dp,
                    end = if (!isFromCurrentUser) MaterialTheme.dimens.xl3 else 0.dp,
                    top = 3.dp
                ), colors = CardDefaults.cardColors(
                containerColor = bubbleColor
            ), shape = RoundedCornerShape(
                topStart = MaterialTheme.dimens.ml,
                topEnd = MaterialTheme.dimens.ml,
                bottomStart = if (!isFromCurrentUser) 1.dp else MaterialTheme.dimens.ml,
                bottomEnd = if (isFromCurrentUser) 1.dp else MaterialTheme.dimens.ml
            )
        ) {
            Box(
                modifier = Modifier.padding(MaterialTheme.dimens.s)
            ) {
                SubcomposeLayout { constraints ->
                    val textPlaceable = subcompose("text") {
                        Text(
                            text = message.content,
                            color = textColor,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }[0].measure(constraints)

                    val iconRowPlaceable = subcompose("iconRow") {
                        if (isFromCurrentUser) {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_tick_green),
                                    contentDescription = if (message.isRead) {
                                        stringResource(R.string.message_read_desc)
                                    } else {
                                        stringResource(R.string.message_delivered_desc)
                                    },
                                    tint = if (message.isRead) MessageSentDoubleTick else MessageSentSingleTick,
                                    modifier = Modifier.size(MaterialTheme.dimens.m)
                                )
                            }
                        }
                    }.map {
                        // Constrain the width of the icon Row to be exactly the width of the Text
                        it.measure(Constraints.fixedWidth(textPlaceable.width))
                    }

                    // Calculate the total height needed
                    val totalHeight = textPlaceable.height + (iconRowPlaceable.firstOrNull()?.height
                        ?: 0) + if (isFromCurrentUser && iconRowPlaceable.isNotEmpty()) 4.dp.roundToPx() else 0

                    layout(textPlaceable.width, totalHeight) {
                        textPlaceable.placeRelative(0, 0)

                        iconRowPlaceable.firstOrNull()?.placeRelative(
                            0, textPlaceable.height + if (isFromCurrentUser) 6.dp.roundToPx() else 0
                        )
                    }
                }
            }
        }
    }
}

/**
 * A composable that provides a text input field and a send button for a chat interface.
 *
 * This component consists of an `OutlinedTextField` for message input and a `FloatingActionButton`
 * to send the message. The layout is a `Row` that fills the screen's width and handles
 * padding for the soft keyboard (`imePadding`). The send button's appearance and enabled
 * state are conditional on whether the `messageText` is blank or not.
 *
 * @param messageText The current text value of the input field.
 * @param onMessageTextChanged A callback function that is invoked when the user types in the
 *                             input field. It provides the updated text.
 * @param onSendMessage A callback function that is invoked when the user clicks the send button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageChatInput(
    messageText: String, onMessageTextChanged: (String) -> Unit, onSendMessage: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.dimens.xs)
            .imePadding(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = onMessageTextChanged,
            modifier = Modifier
                .weight(1f)
                .testTag("messageChatInput"),
            shape = RoundedCornerShape(32.dp),
            maxLines = 6,
            textStyle = MaterialTheme.typography.titleLarge,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (messageText.isNotBlank()) PrimaryPink else LightGray,
                unfocusedBorderColor = LightGray,
                disabledBorderColor = DarkGray,
                focusedLabelColor = PrimaryPink,
                unfocusedLabelColor = SecondaryPink,
                disabledLabelColor = SecondaryPink
            ),
        )

        Spacer(modifier = Modifier.width(6.dp))
        val sendEnabled = messageText.isNotBlank()

        FloatingActionButton(
            onClick = onSendMessage,
            modifier = Modifier
                .size(MaterialTheme.dimens.xl3)
                .clip(CircleShape)
                .testTag("sendBtn"),
            containerColor = if (messageText.isNotBlank()) PrimaryPink else SecondaryPink
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                if (sendEnabled) Color(0xFFF52D7B) else Color.Gray.copy(.2f),
                                if (sendEnabled) Color(0xFFF66D6D) else Color.Gray.copy(.2f)
                            )
                        )
                    )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = stringResource(R.string.send_message_content_desc),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
@Preview
fun MessageGroupItemView() {
    DChatDemoTheme {
        MessageGroupItem(
            MessageGroup(
                messages = listOf(
                    Message("1", "Hello", SARA_USER_ID, LocalDateTime.now(), false),
                    Message("2", "Hi there", ALEX_USER_ID, LocalDateTime.now(), false),
                    Message("2", "Hi there", ALEX_USER_ID, LocalDateTime.now(), false),
                    Message("3", "How are you ?", SARA_USER_ID, LocalDateTime.now(), true),
                    Message("3", "I'm Good thanks", SARA_USER_ID, LocalDateTime.now(), true)
                )
            ), currentUserId = SARA_USER_ID
        )
    }
}

@Composable
@Preview
fun MessageChatInputView() {
    DChatDemoTheme {
        MessageChatInput("", onMessageTextChanged = {}, onSendMessage = {})
    }
}

