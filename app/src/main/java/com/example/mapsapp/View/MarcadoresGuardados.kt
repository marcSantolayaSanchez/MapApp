package com.example.mapsapp.View

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.IconButton

import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsapp.viewModel.MapAppViewModel
import kotlinx.coroutines.launch

@Composable
fun MarcadoresGuardados(navController: NavController, myViewModel: MapAppViewModel) {
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    Text(text = "Marcadores Guardados")
    val marcadores by myViewModel.listaLocalizacion.observeAsState()


    Scaffold(topBar = { ToppAppBar(myViewModel = MapAppViewModel(), state) }){ paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(paddingValues)
        ){
            LazyColumn {
                items(marcadores!!){
                    MarcadorItem(marcador = it, myViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MarcadorItem(marcador : MapAppViewModel.Info, myViewModel: MapAppViewModel) {
    Card(
        border = BorderStroke(4.dp, color = Color(0xFF152935)),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .clickable { }
            .background(color = Color(0xFF2A4165))

    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxHeight()
                .background(color = Color(0xFF2A4165))) {
            Row(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()){
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
            Row(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()) {
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
            IconButton(onClick = {myViewModel.deleteMarker(marcador)}) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "Close", tint = Color.Cyan)
            }

        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToppAppBar(myViewModel: MapAppViewModel, state: DrawerState) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { androidx.compose.material3.Text(text = "Mapa") },
        navigationIcon = {
            androidx.compose.material3.IconButton(onClick = { scope.launch { state.open() } }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
            }
        }
    )
}
