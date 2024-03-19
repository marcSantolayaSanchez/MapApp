package com.example.mapsapp.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapAppViewModel : ViewModel(){
    data class Info(
        var titulo : String,
        var localizacion : LatLng,
        var descripcion : String,
        var imagen : Bitmap?,
    )

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
        _listaLocalizacion.value?.add(Info(titulo, localizacion = geolocalizar.value!!,descripcion, fotoGrosera.value))
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

}