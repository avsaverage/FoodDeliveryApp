package com.avsaverage.fooddeliveryapp.presentation.manager.menu_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
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
import com.avsaverage.fooddeliveryapp.presentation.dialogs.FoodAddDialog
import com.avsaverage.fooddeliveryapp.presentation.dialogs.FoodEditDialog
import kotlinx.coroutines.launch


@Composable
fun MenuScreenManager(
    viewModel: MenuScreenManagerViewModel = hiltViewModel(),
    navController: NavHostController
)  {
    val res = viewModel.res.value
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
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
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            FloatingActionButton(
                modifier = Modifier.size(56.dp),
                onClick = { viewModel.clickAddFood() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    }
    if(viewModel.isFoodEditDialogShown)
    {
        FoodEditDialog(
            viewModel = viewModel,
            itemState = viewModel.selectedItem,
            onDismiss = {
                viewModel.onDismissDialog()
            },
            onConfirm = {
                scope.launch {
                    viewModel.updateFood(it)
                }
            }
        )
    }
    if(viewModel.isFoodAddDialogShown)
    {
        FoodAddDialog(
            onDismiss = { viewModel.onDismissDialog() },
            onConfirm = {
                if (it != null) {
                    viewModel.addFood(it)
                }
            } ,
            viewModel = viewModel)
    }

}
@Composable
fun EachPosition (
    itemState: FoodModelResponse.FoodItems,
    viewModel: MenuScreenManagerViewModel
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
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { viewModel.clickEditFood(itemState) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
                IconButton(onClick = {
                    scope.launch {
                        viewModel.deleteFoodImg(itemState.image!!)
                    }
                    scope.launch {
                        viewModel.delete(itemState.id.toString())
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }

        }
    }
}