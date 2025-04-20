package com.example.photogallery

import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                ImageGalleryScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImageGalleryScreen(modifier: Modifier) {

    // Remembering image read permissions
    val mediaPermissionState = rememberPermissionState(android.Manifest.permission.READ_MEDIA_IMAGES)

    // If permission hasn't been granted, show screen to request permission
    if (!mediaPermissionState.status.isGranted) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Request permission text
            Text(
                text = "Photo gallery needs permission to view images.",
                style = TextStyle(fontSize = 30.sp),
                modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            // Button to launch permission request. This will cause composable function to update.
            Button(onClick = { mediaPermissionState.launchPermissionRequest() }) {
                Text(
                    text = "Grant file permissions",
                    style = TextStyle(fontSize = 30.sp)
                    )
            }
        }
    }

    // Remembering scroll state of column
    val scrollState = rememberScrollState()

    // If permission state has been granted, show image gallery
    if (mediaPermissionState.status.isGranted) {
        Column(
            // Applying vertical scroll to column
            modifier = modifier.verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // DOWNLOADS FOLDER

            // Getting downloads directory
            val downloadsDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            // Getting all images in download folder
            val downloadImageFiles = remember(downloadsDirectory) {
                downloadsDirectory.listFiles()?.filter {
                    // Getting all files with these file extensions
                    it.isFile && it.extension.lowercase() in listOf(
                        "jpg",
                        "jpeg",
                        "png",
                        "gif",
                        "webp"
                    )
                } ?: emptyList()
            }

            // If images are found in downloads folder
            if (downloadImageFiles.isNotEmpty()) {
                // Download pictures label
                Text(
                    text = "Downloads",
                    style = TextStyle(fontSize = 30.sp),
                    textAlign = TextAlign.Start
                )
                // Making grid of download photos
                GalleryGrid(downloadImageFiles);
            }



            // PICTURES FOLDER

            // Getting pictures directory
            val picturesDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

            // Getting all images in picture folder
            val picturesImageFiles = remember(picturesDirectory) {
                // Getting all files with these file extensions
                picturesDirectory.listFiles()?.filter {
                    it.isFile && it.extension.lowercase() in listOf(
                        "jpg",
                        "jpeg",
                        "png",
                        "gif",
                        "webp"
                    )
                } ?: emptyList()
            }

            // If images are found in pictures folder
            if (picturesImageFiles.isNotEmpty()) {
                // Pictures label
                Text(
                    text = "Pictures",
                    style = TextStyle(fontSize = 30.sp),
                    textAlign = TextAlign.Start
                )

                // Making grid of photos in Pictures
                GalleryGrid(picturesImageFiles);
            }

            // If no images found in either folder
            if (downloadImageFiles.isEmpty() and picturesImageFiles.isEmpty()) {
                // No images found
                Text(
                    text = "No images found on device.",
                    style = TextStyle(fontSize = 30.sp),
                    textAlign = TextAlign.Start
                )
            }

        }
    }
}


// Draws grid of images with 3 per row
@Composable
fun GalleryGrid(imageFileList : List<File>) {
    // Grid of unknown size
    LazyVerticalGrid(
        // 3 images per row
        columns = GridCells.Fixed(3),
        // Vertical padding around grid
        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
        // Grid needs a max height to be scrollable
        modifier = Modifier.fillMaxSize().heightIn(max = 50000.dp),
        // Don't want users to scroll through individual grids
        userScrollEnabled = false
    ) {
        // For each image in imagefilelist
        items(imageFileList.size) { index ->
            // Getting image at index
            val imageFile = imageFileList[index]
            // Creating async image from file contents
            AsyncImage(
                model = imageFile,
                contentDescription = imageFile.name,
                // Cropping image
                contentScale = ContentScale.Crop,
                // Making image square
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
            )
        }
    }
}