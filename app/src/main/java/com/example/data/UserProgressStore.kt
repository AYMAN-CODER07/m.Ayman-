package com.example.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.model.AppLanguage
import com.example.qr.QrCodeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.archDataStore by preferencesDataStore(name = "arch3d_coa_prefs")

data class UserProgressData(
    val masteredTopicIds: Set<String> = emptySet(),
    val bookmarkedTopicIds: Set<String> = emptySet(),
    val bestScoresByUnit: Map<String, Int> = emptyMap(),
    val darkThemeEnabled: Boolean = true,
    val language: AppLanguage = AppLanguage.HINGLISH,
    val webShareBaseUrl: String = QrCodeUtils.DEFAULT_WEB_SHARE_URL
)

class UserProgressStore(private val context: Context) {

    private object Keys {
        val MASTERED_TOPICS = stringSetPreferencesKey("mastered_topics")
        val BOOKMARKED_TOPICS = stringSetPreferencesKey("bookmarked_topics")
        val DARK_THEME = booleanPreferencesKey("dark_theme_enabled")
        val HINGLISH_ENABLED = booleanPreferencesKey("hinglish_enabled")
        val APP_LANGUAGE = stringPreferencesKey("app_language")
        val WEB_SHARE_URL = stringPreferencesKey("web_share_base_url")
        fun unitScore(unitId: String) = intPreferencesKey("best_score_$unitId")
    }

    val progressFlow: Flow<UserProgressData> = context.archDataStore.data.map { prefs ->
        val unitIds = listOf("all", "unit0", "unit1", "unit2", "unit3", "unit4", "unit5")
        val scores = buildMap {
            for (u in unitIds) {
                prefs[Keys.unitScore(u)]?.let { put(u, it) }
            }
        }
        val storedLangCode = prefs[Keys.APP_LANGUAGE]
        val resolvedLang = if (storedLangCode != null) {
            AppLanguage.entries.find { it.code == storedLangCode } ?: AppLanguage.HINGLISH
        } else {
            val legacyHinglish = prefs[Keys.HINGLISH_ENABLED] ?: true
            if (legacyHinglish) AppLanguage.HINGLISH else AppLanguage.ENGLISH
        }
        UserProgressData(
            masteredTopicIds = prefs[Keys.MASTERED_TOPICS] ?: emptySet(),
            bookmarkedTopicIds = prefs[Keys.BOOKMARKED_TOPICS] ?: emptySet(),
            bestScoresByUnit = scores,
            darkThemeEnabled = prefs[Keys.DARK_THEME] ?: true,
            language = resolvedLang,
            webShareBaseUrl = prefs[Keys.WEB_SHARE_URL] ?: QrCodeUtils.DEFAULT_WEB_SHARE_URL
        )
    }

    suspend fun toggleMastered(topicId: String) {
        context.archDataStore.edit { prefs ->
            val current = prefs[Keys.MASTERED_TOPICS] ?: emptySet()
            prefs[Keys.MASTERED_TOPICS] = if (topicId in current) current - topicId else current + topicId
        }
    }

    suspend fun toggleBookmark(topicId: String) {
        context.archDataStore.edit { prefs ->
            val current = prefs[Keys.BOOKMARKED_TOPICS] ?: emptySet()
            prefs[Keys.BOOKMARKED_TOPICS] = if (topicId in current) current - topicId else current + topicId
        }
    }

    suspend fun saveBestQuizScore(unitKey: String, scorePercent: Int) {
        context.archDataStore.edit { prefs ->
            val key = Keys.unitScore(unitKey)
            val prev = prefs[key] ?: 0
            if (scorePercent > prev) {
                prefs[key] = scorePercent
            }
        }
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.archDataStore.edit { prefs ->
            prefs[Keys.DARK_THEME] = enabled
        }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.archDataStore.edit { prefs ->
            prefs[Keys.APP_LANGUAGE] = language.code
        }
    }

    suspend fun setWebShareBaseUrl(url: String) {
        context.archDataStore.edit { prefs ->
            val cleaned = url.trim().ifEmpty { QrCodeUtils.DEFAULT_WEB_SHARE_URL }
            prefs[Keys.WEB_SHARE_URL] = cleaned
        }
    }
}
