package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CbcsHinglishContent
import com.example.data.CbcsQuizBank
import com.example.data.CbcsTeluguContent
import com.example.domain.model.AppLanguage
import com.example.ui.theme.CoralError
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.ArchUiState

@Composable
fun QuizArenaScreen(
    uiState: ArchUiState,
    onStartQuizUnit: (String) -> Unit,
    onSubmitOption: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val quiz = uiState.quizState
    val bestScore = uiState.progress.bestScoresByUnit[quiz.unitFilterId]

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 780.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CBCS Exam Quiz Arena",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (bestScore != null) {
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "Best: $bestScore%",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = when (uiState.progress.language) {
                            AppLanguage.TELUGU -> "డిజిటల్ లాజిక్ మరియు 5 CBCS యూనిట్లపై మీ పరిజ్ఞానాన్ని పరీక్షించుకోండి."
                            AppLanguage.HINGLISH -> "Digital Logic aur sabhi 5 CBCS Architecture units par apni taiyari check karo."
                            AppLanguage.ENGLISH -> "Test your mastery across Digital Logic & all 5 CBCS Architecture units."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = quiz.unitFilterId == "all",
                            onClick = { onStartQuizUnit("all") },
                            label = { Text("Full CBCS Mock (${CbcsQuizBank.questions.size})") },
                            modifier = Modifier.testTag("quiz_unit_all")
                        )
                    }
                    items(uiState.units, key = { it.id }) { unit ->
                        FilterChip(
                            selected = quiz.unitFilterId == unit.id,
                            onClick = { onStartQuizUnit(unit.id) },
                            label = { Text(unit.numberRoman) },
                            modifier = Modifier.testTag("quiz_${unit.id}")
                        )
                    }
                }
            }

            if (quiz.isFinished) {
                item {
                    val total = quiz.questions.size.coerceAtLeast(1)
                    val pct = (quiz.correctCount * 100) / total
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(22.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Quiz complete",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(56.dp)
                            )
                            Text(
                                text = "Score: ${quiz.correctCount} / $total ($pct%)",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = when {
                                    pct >= 80 -> "Exam Ready! Strong command of CBCS Computer Architecture."
                                    pct >= 50 -> "Good progress! Revisit the 3D simulations for any missed topics."
                                    else -> "Explore the 3D Lab diagrams and RTL steps, then try again."
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Button(
                                onClick = { onStartQuizUnit(quiz.unitFilterId) },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("quiz_retry_btn")
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Retake Quiz")
                            }
                        }
                    }
                }
            } else {
                val question = quiz.questions.getOrNull(quiz.currentIndex)
                if (question != null) {
                    item {
                        val progress = (quiz.currentIndex + 1).toFloat() / quiz.questions.size.coerceAtLeast(1)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Question ${quiz.currentIndex + 1} of ${quiz.questions.size}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Correct: ${quiz.correctCount}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(50))
                            )
                        }
                    }

                    item {
                        Card(
                            shape = RoundedCornerShape(22.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f), RoundedCornerShape(22.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = question.question,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                question.options.forEachIndexed { idx, optText ->
                                    val selected = quiz.selectedOptionIndex
                                    val isAnswered = selected != null
                                    val isThisSelected = selected == idx
                                    val isThisCorrect = idx == question.correctIndex

                                    val borderColor = when {
                                        isAnswered && isThisCorrect -> EmeraldSuccess
                                        isAnswered && isThisSelected && !isThisCorrect -> CoralError
                                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                                    }
                                    val bgColor = when {
                                        isAnswered && isThisCorrect -> EmeraldSuccess.copy(alpha = 0.14f)
                                        isAnswered && isThisSelected && !isThisCorrect -> CoralError.copy(alpha = 0.14f)
                                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                    }

                                    Surface(
                                        color = bgColor,
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                                            .clip(RoundedCornerShape(14.dp))
                                            .clickable(enabled = !isAnswered) { onSubmitOption(idx) }
                                            .testTag("quiz_option_$idx")
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 14.dp, vertical = 14.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "${'A' + idx}.  $optText",
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = if (isThisSelected || (isAnswered && isThisCorrect)) FontWeight.SemiBold else FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.weight(1f)
                                            )
                                            if (isAnswered && isThisCorrect) {
                                                Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = EmeraldSuccess)
                                            } else if (isAnswered && isThisSelected) {
                                                Icon(Icons.Default.Cancel, contentDescription = "Incorrect", tint = CoralError)
                                            }
                                        }
                                    }
                                }

                                AnimatedVisibility(visible = quiz.selectedOptionIndex != null) {
                                    val lang = uiState.progress.language
                                    val explanationText = when (lang) {
                                        AppLanguage.ENGLISH -> question.explanation
                                        AppLanguage.HINGLISH -> CbcsHinglishContent.quizExplanations[question.id] ?: question.explanation
                                        AppLanguage.TELUGU -> CbcsTeluguContent.quizExplanations[question.id] ?: question.explanation
                                    }
                                    val stepParts = explanationText.split(": ", "; ", " -> ", " — ")
                                        .map { it.trim() }
                                        .filter { it.isNotEmpty() }
                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(12.dp),
                                                verticalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    text = when (lang) {
                                                        AppLanguage.TELUGU -> "దశల వారీ వివరణ"
                                                        AppLanguage.HINGLISH -> "Step-by-Step Explanation"
                                                        AppLanguage.ENGLISH -> "Step-by-Step Explanation"
                                                    },
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                                if (stepParts.size > 1) {
                                                    stepParts.forEachIndexed { idx, part ->
                                                        Text(
                                                            text = "${idx + 1}. $part",
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )
                                                    }
                                                } else {
                                                    Text(
                                                        text = explanationText,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            }
                                        }

                                        Button(
                                            onClick = onNextQuestion,
                                            shape = RoundedCornerShape(14.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(50.dp)
                                                .testTag("quiz_next_btn")
                                        ) {
                                            Text(
                                                text = if (quiz.currentIndex + 1 >= quiz.questions.size) "Finish Quiz" else "Next Question"
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
