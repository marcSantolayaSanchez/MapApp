package com.example.mapsapp.View

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsapp.R
import com.example.mapsapp.viewModel.MapAppViewModel

@Composable
fun GalleryScreen(navController: NavController, myViewModel: MapAppViewModel) {
    val context = LocalContext.current
    var uri: Uri? by remember { mutableStateOf(null) }
    val img: Bitmap? = ContextCompat.getDrawable(context, R.drawable.empty_image)?.toBitmap()
    var bitmap by remember { mutableStateOf(img) }
    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = it?.let { it1 ->
                    ImageDecoder.createSource(context.contentResolver, it1)
                }
                source?.let { it1 ->
                    ImageDecoder.decodeBitmap((it1))
                }
            }
            uri = it
        })
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().background(color = Color.DarkGray)
    ) {
        Button(onClick = {
            launchImage.launch("image/*")
        }) {
            Text(text = "Abrir Galeria")
        }
        Image(
            bitmap = if (bitmap != null) bitmap!!.asImageBitmap() else img!!.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RectangleShape)
                .size(250.dp)
                .background(Color.LightGray)
                .border(width = 1.dp, color = Color.DarkGray, shape = RectangleShape)
        )

        Button(onClick = {
            if (uri != null) myViewModel.uploadImage(uri);
            navController.navigateUp()
        }) {
            Text(text = "Upload image")
        }
        /**GlideImage(
        model = imageUrl,
        contentDescription = "Image from Storage",
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(100.dp)
        )
         **/

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.navigateUp() ; myViewModel.showBottomSheet(myViewModel.geolocalizar.value!!) }) {
                Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "ATRAS")

            }
        }
    }
}
