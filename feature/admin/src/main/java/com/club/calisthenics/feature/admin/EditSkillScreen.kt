package com.club.calisthenics.feature.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.club.calisthenics.core.domain.model.Skill

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSkillScreen(
    skillId: String?,
    onBack: () -> Unit,
    viewModel: EditSkillViewModel = hiltViewModel()
) {
    var skillName by remember { mutableStateOf("") }
    var skillCategory by remember { mutableStateOf("") }
    var skillDifficultyTag by remember { mutableStateOf("") }
    var skillDescription by remember { mutableStateOf("") }
    var skillOrder by remember { mutableStateOf("0") }
    var skillPublished by remember { mutableStateOf(false) }
    
    val skillToEdit by viewModel.skillToEdit.collectAsState()
    val saved by viewModel.saved.collectAsState()

    LaunchedEffect(skillToEdit) {
        skillToEdit?.let {
            skillName = it.name
            skillCategory = it.category
            skillDifficultyTag = it.difficultyTag
            skillDescription = it.description
            skillOrder = it.order.toString()
            skillPublished = it.published
        }
    }

    LaunchedEffect(saved) {
        if (saved) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (skillId == null) "NEW SKILL" else "EDIT SKILL", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AdminTextField(value = skillName, onValueChange = { skillName = it }, label = "Skill Name", placeholder = "e.g. Muscle Up")
            AdminTextField(value = skillCategory, onValueChange = { skillCategory = it }, label = "Category", placeholder = "e.g. Pulling")
            AdminTextField(value = skillDifficultyTag, onValueChange = { skillDifficultyTag = it }, label = "Difficulty", placeholder = "e.g. Advanced")
            AdminTextField(value = skillDescription, onValueChange = { skillDescription = it }, label = "Description", placeholder = "Tutorial content...", singleLine = false, minLines = 4)
            AdminTextField(value = skillOrder, onValueChange = { skillOrder = it }, label = "Display Order", placeholder = "0", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = skillPublished, onCheckedChange = { skillPublished = it })
                Text("Published (Visible to members)")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.saveSkill(
                        Skill(
                            id = skillId ?: "",
                            name = skillName,
                            category = skillCategory,
                            difficultyTag = skillDifficultyTag,
                            description = skillDescription,
                            imageUrl = null,
                            videoUrl = null,
                            order = skillOrder.toIntOrNull() ?: 0,
                            published = skillPublished
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("SAVE SKILL", fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
private fun AdminTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            minLines = minLines,
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = keyboardOptions
        )
    }
}
