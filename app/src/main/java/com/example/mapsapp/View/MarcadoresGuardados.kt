package com.example.mapsapp.View

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField

import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsapp.Navigation.Routes
import com.example.mapsapp.viewModel.MapAppViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarcadoresGuardados(navController: NavController, myViewModel: MapAppViewModel) {
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
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
    val isCameraPermissionGranted by myViewModel.cameraPermissionGranted.observeAsState(false)
    val imagen by myViewModel.fotoGroseraBip.observeAsState(null)
    val imagenUri by myViewModel.fotoGroseraUri.observeAsState(null)
    val showBottomSheet by myViewModel.mostrarShowBottom.observeAsState(false)
    val marcadores by myViewModel.listaLocalizacion.observeAsState()
    val categorias by myViewModel.categoriaSeleccionada.observeAsState()


    LaunchedEffect(categorias) {
        if (categorias != "All") {
            myViewModel.getMarkersFILTRADO(categorias)
        } else {
            myViewModel.getMarkers()
        }
    }

    Scaffold(topBar = {
        ToppAppBar(
            myViewModel = myViewModel,
            state,
            navController
        )
    }) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(paddingValues)
        ) {
            LazyColumn {
                items(marcadores ?: emptyList()) { marcador ->
                    MarcadorItem(
                        marcador = marcador,
                        myViewModel = myViewModel,
                        navController = navController
                    )
                }
            }
        }

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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            singleLine = true,
                            placeholder = {
                                androidx.compose.material3.Text(
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
                                androidx.compose.material3.Text(
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
                                        myViewModel.editMarker(
                                            tituloMarcador,
                                            descripcionMarcador,
                                            null,
                                            categoriaMarcador,
                                            marker
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .height(50.dp)
                                    .wrapContentWidth(),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                androidx.compose.material3.Text(
                                    text = "AÑADIR MARCADOR",
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
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
                                androidx.compose.material3.Text(
                                    text = "HAZ FOTO",
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            }


                        }
                        androidx.compose.material3.Text(
                            text = "CATEGORIA MARCADOR:",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyRow(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(myViewModel.categorias) { categoria ->
                                androidx.compose.material3.Text(
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
                                androidx.compose.material3.Text(
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


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MarcadorItem(
    marcador: MapAppViewModel.Info,
    myViewModel: MapAppViewModel,
    navController: NavController
) {
    Card(
        border = BorderStroke(4.dp, color = Color(0xFF152935)),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .clickable {
                navController.navigate(Routes.DescripcionScreen.route); myViewModel.getMarker(
                marcador.markerId!!
            )
            }
            .background(color = Color(0xFF2A4165))

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxHeight()
                .background(color = Color(0xFF2A4165))
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                GlideImage(
                    model = marcador.imagen,
                    contentDescription = "33",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(150.dp)
                )
                Text(
                    text = marcador.titulo,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB7CBF4),
                        fontSize = 20.sp

                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 0.dp)
                )

                Divider(
                    color = Color.Black, modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
            }
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = marcador.descripcion,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB7CBF4),
                        fontSize = 20.sp

                    ),
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                marcador.categoria?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB7CBF4),
                            fontSize = 20.sp

                        ),
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                IconButton(onClick = { myViewModel.deleteMarker(marcador) }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = Color.Cyan
                    )
                }

                IconButton(onClick = { myViewModel.showBottomSheet(latlng = LatLng(marcador.latitud, marcador.longitud)) }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = Color.Cyan
                    )
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToppAppBar(
    myViewModel: MapAppViewModel,
    state: DrawerState,
    navController: NavController
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    val categorias = listOf("All", "Cafetería", "Hotel", "Tienda", "Restaurante", "Museo", "Parque")


    TopAppBar(
        title = {
            Text(
                text = "Marcadores", fontSize = 20.sp, style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "ATRAS")
            }
        },
        actions = {

            Column {
                IconButton(
                    onClick = { expanded = !expanded },
                    content = {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categorias.forEach { item ->

                        DropdownMenuItem(
                            onClick = {
                                selectedText = item
                                expanded = false
                                myViewModel.modificarCategoria(selectedText)
                            }
                        ) {
                            Text(text = item)
                        }
                    }
                }
            }
        }
    )
}