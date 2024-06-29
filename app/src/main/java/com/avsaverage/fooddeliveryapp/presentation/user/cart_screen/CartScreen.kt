package com.avsaverage.fooddeliveryapp.presentation.user.cart_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.avsaverage.fooddeliveryapp.R
import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse
import com.avsaverage.fooddeliveryapp.navigation.Screens
import kotlinx.coroutines.launch


@Composable
fun CartScreen(
    navController: NavHostController,
    viewModel: CartViewModel = hiltViewModel()
) {

    val cart = viewModel.cartItems
    val total = viewModel.cartTotal.value
    val context = LocalContext.current


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            if (cart.value?.isNotEmpty() == true || cart.value != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    state = rememberLazyListState()
                ) {
                    item {
                        Text(
                            text = "Корзина",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    items(
                        items = cart.value?.toList() ?: emptyList(),
                        key = {
                            it.first.food?.id.toString() ?: ""
                        }
                    ) { cart ->
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        )
                        {
                            EachPosition(
                                itemState = cart.first.food!!,
                                quantity = cart.second
                            )
                        }
                    }
                    item {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 4.dp
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    fontSize = 18.sp,
                    text = "Итого: %.2f Br".format(total),
                    fontWeight = FontWeight.Bold
                )

                Button(
                    onClick = {
                              if(viewModel.cartItems.value?.isNotEmpty() == true)
                              {
                                  navController.navigate(Screens.OrderScreen.route)
                              } else {
                                  Toast.makeText(context, "У вас пустая корзина", Toast.LENGTH_SHORT).show()
                              }
                    },
                ) {
                    Text(
                        text = "Далее",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EachPosition (
    viewModel: CartViewModel = hiltViewModel(),
    itemState: FoodModelResponse.FoodItems,
    quantity: Int
) {
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .size(60.dp),
            model = itemState.image!!,
            contentDescription = "img from storage",
            contentScale = ContentScale.FillBounds,
            error = painterResource(id = R.drawable.not_found)
        )

        Column(
            modifier = Modifier.fillMaxWidth(0.7f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = itemState.name!!,
                fontSize = 18.sp
            )
            Text(
                fontSize = 18.sp,
                text = "Кол-во: $quantity"
            )
            Text(
                fontSize = 18.sp,
                text = "%.2f Br".format(itemState.price?.times(quantity))
            )

        }

        IconButton(onClick ={
            viewModel.userDataState.value.user?.let { viewModel.deleteFromCart(userDataResponse = it, itemState) }
            scope.launch {
                viewModel.userDataState.value.user?.let { viewModel.updateUserData(it) }
            }
        }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = ""
            )
        }

    }
}
