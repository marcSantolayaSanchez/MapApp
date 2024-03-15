package com.example.mapsapp.View

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mapsapp.MainActivity
import com.example.mapsapp.viewModel.MapAppViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
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
            Row() {
                IconButton(onClick = { scope.launch { state.close() } }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                }
                Text("Marcadores", modifier = Modifier.padding(12.dp))
                HorizontalDivider()
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


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun miScaffold(myViewModel: MapAppViewModel, state: DrawerState) {
    val sheetState = rememberModalBottomSheetState(true)
    val scope = rememberCoroutineScope()
    var tituloMarcador by rememberSaveable {
        mutableStateOf("")
    }
    var descripcionMarcador by rememberSaveable {
        mutableStateOf("")
    }
    val showBottomSheet by myViewModel.mostrarShowBottom.observeAsState(false)
    Scaffold(topBar = { myToppAppBar(myViewModel = MapAppViewModel(), state) }) { paddingvalues ->
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(paddingvalues)
        ) {
            val permissionState =
                rememberPermissionState(permission = android.Manifest.permission.ACCESS_FINE_LOCATION)
            LaunchedEffect(Unit) {
                permissionState.launchPermissionRequest()
            }
            if (permissionState.status.isGranted) {
                Mapa(myViewModel)

            } else {
                Text("ayuda")
            }
            Mapa(myViewModel)
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        myViewModel.esconderBottomSheet()
                    },
                    sheetState = sheetState
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = tituloMarcador,
                            onValueChange = { tituloMarcador = it },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            placeholder = {
                                Text(
                                    text = "Nombre del Marcador:",
                                    color = Color.Gray,
                                    modifier = Modifier.alpha(0.5f)
                                )
                            },
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = descripcionMarcador,
                            onValueChange = { descripcionMarcador = it },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            placeholder = {
                                Text(
                                    text = "Descripcion:",
                                    color = Color.Gray,
                                    modifier = Modifier.alpha(0.5f)
                                )
                            },
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            myViewModel.añadirItem(
                                tituloMarcador,
                                descripcionMarcador
                            )
                        }) {
                            Text(text = "BOTON QUE AÑADE")
                        }
                    }


                    /*
                    Button(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                myViewModel.showBottomSheet()
                            }
                        }
                    }) {
                        Text("Hide bottom sheet")
                    }*/
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
@SuppressLint("MissingPermission")
@Composable
fun Mapa(myViewModel: MapAppViewModel) {
    val context = LocalContext.current
    val fusedLocationProviderClient =
        remember {
            LocationServices.getFusedLocationProviderClient(context)
        }
    var lastKnowLocation by remember {mutableStateOf<Location?>(null)}
    var deviceLatLng by remember { mutableStateOf(LatLng(0.0,0.0))}
    val cameraPositionState =
        rememberCameraPositionState{position = CameraPosition.fromLatLngZoom (deviceLatLng,18f)}
    val locationResult = fusedLocationProviderClient.getCurrentLocation(100,null)
    locationResult.addOnCompleteListener(context as MainActivity){task ->
        if (task.isSuccessful){
            lastKnowLocation = task.result
            deviceLatLng = LatLng(lastKnowLocation!!.latitude,lastKnowLocation!!.longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLatLng,18f)
        } else{
            Log.e("Error", "Exception : %s", task.exception)
        }

    }



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
            uiSettings = MapUiSettings(
                compassEnabled = true,
                mapToolbarEnabled = true,
                myLocationButtonEnabled = true
            ),
            onMapClick = {
            }, onMapLongClick = {
                myViewModel.showBottomSheet(it)
            }

        ) {
            Marker(
                state = rememberMarkerState(position = itb),
                title = "ITB",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                onInfoWindowLongClick = {
                },
                draggable = true
            )

            val marcadores by myViewModel.listaLocalizacion.observeAsState()
            marcadores?.forEach {
                Marker(
                    state = MarkerState(it.localizacion),
                    title = it.titulo,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                    snippet = it.descripcion
                )
            }

        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Camara(){
    val permissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }
    if(permissionState.status.isGranted){

    }
}


