package com.club.calisthenics.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.club.calisthenics.core.ui.theme.CalisthenicsTheme

@Composable
fun ThemePreview() {
    CalisthenicsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Calisthenics Club",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "Trendy & Beautiful",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                CalisthenicsCard {
                    Text(
                        text = "This is a trendy card with low elevation and electric lime primary color accent.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                CalisthenicsButton(
                    text = "Get Started",
                    onClick = { }
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1C18)
@Composable
fun DarkThemePreview() {
    ThemePreview()
}
