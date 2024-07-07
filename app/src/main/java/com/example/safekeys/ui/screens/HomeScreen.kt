package com.example.safekeys.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.safekeys.R
import com.example.safekeys.data.model.SortType
import com.example.safekeys.ui.home.CredentialEvent
import com.example.safekeys.ui.home.CredentialViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    //onEvent: (CredentialEvent) -> Unit,
    navController: NavController,
    viewModel: CredentialViewModel = hiltViewModel(),
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "SafeKeys",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        fontSize = 30.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Sort,
                            contentDescription = "Sort",
                            tint = Color.White
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                ),
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        ScrollContent(
            innerPadding,
            //state,

            viewModel,

            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollContent(
    innerPadding: PaddingValues,
    //state: CredentialState,
    //onEvent: (CredentialEvent) -> Unit,
    viewModel: CredentialViewModel,
    //onClick: () -> Unit,
) {

    val state by viewModel.state.collectAsState()

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            viewModel.onEvent(CredentialEvent.ShowDialog)

        }, containerColor = Color.Black) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_credential),
                tint = Color.White
            )
        }
    }) { innerPaddingFab ->

        if (state.isAddingCredential) {
            AddCredentialDialogue()
        }
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddingFab)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 10.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortType.entries.forEach { sortType ->
                        FilterChip(
                            modifier = Modifier.padding(end = 6.dp),
                            shape = RoundedCornerShape(26.dp),
                            selected = state.sortType == sortType,
                            onClick = { viewModel.onEvent(CredentialEvent.SortCredentials(sortType)) },
                            label = { Text(text = sortType.displayName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,

                            )
                        )

                    }
                }
            }
            items(state.credential) { credential ->
                viewModel.onEvent(CredentialEvent.DecryptCredentials(credential))
                val decryptedPassword = state.decryptedPassword[credential.id] ?: ""

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawRoundRect(
                            color = Color.LightGray,
                            size = size,
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .padding(end = 32.dp)
                    ) {
                        Text(
                            text = credential.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = credential.username,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 10,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = decryptedPassword,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 10,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.onEvent(
                                CredentialEvent.DeleteCredential(
                                    credential
                                )
                            )
                        },
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete note",
                            tint = MaterialTheme.colorScheme.surfaceTint
                        )
                    }
                }
            }
        }
    }
}
