package com.example.smartstudyai.notification

import java.util.Calendar

fun dayToCalendar(day: String): Int {
    return when(day.lowercase()) {
        "lunedì", "lunedi" -> Calendar.MONDAY
        "martedì", "martedi" -> Calendar.TUESDAY
        "mercoledì", "mercoledi" -> Calendar.WEDNESDAY
        "giovedì", "giovedi" -> Calendar.THURSDAY
        "venerdì", "venerdi" -> Calendar.FRIDAY
        "sabato" -> Calendar.SATURDAY
        "domenica" -> Calendar.SUNDAY
        else -> Calendar.MONDAY
    }
}

fun parseHour(time: String): Int {
    return time .split(":")[0]  .toInt()
}
fun parseMinute(time: String): Int {
    return time .split(":")[1]  .toInt()
}