package com.example.mapsapp.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsapp.viewModel.MapAppViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DescripcionScreen(myViewModel: MapAppViewModel, marcador: MapAppViewModel.Info){
    Column(modifier = Modifier
        .fillMaxHeight()
        .padding(16.dp)
        .background(color = Color.DarkGray)) {

        Row(modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.End) {
            Text(text = marcador.titulo,
                fontSize = 20.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth())
        }

        GlideImage(
            model = marcador.imagen,
            contentDescription = "foto",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .clip(RoundedCornerShape(20))
                .fillMaxWidth()
                .aspectRatio(5 / 4f)
        )

        Row(modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.End) {
            Text(text = marcador.descripcion,
                fontSize = 20.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth())
        }

        Row(modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.End) {
            marcador.categoria?.let {
                Text(text = it,
                    fontSize = 20.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth())
            }
        }
    }
}