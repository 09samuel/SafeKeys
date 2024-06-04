package com.example.safekeys.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.safekeys.CredentialEvent
import com.example.safekeys.CredentialState

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
                TextField(
                    value = state.username,
                    onValueChange = { onEvent(CredentialEvent.SetUsername(it)) },
                    placeholder = { Text(text = "Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = state.password,
                    onValueChange = { onEvent(CredentialEvent.SetPassword(it)) },
                    placeholder = { Text(text = "Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = state.website,
                    onValueChange = { onEvent(CredentialEvent.SetWebsite(it)) },
                    placeholder = { Text(text = "Website") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = state.title,
                    onValueChange = { onEvent(CredentialEvent.SetTitle(it)) },
                    placeholder = { Text(text = "Title") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = { onEvent(CredentialEvent.SaveCredential) }) {
                    Text(text = "Save")
                }
            }
        }
    )
}
