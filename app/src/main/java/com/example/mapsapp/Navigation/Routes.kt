package com.example.mapsapp.Navigation

sealed class Routes(val route: String) {
    object CamaraScreen : Routes("camara_screen")
    object MapsScreen : Routes("map_screen"){fun createRoute() = "map_screen" }

    object GalleryScreen : Routes("gallery_screen")
}