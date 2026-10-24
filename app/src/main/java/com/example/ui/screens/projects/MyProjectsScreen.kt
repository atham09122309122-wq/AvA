package com.example.ui.screens.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AvaSubTopBar
import com.example.ui.screens.profile.ProjectListItem
import com.example.ui.theme.AvaBlack
import com.example.ui.theme.AvaCard
import com.example.ui.theme.AvaCardBorder
import com.example.ui.theme.AvaPurpleLight
import com.example.ui.theme.AvaPurplePrimary
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel
import com.example.util.stringRes

@Composable
fun MyProjectsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.projects.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }

    val filters = listOf("All", "advertisement", "personal_photo", "story", "template")

    val filtered = remember(projects, selectedFilter) {
        if (selectedFilter == "All") {
            projects
        } else {
            projects.filter { it.projectType == selectedFilter }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        AvaSubTopBar(
            title = "My Studio Projects",
            onBack = { viewModel.navigateBack() },
            actions = {
                IconButton(onClick = { viewModel.navigateTo(AppScreen.CREATE_CHOOSER) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Create", tint = AvaPurpleLight)
                }
            }
        )

        // Filters
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { f ->
                val isSelected = selectedFilter == f
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = f },
                    label = { Text(text = f.replace("_", " ").uppercase(), fontSize = 11.sp, color = if (isSelected) Color.White else AvaTextSecondary) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AvaPurplePrimary,
                        containerColor = AvaCard
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) AvaPurplePrimary else AvaCardBorder,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringRes("empty_projects"), color = AvaTextSecondary, fontSize = 14.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(filtered) { project ->
                    ProjectListItem(
                        project = project,
                        onEdit = {
                            viewModel.activeProject.value = project
                            viewModel.navigateTo(AppScreen.IMAGE_EDITOR)
                        },
                        onDelete = {
                            viewModel.deleteProject(project.id)
                        }
                    )
                }
            }
        }
    }
}
