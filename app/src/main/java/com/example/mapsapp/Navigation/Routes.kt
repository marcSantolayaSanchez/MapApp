package com.example.mapsapp.Navigation

sealed class Routes(val route: String) {
    object CamaraScreen : Routes("camara_screen")
    object MapScreen : Routes("map_screen"){fun createRoute() = "map_screen" }

    object GalleryScreen : Routes("gallery_screen")

    object RegisterScreen : Routes("register_screen")

    object LoginScreen : Routes("login_screen")

    object MarcadoresGuardados : Routes("marcadores_screen")

    object DescripcionScreen : Routes("descripcion_screen")
}