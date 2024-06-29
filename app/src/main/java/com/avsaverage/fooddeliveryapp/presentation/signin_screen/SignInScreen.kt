package com.avsaverage.fooddeliveryapp.presentation.signin_screen
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.avsaverage.fooddeliveryapp.MainActivity
import com.avsaverage.fooddeliveryapp.ManagerActivity
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(navController: NavHostController,viewModel: SignInViewModel = hiltViewModel()) {

    var emailText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signInState.collectAsState(initial = null)


    val width = 350.dp

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        
        Box(
            Modifier.padding(top = 40.dp, start = 16.dp),
            contentAlignment = Alignment.TopStart
        )
        {
            IconButton(
                modifier = Modifier.size(40.dp),
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back button"
                )
            }
        }
       
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            
            
            
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Вход в аккаунт",
                fontSize = 24.sp)

            OutlinedTextField(
                modifier = Modifier
                    .padding(16.dp)
                    .width(width),
                value = emailText,
                shape = RoundedCornerShape(30),
                onValueChange = { emailText = it },
                placeholder = { Text(text = "E-mail") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "E-mail"
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    ),
            )

            OutlinedTextField(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 32.dp)
                    .width(width),
                value = passwordText,
                shape = RoundedCornerShape(30),
                onValueChange = {passwordText = it },
                placeholder = { Text(text = "Пароль")},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Password"
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,)
            )

            Button(
                modifier = Modifier.size(width,60.dp),
                shape = RoundedCornerShape(30),
                onClick = {
                    scope.launch {
                        viewModel.loginUser(emailText, passwordText)
                    }
                }) {
                Text(
                    text =  "Вход",
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier =  Modifier.padding(9.dp))

            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center){
                if(state.value?.isLoading == true){
                    CircularProgressIndicator()
                }
            }
            LaunchedEffect(key1 =  state.value?.isSuccess) {
                scope.launch {
                    if(state.value?.isSuccess == true){
                        if(viewModel.isManager.value)
                        {
                            val intent = Intent(context, ManagerActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent)
                        }
                        else
                        {
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent)
                        }
                    }
                }
            }

            LaunchedEffect(key1 =  state.value?.isError) {
                scope.launch {
                    if(state.value?.isError?.isNotEmpty() == true){
                        val error = state.value?.isError
                        Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}


