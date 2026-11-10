package com.example.smartstudyai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartstudyai.data.entity.StudyActivity
import com.example.smartstudyai.data.entity.StudySchedule

@Composable
fun EditActivityScreen(
    activity: StudyActivity,
    schedules: List<StudySchedule>,
    onSave: (
        StudyActivity,
        List<StudySchedule>
    ) -> Unit,
    onBack: () -> Unit
) {
    var title by remember(activity.activityId) { mutableStateOf(activity.title) }
    var description by remember(activity.activityId) { mutableStateOf(activity.description) }

    val days = listOf(
        "Lunedì",
        "Martedì",
        "Mercoledì",
        "Giovedì",
        "Venerdì",
        "Sabato",
        "Domenica"
    )
    val selectedDays =
        remember(activity.activityId, schedules) {
            mutableStateMapOf<String, Pair<String, String>>().apply {
                schedules.forEach { schedule ->
                    this[schedule.day] = Pair(schedule.startTime, schedule.endTime)
                }
            }
        }
    var showDaySelector by remember { mutableStateOf(false) }
    val hours = remember {
            List(48) { index ->
                val hour = index / 2
                val minute =
                    if (index % 2 == 0) "00"
                    else "30"
                String.format("%02d:%s", hour, minute)
            }
        }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text("Modifica Attività", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Titolo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrizione") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))
            Text("Giorni pianificati", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(10.dp))
        }
        items(selectedDays.keys.toList()) { day ->
            val value = selectedDays[day]
            DayCard(
                day = day,
                start = value?.first ?: "08:00",
                end = value?.second ?: "10:00",
                hours = hours,
                onChange = {
                        start,
                        end -> selectedDays[day] = Pair(start, end)
                },
                onDelete = { selectedDays.remove(day) }
            )
        }
        item {
            Spacer(Modifier.height(15.dp))
            Button(onClick = { showDaySelector = true }) {
                Text("+ Aggiungi giorno")
            }
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    val total = calculateHours(selectedDays)
                    val updatedActivity = activity.copy(
                            title = title,
                            description = description,
                            weeklyHours = total
                        )
                    val updatedSchedules = selectedDays.map { entry ->
                            StudySchedule(
                                activityId = activity.activityId,
                                day = entry.key,
                                startTime = entry.value.first,
                                endTime = entry.value.second
                            )
                        }
                    onSave(updatedActivity, updatedSchedules)
                }
            ) {
                Text("Salva Modifiche")
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = onBack) {
                Text("Annulla")
            }
            Spacer(Modifier.height(24.dp))
        }
    }
    if (showDaySelector) {
        AlertDialog(
            onDismissRequest = { showDaySelector = false },
            title = { Text("Seleziona giorno") },
            text = {
                Column {
                    days.forEach { day ->
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                if (!selectedDays.containsKey(day)) {
                                    selectedDays[day] = Pair("08:00", "10:00")
                                }
                                showDaySelector = false
                            }
                        ) {
                            Text(day)
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}
@Composable
fun EditDayCard(
    day: String,
    start: String,
    end: String,
    hours: List<String>,
    onChange: (String, String) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                day,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                var openStart by remember { mutableStateOf(false) }
                var openEnd by remember { mutableStateOf(false) }

                Box {
                    Button(
                        onClick = { openStart = true }
                    ) {
                        Text(start)
                    }
                    DropdownMenu(
                        expanded = openStart,
                        onDismissRequest = { openStart = false }
                    ) {
                        hours.forEach { hour ->
                            DropdownMenuItem(
                                text = {
                                    Text(hour)
                                },
                                onClick = {
                                    onChange(hour, end)
                                    openStart = false
                                }
                            )
                        }
                    }
                }


                Box {
                    Button(
                        onClick = { openEnd = true }
                    ) { Text(end)
                    }
                    DropdownMenu(
                        expanded = openEnd,
                        onDismissRequest = { openEnd = false }
                    ) {
                        hours.forEach { hour ->
                            DropdownMenuItem(
                                text = { Text(hour) },
                                onClick = {
                                    onChange(start, hour)
                                    openEnd = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Button(onClick = onDelete) {
                Text("Rimuovi giorno")
            }
        }
    }
}
