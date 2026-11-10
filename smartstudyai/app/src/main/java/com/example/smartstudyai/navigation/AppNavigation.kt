package com.example.smartstudyai.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.smartstudyai.data.entity.StudySchedule
import com.example.smartstudyai.data.repository.ActivityRepository
import com.example.smartstudyai.data.repository.ScheduleRepository
import com.example.smartstudyai.data.repository.SensorRepository
import com.example.smartstudyai.data.repository.SessionRepository
import com.example.smartstudyai.notification.ReminderScheduler

import com.example.smartstudyai.ui.screens.ActivityListScreen
import com.example.smartstudyai.ui.screens.CalendarScreen
import com.example.smartstudyai.ui.screens.CreateActivityScreen
import com.example.smartstudyai.ui.screens.EditActivityScreen
import com.example.smartstudyai.ui.screens.SessionScreen
import com.example.smartstudyai.ui.screens.StatisticsScreen

import com.example.smartstudyai.viewmodel.ActivityViewModel
import com.example.smartstudyai.viewmodel.ActivityViewModelFactory
import com.example.smartstudyai.viewmodel.CalendarViewModel
import com.example.smartstudyai.viewmodel.CalendarViewModelFactory
import com.example.smartstudyai.viewmodel.ScheduleViewModel
import com.example.smartstudyai.viewmodel.ScheduleViewModelFactory
import com.example.smartstudyai.viewmodel.SessionActivityViewModel
import com.example.smartstudyai.viewmodel.SessionActivityViewModelFactory
import com.example.smartstudyai.viewmodel.StatisticsViewModel
import com.example.smartstudyai.viewmodel.StatisticsViewModelFactory
import com.example.smartstudyai.viewmodel.StudySessionViewModel
import com.example.smartstudyai.viewmodel.StudySessionViewModelFactory

import com.example.smartstudyai.notification.dayToCalendar
import com.example.smartstudyai.notification.parseHour
import com.example.smartstudyai.notification.parseMinute

@Composable
fun AppNavigation(
    activityRepository: ActivityRepository,
    sensorRepository: SensorRepository,
    sessionRepository: SessionRepository,
    scheduleRepository: ScheduleRepository
) {

    val navController = rememberNavController()

    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {

            val factory = ActivityViewModelFactory(activityRepository)

            val viewModel = viewModel<ActivityViewModel>(factory = factory)

            val activities by viewModel.activities.collectAsState(initial = emptyList())

            ActivityListScreen(

                activities = activities,

                onDelete = { viewModel.deleteActivity(it) },

                onAddClick = {
                    navController.navigate(Screen.Create.route)
                },

                onCalendarClick = {
                    navController.navigate(Screen.Calendar.route)
                },

                onStart = { activity ->
                    navController.navigate("session/${activity.activityId}")
                },

                onEdit = { activity ->
                    navController.navigate("edit/${activity.activityId}")
                },

                onStatistics = { activity ->
                    navController.navigate("statistics/${activity.activityId}")
                }
            )
        }

        composable(Screen.Create.route) {

            val activityFactory = ActivityViewModelFactory(activityRepository)

            val activityViewModel = viewModel<ActivityViewModel>(factory = activityFactory)

            val scheduleFactory = ScheduleViewModelFactory(scheduleRepository)

            val scheduleViewModel = viewModel<ScheduleViewModel>(factory = scheduleFactory)

            CreateActivityScreen(

                onSave = {
                        title,
                        description,
                        hours,
                        selectedDays ->

                    activityViewModel.addActivity(
                        title = title,
                        description = description,
                        weeklyHours = hours,
                        deadline = System.currentTimeMillis()

                    ) { activityId ->
                        val schedules =
                            selectedDays.map { entry ->
                                StudySchedule(
                                    activityId = activityId,
                                    day = entry.key,
                                    startTime = entry.value.first,
                                    endTime = entry.value.second
                                )
                            }

                        scheduleViewModel.replaceSchedules(
                            activityId = activityId,
                            schedules = schedules
                        ) {

                            schedules.forEach { schedule ->
                                ReminderScheduler.scheduleReminder(
                                    context = context,
                                    title = title,
                                    dayOfWeek = dayToCalendar(schedule.day),
                                    hour = parseHour(schedule.startTime),
                                    minute = parseMinute(schedule.startTime)
                                )
                            }
                            navController.popBackStack()
                        }
                    }
                }
            )
        }



        composable(Screen.Calendar.route) {
            val factory = CalendarViewModelFactory(activityRepository, scheduleRepository)
            val viewModel = viewModel<CalendarViewModel>(factory = factory)
            val schedules by viewModel.schedules.collectAsState(initial = emptyList())
            CalendarScreen(schedules = schedules)
        }


        composable(route = "edit/{activityId}") { backStackEntry ->

            val activityId =
                backStackEntry.arguments
                    ?.getString("activityId")
                    ?.toLongOrNull()
                    ?: return@composable

            val activityFactory = ActivityViewModelFactory(activityRepository)

            val activityViewModel = viewModel<ActivityViewModel>(factory = activityFactory)

            val scheduleFactory = ScheduleViewModelFactory(scheduleRepository)

            val scheduleViewModel = viewModel<ScheduleViewModel>(factory = scheduleFactory)

            LaunchedEffect(activityId) { activityViewModel.loadActivity(activityId) }

            val activity by activityViewModel.selectedActivity.collectAsState()

            if (activity != null) {
                val schedules =
                    produceState(initialValue = emptyList() ){
                        value = scheduleRepository.getSchedules(activityId)
                    }

                EditActivityScreen(
                    activity = activity!!,
                    schedules = schedules.value,

                    onSave = {
                            updatedActivity,
                            updatedSchedules ->

                        activityViewModel.updateActivity(updatedActivity)

                        scheduleViewModel.replaceSchedules(
                            activityId = updatedActivity.activityId,
                            schedules = updatedSchedules
                        )

                        navController.popBackStack()
                    },

                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }


        composable(route = "session/{activityId}") { backStackEntry ->

            val activityId =
                backStackEntry.arguments
                    ?.getString("activityId")
                    ?.toLongOrNull()
                    ?: 0L

            val activityFactory = SessionActivityViewModelFactory(activityRepository)

            val activityViewModel = viewModel<SessionActivityViewModel>(factory = activityFactory)

            val sessionFactory = StudySessionViewModelFactory(sensorRepository, sessionRepository)

            val sessionViewModel = viewModel<StudySessionViewModel>(factory = sessionFactory)

            SessionScreen(
                activityId = activityId,
                sessionNumber = 1,
                activityViewModel = activityViewModel,
                viewModel = sessionViewModel,
                onBack = { navController.popBackStack() }
            )
        }



        composable(route = "statistics/{activityId}") { backStackEntry ->

            val activityId =
                backStackEntry.arguments
                    ?.getString("activityId")
                    ?.toLongOrNull()
                    ?: 0L

            val factory =
                StatisticsViewModelFactory(
                    activityRepository,
                    sessionRepository
                )

            val viewModel = viewModel<StatisticsViewModel>(factory = factory)

            StatisticsScreen(
                activityId = activityId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}