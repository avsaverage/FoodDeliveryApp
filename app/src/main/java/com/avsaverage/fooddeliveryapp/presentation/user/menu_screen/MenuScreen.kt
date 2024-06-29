package com.avsaverage.fooddeliveryapp.presentation.user.menu_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.avsaverage.fooddeliveryapp.R
import com.avsaverage.fooddeliveryapp.data.models.FoodModelResponse
import com.avsaverage.fooddeliveryapp.data.repositories.UserDataRepository
import com.avsaverage.fooddeliveryapp.presentation.dialogs.FoodAddDialog
import kotlinx.coroutines.launch

@Composable
fun MenuScreen(
    navController: NavHostController,
    viewModel: MenuViewModel = hiltViewModel(),
) {

    val res = viewModel.res.value
    val scope = rememberCoroutineScope()
    val state = viewModel.saveSate.collectAsState(initial = null)
    val context = LocalContext.current


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        color = MaterialTheme.colorScheme.background
    ) {

        if (res.food?.isNotEmpty() == true) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                state = rememberLazyListState()
            ) {
                items(
                    items = res.food,
                    key = {
                        it.key!!
                    }
                ) { res ->
                    res.key?.let {
                        EachPosition(
                            itemState = res.food!!,
                        )
                    }
                }
            }
        }
    }
    if (viewModel.isFoodAddDialogShown) {
        FoodAddDialog(
            itemState = viewModel.selectedItem,
            onDismiss = {
                viewModel.onDismissDialog()
            },
            onConfirm = {quantity ->
                viewModel.userDataState.value.user?.let { viewModel.selectedItem?.let { it1 ->
                    viewModel.clickAddToCart(it,
                        it1, quantity)
                } }
                scope.launch {
                    viewModel.userDataState.value.user?.let { viewModel.updateUserData(it) }
                }
                viewModel.onDismissDialog()
            }
        )
    }

    LaunchedEffect(key1 =  state.value?.isSuccess) {
        scope.launch {
            if(state.value?.isSuccess == true){
                Toast.makeText(context, "Блюдо добавлено в корзину", Toast.LENGTH_LONG).show()
            }
        }
    }

    LaunchedEffect(key1 =  state.value?.isError) {
        scope.launch {
            if(state.value?.isError?.isNotEmpty() == true){
                val error = state.value?.isError
                Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
            }
        }
    }
}



@Composable
fun EachPosition (
    itemState: FoodModelResponse.FoodItems,
    viewModel: MenuViewModel = hiltViewModel()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            viewModel.onFoodClick(itemState)
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp)),
                model = itemState.image!!,
                contentDescription = "img from storage",
                contentScale = ContentScale.FillBounds,
                error = painterResource(id = R.drawable.not_found)
            )

            Text(
                text = "${itemState.price!!} Br",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = itemState.name!!,
                fontSize = 16.sp,
            )
        }
    }
}