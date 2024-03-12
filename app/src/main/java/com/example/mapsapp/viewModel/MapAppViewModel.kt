package com.example.mapsapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapAppViewModel : ViewModel(){
    private val _mostrarShowBottom = MutableLiveData(false)
    val mostrarShowBottom = _mostrarShowBottom

    private val _geolocalizar = MutableLiveData(LatLng(0.0,0.0))
    val geolocalizar = _geolocalizar

    fun showBottomSheet(latlng : LatLng){
    _mostrarShowBottom.value = true
    _geolocalizar.value = latlng
    }

    fun esconderBottomSheet(){
        _mostrarShowBottom.value = false
    }
}