package com.example.smartstudyai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartstudyai.data.entity.StudyActivity

@Composable
fun ActivityCard(
    activity: StudyActivity,
    onDelete: () -> Unit,
    onStart: () -> Unit,
    onEdit: () -> Unit,
    onStatistics: () -> Unit
){

    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = activity.title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(activity.description)
            Spacer(Modifier.height(8.dp))
            Text("Ore settimanali: ${activity.weeklyHours}")

            Spacer(Modifier.height(8.dp))

            Row {
                Button(onClick = onStart) {
                    Text("▶️")
                }
                Spacer(modifier = Modifier.width(5.dp))
                Button(onClick = onEdit) {
                    Text("✏️")
                }
                Spacer(modifier = Modifier.width(5.dp))
                Button(onClick = onStatistics) {
                    Text("📊")
                }
                Spacer(modifier = Modifier.width(5.dp))
                Button(onClick = onDelete) {
                    Text("🗑️")
                }
            }
        }
    }
}


@Composable
fun ActivityListScreen(
    activities: List<StudyActivity>,
    onDelete: (StudyActivity) -> Unit,
    onAddClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onStart: (StudyActivity) -> Unit,
    onEdit: (StudyActivity) -> Unit,
    onStatistics: (StudyActivity) -> Unit
) {

    Scaffold(
        floatingActionButton = {
            Row {
                FloatingActionButton(onClick = onAddClick) {
                    Text("+")
                }
                Spacer(modifier = Modifier.width(12.dp))
                FloatingActionButton(onClick = onCalendarClick) {
                    Text("📅")
                }
            }
        }
    ) { padding ->

        LazyColumn(modifier = Modifier.padding(padding)) {
            items(activities) { activity ->

                ActivityCard(
                    activity = activity,
                    onDelete = { onDelete(activity) },
                    onStart = { onStart(activity) },
                    onEdit = {onEdit(activity) },
                    onStatistics = { onStatistics(activity) }
                )
            }
        }
    }
}