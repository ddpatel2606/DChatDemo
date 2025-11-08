package com.dixitpatel.dchatdemo.feature_chat.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dixitpatel.dchatdemo.R
import com.dixitpatel.dchatdemo.feature_chat.domain.models.User
import com.dixitpatel.dchatdemo.feature_chat.presentation.chatlistscreen.ChatListViewModel
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.DarkGray
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.LightGray
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.MessageBubbleReceived
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.PrimaryPink
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.dimens

/**
 * A composable function that displays the top app bar for the chat screen.
 * It includes a back button, the current user's avatar and name, and an options menu.
 *
 * @param uiState The current state of the chat UI, containing information like the selected user.
 * @param onBackClick A lambda function to be invoked when the back button is clicked.
 * @param onToggleUserSheet A lambda function to be invoked when the user's name or avatar is clicked, typically to show a user selection sheet.
 * @param onShowMenu A lambda function to be invoked to show the dropdown menu.
 * @param showMenu A boolean indicating whether the dropdown menu should be shown.
 * @param onClearChat A lambda function to be invoked when the "Clear Chat" option is selected from the menu.
 * @param onDismiss A lambda function to be invoked when the dropdown menu is dismissed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    uiState: ChatListViewModel.ChatUIState,
    onBackClick: () -> Unit,
    onToggleUserSheet: () -> Unit,
    onShowMenu: () -> Unit,
    showMenu: Boolean,
    onClearChat: () -> Unit,
    onDismiss: () -> Unit,
) {
    TopAppBar(navigationIcon = {
        IconButton(modifier = Modifier.testTag("backBtn"), onClick = onBackClick) {
            Icon(
                painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back_content_desc),
                tint = PrimaryPink
            )
        }
    }, title = {
        Row(
            modifier = Modifier.clickable { onToggleUserSheet() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(uiState.selectedUser?.profilePic ?: R.drawable.ic_empty_profile)
            Spacer(Modifier.width(MaterialTheme.dimens.s))
            Text(
                text = uiState.selectedUser?.name ?: stringResource(R.string.unknown_user),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            IconButton(modifier = Modifier.testTag("arrowBtn"), onClick =  { onToggleUserSheet() }) {
                Icon(
                    painterResource(R.drawable.ic_arrow_down),
                    contentDescription = stringResource(R.string.arrow_down_content_desc),
                    tint = LightGray
                )
            }
        }
    }, actions = {
        DropdownMenuButton(
            expanded = showMenu,
            onShowMenu = onShowMenu,
            onDismiss = onDismiss,
            onClearChat = onClearChat
        )
    })
}

/**
 * A composable function that displays a circular user avatar.
 * The avatar is a clipped circular image with a pink background.
 *
 * @param image The drawable resource ID for the user's avatar image.
 */
@Composable
private fun UserAvatar(image: Int) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(PrimaryPink),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = stringResource(R.string.profile_picture_content_desc)
        )
    }
}

/**
 * A composable that displays a menu button (three dots icon) which, when clicked,
 * shows a dropdown menu. The menu currently contains an option to "Clear chat".
 *
 * @param expanded Whether the dropdown menu is currently visible.
 * @param onShowMenu Lambda to be invoked when the menu button is clicked, used to show the menu.
 * @param onDismiss Lambda to be invoked when the user requests to dismiss the menu (e.g., by tapping outside of it).
 * @param onClearChat Lambda to be invoked when the "Clear chat" menu item is clicked.
 */
@Composable
private fun DropdownMenuButton(
    expanded: Boolean, onShowMenu: () -> Unit, onDismiss: () -> Unit, onClearChat: () -> Unit
) {
    Box {
        IconButton(onClick = onShowMenu) {
            Icon(
                painterResource(R.drawable.ic_menu),
                contentDescription = stringResource(id = R.string.menu_options_content_desc),
                tint = DarkGray
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.clear_chat),
                        style = MaterialTheme.typography.titleMedium
                    )
                }, onClick = onClearChat
            )
        }
    }
}

/**
 * A composable that displays a sheet for selecting a user from a list.
 * The sheet appears with a slide-down animation and contains a list of available users.
 * The currently selected user is highlighted.
 *
 * @param modifier The modifier to be applied to the component.
 * @param visible Controls the visibility of the sheet. If true, the sheet is shown.
 * @param users The list of [User] objects to display.
 * @param selected The ID of the currently selected user, used for highlighting. Can be null if no user is selected.
 * @param onUserClick A lambda function that is invoked when a user row is clicked, passing the selected [User].
 */
@Composable
fun UserSelectionSheet(
    modifier: Modifier = Modifier,
    visible: Boolean,
    users: List<User>,
    selected: String?,
    onUserClick: (User) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
        modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .shadow(2.dp).testTag("userSheet"),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(MaterialTheme.dimens.s)) {
                Text(
                    stringResource(R.string.select_user),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(MaterialTheme.dimens.xs))

                users.forEach { user ->
                    val selectedBg = if (user.id == selected) MessageBubbleReceived
                    else MaterialTheme.colorScheme.surface

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onUserClick(user) }
                        .background(selectedBg)
                        .padding(10.dp)
                        .testTag("userSheetRow")) {
                        UserAvatar(user.profilePic)
                        Spacer(Modifier.width(MaterialTheme.dimens.s))
                        Text(
                            user.name, style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}