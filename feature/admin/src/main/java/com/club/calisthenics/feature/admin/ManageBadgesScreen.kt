package com.club.calisthenics.feature.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.club.calisthenics.core.domain.model.Badge
import com.club.calisthenics.core.ui.components.CalisthenicsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageBadgesScreen(
    onBack: () -> Unit,
    onNavigateToCreateBadge: () -> Unit,
    onNavigateToEditBadge: (String) -> Unit,
    viewModel: ManageBadgesViewModel = hiltViewModel()
) {
    val badges by viewModel.allBadges.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MANAGE BADGES", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreateBadge) {
                Icon(Icons.Default.Add, contentDescription = "Create Badge")
            }
        }
    ) { innerPadding ->
        if (badges.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("No badges found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(badges) { badge ->
                    BadgeManagementItem(
                        badge = badge,
                        onEdit = { onNavigateToEditBadge(badge.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BadgeManagementItem(
    badge: Badge,
    onEdit: () -> Unit
) {
    CalisthenicsCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = badge.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    }
}
