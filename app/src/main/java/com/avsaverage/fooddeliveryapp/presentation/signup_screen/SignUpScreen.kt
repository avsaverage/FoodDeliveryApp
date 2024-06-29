package com.avsaverage.fooddeliveryapp.presentation.signup_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.avsaverage.fooddeliveryapp.navigation.Screens
import kotlinx.coroutines.launch


@Composable

fun SignUpScreen(navController: NavHostController, viewModel: SignUpViewModel = hiltViewModel()) {

    var emailText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signUpState.collectAsState(initial = null)
    val createState = viewModel.createState.collectAsState(initial = null)

    val width = 350.dp
    val focusManager = LocalFocusManager.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {


            Text(
                modifier = Modifier.padding(16.dp),
                text = "Регистрация",
                fontSize = 24.sp)

            OutlinedTextField(
                modifier = Modifier
                    .padding(16.dp)
                    .width(width)
                    .testTag("textField1"),
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
                )
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Button(
                modifier = Modifier.size(width,60.dp),
                shape = RoundedCornerShape(30),
                onClick = {
                    scope.launch {
                        focusManager.clearFocus()
                        viewModel.registerUser(emailText, passwordText)
                    }
                }) {
                Text(
                    text =  "Регистрация",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier =  Modifier.padding(8.dp))

            OutlinedButton(
                modifier = Modifier.size(width,60.dp),
                shape = RoundedCornerShape(30),
                onClick = {
                    scope.launch {
                        focusManager.clearFocus()
                        navController.navigate(Screens.SignInScreen.route)
                    }
                }) {
                Text(
                    text =  "Войти в аккаунт",
                    fontSize = 16.sp
                )
            }

            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center){
                if(state.value?.isLoading == true ){
                    CircularProgressIndicator()
                }
            }

            LaunchedEffect(key1 =  state.value?.isSuccess) {
                scope.launch {
                    if(state.value?.isSuccess == true){
                        viewModel.createUser()
                        navController.navigate(Screens.SignUpScreen.route)
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