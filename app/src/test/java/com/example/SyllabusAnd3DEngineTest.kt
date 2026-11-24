package com.example

import com.example.data.CbcsCurriculumRepository
import com.example.data.CbcsHinglishContent
import com.example.data.CbcsQuizBank
import com.example.data.CbcsTeluguContent
import com.example.domain.model.AppLanguage
import com.example.domain.model.SceneType3D
import com.example.engine2d.Diagram2DPresets
import com.example.engine3d.Camera3D
import com.example.engine3d.ScenePresets3D
import com.example.engine3d.Vec3
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SyllabusAnd3DEngineTest {

    @Test
    fun `all 6 cbcs units have topics and valid quiz questions`() {
        val units = CbcsCurriculumRepository.units
        assertTrue("Expected 6 units (Unit 0..V), got ${units.size}", units.size == 6)
        for (unit in units) {
            val unitTopics = CbcsCurriculumRepository.getTopicsForUnit(unit.id)
            assertTrue("Unit ${unit.id} should have topics", unitTopics.isNotEmpty())
            val unitQuestions = CbcsQuizBank.getQuestionsForUnit(unit.id)
            assertTrue("Unit ${unit.id} should have quiz questions", unitQuestions.isNotEmpty())
        }
        assertTrue("Expected 26 quiz questions, got ${CbcsQuizBank.questions.size}", CbcsQuizBank.questions.size == 26)
        for (q in CbcsQuizBank.questions) {
            assertTrue(q.correctIndex in q.options.indices)
            assertTrue(q.explanation.isNotBlank())
        }
    }

    @Test
    fun `all 3d scene presets build non-empty geometry and project cleanly`() {
        val camera = Camera3D()
        for (topic in CbcsCurriculumRepository.topics) {
            val step = topic.steps.firstOrNull()
            val modesCount = topic.interactiveModes.size.coerceAtLeast(1)
            for (mode in 0 until modesCount) {
                val assembly = ScenePresets3D.buildScene(topic.sceneType, step, mode)
                assertTrue("Scene ${topic.sceneType} must have boxes", assembly.boxes.isNotEmpty())
                assertTrue("Scene ${topic.sceneType} must have buses", assembly.buses.isNotEmpty())
                for (box in assembly.boxes) {
                    val proj = camera.project(box.center, 1080f, 800f)
                    assertFalse(proj.offset.x.isNaN())
                    assertFalse(proj.offset.y.isNaN())
                }
            }
        }
        val origin = camera.project(Vec3(0f, 0f, 0f), 1000f, 1000f)
        assertFalse(origin.depth.isNaN())
    }

    @Test
    fun `every SceneType3D enum value is represented in curriculum`() {
        val used = CbcsCurriculumRepository.topics.map { it.sceneType }.toSet()
        assertTrue("Expected at least 17 topics, got ${used.size}", used.size >= 17)
        for (sceneType in SceneType3D.entries) {
            val scene = ScenePresets3D.buildScene(sceneType, null, 0)
            assertTrue(scene.boxes.isNotEmpty())
        }
    }

    @Test
    fun `hinglish and telugu content and 2d diagrams exist for all cbcs topics and quizzes`() {
        for (topic in CbcsCurriculumRepository.topics) {
            val hinglish = CbcsHinglishContent.topics[topic.id]
            assertTrue("Missing Hinglish data for ${topic.id}", hinglish != null)
            assertTrue(hinglish!!.summary.isNotBlank())
            assertTrue("Hinglish step count mismatch for ${topic.id}", hinglish.stepDescriptions.size == topic.steps.size)

            val telugu = CbcsTeluguContent.topics[topic.id]
            assertTrue("Missing Telugu data for ${topic.id}", telugu != null)
            assertTrue(telugu!!.summary.isNotBlank())
            assertTrue("Telugu step count mismatch for ${topic.id}", telugu.stepDescriptions.size == topic.steps.size)

            for (lang in AppLanguage.entries) {
                val diag = Diagram2DPresets.buildDiagram(
                    sceneType = topic.sceneType,
                    step = topic.steps.firstOrNull(),
                    modeIndex = 0,
                    language = lang
                )
                assertTrue("Blank 2D diagram title for ${topic.id} in $lang", diag.title.isNotBlank())
                assertTrue(
                    "Empty 2D diagram for ${topic.id} in $lang",
                    diag.isPipelineGantt || diag.isKMapGrid || diag.boxes.isNotEmpty()
                )
            }
        }

        for (q in CbcsQuizBank.questions) {
            assertTrue("Missing Hinglish explanation for ${q.id}", CbcsHinglishContent.quizExplanations[q.id]?.isNotBlank() == true)
            assertTrue("Missing Telugu explanation for ${q.id}", CbcsTeluguContent.quizExplanations[q.id]?.isNotBlank() == true)
        }
    }

    @Test
    fun `worked step by step problems exist in all 3 languages for every cbcs topic`() {
        for (topic in CbcsCurriculumRepository.topics) {
            for (lang in AppLanguage.entries) {
                val problems = com.example.data.CbcsWorkedProblems.getProblemsForTopic(topic.id, lang)
                assertTrue("Missing worked problems for ${topic.id} in $lang", problems.isNotEmpty())
                for (problem in problems) {
                    assertTrue(problem.title.isNotBlank())
                    assertTrue(problem.statement.isNotBlank())
                    assertTrue("Problem ${problem.id} needs >= 2 steps", problem.steps.size >= 2)
                    assertTrue(problem.finalAnswer.isNotBlank())
                    for (step in problem.steps) {
                        assertTrue(step.title.isNotBlank())
                        assertTrue(step.explanation.isNotBlank())
                    }
                }
            }
        }
    }

    @Test
    fun `web qr url builder and standalone html companion generate valid outputs`() {
        val url = com.example.qr.QrCodeUtils.buildWebShareUrl(
            baseUrl = "example.com/coa/",
            topicId = "karnaugh_map",
            language = AppLanguage.TELUGU
        )
        assertTrue(url == "https://example.com/coa?topic=karnaugh_map&lang=te")
        assertTrue(com.example.qr.QrCodeUtils.extractArchTopicIdFromUrl(url) == "karnaugh_map")
        assertTrue(com.example.qr.QrCodeUtils.isWebUrl(url))

        val html = com.example.qr.WebCompanionHtmlBuilder.buildStandaloneHtml(AppLanguage.HINGLISH)
        assertTrue(html.contains("<!DOCTYPE html>"))
        assertTrue(html.contains("karnaugh_map"))
        assertTrue(html.contains("number_systems_codes"))
    }
}
