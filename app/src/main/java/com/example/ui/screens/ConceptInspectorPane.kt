package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.TopicHinglishData
import com.example.domain.model.AppLanguage
import com.example.domain.model.ConceptTopic
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.JetBrainsMonoFontFamily

@Composable
fun ConceptInspectorPane(
    topic: ConceptTopic,
    language: AppLanguage,
    localizedData: TopicHinglishData?,
    currentStepIndex: Int,
    selectedPartId: String?,
    isMastered: Boolean,
    onSelectPart: (String?) -> Unit,
    onSelectStep: (Int) -> Unit,
    onToggleMastered: () -> Unit,
    embeddedInScroll: Boolean = false,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember(topic.id) { mutableIntStateOf(0) }
    val tabs = when (language) {
        AppLanguage.TELUGU -> listOf("నోట్స్ (అర్థం)", "భాగాలు & ఫ్లో", "పరీక్ష వైవా")
        AppLanguage.HINGLISH -> listOf("Samjho (Notes)", "Parts & Flow", "Exam Viva")
        AppLanguage.ENGLISH -> listOf(
            stringResource(R.string.tab_notes),
            stringResource(R.string.tab_parts),
            stringResource(R.string.tab_exam)
        )
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        ) {
            tabs.forEachIndexed { idx, title ->
                Tab(
                    selected = selectedTab == idx,
                    onClick = { selectedTab = idx },
                    text = { Text(title, style = MaterialTheme.typography.labelLarge) },
                    modifier = Modifier.testTag("inspector_tab_$idx")
                )
            }
        }

        val contentModifier = if (embeddedInScroll) {
            Modifier
                .fillMaxWidth()
                .padding(14.dp)
        } else {
            Modifier
                .weight(1f)
                .fillMaxWidth()
        }

        if (embeddedInScroll) {
            Column(
                modifier = contentModifier,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InspectorTabBody(
                    selectedTab = selectedTab,
                    topic = topic,
                    language = language,
                    localizedData = localizedData,
                    currentStepIndex = currentStepIndex,
                    selectedPartId = selectedPartId,
                    isMastered = isMastered,
                    onSelectPart = onSelectPart,
                    onSelectStep = onSelectStep,
                    onToggleMastered = onToggleMastered
                )
            }
        } else {
            LazyColumn(
                modifier = contentModifier,
                contentPadding = PaddingValues(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    InspectorTabBody(
                        selectedTab = selectedTab,
                        topic = topic,
                        language = language,
                        localizedData = localizedData,
                        currentStepIndex = currentStepIndex,
                        selectedPartId = selectedPartId,
                        isMastered = isMastered,
                        onSelectPart = onSelectPart,
                        onSelectStep = onSelectStep,
                        onToggleMastered = onToggleMastered
                    )
                }
            }
        }
    }
}

