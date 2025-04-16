package com.example.photogallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil3.compose.AsyncImage
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageGalleryScreen()
        }
    }
}

@Composable
fun ImageGalleryScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val imageFile = File(downloadsDirectory, "images.jpg") // Replace with your image file name

        if (imageFile.exists()) {
            AsyncImage(
                model = imageFile,
                contentDescription = "Image from Downloads",
                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery), // Optional placeholder
                error = painterResource(id = android.R.drawable.ic_menu_report_image),     // Optional error image
                modifier = Modifier.size(200.dp)
            )
        } else {
            Text("Image not found in Downloads")
        }
    }

}