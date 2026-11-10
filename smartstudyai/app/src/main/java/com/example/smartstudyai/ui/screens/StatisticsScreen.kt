package com.example.smartstudyai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartstudyai.viewmodel.StatisticsViewModel

@Composable
fun StatisticsScreen(
    activityId: Long,
    viewModel: StatisticsViewModel,
    onBack: () -> Unit
) {
    val activityTitle by viewModel.activityTitle.collectAsState()
    val statistics by viewModel.statistics.collectAsState()
    val sessions by viewModel.sessions.collectAsState()

    LaunchedEffect(activityId) {
        viewModel.loadStatistics(activityId)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {

        item {
            Text(
                text = activityTitle,
                style = MaterialTheme.typography.headlineMedium
            )

        }
        item {
            Card(modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Statistiche generali",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(text = "Sessioni totali: " + statistics.totalSessions)
                    Spacer(Modifier.height(8.dp))
                    Text(text = "Tempo totale: " + formatDuration(statistics.totalDurationSeconds))
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Confidenza: " + "%.1f%%".format(statistics.averageConfidence * 100)
                    )
                }
            }
        }

        item {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Sessioni",
                style = MaterialTheme.typography.titleLarge
            )

        }

        items(
            items = sessions,
            key = { session -> session.id }
        ) { session ->
            Card(modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp) ) {
                    Text(
                        text = "Sessione #${session.id}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))

                    Text(text = "Durata: " + formatDuration(session.durationSeconds))
                    Spacer(Modifier.height(4.dp))
                    Text(text = "Predizione: " + session.prediction)
                    Spacer(Modifier.height(4.dp))
                    Text(text = "Confidenza: " + "%.1f%%".format(session.averageConfidence * 100))
                }
            }
        }

        if (sessions.isEmpty()) {
            item {
                Text(
                    text = "Non sono ancora presenti sessioni.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
            Button(onClick = onBack) {
                Text("Indietro")
            }
        }
    }
}

fun formatDuration(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return String.format("%02dh %02dm %02ds", hours, minutes, remainingSeconds)

}