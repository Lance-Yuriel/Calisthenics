package com.club.calisthenics.feature.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.club.calisthenics.core.domain.model.Event
import com.club.calisthenics.core.ui.components.EventCard

@Composable
fun EventsScreen(
    onEventClick: (String) -> Unit,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "SESSIONS",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        when (val state = uiState) {
            is EventsUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            is EventsUiState.Success -> {
                EventsList(
                    events = state.upcomingEvents,
                    onEventClick = onEventClick
                )
            }
            is EventsUiState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun EventsList(
    events: List<Event>,
    onEventClick: (String) -> Unit
) {
    if (events.isEmpty()) {
        Text(
            text = "No upcoming sessions found.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(events) { event ->
                EventCard(
                    title = event.title,
                    location = event.location,
                    dateTime = event.startAt,
                    imageUrl = event.coverImageUrl,
                    onClick = { onEventClick(event.id) }
                )
            }
        }
    }
}
