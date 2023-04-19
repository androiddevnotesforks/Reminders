package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreOptionsPicker(
    isVisible: Boolean,
    sheetState: SheetState,
    onDelete: () -> Unit,
    onCopy: () -> Unit,
    onLabels: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    if (isVisible)
        ModalBottomSheet(
            onDismissRequest = {
                if (sheetState.isVisible)
                    scope.launch { sheetState.hide() }
            },
            modifier = modifier,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                ListItem(
                    headlineContent = { Text(text = "Delete") },
                    supportingContent = { Text(text = "Delete this Task") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = "Delete the item"
                        )
                    },
                    modifier = Modifier.clickable(onClick = onDelete, role = Role.Button)
                )
                ListItem(
                    headlineContent = { Text(text = "Make A Copy") },
                    supportingContent = { Text(text = "Make a second copy of the task") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = "Delete the item"
                        )
                    },
                    modifier = Modifier.clickable(onClick = onCopy, role = Role.Button)
                )
                ListItem(
                    headlineContent = { Text(text = "Labels") },
                    supportingContent = { Text(text = "Add labels to the Task") },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Label,
                            contentDescription = "Label"
                        )
                    },
                    modifier = Modifier.clickable(onClick = onLabels, role = Role.Button)
                )
            }
        }
}