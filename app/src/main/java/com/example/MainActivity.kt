package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.components.LanguageMenuButton
import com.example.ui.screens.Concept3DLabScreen
import com.example.ui.screens.HomeCurriculumScreen
import com.example.ui.screens.QrWebScreen
import com.example.ui.screens.QuizArenaScreen
import com.example.ui.screens.Sandbox3DHubScreen
import com.example.ui.theme.Arch3dCoaTheme
import com.example.ui.viewmodel.ArchViewModel

const val ROUTE_SYLLABUS = "syllabus"
const val ROUTE_LAB_HUB = "lab_hub"
const val ROUTE_QUIZ = "quiz"
const val ROUTE_QR_WEB = "qr_web"
const val ROUTE_CONCEPT_3D = "concept_3d/{topicId}"

private data class TopLevelDestination(
    val route: String,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

private val TopDestinations = listOf(
    TopLevelDestination(ROUTE_SYLLABUS, R.string.nav_syllabus, Icons.AutoMirrored.Filled.MenuBook, Icons.AutoMirrored.Outlined.MenuBook, "nav_syllabus"),
    TopLevelDestination(ROUTE_LAB_HUB, R.string.nav_lab_3d, Icons.Filled.ViewInAr, Icons.Outlined.ViewInAr, "nav_lab_3d"),
    TopLevelDestination(ROUTE_QUIZ, R.string.nav_quiz, Icons.Filled.Quiz, Icons.Outlined.Quiz, "nav_quiz"),
    TopLevelDestination(ROUTE_QR_WEB, R.string.nav_qr_web, Icons.Filled.QrCodeScanner, Icons.Outlined.QrCodeScanner, "nav_qr_web")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ArchViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            Arch3dCoaTheme(darkTheme = uiState.progress.darkThemeEnabled) {
                Arch3dAppShell(
                    viewModel = viewModel,
                    uiState = uiState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Arch3dAppShell(
    viewModel: ArchViewModel,
    uiState: com.example.ui.viewmodel.ArchUiState
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val isTopLevelRoute = currentDestination?.route in setOf(ROUTE_SYLLABUS, ROUTE_LAB_HUB, ROUTE_QUIZ, ROUTE_QR_WEB)

    fun navigateToConceptLab(topicId: String, startIn3D: Boolean) {
        viewModel.openTopic(topicId, startIn3D = startIn3D)
        navController.navigate("concept_3d/$topicId")
    }

    fun navigateTopLevel(route: String) {
        viewModel.stopSimulation()
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useRail = maxWidth >= 680.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                if (isTopLevelRoute) {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.headlineSmall
                            )
                        },
                        actions = {
                            LanguageMenuButton(
                                selectedLanguage = uiState.progress.language,
                                onSelectLanguage = viewModel::setLanguage,
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .testTag("top_language_selector")
                            )
                            IconButton(
                                onClick = { viewModel.toggleDarkTheme() },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("theme_toggle_btn")
                            ) {
                                Icon(
                                    imageVector = if (uiState.progress.darkThemeEnabled) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = if (uiState.progress.darkThemeEnabled) "Switch to Blueprint Light Theme" else "Switch to Silicon Dark Theme"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )
                }
            },
            bottomBar = {
                if (isTopLevelRoute && !useRail) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        TopDestinations.forEach { dest ->
                            val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateTopLevel(dest.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                        contentDescription = stringResource(dest.labelRes)
                                    )
                                },
                                label = { Text(stringResource(dest.labelRes)) },
                                modifier = Modifier.testTag(dest.testTag)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isTopLevelRoute && useRail) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        TopDestinations.forEach { dest ->
                            val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateTopLevel(dest.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                        contentDescription = stringResource(dest.labelRes)
                                    )
                                },
                                label = { Text(stringResource(dest.labelRes)) },
                                modifier = Modifier.testTag(dest.testTag)
                            )
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = ROUTE_SYLLABUS
                    ) {
                        composable(ROUTE_SYLLABUS) {
                            HomeCurriculumScreen(
                                uiState = uiState,
                                onSelectUnit = viewModel::selectUnitFilter,
                                onSearchChange = viewModel::updateSearchQuery,
                                onOpenTopic3D = { topicId -> navigateToConceptLab(topicId, startIn3D = false) },
                                onToggleBookmark = viewModel::toggleBookmark
                            )
                        }

                        composable(ROUTE_LAB_HUB) {
                            Sandbox3DHubScreen(
                                uiState = uiState,
                                onOpenTopic3D = { topicId -> navigateToConceptLab(topicId, startIn3D = true) }
                            )
                        }

                        composable(ROUTE_QUIZ) {
                            QuizArenaScreen(
                                uiState = uiState,
                                onStartQuizUnit = viewModel::startQuiz,
                                onSubmitOption = viewModel::submitQuizOption,
                                onNextQuestion = viewModel::advanceQuiz
                            )
                        }

                        composable(ROUTE_QR_WEB) {
                            QrWebScreen(
                                uiState = uiState,
                                onSetWebBaseUrl = viewModel::setWebShareBaseUrl,
                                onOpenTopicInApp = { topicId -> navigateToConceptLab(topicId, startIn3D = false) }
                            )
                        }

                        composable(
                            route = ROUTE_CONCEPT_3D,
                            arguments = listOf(navArgument("topicId") { type = NavType.StringType })
                        ) {
                            Concept3DLabScreen(
                                uiState = uiState,
                                onBack = {
                                    viewModel.stopSimulation()
                                    navController.popBackStack()
                                },
                                onSelectViewMode = viewModel::setVisualViewMode,
                                onSelectLanguage = viewModel::setLanguage,
                                onSelectMode = viewModel::selectInteractiveMode,
                                onSelectPart = viewModel::selectPart,
                                onPrevStep = viewModel::prevStep,
                                onNextStep = viewModel::nextStep,
                                onTogglePlay = viewModel::toggleSimulationPlay,
                                onSelectStep = viewModel::selectStep,
                                onToggleMastered = viewModel::toggleMastered,
                                onToggleBookmark = viewModel::toggleBookmark
                            )
                        }
                    }
                }
            }
        }
    }
}
