package com.example.smartstudyai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CreateActivityScreen(
    onSave: (
        String,
        String,
        Float,
        Map<String, Pair<String, String>>
    ) -> Unit)
{
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("")}
    val days = listOf(
        "Lunedì",
        "Martedì",
        "Mercoledì",
        "Giovedì",
        "Venerdì",
        "Sabato",
        "Domenica"
    )
    val selectedDays = remember { mutableStateMapOf<String, Pair<String,String>>() }
    var showDaySelector by remember { mutableStateOf(false) }
    val hours =
        remember {
            List(48){ index ->
                val hour = index / 2
                val minute =
                    if(index % 2 == 0) "00"
                    else "30"
                String.format("%02d:%s", hour, minute)
            }
        }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item {
            Text("Nuova Attività", style = MaterialTheme.typography.headlineMedium)
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
        items(selectedDays.keys.toList() ){ day ->

            val value = selectedDays[day]
            DayCard(
                day = day,
                start = value?.first ?: "08:00",
                end = value?.second ?: "10:00",
                hours = hours,
                onChange = { start,end ->
                    selectedDays[day] = Pair(start, end)
                },
                onDelete = { selectedDays.remove(day) }
            )
        }

        item {
            Spacer(Modifier.height(15.dp))
            Button(
                onClick = { showDaySelector = true }
            ){
                Text("+ Aggiungi giorno")
            }
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    val total = calculateHours(selectedDays)
                    onSave(
                        title,
                        description,
                        total,
                        selectedDays.toMap()
                    )
                }
            ) {
                Text("Salva")
            }
        }
    }
    if(showDaySelector){
        AlertDialog(
            onDismissRequest = { showDaySelector=false },
            title = { Text("Seleziona giorno") },
            text = {
                Column {
                    days.forEach { day ->
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                if(!selectedDays.containsKey(day)){
                                    selectedDays[day] = Pair("08:00", "10:00")
                                }
                                showDaySelector=false
                            }
                        ){
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
fun DayCard(
    day:String,
    start:String,
    end:String,
    hours:List<String>,
    onChange:(String,String)->Unit,
    onDelete:()->Unit
){
    Card( modifier = Modifier.fillMaxWidth().padding(8.dp)){
        Column(modifier = Modifier.padding(12.dp)) {
            Text(day, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                var openStart by remember { mutableStateOf(false) }
                var openEnd by remember { mutableStateOf(false) }
                Box{
                    Button(onClick = { openStart=true }) {
                        Text(start)
                    }
                    DropdownMenu(
                        expanded=openStart,
                        onDismissRequest={ openStart=false }
                    ){
                        hours.forEach {
                            DropdownMenuItem(
                                text={ Text(it) },
                                onClick={
                                    onChange(it, end)
                                    openStart=false
                                }
                            )
                        }
                    }
                }
                Box{
                    Button(onClick={ openEnd=true }
                    ){
                        Text(end)
                    }

                    DropdownMenu(
                        expanded=openEnd,
                        onDismissRequest={ openEnd=false }
                    ){
                        hours.forEach {
                            DropdownMenuItem(
                                text={ Text(it) },
                                onClick={
                                    onChange(start, it)
                                    openEnd=false
                                }
                            )
                        }
                    }

                }
            }
            Spacer(Modifier.height(10.dp))
            Button(onClick = onDelete){
                Text("Rimuovi giorno")
            }
        }
    }
}






fun calculateHours(schedule: Map<String,Pair<String,String>>):Float{

    var total = 0f
    schedule.values.forEach {
        val start = it.first.split(":")
        val end = it.second.split(":")
        val startMinutes = start[0].toInt()*60 + start[1].toInt()
        val endMinutes = end[0].toInt()*60 + end[1].toInt()
        if(endMinutes > startMinutes){
            total += (endMinutes-startMinutes)/60f
        }
    }
    return total
}