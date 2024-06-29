package com.avsaverage.fooddeliveryapp.presentation.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse

@Composable
fun FoodAddDialog(
    itemState: FoodModelResponse.FoodItems?,
    onDismiss:() -> Unit,
    onConfirm: (quantity : Int) -> Unit
) {
    var text by rememberSaveable { mutableStateOf("1") }
    var isEnabledMinus by rememberSaveable { mutableStateOf(false) }
    var isEnabledPlus by rememberSaveable { mutableStateOf(true) }

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

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            AsyncImage(
                                model = itemState.image,
                                contentDescription = itemState.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Text(
                                text = itemState.description!!,
                                fontSize = 16.sp,
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = itemState.name!!,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )

                                Text(
                                    text = "${itemState.price!!} Br",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.LightGray),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent,
                                            contentColor = Color.Black,
                                            disabledContainerColor = Color.Transparent
                                        ),
                                        shape = RoundedCornerShape(0.dp),
                                        enabled = isEnabledMinus,
                                        onClick = {
                                            var currInt = text.toInt()
                                            currInt--
                                            text = currInt.toString()
                                            if (text.toInt() <= 1) {
                                                isEnabledMinus = false
                                            } else {
                                                isEnabledPlus = true
                                            }
                                        }
                                    ) {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(0.2f),
                                            text = "-",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Text(
                                        text = text,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Button(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent,
                                            contentColor = Color.Black,
                                            disabledContainerColor = Color.Transparent
                                        ),
                                        enabled = isEnabledPlus,
                                        shape = RoundedCornerShape(0.dp),
                                        onClick = {
                                            var currInt = text.toInt()
                                            currInt++
                                            text = currInt.toString()
                                            if (currInt >= 100) {
                                                isEnabledPlus = false
                                            } else {
                                                isEnabledMinus = true
                                            }
                                        }
                                    ) {
                                        Text(
                                            text = "+",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f),
                                    shape = RoundedCornerShape(16.dp),
                                    onClick = {
                                        onConfirm(text.toInt())
                                    }
                                ) {
                                    Text(
                                        text = "Сохранить",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
