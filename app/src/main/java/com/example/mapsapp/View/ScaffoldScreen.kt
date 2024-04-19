package com.example.mapsapp.View

import Model.UserPrefs
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.rounded.BackHand
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/*@Composable
fun MapScreen() {
    MyDrawer(myViewModel = MapAppViewModel())
}*/

@Composable
fun MyDrawer(myViewModel: MapAppViewModel) {
    val navigationController = rememberNavController()
    val scope = rememberCoroutineScope()
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val userPrefs = UserPrefs(context)
    val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())
    ModalNavigationDrawer(drawerState = state, gesturesEnabled = false, drawerContent = {
        ModalDrawerSheet {
            Row() {
                IconButton(onClick = { scope.launch { state.close()}}) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                }
                Text("Marcadores", modifier = Modifier.padding(12.dp))
                HorizontalDivider()
            }
            NavigationDrawerItem(
                label = { Text(text = "Marcadores Guardados") }, selected = false, onClick = {
                    scope.launch {
                        state.close()
                        navigationController.navigate(Routes.MarcadoresGuardados.route)
                    }
                })
            Spacer(modifier = Modifier.weight(1F))
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Log Out", modifier = Modifier
                    .clickable {
                        myViewModel.logout();
                        CoroutineScope(Dispatchers.IO).launch {
                            userPrefs.saveUserData(
                                storedUserData.value[0],
                                storedUserData.value[1],
                                "false"
                            )
                        };navigationController.navigate(Routes.LoginScreen.route)
                    }, style = TextStyle(fontSize = 25.sp, fontStyle = FontStyle.Italic))
                Icon(imageVector = Icons.Rounded.BackHand, contentDescription = "Icono")
            }


        }
    }) {
        miScaffold(myViewModel, state, navController = navigationController)

    }
}


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun miScaffold(myViewModel: MapAppViewModel, state: DrawerState, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.LoginScreen.route
    ) {
        composable(Routes.MapScreen.route) { MapScreen(myViewModel, state, navController) }
        composable(Routes.CamaraScreen.route) {
            CamaraScreen(
                navController = navController,
                myViewModel
            )
        }
        composable(Routes.GalleryScreen.route) {
            GalleryScreen(
                navController = navController,
                myViewModel
            )
        }
        composable(Routes.RegisterScreen.route) {
            RegisterScreen(
                navController = navController,
                myViewModel
            )
        }
        composable(Routes.LoginScreen.route) {
            LoginScreen(
                navController = navController,
                myViewModel
            )
        }

        composable(Routes.MarcadoresGuardados.route) {
            MarcadoresGuardados(
                navController = navController,
                myViewModel
            )
        }


    }

}

