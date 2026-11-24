package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CbcsCurriculumRepository
import com.example.data.CbcsQuizBank
import com.example.data.UserProgressData
import com.example.data.UserProgressStore
import com.example.domain.model.AppLanguage
import com.example.domain.model.CbcsUnit
import com.example.domain.model.ConceptTopic
import com.example.domain.model.QuizQuestion
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class VisualViewMode {
    STEP_BY_STEP,
    DIAGRAM_2D,
    MODEL_3D
}

data class QuizSessionState(
    val unitFilterId: String = "all",
    val questions: List<QuizQuestion> = CbcsQuizBank.questions,
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val correctCount: Int = 0,
    val isFinished: Boolean = false
)

data class ArchUiState(
    val units: List<CbcsUnit> = CbcsCurriculumRepository.units,
    val allTopics: List<ConceptTopic> = CbcsCurriculumRepository.topics,
    val selectedUnitFilter: String = "all",
    val searchQuery: String = "",
    val activeTopic: ConceptTopic = CbcsCurriculumRepository.topics.first(),
    val currentStepIndex: Int = 0,
    val interactiveModeIndex: Int = 0,
    val visualViewMode: VisualViewMode = VisualViewMode.DIAGRAM_2D,
    val isSimPlaying: Boolean = false,
    val selectedPartId: String? = null,
    val progress: UserProgressData = UserProgressData(),
    val quizState: QuizSessionState = QuizSessionState()
) {
    val filteredTopics: List<ConceptTopic>
        get() {
            val byUnit = if (selectedUnitFilter == "all") allTopics else allTopics.filter { it.unitId == selectedUnitFilter }
            val q = searchQuery.trim()
            if (q.isEmpty()) return byUnit
            return byUnit.filter { topic ->
                topic.title.contains(q, ignoreCase = true) ||
                    topic.subtitle.contains(q, ignoreCase = true) ||
                    topic.summary.contains(q, ignoreCase = true) ||
                    topic.hardwareParts.any { it.name.contains(q, ignoreCase = true) || it.shortCode.contains(q, ignoreCase = true) }
            }
        }
}

class ArchViewModel(application: Application) : AndroidViewModel(application) {

    private val progressStore = UserProgressStore(application.applicationContext)

    private val _uiState = MutableStateFlow(ArchUiState())
    val uiState: StateFlow<ArchUiState> = _uiState.asStateFlow()

    private var simJob: Job? = null

    init {
        viewModelScope.launch {
            progressStore.progressFlow.collect { progressData ->
                _uiState.update { it.copy(progress = progressData) }
            }
        }
    }

    fun selectUnitFilter(unitId: String) {
        _uiState.update { it.copy(selectedUnitFilter = unitId) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun openTopic(topicId: String, startIn3D: Boolean = false) {
        val topic = CbcsCurriculumRepository.getTopicById(topicId) ?: return
        stopSimulation()
        val initialMode = when {
            startIn3D -> VisualViewMode.MODEL_3D
            topic.prefersStepByStep -> VisualViewMode.STEP_BY_STEP
            else -> VisualViewMode.DIAGRAM_2D
        }
        _uiState.update {
            it.copy(
                activeTopic = topic,
                currentStepIndex = 0,
                interactiveModeIndex = 0,
                visualViewMode = initialMode,
                selectedPartId = null,
                isSimPlaying = false
            )
        }
    }

    fun setVisualViewMode(mode: VisualViewMode) {
        _uiState.update { it.copy(visualViewMode = mode) }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            progressStore.setLanguage(language)
        }
    }

    fun setWebShareBaseUrl(url: String) {
        viewModelScope.launch {
            progressStore.setWebShareBaseUrl(url)
        }
    }

    fun selectInteractiveMode(modeIndex: Int) {
        _uiState.update { it.copy(interactiveModeIndex = modeIndex) }
    }

    fun selectPart(partId: String?) {
        _uiState.update { state ->
            val next = if (state.selectedPartId == partId) null else partId
            state.copy(selectedPartId = next)
        }
    }

    fun nextStep() {
        _uiState.update { state ->
            val max = state.activeTopic.steps.size.coerceAtLeast(1)
            state.copy(currentStepIndex = (state.currentStepIndex + 1) % max)
        }
    }

    fun prevStep() {
        _uiState.update { state ->
            val max = state.activeTopic.steps.size.coerceAtLeast(1)
            state.copy(currentStepIndex = if (state.currentStepIndex == 0) max - 1 else state.currentStepIndex - 1)
        }
    }

    fun selectStep(index: Int) {
        _uiState.update { state ->
            val clamped = index.coerceIn(0, (state.activeTopic.steps.size - 1).coerceAtLeast(0))
            state.copy(currentStepIndex = clamped)
        }
    }

    fun toggleSimulationPlay() {
        if (_uiState.value.isSimPlaying) {
            stopSimulation()
        } else {
            startSimulation()
        }
    }

    private fun startSimulation() {
        simJob?.cancel()
        _uiState.update { it.copy(isSimPlaying = true) }
        simJob = viewModelScope.launch {
            while (true) {
                delay(2400L)
                nextStep()
            }
        }
    }

    fun stopSimulation() {
        simJob?.cancel()
        simJob = null
        _uiState.update { it.copy(isSimPlaying = false) }
    }

    fun toggleMastered(topicId: String) {
        viewModelScope.launch {
            progressStore.toggleMastered(topicId)
        }
    }

    fun toggleBookmark(topicId: String) {
        viewModelScope.launch {
            progressStore.toggleBookmark(topicId)
        }
    }

    fun toggleDarkTheme() {
        val current = _uiState.value.progress.darkThemeEnabled
        viewModelScope.launch {
            progressStore.setDarkTheme(!current)
        }
    }

    // Quiz Actions
    fun startQuiz(unitId: String) {
        val list = CbcsQuizBank.getQuestionsForUnit(unitId)
        _uiState.update {
            it.copy(
                quizState = QuizSessionState(
                    unitFilterId = unitId,
                    questions = list,
                    currentIndex = 0,
                    selectedOptionIndex = null,
                    correctCount = 0,
                    isFinished = false
                )
            )
        }
    }

    fun submitQuizOption(optionIndex: Int) {
        _uiState.update { state ->
            val qState = state.quizState
            if (qState.selectedOptionIndex != null || qState.isFinished) return@update state
            val currentQ = qState.questions.getOrNull(qState.currentIndex) ?: return@update state
            val isCorrect = optionIndex == currentQ.correctIndex
            state.copy(
                quizState = qState.copy(
                    selectedOptionIndex = optionIndex,
                    correctCount = qState.correctCount + if (isCorrect) 1 else 0
                )
            )
        }
    }

    fun advanceQuiz() {
        val qState = _uiState.value.quizState
        val nextIndex = qState.currentIndex + 1
        if (nextIndex >= qState.questions.size) {
            val total = qState.questions.size.coerceAtLeast(1)
            val pct = (qState.correctCount * 100) / total
            viewModelScope.launch {
                progressStore.saveBestQuizScore(qState.unitFilterId, pct)
            }
            _uiState.update {
                it.copy(quizState = qState.copy(isFinished = true))
            }
        } else {
            _uiState.update {
                it.copy(
                    quizState = qState.copy(
                        currentIndex = nextIndex,
                        selectedOptionIndex = null
                    )
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        simJob?.cancel()
    }
}
