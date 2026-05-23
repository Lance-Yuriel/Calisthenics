package com.club.calisthenics.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.club.calisthenics.core.domain.model.User
import com.club.calisthenics.core.domain.model.UserRole
import com.club.calisthenics.core.ui.components.CalisthenicsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDirectoryScreen(
    onBack: () -> Unit,
    onMemberClick: (String) -> Unit,
    viewModel: MemberDirectoryViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val members by viewModel.members.collectAsState()
    
    val filteredMembers = remember(members, searchQuery) {
        members.filter { 
            it.displayName?.contains(searchQuery, ignoreCase = true) == true ||
            it.email.contains(searchQuery, ignoreCase = true)
        }.sortedBy { it.displayName?.lowercase() ?: "" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MEMBERS", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                placeholder = { Text("Search by name...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            if (filteredMembers.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No members found.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredMembers) { member ->
                        MemberItem(
                            member = member,
                            onClick = { onMemberClick(member.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberItem(member: User, onClick: () -> Unit) {
    CalisthenicsCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(56.dp).clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                if (!member.photoUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = member.photoUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = member.displayName?.firstOrNull()?.toString()?.uppercase() ?: "A",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.displayName ?: "Athlete",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (member.role == UserRole.ADMIN) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "ADMIN",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}
