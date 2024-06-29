package com.avsaverage.fooddeliveryapp.presentation.dialogs

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
 import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse
import com.avsaverage.fooddeliveryapp.presentation.manager.menu_screen.MenuScreenManagerViewModel
import kotlinx.coroutines.launch


@Composable
fun FoodEditDialog(
    itemState: FoodModelResponse.FoodItems?,
    onDismiss:() -> Unit,
    onConfirm: (FoodModelResponse.FoodItems?) -> Unit,
    viewModel: MenuScreenManagerViewModel
) {
    var name by rememberSaveable { mutableStateOf(itemState?.name) }
    var description by rememberSaveable { mutableStateOf(itemState?.description) }
    var price by rememberSaveable { mutableStateOf(itemState?.price) }
    var image by rememberSaveable { mutableStateOf(itemState?.image) }
    var selectedImage by rememberSaveable { mutableStateOf<ByteArray?>(null)}
    var imageAccessToken by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
             if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { selectedUri ->
                    val inputStream = context.contentResolver.openInputStream(selectedUri)
                    val byteArray = inputStream?.readBytes()
                    selectedImage = byteArray
                    image = selectedUri.toString()
                }
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/*"
                }
                launcher.launch(intent)
            } else {
                Toast.makeText(context, "Отказано в доступе", Toast.LENGTH_SHORT).show()
            }
        }
    )

    if (itemState != null) {
        Dialog(
            properties = DialogProperties(
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = onDismiss,
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        ),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = image,
                                    contentDescription = itemState.name,
                                    modifier = Modifier
                                        .width(200.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable {
                                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                                        },
                                    contentScale = ContentScale.Fit
                                )
                                IconButton(onClick = {
                                    if(selectedImage != null)
                                    {
                                        scope.launch {
                                            itemState.image?.let { viewModel.deleteFoodImg(it) }
                                        }
                                        scope.launch {
                                            viewModel.uploadImg(selectedImage)
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(context,"Не выбрана новая картинка", Toast.LENGTH_SHORT).show()
                                    }

                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Image"
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Название блюда: ",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                OutlinedTextField(
                                    value = name!!,
                                    onValueChange = {
                                        name = it
                                    },
                                    placeholder = { Text(text = "Название блюда") }
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Описание блюда: ",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                OutlinedTextField(
                                    value = description!!,
                                    onValueChange = {
                                        description = it
                                    },
                                    placeholder = { Text(text = "Описание блюда") }
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Цена блюда: ",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                OutlinedTextField(
                                    value = price!!.toString(),
                                    onValueChange = {
                                        price = it.toDouble()
                                    },
                                    placeholder = { Text(text = "Цена блюда") }
                                )
                            }
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    itemState.name = name
                                    itemState.price = price
                                    itemState.description = description
                                    onConfirm(itemState)
                                    viewModel.onDismissDialog() }) {
                                Text(
                                    text = "Сохранить",
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



