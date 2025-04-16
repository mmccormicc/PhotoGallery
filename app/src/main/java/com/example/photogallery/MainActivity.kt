package com.example.photogallery

import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@Composable
fun ImageGalleryScreen(modifier: Modifier) {
    // Remembering scroll state
    val scrollState = rememberScrollState()

    Column(
        // Applying vertical scroll to column
        modifier = modifier.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // DOWNLOADS FOLDER

        // Getting downloads directory
        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Getting all images in download folder
        val downloadImageFiles = remember(downloadsDirectory) {
            downloadsDirectory.listFiles()?.filter {
                it.isFile && it.extension.lowercase() in listOf("jpg", "jpeg", "png", "gif", "webp")
            } ?: emptyList()
        }

        // Downloads label
        Text(text = "Downloads", style = TextStyle(fontSize = 30.sp), textAlign = TextAlign.Start)

        // Making grid of download photos
        GalleryGrid(downloadImageFiles);



        // PICTURES FOLDER

        // Getting pictures directory
        val picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        // Getting all images in picture folder
        val picturesImageFiles = remember(picturesDirectory) {
            picturesDirectory.listFiles()?.filter {
                it.isFile && it.extension.lowercase() in listOf("jpg", "jpeg", "png", "gif", "webp")
            } ?: emptyList()
        }

        // Pictures label
        Text(text = "Pictures", style = TextStyle(fontSize = 30.sp), textAlign = TextAlign.Start)

        // Making grid of pictures photos
        GalleryGrid(picturesImageFiles);

    }

}

@Composable
fun GalleryGrid(imageFileList : List<File>) {
    // If image files are found
    if (imageFileList.isNotEmpty()) {
        // Grid of unknown size
        LazyVerticalGrid(
            // 3 images per row
            columns = GridCells.Fixed(3),
            // Vertical padding around grid
            contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
            // Grid needs a max height to be scrollable
            modifier = Modifier.fillMaxSize().heightIn(max = 50000.dp),
            userScrollEnabled = false
        ) {
            // For each image in imagefilelist
            items(imageFileList.size) { index ->
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
}