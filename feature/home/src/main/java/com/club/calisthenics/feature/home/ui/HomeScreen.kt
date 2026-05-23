package com.club.calisthenics.feature.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.club.calisthenics.core.domain.model.Event
import com.club.calisthenics.core.domain.model.UserStats
import com.club.calisthenics.core.ui.components.CalisthenicsButton
import com.club.calisthenics.core.ui.components.CalisthenicsCard
import com.club.calisthenics.core.ui.components.EventCard
import com.club.calisthenics.core.ui.components.QRScanner
import com.club.calisthenics.feature.home.HomeUiState
import com.club.calisthenics.feature.home.HomeViewModel

@Composable
fun HomeScreen(
    onEventClick: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onCreateEventClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isScanning by remember { mutableStateOf(false) }

    if (isScanning) {
        QRScanner(
            onResult = { result ->
                viewModel.checkIn(result)
                isScanning = false
            },
            onClose = { isScanning = false }
        )
    } else {
        HomeScreenContent(
            uiState = uiState,
            onEventClick = onEventClick,
            onNotificationsClick = onNotificationsClick,
            onCreateEventClick = onCreateEventClick,
            onScanClick = { isScanning = true }
        )
    }
}

@Composable
internal fun HomeScreenContent(
    uiState: HomeUiState,
    onEventClick: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onCreateEventClick: () -> Unit,
    onScanClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        val user = (uiState as? HomeUiState.Success)?.user
        HeaderSection(
            displayName = user?.displayName,
            role = user?.role?.name,
            onNotificationsClick = onNotificationsClick
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        when (uiState) {
            is HomeUiState.Loading -> {
                Text("Loading...", style = MaterialTheme.typography.bodyLarge)
            }
            is HomeUiState.Success -> {
                SuccessContent(uiState, onEventClick, onCreateEventClick, onScanClick)
            }
            is HomeUiState.Error -> {
                Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun HeaderSection(displayName: String?, role: String?, onNotificationsClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = displayName?.uppercase() ?: "ATHLETE",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                if (role == "ADMIN") {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = androidx.compose.foundation.shape.CircleShape
                    ) {
                        Text(
                            text = "ADMIN",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
        IconButton(onClick = onNotificationsClick) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SuccessContent(
    state: HomeUiState.Success,
    onEventClick: (String) -> Unit,
    onCreateEventClick: () -> Unit,
    onScanClick: () -> Unit
) {
    val isAdmin = state.user?.role?.name == "ADMIN"
    
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        StatsSection(state.stats)
        
        if (state.featuredEvent != null) {
            FeaturedEventSection(
                event = state.featuredEvent,
                onClick = { onEventClick(state.featuredEvent.id) }
            )
        } else {
            EmptyFeaturedSection(isAdmin = isAdmin, onCreateClick = onCreateEventClick)
        }

        state.announcement?.let { announcement ->
            AnnouncementSection(announcement)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        CalisthenicsButton(
            text = "Scan QR Code",
            onClick = onScanClick
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
private fun FeaturedEventSection(
    event: Event,
    onClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "UPCOMING SESSION",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        EventCard(
            title = event.title,
            location = event.location,
            dateTime = event.startAt,
            imageUrl = event.coverImageUrl,
            status = event.state.name,
            onClick = onClick
        )
    }
}

@Composable
private fun EmptyFeaturedSection(isAdmin: Boolean, onCreateClick: () -> Unit) {
    CalisthenicsCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "No upcoming sessions found.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (isAdmin) {
                Button(onClick = onCreateClick) {
                    Text("CREATE SESSION")
                }
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
