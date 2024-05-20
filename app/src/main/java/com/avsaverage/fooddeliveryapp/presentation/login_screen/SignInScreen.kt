package com.avsaverage.fooddeliveryapp.presentation.login_screen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(navController: NavHostController) {

    var emailText by remember {
        mutableStateOf("")
    }

    var passwordText by remember {
        mutableStateOf("")
    }

    val width = 350.dp

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
                onClick = { /*TODO*/ }) {
                Text(
                    text =  "Вход",
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier =  Modifier.padding(9.dp))

            OutlinedButton(
                modifier = Modifier.size(width,60.dp),
                shape = RoundedCornerShape(30),
                onClick = {navController.navigate("RegisterScreen")}) {
                Text(
                    text =  "Регистрация",
                    fontSize = 16.sp
                )
            }

            
        }

    }
}


