package com.example.mapsapp.viewModel

import Model.Repository
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class MapAppViewModel : ViewModel(){
    data class Info(
        var markerId : String? = null,
        var titulo : String,
        var latitud : Double,
        var longitud : Double,
        var descripcion : String,
        var imagen : Bitmap?,
    ){
        constructor() : this(null,"",0.0,0.0,"",null)
    }

    val repository = Repository()

    private val _mostrarShowBottom = MutableLiveData(false)
    val mostrarShowBottom = _mostrarShowBottom

    private val _geolocalizar = MutableLiveData(LatLng(0.0,0.0))
    val geolocalizar = _geolocalizar

    private val _fotoGrosera = MutableLiveData<Bitmap>()
    val fotoGrosera = _fotoGrosera

    private val _listaLocalizacion = MutableLiveData<MutableList<Info>>(mutableListOf())
    val listaLocalizacion = _listaLocalizacion

    private val _cameraPermissionGranted = MutableLiveData(false)
    val cameraPermissionGranted = _cameraPermissionGranted

    private val _shouldShowPermissionRationale = MutableLiveData(false)
    val shouldShowPermissionRationale = _shouldShowPermissionRationale

    private val _showPermissionDenied = MutableLiveData(false)
    val showPermissionDenied = _showPermissionDenied

    fun showBottomSheet(latlng : LatLng){
    _mostrarShowBottom.value = true
    _geolocalizar.value = latlng

    }
    fun guardarFoto(imagen: Bitmap){
        _fotoGrosera.value = imagen
    }

    fun esconderBottomSheet(){
        _mostrarShowBottom.value = false
    }

    fun añadirItem(titulo : String, descripcion: String){
        _listaLocalizacion.value?.add(Info(null,titulo, _geolocalizar.value!!.latitude, _geolocalizar.value!!.longitude,descripcion, fotoGrosera.value))
    }

    fun setCameraPermissionGranted(granted : Boolean){
        _cameraPermissionGranted.value = granted
    }

    fun setShouldShowPermissionRationale(should: Boolean){
        _shouldShowPermissionRationale.value = should
    }

    fun setShowPermissionDenied(denied : Boolean){
        _showPermissionDenied.value = denied
    }

    fun getMarkers() {
        repository.getMarkers().addSnapshotListener{value,error ->
            if (error != null){
                Log.e("FireStore error", error.message.toString())
                return@addSnapshotListener
            }
            val tempList = mutableListOf<Info>()
            for (dc : DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    val newMarker = dc.document.toObject(Info::class.java)
                    newMarker.markerId = dc.document.id
                    tempList.add(newMarker)
                }
            }
            _listaLocalizacion.value = tempList
        }
    }

    fun getMarker(markerId : String) {
        repository.getMarker(markerId).addSnapshotListener{value, error ->
            if (error != null){
                Log.w("MarkerRepository", "Listen failed", error)
                return@addSnapshotListener
            }
            if(value != null && value.exists()) {
                val marker = value.toObject(Info::class.java)
                if(marker != null){
                    marker.markerId = markerId
                }

            } else {
                Log.d("MarkerRepository", "Current data: null")
            }
        }
    }

}