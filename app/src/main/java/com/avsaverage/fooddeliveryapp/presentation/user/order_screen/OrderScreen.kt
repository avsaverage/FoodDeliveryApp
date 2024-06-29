package com.avsaverage.fooddeliveryapp.presentation.user.order_screen


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.avsaverage.fooddeliveryapp.MainActivity
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {

    val orderModelResponse = viewModel.orderModelResponse
    val scope = rememberCoroutineScope()
    val state = viewModel.state.collectAsState(initial = null)
    val context = LocalContext.current


    val year: Int
    val month: Int
    val day: Int
    val hourOfDay: Int
    val minute: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()
    hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
    minute = calendar.get(Calendar.MINUTE)


    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        {_: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            viewModel.selectedDate.value = "$year-$month-$dayOfMonth"
        }, year, month, day
    )

    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        {_, hour: Int, minute: Int ->
            val formattedHour = "%02d".format(hour)
            val formattedMinute = "%02d".format(minute)
        viewModel.selectedTime.value = "$formattedHour:$formattedMinute"
        }, hourOfDay, minute, true
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
                Text(
                    text = "Формирование заказа",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Выберите дату: ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = viewModel.selectedDate.value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = {
                    datePickerDialog.show()
                }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = ""
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Выберите время: ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = viewModel.selectedTime.value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = {
                    timePickerDialog.show()
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = ""
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Адрес: ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedTextField(
                    value = viewModel.address.value,
                    onValueChange = { newAddress ->
                        viewModel.address.value = newAddress
                    },
                    placeholder = { Text(text = "Адрес")}
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Имя: ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = viewModel.name.value,
                    onValueChange = { name ->
                        viewModel.name.value = name
                    },
                    placeholder = { Text(text = "Имя")}
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Телефон: ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                OutlinedTextField(
                    value = viewModel.telephone.value,
                    onValueChange = { telephone ->
                        viewModel.telephone.value = telephone
                    },
                    placeholder = { Text(text = "Телефон")}
                )
            }

            LaunchedEffect(key1 =  state.value?.isSuccess) {
                scope.launch {
                    if(state.value?.isSuccess == true){
                        navController.popBackStack()
                    }
                }
            }


            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                if(viewModel.address.value != "" && viewModel.name.value != "" && viewModel.telephone.value != "")
                {
                    scope.launch {
                        orderModelResponse.value.orderItems = viewModel.userDataState.value.user?.cart
                        orderModelResponse.value.uid = viewModel.userDataState.value.user?.uid
                        orderModelResponse.value.telephone = viewModel.telephone.value
                        orderModelResponse.value.time = viewModel.selectedTime.value
                        orderModelResponse.value.date = viewModel.selectedDate.value
                        orderModelResponse.value.address = viewModel.address.value
                        orderModelResponse.value.name = viewModel.name.value
                        viewModel.clickConfirm(viewModel.orderModelResponse.value)
                    }
                    scope.launch {
                        viewModel.userDataState.value.user?.cart = emptyList()
                        viewModel.userDataState.value.user?.address = viewModel.address.value
                        viewModel.userDataState.value.user?.telephone = viewModel.telephone.value
                        viewModel.userDataState.value.user?.name = viewModel.name.value
                        viewModel.userDataState.value.user?.let { viewModel.updateUserData(it) }
                    }
                }
                else{
                    Toast.makeText(context, "Поля не должны быть пустыми", Toast.LENGTH_SHORT).show()
                }
            }
            ) {
                Text(
                    fontSize = 16.sp,
                    text = "Оформить"
                )

            }

        }
    }
}
