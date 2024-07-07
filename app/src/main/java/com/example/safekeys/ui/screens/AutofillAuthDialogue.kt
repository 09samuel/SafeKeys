package com.example.safekeys.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safekeys.ui.auth.autofill.AutofillAuthEvent
import com.example.safekeys.ui.auth.autofill.AutofillAuthState
import com.example.safekeys.ui.auth.login.LoginEvent
import com.example.safekeys.ui.home.CredentialEvent
import com.example.safekeys.utils.TestTags
import kotlin.reflect.KFunction2


@Composable
fun AutofillAuthDialogue(
    state: AutofillAuthState,
    onLoginSuccess: () -> Unit,
    onEvent: (AutofillAuthEvent) -> Unit) {

    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .background(Color.Black)
            ) {
                Column(
                    modifier = Modifier
                        .padding(30.dp, 50.dp, 30.dp,30.dp)
                        .fillMaxSize()
                ) {

                    Text(
                        text = "SafeKeys",
                        color = Color.White,
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Unbreakable Password Security, Offline and Encrypted",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }

            // Content below the box
            Column(
                modifier = Modifier
                    .padding(40.dp)
                    .fillMaxSize()
                    .weight(0.4f)
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                OutlinedTextField(
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    value = state.password,
                    onValueChange =  {
                        onEvent(AutofillAuthEvent.SetPassword(it))
                    },
                    isError = state.passwordError != null,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null
                        )
                    },
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

                Spacer(modifier = Modifier.height(30.dp))

                OutlinedButton(
                    onClick =  {
                        onEvent(AutofillAuthEvent.Submit(onClick = onLoginSuccess))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(48.dp),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(text = "Login", fontSize = 18.sp)
                }
            }
        }
    }

}