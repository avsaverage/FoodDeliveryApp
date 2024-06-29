package com.avsaverage.fooddeliveryapp.presentation.manager.order_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.avsaverage.fooddeliveryapp.R
import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse
import com.avsaverage.fooddeliveryapp.data.models.OrderModelResponse
import com.avsaverage.fooddeliveryapp.presentation.user.menu_screen.MenuViewModel
import kotlinx.coroutines.launch

@Composable
fun OrderScreenManager(
    viewModel: OrderScreenManagerViewModel = hiltViewModel()
) {
    val res = viewModel.res.value
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        color = MaterialTheme.colorScheme.background
    ) {

        if (res.orders?.isNotEmpty() == true) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                state = rememberLazyListState()
            ) {
                items(
                    items = res.orders,
                    key = {
                        it.uid!!
                    }
                ) { res ->
                    res.let {
                        EachPosition(
                            orderState = res,
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun EachPosition (
    orderState: OrderModelResponse,
    viewModel: OrderScreenManagerViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()


    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(text = "№ заказа: ${orderState.uid}",)
            Text(text = "Имя получателя : ${orderState.name}")
            Text(text = "Дата доставки заказа: ${orderState.date}")
            Text(text = "Время доставки заказа: ${orderState.time}")
            Text(text = when {
                orderState.isDelivered == true && orderState.isAccepted == true && orderState.isReady == true -> "Состояние заказа: Доставлен"
                orderState.isReady == true && orderState.isAccepted == true -> "Состояние заказа: Готов"
                orderState.isAccepted == true -> "Состояние заказа: Принят"
                else -> "Состояние заказа: Не принят"
            })
            Button(onClick = {
                             scope.launch {
                                 viewModel.onStateChangeClick(orderState)
                             }
            },
                enabled = !orderState.isDelivered!!
            ) {
                Text(text = when {
                    orderState.isDelivered == true && orderState.isAccepted == true && orderState.isReady == true -> "Завершен"
                    orderState.isReady == true && orderState.isAccepted == true -> "Доставлен"
                    orderState.isAccepted == true -> "Готов"
                    else -> "Принять"
                })
            }
        }
    }
}