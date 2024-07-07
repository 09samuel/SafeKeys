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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.safekeys.ui.home.CredentialEvent
import com.example.safekeys.ui.home.CredentialState
import com.example.safekeys.ui.home.CredentialViewModel
import com.example.safekeys.utils.TestTags

@Composable
fun AddCredentialDialogue(
    viewModel: CredentialViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    AlertDialog(
        modifier = Modifier.testTag(TestTags.CREDENTIAL_DIALOGUE),
        onDismissRequest = { viewModel.onEvent(CredentialEvent.HideDialog) },
        title = { "Add Credential" },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    singleLine = true,
                    value = state.username,
                    onValueChange = { viewModel.onEvent(CredentialEvent.SetUsername(it)) },
                    placeholder = { Text(text = "Username") },
                    modifier = Modifier.fillMaxWidth().testTag(TestTags.USERNAME_TEXT_FIELD),
                    isError = state.usernameError != null,
                )
                if (state.usernameError != null) {
                    Text(
                        text = state.usernameError!!,
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 14.sp,
                        fontSize = 12.sp
                    )
                }

                OutlinedTextField(
                    singleLine = true,
                    value = state.password,
                    onValueChange = { viewModel.onEvent(CredentialEvent.SetPassword(it)) },
                    placeholder = { Text(text = "Password") },
                    modifier = Modifier.fillMaxWidth().testTag(TestTags.PASSWORD_TEXT_FIELD),
                    isError = state.passwordError != null,
                )
                if (state.passwordError != null) {
                    Text(
                        text = state.passwordError!!,
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 14.sp,
                        fontSize = 12.sp
                    )
                }

                OutlinedTextField(
                    singleLine = true,
                    value = state.website,
                    onValueChange = { viewModel.onEvent(CredentialEvent.SetWebsite(it)) },
                    placeholder = { Text(text = "Website") },
                    modifier = Modifier.fillMaxWidth().testTag(TestTags.WEBSITE_TEXT_FIELD),
                    isError = state.websiteError != null,
                )
                if (state.websiteError != null) {
                    Text(
                        text = state.websiteError!!,
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 14.sp,
                        fontSize = 12.sp
                    )
                }

                OutlinedTextField(
                    singleLine = true,
                    value = state.title,
                    onValueChange = { viewModel.onEvent(CredentialEvent.SetTitle(it)) },
                    placeholder = { Text(text = "Title") },
                    modifier = Modifier.fillMaxWidth().testTag(TestTags.TITLE_TEXT_FIELD),
                    isError = state.titleError != null,
                )
                if (state.titleError != null) {
                    Text(
                        text = state.titleError!!,
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
                        viewModel.onEvent(CredentialEvent.SaveCredential)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(48.dp)
                        .semantics { contentDescription = "Save Credential" },
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
