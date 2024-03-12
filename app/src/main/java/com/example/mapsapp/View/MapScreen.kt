package com.example.mapsapp.View

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mapsapp.viewModel.MapAppViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch



@Composable
fun MapScreen(navController: NavController) {
    MyDrawer(myViewModel = MapAppViewModel())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDrawer(myViewModel: MapAppViewModel) {
    val scope = rememberCoroutineScope()
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(drawerState = state, gesturesEnabled = false, drawerContent = {
        ModalDrawerSheet {
            Row {
                IconButton(onClick = { scope.launch { state.close() } }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                }
                Text("Marcadores", modifier = Modifier.padding(12.dp))
                Divider()
            }
            NavigationDrawerItem(
                label = { Text(text = "Marcador 1") }, selected = false, onClick = {
                    scope.launch {
                        state.close()
                    }
                })
        }
    }) {
        miScaffold(myViewModel, state)

    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun miScaffold(myViewModel: MapAppViewModel, state: DrawerState) {
    Scaffold(topBar = { myToppAppBar(myViewModel = MapAppViewModel(), state) }) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(it)
        ) {
            Mapa(myViewModel)
            //val sheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            val showBottomSheet by myViewModel.mostrarShowBottom.observeAsState(false)
            if (showBottomSheet) {
                ModalBottomSheet(onDismissRequest = { /* Executed when the sheet is dismissed */ }) {
                    // Sheet content
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myToppAppBar(myViewModel: MapAppViewModel, state: DrawerState) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(text = "Mapa") },
        navigationIcon = {
            IconButton(onClick = { scope.launch { state.open() } }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
            }
        }
    )
}

@Composable
fun Mapa(myViewModel: MapAppViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        val itb = LatLng(41.4534265, 2.1837151)
        val cameraPositionState = rememberCameraPositionState() {
            position = CameraPosition.fromLatLngZoom(itb, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isBuildingEnabled = true, mapType = MapType.HYBRID),
            uiSettings = MapUiSettings(compassEnabled = true, mapToolbarEnabled = true, myLocationButtonEnabled = true),
            onMapClick = {
            }, onMapLongClick = {

            }

        ){
            Marker(
                state = rememberMarkerState(position = itb ),
                title = "ITB",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                        onInfoWindowLongClick = {
                },
                draggable = true
            )
        }
    }
}
