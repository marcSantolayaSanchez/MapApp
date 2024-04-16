package Model

import com.example.mapsapp.viewModel.MapAppViewModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Repository {
    private val database = FirebaseFirestore.getInstance()
    fun addMarker(marker: MapAppViewModel.Info){
        database.collection("markers")
            .add(
                hashMapOf(
                    "uid" to marker.usurioId,
                    "titulo" to marker.titulo,
                    "latitud" to marker.latitud,
                    "longitud" to marker.longitud,
                    "descripcion" to marker.descripcion,
                    "imagen" to marker.imagen
                )
            )
    }
    fun editMarker(editedMarker : MapAppViewModel.Info){
        database.collection("markers").document(editedMarker.markerId!!).set(
            hashMapOf(
                "titulo" to editedMarker.titulo,
                "latitud" to editedMarker.latitud,
                "longitud" to editedMarker.longitud,
                "descripcion" to editedMarker.descripcion,
                "imagen" to editedMarker.imagen
            )
        )
    }

    fun deleteMarker(markerID : String){
        database.collection("users").document(markerID).delete()
    }

    fun getMarkers() : CollectionReference {
        return database.collection("markers")
    }

    fun getMarker(userMarker : String) : DocumentReference{
        return database.collection("marker").document(userMarker)
    }


}