package com.example.safekeys.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safekeys.ui.home.CredentialEvent
import com.example.safekeys.ui.home.CredentialState

@Composable
fun AddCredentialDialogue(
    state: CredentialState,
    onEvent: (CredentialEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onEvent(CredentialEvent.HideDialog) },
        title = { "Add Credential" },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    singleLine = true,
                    value = state.username,
                    onValueChange = { onEvent(CredentialEvent.SetUsername(it)) },
                    placeholder = { Text(text = "Username") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.usernameError != null,
                )
                if (state.usernameError != null) {
                    Text(
                        text = state.usernameError,
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 14.sp,
                        fontSize = 12.sp
                    )
                }

                OutlinedTextField(
                    singleLine = true,
                    value = state.password,
                    onValueChange = { onEvent(CredentialEvent.SetPassword(it)) },
                    placeholder = { Text(text = "Password") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.passwordError != null,
                )
                if (state.passwordError != null) {
                    Text(
                        text = state.passwordError,
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 14.sp,
                        fontSize = 12.sp
                    )
                }

                OutlinedTextField(
                    singleLine = true,
                    value = state.website,
                    onValueChange = { onEvent(CredentialEvent.SetWebsite(it)) },
                    placeholder = { Text(text = "Website") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.websiteError != null,
                )
                if (state.websiteError != null) {
                    Text(
                        text = state.websiteError,
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 14.sp,
                        fontSize = 12.sp
                    )
                }

                OutlinedTextField(
                    singleLine = true,
                    value = state.title,
                    onValueChange = { onEvent(CredentialEvent.SetTitle(it)) },
                    placeholder = { Text(text = "Title") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.titleError != null,
                )
                if (state.titleError != null) {
                    Text(
                        text = state.titleError,
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 14.sp,
                        fontSize = 12.sp
                    )
                }

            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                OutlinedButton(
                    onClick = {
                        onEvent(CredentialEvent.SaveCredential)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(48.dp),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(text = "Save")
                }
            }
        }
    )
}
