package com.club.calisthenics.feature.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.club.calisthenics.core.domain.model.Badge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBadgeScreen(
    badgeId: String?,
    onBack: () -> Unit,
    viewModel: EditBadgeViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(true) }

    val badgeToEdit by viewModel.badgeToEdit.collectAsState()
    val saved by viewModel.saved.collectAsState()

    LaunchedEffect(badgeToEdit) {
        badgeToEdit?.let {
            name = it.name
            description = it.description
            imageUrl = it.imageUrl ?: ""
            isActive = it.isActive
        }
    }

    LaunchedEffect(saved) {
        if (saved) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (badgeId == null) "CREATE BADGE" else "EDIT BADGE", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AdminTextField(value = name, onValueChange = { name = it }, label = "Badge Name", placeholder = "e.g. Muscle Up King")
            AdminTextField(value = description, onValueChange = { description = it }, label = "Description", placeholder = "How to earn it?", singleLine = false, minLines = 3)
            AdminTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = "Icon URL (Optional)", placeholder = "https://...")

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isActive, onCheckedChange = { isActive = it })
                Text("Active (Visible to athletes)")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.saveBadge(
                        Badge(
                            id = badgeId ?: "",
                            name = name,
                            description = description,
                            imageUrl = imageUrl.takeIf { it.isNotBlank() },
                            isActive = isActive
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("SAVE BADGE", fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun AdminTextField(value: String, onValueChange: (String) -> Unit, label: String, placeholder: String, singleLine: Boolean = true, minLines: Int = 1) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            minLines = minLines,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
