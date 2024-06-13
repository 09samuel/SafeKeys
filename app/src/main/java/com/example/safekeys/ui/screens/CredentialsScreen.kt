//package com.example.safekeys.ui.screens
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.safekeys.ui.home.CredentialEvent
//import com.example.safekeys.ui.home.CredentialState
//import com.example.safekeys.data.model.SortType
//import com.example.safekeys.ui.home.CredentialViewModel
//
//@Composable
//fun CredentialsScreen(
//    state: CredentialState,
//    onEvent: (CredentialEvent) -> Unit,
//    viewModel: CredentialViewModel,
//    onClick: () -> Unit,
//) {
//    Scaffold(floatingActionButton = {
//        FloatingActionButton(onClick = {
//            onEvent(CredentialEvent.ShowDialog)
//
//        }) {
//            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Credential")
//        }
//    }) { padding ->
//        if (state.isAddingCredential) {
//            AddCredentialDialogue(state = state, onEvent = onEvent)
//        }
//        LazyColumn(
//            contentPadding = padding,
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            item {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .horizontalScroll(rememberScrollState()),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    SortType.values().forEach { sortType ->
//                        Row(
//                            modifier = Modifier.clickable {
//                                onEvent(CredentialEvent.SortCredentials(sortType))
//                            },
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            RadioButton(
//                                selected = state.sortType == sortType,
//                                onClick = { onEvent(CredentialEvent.SortCredentials(sortType)) }
//                            )
//                            Text(text = sortType.name)
//                        }
//                    }
//                }
//            }
//            items(state.credential) { credential ->
//                onEvent(CredentialEvent.DecryptCredentials(credential))
//                val decryptedPassword = state.decryptedPassword[credential.id] ?: ""
//                Row(modifier = Modifier.fillMaxWidth()) {
//                    Column(modifier = Modifier.weight(1f)) {
//                        Text(text = credential.title, fontSize = 20.sp)
//                        Text(text = decryptedPassword, fontSize = 12.sp)
//                    }
//                    IconButton(onClick = { onEvent(CredentialEvent.DeleteCredential(credential)) }) {
//                        Icon(
//                            imageVector = Icons.Default.Delete,
//                            contentDescription = "Delete Credential"
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
