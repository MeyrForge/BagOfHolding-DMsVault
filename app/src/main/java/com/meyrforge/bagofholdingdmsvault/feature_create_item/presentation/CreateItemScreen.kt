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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
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
import com.meyrforge.bagofholdingdmsvault.ui.sharedComponents.TopBarComponent
import com.meyrforge.bagofholdingdmsvault.ui.theme.Copper
import com.meyrforge.bagofholdingdmsvault.ui.theme.Corner
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
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painterResource(R.drawable.bag_of_holding),
            "Banner",
            modifier = Modifier
                .size(900.dp)
                .alpha(0.3f)
        )
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                TopBarComponent("Crear Ítem")
            }
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
                CategoriesDropdownComponent(onCategoryChange = { viewModel.onItemCategoryChange(it) })
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
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
                                placeholder = painterResource(id = R.drawable.press_to_add_image), // Reemplaza con tu placeholder
                                error = painterResource(id = R.drawable.image_not_available) // Reemplaza con tu imagen de error
                            ),
                            contentDescription = "Imagen seleccionada",
                            modifier = Modifier
                                .fillMaxSize()
//                                .border(
//                                    4.dp,
//                                    GreyishBrown,
//                                    RoundedCornerShape(8.dp)
//                                )
//                                .clip(RoundedCornerShape(8.dp))
                            ,
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        // Placeholder si no hay imagen seleccionada
                        Image(
                            painter = painterResource(id = R.drawable.press_to_add_image), // Reemplaza con tu placeholder
                            contentDescription = "Toca para seleccionar una imagen",
                            modifier = Modifier
                                .fillMaxHeight()
                                .background(Copper, RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    4.dp,
                                    GreyishBrown,
                                    RoundedCornerShape(8.dp)
                                ),
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
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Corner,
                            contentColor = Gold
                        )
                    ) {
                        if (isUploading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Guardando...",
                                fontFamily = FontFamily(Font(R.font.caudex_regular))
                            )
                        } else {
                            Text("Crear Ítem", fontFamily = FontFamily(Font(R.font.caudex_regular)))
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
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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
                .border(2.dp, GreyishBrown, RoundedCornerShape(5.dp))
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
                Icon(
                    Icons.Outlined.ArrowDropDown,
                    "Deslpegar",
                    tint = DarkBrown,
                    modifier = Modifier.size(24.dp)
                )
            }

            DropdownMenu(
                containerColor = Copper,
                border = BorderStroke(4.dp, GreyishBrown),
                expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                categories.forEachIndexed { index, category ->
                    DropdownMenuItem(
                        modifier = Modifier.border(2.dp, GreyishBrown),
                        leadingIcon = {
                            when (category) {
                                Category.MINI -> {
                                    Image(
                                        painterResource(R.drawable.ic_minis),
                                        "Mini",
                                        modifier = Modifier.size(100.dp)
                                    )
                                }

                                Category.DADO -> {
                                    Image(
                                        painterResource(R.drawable.ic_dices), "Dado",
                                        modifier = Modifier.size(100.dp)
                                    )
                                }

                                Category.MAPA -> {
                                    Image(
                                        painterResource(R.drawable.ic_maps), "Mapa",
                                        modifier = Modifier.size(100.dp)
                                    )
                                }

                                Category.LIBRO -> {
                                    Image(
                                        painterResource(R.drawable.ic_books), "Libro",
                                        modifier = Modifier.size(100.dp)
                                    )
                                }

                                Category.PROP -> {
                                    Image(
                                        painterResource(R.drawable.ic_props), "Prop",
                                        modifier = Modifier.size(100.dp)
                                    )
                                }

                                Category.OTRO -> {
                                    Image(
                                        painterResource(R.drawable.ic_other), "Otro",
                                        modifier = Modifier.size(100.dp)
                                    )
                                }
                            }
                        },
                        text = {
                            Text(
                                text = category.name,
                                color = DarkBrown,
                                textAlign = TextAlign.Center,
                                fontFamily = FontFamily(Font(R.font.caudex_regular)),
                                fontSize = 24.sp

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
