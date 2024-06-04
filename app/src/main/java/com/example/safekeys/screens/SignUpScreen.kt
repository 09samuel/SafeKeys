package com.example.safekeys.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safekeys.components.NormalTextComponent
import com.example.safekeys.ui.auth.SignUpViewModel
import com.example.safekeys.ui.auth.SignUpEvent
import com.example.safekeys.ui.auth.SignUpState


@Composable
fun SignUpScreen(
    viewmodel: SignUpViewModel,
    state: SignUpState,
    onEvent: (SignUpEvent) -> Unit,
    onClick: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {

//        val context = LocalContext.current
//        LaunchedEffect(key1 = context) {
//            viewmodel.onEvent()
//            viewModel.validationEvents.collect { event ->
//                when (event) {
//                    is MainViewModel.ValidationEvent.Success -> {
//                        Toast.makeText(
//                            context,
//                            "Registration successful",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//            }
//        }

        Column(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Register",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 37.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
            )

            Spacer(modifier = Modifier.height(10.dp))

            NormalTextComponent("Hello User", FontWeight.Normal, 20.sp)
            Spacer(modifier = Modifier.height(60.dp))


            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                value = state.password,
                onValueChange = {
                    onEvent(SignUpEvent.SetPassword(it))
                },
                isError = state.passwordError != null,
                leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) },
                trailingIcon = {
                    val iconImage =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description =
                        if (passwordVisible) "Hide Password" else "Show Password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = iconImage, contentDescription = description)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            if (state.passwordError != null) {
                Text(
                    text = state.passwordError,
                    color = MaterialTheme.colorScheme.error,
                    lineHeight = 14.sp,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Confirm Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                value = state.confirmPassword,
                onValueChange = {
                    onEvent(SignUpEvent.SetConfirmPassword(it))
                },
                isError = state.confirmPasswordError != null,
                leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = null) },
                trailingIcon = {
                    val iconImage =
                        if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description =
                        if (confirmPasswordVisible) "Hide Password" else "Show Password"

                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = iconImage, contentDescription = description)
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            if (state.confirmPasswordError != null) {
                Text(
                    text = state.confirmPasswordError,
                    color = MaterialTheme.colorScheme.error,
                    lineHeight = 14.sp,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedButton(
                onClick = { onEvent(SignUpEvent.Submit(onClick = onClick)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Register", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(60.dp))


            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}


