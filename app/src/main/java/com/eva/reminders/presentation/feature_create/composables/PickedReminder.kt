package com.eva.reminders.presentation.feature_create.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.eva.reminders.presentation.feature_create.utils.ReminderFrequency
import com.eva.reminders.presentation.feature_create.utils.TaskReminderState
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickedReminder(
    show: Boolean,
    state: TaskReminderState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = show,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        val data by remember(state) {
            derivedStateOf {
                val formattedTime = state.time.schedule
                    .format(DateTimeFormatter.ofPattern("h:mm a"))
                val repeat = when (state.frequency) {
                    ReminderFrequency.DO_NOT_REPEAT -> "Once"
                    ReminderFrequency.DAILY -> "Daily"
                }
                "${state.date.text}, $formattedTime ( $repeat )"
            }
        }
        FilterChip(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = "Reminder Time"
                )
            },
            onClick = onClick,
            label = { Text(text = data) },
            selected = true,
            modifier = modifier,
            colors = FilterChipDefaults.filterChipColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                iconColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            border = FilterChipDefaults.filterChipBorder(
                selectedBorderColor = MaterialTheme.colorScheme.onSurface
                    .copy(alpha = .75f)
            )
        )
    }
}

@Preview
@Composable
fun PickedReminderPreview() {
    PickedReminder(
        show = true,
        state = TaskReminderState(),
        onClick = {}
    )
}