package com.eva.reminders.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.eva.reminders.presentation.feature_create.AddTaskViewModel
import com.eva.reminders.presentation.feature_create.CreateReminderRoute
import com.eva.reminders.presentation.feature_home.HomeRoute
import com.eva.reminders.presentation.feature_home.HomeViewModel
import com.eva.reminders.presentation.feature_labels.EditLabelRoute
import com.eva.reminders.presentation.feature_labels.LabelsViewModel
import com.eva.reminders.presentation.utils.LocalArrangementStyle
import com.eva.reminders.presentation.utils.NavConstants
import com.eva.reminders.presentation.utils.NavRoutes

@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier
) {
    val navHost = rememberNavController()
    NavHost(
        navController = navHost,
        startDestination = NavRoutes.Home.route,
        modifier = modifier
    ) {
        composable(NavRoutes.Home.route) {
            val labelsViewModel = hiltViewModel<LabelsViewModel>()
            val labels by labelsViewModel.allLabels.collectAsStateWithLifecycle()

            val homeViewModel = hiltViewModel<HomeViewModel>()
            val tasks by homeViewModel.tasks.collectAsStateWithLifecycle()
            val colorOptions by homeViewModel.colorOptions.collectAsStateWithLifecycle()
            val currentTab by homeViewModel.currentTab.collectAsStateWithLifecycle()
            val style by homeViewModel.arrangement.collectAsStateWithLifecycle()
            val searchResults by homeViewModel.searchedTasks.collectAsStateWithLifecycle()
            val homSearchBarState by homeViewModel.homeSearchBarState.collectAsStateWithLifecycle()

            CompositionLocalProvider(LocalArrangementStyle provides style) {
                HomeRoute(
                    navController = navHost,
                    labels = labels,
                    tasks = tasks,
                    tab = currentTab,
                    searchBarState = homSearchBarState,
                    searchResultsType = searchResults,
                    colorOptions = colorOptions,
                    onArrangementChange = homeViewModel::onArrangementChange,
                    onTabChange = homeViewModel::changeCurrentTab,
                    uiEvents = homeViewModel.uiEvents,
                    onSearchType = homeViewModel::onSearchType,
                    onSearchBarEvents = homeViewModel::onSearchBarEvents
                )
            }
        }
        composable(
            NavRoutes.AddTask.route + NavConstants.TASK_ID_QUERY_PARAMS,
            arguments = listOf(
                navArgument(NavConstants.TASK_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            val viewModel = hiltViewModel<AddTaskViewModel>()

            val addLabelViewModel = viewModel.addLabelToTasks
            val queriedLabels by addLabelViewModel.labelSelector.collectAsStateWithLifecycle()
            val query by addLabelViewModel.query.collectAsStateWithLifecycle()
            val content by viewModel.showContent.collectAsStateWithLifecycle()

            val pickedLabels by addLabelViewModel.selectedLabelsAsFlow.collectAsStateWithLifecycle()

            if (content.isLoading)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = { CircularProgressIndicator() }
                )
            AnimatedVisibility(
                visible = !content.isLoading,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CreateReminderRoute(
                    navController = navHost,
                    state = content.content,
                    labelSearchQuery = query,
                    onLabelSearchQuery = addLabelViewModel::searchLabels,
                    onNewLabelCreate = addLabelViewModel::createLabel,
                    onAddTaskEvents = viewModel::onAddTaskEvents,
                    queriedLabels = queriedLabels,
                    pickedLabels = pickedLabels.map { it.toModel() },
                    onLabelSelect = addLabelViewModel::onSelect,
                    uiEvents = viewModel.uiEvents
                )
            }

        }
        composable(NavRoutes.EditLabels.route) {
            val viewModel = hiltViewModel<LabelsViewModel>()
            val createState =
                viewModel.createLabelState.collectAsStateWithLifecycle()
            val editState =
                viewModel.updatedLabels.collectAsStateWithLifecycle()
            EditLabelRoute(
                navController = navHost,
                createLabelState = createState.value,
                editLabelState = editState.value,
                onCreateLabelEvent = viewModel::onCreateLabelEvent,
                onEditLabelEvent = viewModel::onUpdateLabelEvent,
                uiEvents = viewModel.uiEvents
            )
        }
    }
}