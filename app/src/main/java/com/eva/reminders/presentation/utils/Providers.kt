package com.eva.reminders.presentation.utils

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import com.eva.reminders.presentation.feature_home.utils.TaskArrangementStyle

val LocalArrangementStyle = compositionLocalOf { TaskArrangementStyle.BLOCK_STYLE }

val LocalSnackBarHostProvider = compositionLocalOf { SnackbarHostState() }