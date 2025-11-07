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
            Spacer(Modifier.width(12.dp))
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
            Column(Modifier.padding(12.dp)) {
                Text(
                    stringResource(R.string.select_user),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))

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
                        Spacer(Modifier.width(12.dp))
                        Text(
                            user.name, style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}