package com.example.smartstudyai.navigation

sealed class Screen(
    val route: String
) {

    object Home : Screen("home")
    object Create : Screen("create")
    object Edit : Screen("edit")
    object Session : Screen("session")
    object Calendar : Screen("calendar")
    object Statistics : Screen("statistics")

}