@Composable
private fun InspectorTabBody(
    selectedTab: Int,
    topic: ConceptTopic,
    language: AppLanguage,
    localizedData: TopicHinglishData?,
    currentStepIndex: Int,
    selectedPartId: String?,
    isMastered: Boolean,
    onSelectPart: (String?) -> Unit,
    onSelectStep: (Int) -> Unit,
    onToggleMastered: () -> Unit
) {
    when (selectedTab) {
        0 -> {
            val summaryText = if (language == AppLanguage.ENGLISH) {
                topic.summary
            } else {
                localizedData?.summary ?: topic.summary
            }
            val keyPointsList = if (language == AppLanguage.ENGLISH) {
                topic.keyPoints
            } else {
                localizedData?.keyPoints ?: topic.keyPoints
            }

            Text(
                text = summaryText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = when (language) {
                            AppLanguage.TELUGU -> "RTL మరియు పరీక్ష సూత్రాలు"
                            AppLanguage.HINGLISH -> "RTL aur Exam Formulas"
                            AppLanguage.ENGLISH -> "RTL & Key Formulas (CBCS Exam)"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    topic.rtlOrFormulas.forEach { line ->
                        Text(
                            text = "• $line",
                            style = MaterialTheme.typography.labelMedium.copy(fontFamily = JetBrainsMonoFontFamily),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            Text(
                text = when (language) {
                    AppLanguage.TELUGU -> "ముఖ్యమైన అంశాలు (సులభ భాషలో)"
                    AppLanguage.HINGLISH -> "Important Points (Aasaan Bhasha Mein)"
                    AppLanguage.ENGLISH -> "Key Syllabus Takeaways"
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            keyPointsList.forEach { point ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("▸", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text(
                        text = point,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        1 -> {
            Text(
                text = when (language) {
                    AppLanguage.TELUGU -> "హైలైట్ చేయడానికి ఏదైనా హార్డ్‌వేర్ బ్లాక్‌పై ట్యాప్ చేయండి:"
                    AppLanguage.HINGLISH -> "Kisi bhi Hardware Block par tap karke highlight karein:"
                    AppLanguage.ENGLISH -> "Tap any Hardware Module to highlight it on the canvas:"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            topic.hardwareParts.forEach { part ->
                val isSelected = part.id == selectedPartId
                val roleText = if (language == AppLanguage.ENGLISH) {
                    part.roleInCbcs
                } else {
                    localizedData?.partRoles?.get(part.id) ?: part.roleInCbcs
                }
                Surface(
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                            RoundedCornerShape(14.dp)
                        )
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelectPart(part.id) }
                        .testTag("part_row_${part.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = part.shortCode,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
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
                    }
                }
            }

            if (topic.steps.size > 1) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (language) {
                        AppLanguage.TELUGU -> "దశల వారీ సిమ్యులేషన్ క్రమం"
                        AppLanguage.HINGLISH -> "Step-by-Step Sequence"
                        AppLanguage.ENGLISH -> "Simulation Sequence"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                topic.steps.forEachIndexed { idx, step ->
                    val isCurr = idx == currentStepIndex
                    Surface(
                        color = if (isCurr) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectStep(idx) }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = step.title,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = step.rtlAction,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        else -> {
            Text(
                text = when (language) {
                    AppLanguage.TELUGU -> "CBCS పరీక్ష & వైవా ప్రశ్నలు (తెలుగు జవాబులు)"
                    AppLanguage.HINGLISH -> "CBCS Exam & Viva Questions (Hinglish Answer)"
                    AppLanguage.ENGLISH -> "CBCS University & Viva Questions"
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            topic.vivaQuestions.forEachIndexed { idx, vq ->
                var expanded by remember(topic.id, idx) { mutableStateOf(idx == 0) }
                val ansText = if (language == AppLanguage.ENGLISH) {
                    vq.answer
                } else {
                    localizedData?.vivaAnswers?.getOrNull(idx) ?: vq.answer
                }
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { expanded = !expanded }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Q${idx + 1}. ${vq.question}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Toggle answer"
                            )
                        }
                        AnimatedVisibility(visible = expanded) {
                            Text(
                                text = ansText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(6.dp))
    Button(
        onClick = onToggleMastered,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isMastered) EmeraldSuccess else MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("mark_understood_btn")
    ) {
        Icon(
            imageVector = if (isMastered) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isMastered) {
                when (language) {
                    AppLanguage.TELUGU -> "బాగా అర్థమైంది! (Mastered)"
                    AppLanguage.HINGLISH -> "Samajh Aa Gaya! (Mastered)"
                    AppLanguage.ENGLISH -> "Concept Mastered!"
                }
            } else {
                when (language) {
                    AppLanguage.TELUGU -> "అర్థమైంది — పూర్తి చేయి"
                    AppLanguage.HINGLISH -> "Samajh Aa Gaya — Mark Done"
                    AppLanguage.ENGLISH -> stringResource(R.string.mark_understood)
                }
            },
            style = MaterialTheme.typography.labelLarge
        )
    }
}
