package com.club.calisthenics.feature.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.club.calisthenics.core.domain.model.Event
import com.club.calisthenics.core.ui.components.CalisthenicsButton
import com.club.calisthenics.core.ui.components.QRScanner
import com.club.calisthenics.core.ui.util.QRGenerator
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    onBackClick: () -> Unit,
    viewModel: EventDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isScanning by remember { mutableStateOf(false) }

    if (isScanning) {
        QRScanner(
            onResult = { result ->
                val currentState = uiState
                if (currentState is EventDetailUiState.Success && result == currentState.event.id) {
                    viewModel.checkIn()
                }
                isScanning = false
            },
            onClose = { isScanning = false }
        )
    } else {
        val hasImage = (uiState as? EventDetailUiState.Success)?.event?.coverImageUrl != null
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { if (!hasImage) Text("Session Details") },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = if (hasImage) Color.Black.copy(alpha = 0.3f) else Color.Transparent,
                                contentColor = if (hasImage) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent
                    )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (val state = uiState) {
                    is EventDetailUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is EventDetailUiState.Success -> {
                        var showQrDialog by remember { mutableStateOf(false) }
                        
                        EventDetailContent(
                            event = state.event,
                            isAttending = state.isAttending,
                            isWaitlisted = state.isWaitlisted,
                            isFull = state.isFull,
                            isAdmin = state.isAdmin,
                            onRsvpClick = { viewModel.toggleRsvp() },
                            onScanClick = { isScanning = true },
                            onShowQrClick = { showQrDialog = true },
                            paddingValues = innerPadding
                        )

                        if (showQrDialog) {
                            QrCodeDialog(
                                eventId = state.event.id,
                                eventTitle = state.event.title,
                                onDismiss = { showQrDialog = false }
                            )
                        }
                    }
                    is EventDetailUiState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EventDetailContent(
    event: Event,
    isAttending: Boolean,
    isWaitlisted: Boolean,
    isFull: Boolean,
    isAdmin: Boolean,
    onRsvpClick: () -> Unit,
    onScanClick: () -> Unit,
    onShowQrClick: () -> Unit,
    paddingValues: PaddingValues
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (!event.coverImageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = event.coverImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))
        }

        Column(modifier = Modifier.padding(24.dp)) {
            if (isAdmin) {
                OutlinedButton(
                    onClick = onShowQrClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.QrCode, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SHOW CHECK-IN QR")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoRow(
                icon = Icons.Default.Schedule,
                title = event.startAt.format(dateFormatter),
                subtitle = "${event.startAt.format(timeFormatter)} - ${event.endAt.format(timeFormatter)}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(
                icon = Icons.Default.LocationOn,
                title = event.location,
                subtitle = "Tap for directions"
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isAttending) {
                CalisthenicsButton(
                    text = "Check-in with QR",
                    onClick = onScanClick,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            Text(
                text = "ABOUT THIS SESSION",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "ATTENDEES",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${event.attendees.size} / ${event.capacity} people joined",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(48.dp))

            val buttonText = when {
                isAttending -> "Cancel RSVP"
                isWaitlisted -> "Leave Waitlist"
                isFull -> "Join Waitlist"
                else -> "RSVP Now"
            }

            CalisthenicsButton(
                text = buttonText,
                onClick = onRsvpClick,
                enabled = true, // Waitlist always possible unless cancelled
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
        }
    }
}

@Composable
private fun QrCodeDialog(
    eventId: String,
    eventTitle: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = eventTitle.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                val qrBitmap = remember(eventId) { QRGenerator.generateQRCode(eventId) }
                
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (qrBitmap != null) {
                        androidx.compose.foundation.Image(
                            bitmap = qrBitmap.asImageBitmap(),
                            contentDescription = "Check-in QR Code",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            text = "Error generating QR",
                            color = Color.Red,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Athletes can scan this to check-in",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("CLOSE")
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(8.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
