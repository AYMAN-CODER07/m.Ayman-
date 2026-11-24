package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.CbcsHinglishContent
import com.example.data.CbcsTeluguContent
import com.example.data.TopicHinglishData
import com.example.domain.model.AppLanguage
import com.example.domain.model.ConceptTopic
import com.example.domain.model.HardwarePartInfo
import com.example.domain.model.SimulationStep
import com.example.engine2d.Diagram2DPresets
import com.example.engine2d.Interactive2DDiagramViewport
import com.example.engine3d.Interactive3DViewport
import com.example.engine3d.ScenePresets3D
import com.example.ui.components.LanguageMenuButton
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.viewmodel.ArchUiState
import com.example.ui.viewmodel.VisualViewMode

@Composable
fun Concept3DLabScreen(
    uiState: ArchUiState,
    onBack: () -> Unit,
    onSelectViewMode: (VisualViewMode) -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onSelectMode: (Int) -> Unit,
    onSelectPart: (String?) -> Unit,
    onPrevStep: () -> Unit,
    onNextStep: () -> Unit,
    onTogglePlay: () -> Unit,
    onSelectStep: (Int) -> Unit,
    onToggleMastered: (String) -> Unit,
    onToggleBookmark: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val topic = uiState.activeTopic
    val language = uiState.progress.language
    val localizedData = remember(topic.id, language) {
        when (language) {
            AppLanguage.ENGLISH -> null
            AppLanguage.HINGLISH -> CbcsHinglishContent.topics[topic.id]
            AppLanguage.TELUGU -> CbcsTeluguContent.topics[topic.id]
        }
    }
    val currentStep = topic.steps.getOrNull(uiState.currentStepIndex)

    val scene3D = remember(topic.sceneType, currentStep, uiState.interactiveModeIndex) {
        ScenePresets3D.buildScene(
            sceneType = topic.sceneType,
            step = currentStep,
            interactiveModeIndex = uiState.interactiveModeIndex
        )
    }
    val diagram2D = remember(topic.sceneType, currentStep, uiState.interactiveModeIndex, language) {
        Diagram2DPresets.buildDiagram(
            sceneType = topic.sceneType,
            step = currentStep,
            modeIndex = uiState.interactiveModeIndex,
            language = language
        )
    }

    val selectedPart = topic.hardwareParts.find { it.id == uiState.selectedPartId }
    val isMastered = topic.id in uiState.progress.masteredTopicIds
    val isBookmarked = topic.id in uiState.progress.bookmarkedTopicIds

    Column(modifier = modifier.fillMaxSize()) {
        LabHeaderBar(
            topic = topic,
            language = language,
            isBookmarked = isBookmarked,
            onBack = onBack,
            onSelectLanguage = onSelectLanguage,
            onToggleBookmark = { onToggleBookmark(topic.id) }
        )

        ViewModeSwitcherRow(
            viewMode = uiState.visualViewMode,
            language = language,
            onSelectViewMode = onSelectViewMode
        )

        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            val isWide = maxWidth >= 720.dp
            if (isWide) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(0.54f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (uiState.visualViewMode == VisualViewMode.STEP_BY_STEP) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                StepByStepProblemsPane(
                                    topicId = topic.id,
                                    language = language
                                )
                            }
                        } else {
                            if (topic.interactiveModes.isNotEmpty()) {
                                ModeSelectorRow(
                                    modes = topic.interactiveModes,
                                    selectedIndex = uiState.interactiveModeIndex,
                                    onSelectMode = onSelectMode
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                if (uiState.visualViewMode == VisualViewMode.DIAGRAM_2D) {
                                    Interactive2DDiagramViewport(
                                        diagram = diagram2D,
                                        selectedPartId = uiState.selectedPartId,
                                        language = language,
                                        onPartTapped = onSelectPart,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Interactive3DViewport(
                                        scene = scene3D,
                                        selectedPartId = uiState.selectedPartId,
                                        onPartTapped = onSelectPart,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                SelectedPartOverlay(
                                    part = selectedPart,
                                    language = language,
                                    localizedData = localizedData,
                                    onDismiss = { onSelectPart(null) },
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 58.dp, start = 12.dp, end = 12.dp)
                                )
                            }
                            SimulationControlBar(
                                steps = topic.steps,
                                currentStepIndex = uiState.currentStepIndex,
                                isPlaying = uiState.isSimPlaying,
                                language = language,
                                localizedData = localizedData,
                                onPrev = onPrevStep,
                                onNext = onNextStep,
                                onTogglePlay = onTogglePlay
                            )
                        }
                    }

                    ConceptInspectorPane(
                        topic = topic,
                        language = language,
                        localizedData = localizedData,
                        currentStepIndex = uiState.currentStepIndex,
                        selectedPartId = uiState.selectedPartId,
                        isMastered = isMastered,
                        onSelectPart = onSelectPart,
                        onSelectStep = onSelectStep,
                        onToggleMastered = { onToggleMastered(topic.id) },
                        modifier = Modifier
                            .weight(0.46f)
                            .fillMaxHeight()
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (uiState.visualViewMode == VisualViewMode.STEP_BY_STEP) {
                        item {
                            StepByStepProblemsPane(
                                topicId = topic.id,
                                language = language
                            )
                        }
                    } else {
                        if (topic.interactiveModes.isNotEmpty()) {
                            item {
                                ModeSelectorRow(
                                    modes = topic.interactiveModes,
                                    selectedIndex = uiState.interactiveModeIndex,
                                    onSelectMode = onSelectMode
                                )
                            }
                        }

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(310.dp)
                            ) {
                                if (uiState.visualViewMode == VisualViewMode.DIAGRAM_2D) {
                                    Interactive2DDiagramViewport(
                                        diagram = diagram2D,
                                        selectedPartId = uiState.selectedPartId,
                                        language = language,
                                        onPartTapped = onSelectPart,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Interactive3DViewport(
                                        scene = scene3D,
                                        selectedPartId = uiState.selectedPartId,
                                        onPartTapped = onSelectPart,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                SelectedPartOverlay(
                                    part = selectedPart,
                                    language = language,
                                    localizedData = localizedData,
                                    onDismiss = { onSelectPart(null) },
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 54.dp, start = 10.dp, end = 10.dp)
                                )
                            }
                        }

                        item {
                            SimulationControlBar(
                                steps = topic.steps,
                                currentStepIndex = uiState.currentStepIndex,
                                isPlaying = uiState.isSimPlaying,
                                language = language,
                                localizedData = localizedData,
                                onPrev = onPrevStep,
                                onNext = onNextStep,
                                onTogglePlay = onTogglePlay
                            )
                        }
                    }

                    item {
                        ConceptInspectorPane(
                            topic = topic,
                            language = language,
                            localizedData = localizedData,
                            currentStepIndex = uiState.currentStepIndex,
                            selectedPartId = uiState.selectedPartId,
                            isMastered = isMastered,
                            onSelectPart = onSelectPart,
                            onSelectStep = onSelectStep,
                            onToggleMastered = { onToggleMastered(topic.id) },
                            embeddedInScroll = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LabHeaderBar(
    topic: ConceptTopic,
    language: AppLanguage,
    isBookmarked: Boolean,
    onBack: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleBookmark: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(48.dp)
                .testTag("lab_back_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to syllabus"
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = topic.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${topic.examWeightage} • ${topic.difficulty}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        LanguageMenuButton(
            selectedLanguage = language,
            onSelectLanguage = onSelectLanguage,
            modifier = Modifier
                .padding(end = 2.dp)
                .testTag("lab_language_selector")
        )
        IconButton(
            onClick = onToggleBookmark,
            modifier = Modifier
                .size(48.dp)
                .testTag("lab_bookmark_button")
        ) {
            Icon(
                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = "Bookmark concept",
                tint = if (isBookmarked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ViewModeSwitcherRow(
    viewMode: VisualViewMode,
    language: AppLanguage,
    onSelectViewMode: (VisualViewMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = viewMode == VisualViewMode.STEP_BY_STEP,
                onClick = { onSelectViewMode(VisualViewMode.STEP_BY_STEP) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                modifier = Modifier.testTag("view_mode_steps")
            ) {
                Text(
                    text = when (language) {
                        AppLanguage.TELUGU -> "దశలు"
                        AppLanguage.HINGLISH -> "Steps"
                        AppLanguage.ENGLISH -> "Steps"
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            SegmentedButton(
                selected = viewMode == VisualViewMode.DIAGRAM_2D,
                onClick = { onSelectViewMode(VisualViewMode.DIAGRAM_2D) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
                icon = {
                    Icon(
                        imageVector = Icons.Default.AccountTree,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                modifier = Modifier.testTag("view_mode_2d")
            ) {
                Text(
                    text = if (language == AppLanguage.TELUGU) "2D చిత్రం" else "2D Diagram",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            SegmentedButton(
                selected = viewMode == VisualViewMode.MODEL_3D,
                onClick = { onSelectViewMode(VisualViewMode.MODEL_3D) },
                shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
                icon = {
                    Icon(
                        imageVector = Icons.Default.ViewInAr,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                },
                modifier = Modifier.testTag("view_mode_3d")
            ) {
                Text(
                    text = if (language == AppLanguage.TELUGU) "3D మోడల్" else "3D Model",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ModeSelectorRow(
    modes: List<String>,
    selectedIndex: Int,
    onSelectMode: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(modes) { idx, modeName ->
            FilterChip(
                selected = idx == selectedIndex,
                onClick = { onSelectMode(idx) },
                label = { Text(modeName) },
                modifier = Modifier.testTag("mode_chip_$idx")
            )
        }
    }
}

@Composable
private fun SelectedPartOverlay(
    part: HardwarePartInfo?,
    language: AppLanguage,
    localizedData: TopicHinglishData?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = part != null, modifier = modifier) {
        if (part != null) {
            val roleText = if (language == AppLanguage.ENGLISH) {
                part.roleInCbcs
            } else {
                localizedData?.partRoles?.get(part.id) ?: part.roleInCbcs
            }
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = part.shortCode,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = part.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = roleText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Dismiss part details")
                    }
                }
            }
        }
    }
}

@Composable
private fun SimulationControlBar(
    steps: List<SimulationStep>,
    currentStepIndex: Int,
    isPlaying: Boolean,
    language: AppLanguage,
    localizedData: TopicHinglishData?,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onTogglePlay: () -> Unit
) {
    val step = steps.getOrNull(currentStepIndex) ?: return
    val stepDesc = if (language == AppLanguage.ENGLISH) {
        step.description
    } else {
        localizedData?.stepDescriptions?.getOrNull(currentStepIndex) ?: step.description
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f), RoundedCornerShape(18.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = step.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = step.rtlAction,
                        style = MaterialTheme.typography.labelMedium.copy(fontFamily = JetBrainsMonoFontFamily),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (steps.size > 1) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onPrev,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("sim_prev_btn")
                        ) {
                            Icon(Icons.Default.SkipPrevious, contentDescription = "Previous step")
                        }
                        Surface(
                            onClick = onTogglePlay,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("sim_play_btn")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause simulation" else "Play simulation",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                        IconButton(
                            onClick = onNext,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("sim_next_btn")
                        ) {
                            Icon(Icons.Default.SkipNext, contentDescription = "Next step")
                        }
                    }
                }
            }

            Text(
                text = stepDesc,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (step.registerSnapshot.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(step.registerSnapshot.entries.toList()) { (reg, value) ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "$reg: $value",
                                style = MaterialTheme.typography.labelSmall.copy(fontFamily = JetBrainsMonoFontFamily),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
