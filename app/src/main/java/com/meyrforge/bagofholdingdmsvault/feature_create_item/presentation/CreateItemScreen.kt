package com.meyrforge.bagofholdingdmsvault.feature_create_item.presentation

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.meyrforge.bagofholdingdmsvault.R
import com.meyrforge.bagofholdingdmsvault.common.Category
import com.meyrforge.bagofholdingdmsvault.ui.sharedComponents.InputTextFieldComponent
import com.meyrforge.bagofholdingdmsvault.ui.theme.Copper
import com.meyrforge.bagofholdingdmsvault.ui.theme.DarkBrown
import com.meyrforge.bagofholdingdmsvault.ui.theme.Gold
import com.meyrforge.bagofholdingdmsvault.ui.theme.GreyishBrown

@Preview
@Composable
fun CreateItemScreen(viewModel: CreateItemViewModel = hiltViewModel()) {
    val currentSelectedImageUri = viewModel.selectedImageUri
    val itemName by viewModel.itemName
    val itemDescription by viewModel.itemDescription

    val context = LocalContext.current

    val isUploading by viewModel.isUploading
    val uploadedImageUrl by viewModel.uploadedImageUrl
    val uploadError by viewModel.uploadError

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.updateSelectedImageUri(uri)
        }
    }

    LazyColumn {
        item {
            InputTextFieldComponent(
                "Nombre",
                false,
                itemName,
                onTextChange = { viewModel.onItemNameChange(it) })
        }
        item {
            InputTextFieldComponent(
                "Descripción",
                true,
                itemDescription,
                onTextChange = { viewModel.onItemDescriptionChange(it) })
        }
        item {
            CategoriesDropdownComponent(onCategoryChange = {viewModel.onItemCategoryChange(it)})
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clickable {
                        pickMediaLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                if (currentSelectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = currentSelectedImageUri,
                            placeholder = painterResource(id = R.drawable.placeholder_photo), // Reemplaza con tu placeholder
                            error = painterResource(id = R.drawable.no_photo_available) // Reemplaza con tu imagen de error
                        ),
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                4.dp,
                                GreyishBrown,
                                RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder si no hay imagen seleccionada
                    Image(
                        painter = painterResource(id = R.drawable.placeholder_photo), // Reemplaza con tu placeholder
                        contentDescription = "Toca para seleccionar una imagen",
                        modifier = Modifier
                            .fillMaxHeight()
                            .border(
                                4.dp,
                                GreyishBrown,
                                RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                }
            }

        }
        item {
            if (currentSelectedImageUri != null) {
                Button(
                    onClick = { viewModel.uploadItemImage() },
                    enabled = !isUploading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Subiendo...")
                    } else {
                        Text("Subir Imagen y Crear Ítem")
                    }
                }
            }
        }


        item {
            uploadError?.let { error ->
                Text(error, color = MaterialTheme.colorScheme.error)
            }
        }

        item {

            uploadedImageUrl?.let { url ->
                Toast.makeText(context, "Imagen subida!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun UploadImageItemComponent(onImageClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Image(
            painterResource(R.drawable.no_photo_available),
            "No Foto",
            modifier = Modifier
                .border(5.dp, GreyishBrown)
                .height(300.dp)
                .clickable(onClick = { onImageClick() })
        )
    }
}

@Composable
fun CategoriesDropdownComponent(onCategoryChange: (Category) -> Unit) {
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val itemPosition = remember { mutableIntStateOf(0) }
    val categories = enumValues<Category>()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Categoría",
            color = Gold,
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.caudex_regular))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Copper, RoundedCornerShape(5.dp))
                .border(5.dp, GreyishBrown, RoundedCornerShape(5.dp))
                .padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isDropDownExpanded.value = true
                    }
            ) {
                Text(
                    categories[itemPosition.intValue].name,
                    color = DarkBrown,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.caudex_regular))
                )
                Icon(Icons.Outlined.ArrowDropDown, "Deslpegar", tint = DarkBrown)
            }

            DropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Copper,
                border = BorderStroke(4.dp, GreyishBrown),
                expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                categories.forEachIndexed { index, category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category.name,
                                color = DarkBrown,
                                textAlign = TextAlign.Center
                            )
                        },
                        onClick = {
                            isDropDownExpanded.value = false
                            itemPosition.intValue = index
                            onCategoryChange(category)
                        })
                }
            }
        }
    }
}
