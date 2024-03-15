package com.example.mapsapp.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapAppViewModel : ViewModel(){
    data class Info(
        var titulo : String,
        /*var imagen : Bitmap,*/
        var localizacion : LatLng,
        var descripcion : String
    )

    private val _mostrarShowBottom = MutableLiveData(false)
    val mostrarShowBottom = _mostrarShowBottom

    private val _geolocalizar = MutableLiveData(LatLng(0.0,0.0))
    val geolocalizar = _geolocalizar

    private val _listaLocalizacion = MutableLiveData<MutableList<Info>>(mutableListOf())
    val listaLocalizacion = _listaLocalizacion
    fun showBottomSheet(latlng : LatLng){
    _mostrarShowBottom.value = true
    _geolocalizar.value = latlng
    }

    fun esconderBottomSheet(){
        _mostrarShowBottom.value = false
    }

    fun añadirItem(titulo : String, descripcion: String){
        _listaLocalizacion.value?.add(Info(titulo, localizacion = geolocalizar.value!!, descripcion))
    }
}