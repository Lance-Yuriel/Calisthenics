package com.club.calisthenics.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.club.calisthenics.core.domain.model.Event
import com.club.calisthenics.core.domain.model.UserStats
import com.club.calisthenics.core.ui.components.CalisthenicsButton
import com.club.calisthenics.core.ui.components.CalisthenicsCard
import com.club.calisthenics.feature.home.HomeUiState
import com.club.calisthenics.feature.home.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(uiState = uiState)
}

@Composable
internal fun HomeScreenContent(
    uiState: HomeUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        HeaderSection()
        
        Spacer(modifier = Modifier.height(32.dp))

        when (uiState) {
            is HomeUiState.Loading -> {
                // Skeleton could go here
                Text("Loading...", style = MaterialTheme.typography.bodyLarge)
            }
            is HomeUiState.Success -> {
                SuccessContent(uiState)
            }
            is HomeUiState.Error -> {
                Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    Column {
        Text(
            text = "Welcome back,",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "ATHLETE",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun SuccessContent(state: HomeUiState.Success) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        StatsSection(state.stats)
        
        state.featuredEvent?.let { event ->
            FeaturedEventSection(event)
        }

        state.announcement?.let { announcement ->
            AnnouncementSection(announcement)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        CalisthenicsButton(
            text = "Scan QR Code",
            onClick = { /* TODO */ }
        )
    }
}

@Composable
private fun StatsSection(stats: UserStats) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(
            label = "Attendance",
            value = stats.attendanceCount.toString(),
            modifier = Modifier.weight(1f)
        )
        StatCard(
            label = "Badges",
            value = stats.badgeCount.toString(),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    CalisthenicsCard(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FeaturedEventSection(event: Event) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "UPCOMING SESSION",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        CalisthenicsCard(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = event.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun AnnouncementSection(text: String) {
    CalisthenicsCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "📢",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
