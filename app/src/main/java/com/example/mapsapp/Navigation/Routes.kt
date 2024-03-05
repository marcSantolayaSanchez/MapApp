package com.example.mapsapp.Navigation

sealed class Routes(val route: String) {
    object MapsScreen : Routes("map_screen"){fun createRoute() = "map_screen" }
}