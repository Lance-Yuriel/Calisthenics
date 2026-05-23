package com.club.calisthenics.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun EventCard(
    title: String,
    location: String,
    dateTime: LocalDateTime,
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM d • HH:mm")
    
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .then(if (!imageUrl.isNullOrEmpty()) Modifier.height(180.dp) else Modifier.wrapContentHeight()),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Gradient Overlay
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = if (!imageUrl.isNullOrEmpty()) Arrangement.Bottom else Arrangement.Top
            ) {
                if (!imageUrl.isNullOrEmpty()) {
                    // When there's an image, keep the layout aligned to bottom
                    Spacer(modifier = Modifier.weight(1f))
                }
                
                Text(
                    text = dateTime.format(dateFormatter).uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (!imageUrl.isNullOrEmpty()) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (!imageUrl.isNullOrEmpty()) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (!imageUrl.isNullOrEmpty()) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
