package com.meyrforge.bagofholdingdmsvault.feature_create_item.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.meyrforge.bagofholdingdmsvault.R
import com.meyrforge.bagofholdingdmsvault.ui.theme.Copper
import com.meyrforge.bagofholdingdmsvault.ui.theme.DarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.DarkWood
import com.meyrforge.bagofholdingdmsvault.ui.theme.DeepDarkBrown

@Preview
@Composable
fun CreateItemScreen() {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // ActivityResultLauncher for picking an image
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        // Callback is invoked when the user selects an image or closes the picker
        if (uri != null) {
            selectedImageUri = uri
            // Here you can handle the URI, e.g., save it to your ViewModel,
            // upload it to a server, or display it.
        }
    }

    LazyColumn {
        item {
            InputTextFieldComponent("Nombre")
        }
        item {
            InputTextFieldComponent("Descripción")
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().height(300.dp)){
                if(selectedImageUri==null){
                    UploadImageItemComponent {
                        // Launch the photo picker
                        pickMediaLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                }else{
                    selectedImageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = uri, // Directly pass the Uri here
                                // You can still customize the request within rememberAsyncImagePainter
                                // For example, adding a placeholder or error drawable:
                                placeholder = painterResource(id = R.drawable.no_photo_available),
                                error = painterResource(id = R.drawable.no_photo_available),
                                // Or more complex request modifications:
                                // imageLoader = LocalContext.current.imageLoader, // If you have a custom ImageLoader
                                // onState = { state -> /* Do something with the loading state */ }
                            ),
                            contentDescription = "Imagen seleccionada",
                            modifier = Modifier
                                .border(5.dp, DarkBrown), // Adjust height as needed
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun InputTextFieldComponent(label: String) {
    var textValue by remember { mutableStateOf("") }

    TextField(
        value = textValue,
        onValueChange = { newValue ->
            textValue = newValue
        },
        label = { Text(label, fontFamily = FontFamily(Font(R.font.caudex_regular))) },
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(4.dp, DeepDarkBrown, RoundedCornerShape(5.dp))
            .padding(10.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Copper,
            unfocusedLabelColor = DarkBrown,
            focusedContainerColor = DarkWood,
            focusedLabelColor = DeepDarkBrown
        )
    )
}

@Composable
fun UploadImageItemComponent(onImageClick:()->Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Image(
            painterResource(R.drawable.no_photo_available),
            "No Foto",
            modifier = Modifier
                .border(5.dp, DarkBrown)
                .height(300.dp)
                .clickable(onClick = { onImageClick() })
        )
    }
}