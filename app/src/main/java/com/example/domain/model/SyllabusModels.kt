package com.example.domain.model

enum class AppLanguage(val code: String, val displayLabel: String) {
    ENGLISH("en", "English"),
    HINGLISH("hi", "Hinglish"),
    TELUGU("te", "తెలుగు")
}

enum class SceneType3D {
    NUMBER_SYSTEMS_CODES,
    LOGIC_GATES,
    KARNAUGH_MAP,
    MUX_DEMUX,
    FLIP_FLOPS,
    BUS_STRUCTURES,
    VON_NEUMANN,
    HARVARD_VS_VON_NEUMANN,
    INSTRUCTION_CYCLE,
    ALU_MICRO_OPS,
    ADDRESSING_MODES,
    CONTROL_UNIT,
    PIPELINE_5_STAGE,
    MEMORY_HIERARCHY,
    CACHE_MAPPING,
    VIRTUAL_MEMORY_TLB,
    IO_DMA_TRANSFER,
    FLYNNS_MULTICORE
}

data class CbcsUnit(
    val id: String,
    val numberRoman: String,
    val title: String,
    val subtitle: String,
    val creditsHours: String
)

data class SimulationStep(
    val title: String,
    val rtlAction: String,
    val description: String,
    val activePartIds: Set<String>,
    val activeBusIds: Set<String>,
    val registerSnapshot: Map<String, String> = emptyMap()
)

data class HardwarePartInfo(
    val id: String,
    val name: String,
    val shortCode: String,
    val roleInCbcs: String
)

data class VivaQuestion(
    val question: String,
    val answer: String
)

data class ProblemStep(
    val title: String,
    val expression: String = "",
    val explanation: String
)

data class WorkedProblem(
    val id: String,
    val title: String,
    val statement: String,
    val diagramNecessary: Boolean = false,
    val steps: List<ProblemStep>,
    val finalAnswer: String
)

data class LocalizedProblemOverlay(
    val title: String,
    val statement: String,
    val stepTitles: List<String>,
    val stepExplanations: List<String>,
    val finalAnswer: String
)

data class ConceptTopic(
    val id: String,
    val unitId: String,
    val title: String,
    val subtitle: String,
    val examWeightage: String,
    val difficulty: String,
    val sceneType: SceneType3D,
    val prefersStepByStep: Boolean = false,
    val interactiveModes: List<String> = emptyList(),
    val summary: String,
    val keyPoints: List<String>,
    val rtlOrFormulas: List<String>,
    val hardwareParts: List<HardwarePartInfo>,
    val steps: List<SimulationStep>,
    val vivaQuestions: List<VivaQuestion>
)

data class QuizQuestion(
    val id: String,
    val unitId: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)
