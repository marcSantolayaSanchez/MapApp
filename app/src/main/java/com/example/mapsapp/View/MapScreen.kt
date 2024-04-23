package com.example.mapsapp.View

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.mapsapp.MainActivity
import com.example.mapsapp.Navigation.Routes
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(myViewModel: MapAppViewModel, state: DrawerState, navController: NavHostController) {
    val isCameraPermissionGranted by myViewModel.cameraPermissionGranted.observeAsState(false)
    val imagen by myViewModel.fotoGroseraBip.observeAsState(null)
    val imagenUri by myViewModel.fotoGroseraUri.observeAsState(null)
    val showPermissionDenied by myViewModel.showPermissionDenied.observeAsState(false)
    val sheetState = rememberModalBottomSheetState(true)
    var tituloMarcador by rememberSaveable {
        mutableStateOf("")
    }
    var descripcionMarcador by rememberSaveable {
        mutableStateOf("")
    }
    var categoriaMarcador by rememberSaveable {
        mutableStateOf("")
    }

    val showBottomSheet by myViewModel.mostrarShowBottom.observeAsState(false)
    Scaffold(
        topBar = { myToppAppBar(myViewModel = myViewModel, state = state) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            val permissionState =
                rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
            LaunchedEffect(Unit) {
                permissionState.launchPermissionRequest()
            }


            if (permissionState.status.isGranted) {
                Mapa(myViewModel = myViewModel)
            } else {
                Text("Permission denied. Please enable location access.")
            }


            myViewModel.getMarkers()


            if (showBottomSheet) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {

                            OutlinedTextField(
                                value = tituloMarcador,
                                onValueChange = { tituloMarcador = it },
                                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                                singleLine = true,
                                placeholder = {
                                    Text(
                                        text = "Nombre del Marcador",
                                        color = Color.Gray,
                                        modifier = Modifier.alpha(0.5f)
                                    )
                                },
                                shape = RoundedCornerShape(16.dp)
                            )

                            OutlinedTextField(
                                value = descripcionMarcador,
                                onValueChange = { descripcionMarcador = it },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                placeholder = {
                                    Text(
                                        text = "Descripción",
                                        color = Color.Gray,
                                        modifier = Modifier.alpha(0.5f)
                                    )
                                },
                                shape = RoundedCornerShape(16.dp)
                            )


                            imagen?.let { bitmap ->
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Imagen",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .padding(top = 10.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = {
                                        myViewModel.esconderBottomSheet()
                                        if (imagenUri != null) {
                                            myViewModel.uploadImage(
                                                imagenUri,
                                                tituloMarcador,
                                                descripcionMarcador,
                                                categoriaMarcador
                                            )
                                        } else {
                                            myViewModel.añadirItem(
                                                tituloMarcador,
                                                descripcionMarcador,
                                                null,
                                                categoriaMarcador
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .height(50.dp)
                                        .wrapContentWidth(),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(text = "AÑADIR MARCADOR",
                                        color = Color.White,
                                        fontSize = 18.sp)
                                }

                                Button(
                                    onClick = {
                                        if (!isCameraPermissionGranted) {
                                            navController.navigate(Routes.CamaraScreen.route)
                                        }
                                    }, modifier = Modifier
                                        .height(50.dp)
                                        .wrapContentWidth()
                                        .padding(horizontal = 16.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        text = "HAZ FOTO",
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                }


                            }
                            Text(
                                text = "CATEGORIA MARCADOR:",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            LazyRow(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(myViewModel.categorias) { categoria ->
                                    Text(
                                        text = categoria,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                            .background(
                                                color = if (categoria == categoriaMarcador) Color.Gray else Color.Transparent,
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                            .clickable {
                                                categoriaMarcador = categoria
                                            }
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = {
                                        if (!isCameraPermissionGranted) {
                                            navController.navigate(Routes.GalleryScreen.route)
                                        }
                                    },
                                    modifier = Modifier
                                        .height(50.dp)
                                        .wrapContentWidth()
                                        .padding(horizontal = 16.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        text = "GALERÍA",
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                }
                            }


                            if (showPermissionDenied) {
                                PermissionDeclinedScreen()
                            }
                        }
                    },
                    onDismissRequest = {
                        myViewModel.esconderBottomSheet()
                    }
                )
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
    var lastKnowLocation by remember { mutableStateOf<Location?>(null) }
    var deviceLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val cameraPositionState =
        rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(deviceLatLng, 18f) }
    val locationResult = fusedLocationProviderClient.getCurrentLocation(100, null)
    locationResult.addOnCompleteListener(context as MainActivity) { task ->
        if (task.isSuccessful) {
            lastKnowLocation = task.result
            deviceLatLng = LatLng(lastKnowLocation!!.latitude, lastKnowLocation!!.longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLatLng, 18f)
        } else {
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
            properties = MapProperties(isBuildingEnabled = true, mapType = MapType.NORMAL),
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
            myViewModel.getMarkers()
            marcadores?.forEach {
                Marker(
                    state = MarkerState(LatLng(it.latitud, it.longitud)),
                    title = it.titulo,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                    snippet = it.descripcion,
                )
            }

        }
    }
}


@Composable
fun PermissionDeclinedScreen() {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Permission required", fontWeight = FontWeight.Bold)
        Text(text = "This app needs acces to the camera to take photos")
        Button(onClick = {
            openAppSettings(context as Activity)
        }) {
            Text(text = "Accept")
        }
    }
}

fun openAppSettings(activity: Activity) {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", activity.packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    activity.startActivity(intent)
}


