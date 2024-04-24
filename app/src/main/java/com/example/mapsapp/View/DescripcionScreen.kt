package com.example.mapsapp.View

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsapp.viewModel.MapAppViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DescripcionScreen(myViewModel: MapAppViewModel, navController: NavController) {
    val descripcionMarcador = myViewModel.marcadorActual.observeAsState()
    Scaffold(topBar = {
        TopAppBar(
            myViewModel = myViewModel,
            navController
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .background(color = Color.DarkGray)
        ) {
            descripcionMarcador.value?.let { marcador ->
                Text(
                    text = marcador.titulo,
                    fontSize = 30.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 70.dp)
                )

                GlideImage(
                    model = marcador.imagen,
                    contentDescription = "foto",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(5 / 4f)
                        .padding(top = 70.dp)
                        .size(150.dp)
                        .scale(0.8f)
                )

                Text(
                    text = marcador.descripcion,
                    fontSize = 30.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 70.dp)
                )

                Text(
                    text = marcador.categoria ?: "",
                    fontSize = 30.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 170.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    myViewModel: MapAppViewModel,
    navController: NavController
) {
    TopAppBar(
        title = { Text(text = "Descripción", fontSize = 20.sp, style = TextStyle(
            fontWeight = FontWeight.Bold
        )) },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "ATRAS")
            }
        },
)}
