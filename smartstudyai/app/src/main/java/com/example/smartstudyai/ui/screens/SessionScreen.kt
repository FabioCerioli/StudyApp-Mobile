package com.example.smartstudyai.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartstudyai.viewmodel.SessionActivityViewModel
import com.example.smartstudyai.viewmodel.StudySessionViewModel

@Composable
fun SessionScreen(
    activityId: Long,
    sessionNumber: Int,
    activityViewModel: SessionActivityViewModel,
    viewModel: StudySessionViewModel,
    onBack: () -> Unit
) {
    val isStudying by viewModel.isStudying.collectAsState()
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val prediction by viewModel.prediction.collectAsState()
    val confidence by viewModel.confidence.collectAsState()
    val connectionError by viewModel.connectionError.collectAsState()
    val hasPrediction by viewModel.hasPrediction.collectAsState()
    val activity by activityViewModel.activity.collectAsState()

    LaunchedEffect(activityId) {
        activityViewModel.loadActivity(activityId)
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Sessione di studio",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = activity?.title ?: "Caricamento...",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = formatTime(elapsedTime),
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            text = "Tempo di sessione",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                when {
                    connectionError -> {
                        Text(
                            text = "ERRORE DI CONNESSIONE",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    hasPrediction -> {
                        val isStudy = prediction.equals("study", ignoreCase = true)
                        Text(
                            text =
                                if (isStudy) "STUDIO"
                                else "NON STUDIO",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Confidenza: ${ String.format("%.1f%%", confidence * 100) }",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        if (!isStudying) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { onBack() }
                ) {
                    Text("Indietro")
                }
                Button(
                    onClick = { viewModel.startSession(activityId) }
                ) {Text("Avvia") }
            }
        } else {
            Button(
                onClick = { viewModel.stopSession() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Termina sessione") }
        }
    }
}

fun formatTime(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    return String.format(
        "%02d:%02d:%02d", hours, minutes, remainingSeconds
    )

}