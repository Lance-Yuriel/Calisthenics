package com.club.calisthenics.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "planche")
    
    // Vertical motion for the push-up
    val translationY by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f, // Guy goes down
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pushup"
    )
    
    // Subtle scale for effort
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "effort"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Planche Animation Box
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .offset(y = translationY.dp)
                    .scale(scale),
                contentAlignment = Alignment.Center
            ) {
                // Cartoon Image Placeholder (Using Emoji to simulate the planche guy)
                Text(
                    text = "🤸", 
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 80.sp)
                )
                
                // Representing the parallettes
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .width(100.dp)
                        .height(4.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), androidx.compose.foundation.shape.CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "CALISTHENICS CLUB",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 4.sp
            )
            
            Text(
                text = "HOLDING THE LINE...",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
