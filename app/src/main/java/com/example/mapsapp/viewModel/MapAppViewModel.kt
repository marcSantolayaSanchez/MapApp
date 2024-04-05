package com.example.mapsapp.viewModel

import Model.Repository
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MapAppViewModel : ViewModel(){
    data class Info(
        var markerId : String? = null,
        var titulo : String,
        var latitud : Double,
        var longitud : Double,
        var descripcion : String,
        var imagen : String?,
    ){
        constructor() : this(null,"",0.0,0.0,"",null)
    }

    val repository = Repository()

    private val _mostrarShowBottom = MutableLiveData(false)
    val mostrarShowBottom = _mostrarShowBottom

    private val _mostrarImagen = MutableLiveData(false)
    val mostrarImagen = _mostrarImagen

    private val _geolocalizar = MutableLiveData(LatLng(0.0,0.0))
    val geolocalizar = _geolocalizar

    private val _fotoGrosera = MutableLiveData<String>()
    val fotoGrosera = _fotoGrosera

    private val _fotoGroseraBip = MutableLiveData<Bitmap?>()
    val fotoGroseraBip = _fotoGroseraBip

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
    fun guardarFoto(imagen: String){
        _fotoGrosera.value = imagen
    }

    fun esconderBottomSheet(){
        _mostrarShowBottom.value = false
    }

    fun añadirItem(titulo : String, descripcion: String){
        _listaLocalizacion.value?.add(Info(null,titulo, _geolocalizar.value!!.latitude, _geolocalizar.value!!.longitude,descripcion, fotoGrosera.value))
        repository.addMarker(Info(null,titulo, _geolocalizar.value!!.latitude, _geolocalizar.value!!.longitude,descripcion, fotoGrosera.value))
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
    fun uploadImage(imageUri : Uri?){
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("images/$fileName")
        if (imageUri != null) {
            storage.putFile(imageUri)
                .addOnSuccessListener {
                    Log.i("IMAGE UPLOAD", "Image uploaded succesfully")
                    storage.downloadUrl.addOnSuccessListener {
                        Log.i("IMAGEN", it.toString())
                    }
                }
                .addOnFailureListener(){
                    Log.i("IMAGE UPLOAD", "Image upload failed")
                }
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

    fun addMarker(info : Info){
        repository.addMarker(info)
    }

}