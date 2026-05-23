package com.club.calisthenics.feature.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.club.calisthenics.core.domain.model.Event
import com.club.calisthenics.core.domain.model.EventState
import com.club.calisthenics.core.ui.components.EventCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageEventsScreen(
    onBack: () -> Unit,
    onNavigateToCreateEvent: () -> Unit,
    onNavigateToEditEvent: (String) -> Unit,
    viewModel: ManageEventsViewModel = hiltViewModel()
) {
    val events by viewModel.allEvents.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MANAGE SESSIONS", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreateEvent) {
                Icon(Icons.Default.Add, contentDescription = "Create Session")
            }
        }
    ) { innerPadding ->
        if (events.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("No sessions found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(events) { event ->
                    EventManagementItem(
                        event = event,
                        onEdit = { onNavigateToEditEvent(event.id) },
                        onDelete = { viewModel.deleteEvent(event.id) },
                        onUpdateState = { newState -> viewModel.updateEventState(event.id, newState) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EventManagementItem(
    event: Event,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onUpdateState: (EventState) -> Unit
) {
    var showStateMenu by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        EventCard(
            title = event.title,
            location = event.location,
            dateTime = event.startAt,
            imageUrl = event.coverImageUrl,
            onClick = onEdit
        )
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                AssistChip(
                    onClick = { showStateMenu = true },
                    label = { Text(event.state.name) },
                    trailingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
                DropdownMenu(expanded = showStateMenu, onDismissRequest = { showStateMenu = false }) {
                    EventState.values().forEach { state ->
                        DropdownMenuItem(
                            text = { Text(state.name) },
                            onClick = {
                                onUpdateState(state)
                                showStateMenu = false
                            }
                        )
                    }
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
