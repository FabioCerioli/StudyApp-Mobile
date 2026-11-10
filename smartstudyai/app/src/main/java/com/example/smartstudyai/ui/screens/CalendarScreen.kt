package com.example.smartstudyai.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartstudyai.viewmodel.CalendarSchedule

@Composable
fun CalendarScreen(schedules: List<CalendarSchedule>) {

    val days = listOf(
        "Lunedì",
        "Martedì",
        "Mercoledì",
        "Giovedì",
        "Venerdì",
        "Sabato",
        "Domenica"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text( text = "Calendario Settimanale",
                style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Numero schedule: ${schedules.size}")
        }

        items(days) { day ->
            val daySchedules = schedules
                    .filter { it.schedule.day == day }
                    .sortedBy { it.schedule.startTime }
            DaySection(day = day, schedules = daySchedules)
        }
    }
}

@Composable
fun DaySection(day: String, schedules: List<CalendarSchedule>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = day, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        if (schedules.isEmpty()) {
            Text(text = "Nessuna attività", style = MaterialTheme.typography.bodyMedium)
        } else {
            schedules.forEach { item ->
                ScheduleCard(item)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
@Composable
fun ScheduleCard(item: CalendarSchedule){
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "${item.schedule.startTime} - " + item.schedule.endTime,
                style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.activity?.title ?: "Attività sconosciuta")
        }
    }
}