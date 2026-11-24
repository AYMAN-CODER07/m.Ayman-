package com.example.engine2d

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AppLanguage
import com.example.engine3d.BusType
import com.example.ui.theme.BusAddressColor
import com.example.ui.theme.BusControlColor
import com.example.ui.theme.BusDataColor
import com.example.ui.theme.CoralError
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.UnitHazardColor
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Interactive2DDiagramViewport(
    diagram: DiagramScene2D,
    selectedPartId: String?,
    language: AppLanguage,
    onPartTapped: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val hinglish = language == AppLanguage.HINGLISH
    val telugu = language == AppLanguage.TELUGU
    val infiniteTransition = rememberInfiniteTransition(label = "diag_flow")
    val pulsePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "diag_pulse"
    )

    val textMeasurer = rememberTextMeasurer()
    var hitBoxes by remember { mutableStateOf<List<Pair<String, Rect>>>(emptyList()) }

    val surfaceBg = MaterialTheme.colorScheme.surface
    val gridColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val subTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(surfaceBg)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.65f), RoundedCornerShape(22.dp))
            .testTag("interactive_2d_diagram")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = diagram.title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }

            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .pointerInput(hitBoxes) {
                        detectTapGestures { offset ->
                            val hit = hitBoxes.firstOrNull { it.second.contains(offset) }
                            onPartTapped(hit?.first)
                        }
                    }
            ) {
                val w = size.width
                val h = size.height
                if (w <= 0f || h <= 0f) return@Canvas

                // Subtle 2D engineering blueprint grid
                val gridStep = 36f
                var gx = 0f
                while (gx < w) {
                    drawLine(gridColor, Offset(gx, 0f), Offset(gx, h), strokeWidth = 1f)
                    gx += gridStep
                }
                var gy = 0f
                while (gy < h) {
                    drawLine(gridColor, Offset(0f, gy), Offset(w, gy), strokeWidth = 1f)
                    gy += gridStep
                }

                if (diagram.isPipelineGantt) {
                    drawPipelineGanttDiagram(
                        textMeasurer = textMeasurer,
                        hasHazardBubble = diagram.pipelineHazardBubble,
                        language = language,
                        textColor = onSurfaceColor,
                        subColor = subTextColor,
                        pulsePhase = pulsePhase
                    )
                    hitBoxes = emptyList()
                    return@Canvas
                }

                if (diagram.isKMapGrid) {
                    drawKMapDiagram(
                        textMeasurer = textMeasurer,
                        dontCareMode = diagram.kMapDontCareMode,
                        language = language,
                        textColor = onSurfaceColor,
                        subColor = subTextColor
                    )
                    hitBoxes = emptyList()
                    return@Canvas
                }

                // Draw Arrows first
                for (arrow in diagram.arrows) {
                    val start = Offset(arrow.fromX * w, arrow.fromY * h)
                    val end = Offset(arrow.toX * w, arrow.toY * h)
                    val col = when (arrow.busType) {
                        BusType.ADDRESS -> BusAddressColor
                        BusType.DATA -> BusDataColor
                        BusType.CONTROL -> BusControlColor
                        BusType.ALERT -> UnitHazardColor
                    }
                    drawDirectionalArrow(
                        start = start,
                        end = end,
                        color = col,
                        isActive = arrow.isActive,
                        bidirectional = arrow.bidirectional,
                        pulsePhase = pulsePhase,
                        label = arrow.label,
                        textMeasurer = textMeasurer,
                        labelTextColor = onSurfaceColor,
                        labelBg = surfaceBg
                    )
                }

                // Draw Boxes & collect hit targets
                val newHits = mutableListOf<Pair<String, Rect>>()
                for (box in diagram.boxes) {
                    val left = box.x * w
                    val top = box.y * h
                    val bw = box.w * w
                    val bh = box.h * h
                    val rect = Rect(left, top, left + bw, top + bh)
                    newHits += box.id to rect

                    val isSelected = box.id == selectedPartId
                    val fillAlpha = if (box.isActive || isSelected) 0.24f else 0.13f
                    drawRoundRect(
                        color = box.color.copy(alpha = fillAlpha),
                        topLeft = Offset(left, top),
                        size = Size(bw, bh),
                        cornerRadius = CornerRadius(18f, 18f)
                    )
                    drawRoundRect(
                        color = if (isSelected) Color.White else if (box.isActive) box.color else box.color.copy(alpha = 0.65f),
                        topLeft = Offset(left, top),
                        size = Size(bw, bh),
                        cornerRadius = CornerRadius(18f, 18f),
                        style = Stroke(width = if (isSelected) 4.5f else if (box.isActive) 3f else 1.8f)
                    )

                    val titleRes = textMeasurer.measure(
                        text = box.title,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = onSurfaceColor
                        )
                    )
                    val subRes = if (box.subtitle.isNotBlank()) {
                        textMeasurer.measure(
                            text = box.subtitle,
                            style = TextStyle(
                                fontSize = 10.sp,
                                color = subTextColor
                            )
                        )
                    } else null

                    val totalTextH = titleRes.size.height + (subRes?.size?.height?.plus(4) ?: 0)
                    val startY = top + (bh - totalTextH) * 0.5f

                    drawText(
                        textLayoutResult = titleRes,
                        topLeft = Offset(
                            (left + (bw - titleRes.size.width) * 0.5f).coerceIn(left + 4f, left + bw - 4f),
                            startY.coerceAtLeast(top + 4f)
                        )
                    )
                    if (subRes != null && bh > 46f) {
                        drawText(
                            textLayoutResult = subRes,
                            topLeft = Offset(
                                (left + (bw - subRes.size.width) * 0.5f).coerceIn(left + 4f, left + bw - 4f),
                                (startY + titleRes.size.height + 4f).coerceAtMost(top + bh - subRes.size.height - 2f)
                            )
                        )
                    }
                }
                hitBoxes = newHits

                if (diagram.isHierarchyPyramid) {
                    drawPyramidSideAnnotations(
                        textMeasurer = textMeasurer,
                        language = language,
                        missActive = diagram.pyramidMissActive,
                        primaryColor = primaryColor,
                        textColor = onSurfaceColor
                    )
                }
            }
        }

        if (!diagram.isPipelineGantt && !diagram.isKMapGrid) {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Text(
                    text = when (language) {
                        AppLanguage.TELUGU -> "బ్లాక్‌పై ట్యాప్ చేయండి"
                        AppLanguage.HINGLISH -> "Kisi bhi block par tap karo"
                        AppLanguage.ENGLISH -> "Tap any block to inspect"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

private fun DrawScope.drawDirectionalArrow(
    start: Offset,
    end: Offset,
    color: Color,
    isActive: Boolean,
    bidirectional: Boolean,
    pulsePhase: Float,
    label: String,
    textMeasurer: TextMeasurer,
    labelTextColor: Color,
    labelBg: Color
) {
    val lineColor = if (isActive) color else color.copy(alpha = 0.42f)
    val strokeW = if (isActive) 4.2f else 2.2f

    drawLine(
        color = lineColor,
        start = start,
        end = end,
        strokeWidth = strokeW,
        cap = StrokeCap.Round
    )

    val angle = atan2(end.y - start.y, end.x - start.x)
    drawArrowHead(end, angle, lineColor)
    if (bidirectional) {
        drawArrowHead(start, angle + Math.PI.toFloat(), lineColor)
    }

    if (isActive) {
        val px = start.x + (end.x - start.x) * pulsePhase
        val py = start.y + (end.y - start.y) * pulsePhase
        drawCircle(color = color.copy(alpha = 0.35f), radius = 11f, center = Offset(px, py))
        drawCircle(color = Color.White, radius = 5f, center = Offset(px, py))
    }

    if (label.isNotBlank()) {
        val mid = Offset((start.x + end.x) * 0.5f, (start.y + end.y) * 0.5f)
        val res = textMeasurer.measure(
            text = label,
            style = TextStyle(fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = labelTextColor)
        )
        val pad = 5f
        val rectTopLeft = Offset(mid.x - res.size.width * 0.5f - pad, mid.y - res.size.height * 0.5f - 14f)
        drawRoundRect(
            color = labelBg.copy(alpha = 0.92f),
            topLeft = rectTopLeft,
            size = Size(res.size.width + pad * 2f, res.size.height + 4f),
            cornerRadius = CornerRadius(6f, 6f)
        )
        drawText(res, topLeft = Offset(rectTopLeft.x + pad, rectTopLeft.y + 2f))
    }
}

private fun DrawScope.drawArrowHead(tip: Offset, angleRad: Float, color: Color) {
    val headLen = 14f
    val spread = 0.48f
    val p1 = Offset(
        tip.x - headLen * cos(angleRad - spread),
        tip.y - headLen * sin(angleRad - spread)
    )
    val p2 = Offset(
        tip.x - headLen * cos(angleRad + spread),
        tip.y - headLen * sin(angleRad + spread)
    )
    val path = Path().apply {
        moveTo(tip.x, tip.y)
        lineTo(p1.x, p1.y)
        lineTo(p2.x, p2.y)
        close()
    }
    drawPath(path = path, color = color)
}

private fun DrawScope.drawPipelineGanttDiagram(
    textMeasurer: TextMeasurer,
    hasHazardBubble: Boolean,
    language: AppLanguage,
    textColor: Color,
    subColor: Color,
    pulsePhase: Float
) {
    val w = size.width
    val h = size.height
    val cols = 7
    val rows = 4
    val leftPad = w * 0.14f
    val topPad = h * 0.16f
    val cellW = (w - leftPad - 20f) / cols
    val cellH = (h - topPad - 46f) / rows

    val stageColors = mapOf(
        "IF" to Color(0xFF0EA5E9),
        "ID" to Color(0xFF7C3AED),
        "EX" to Color(0xFFD97706),
        "MEM" to Color(0xFF4F46E5),
        "WB" to Color(0xFF059669),
        "STALL" to CoralError
    )

    for (c in 0 until cols) {
        val hdr = textMeasurer.measure("T${c + 1}", TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = subColor))
        drawText(hdr, topLeft = Offset(leftPad + c * cellW + (cellW - hdr.size.width) * 0.5f, topPad - 24f))
    }

    val instStages = if (!hasHazardBubble) {
        listOf(
            listOf(0 to "IF", 1 to "ID", 2 to "EX", 3 to "MEM", 4 to "WB"),
            listOf(1 to "IF", 2 to "ID", 3 to "EX", 4 to "MEM", 5 to "WB"),
            listOf(2 to "IF", 3 to "ID", 4 to "EX", 5 to "MEM", 6 to "WB"),
            listOf(3 to "IF", 4 to "ID", 5 to "EX", 6 to "MEM")
        )
    } else {
        listOf(
            listOf(0 to "IF", 1 to "ID", 2 to "EX", 3 to "MEM", 4 to "WB"),
            listOf(1 to "IF", 2 to "ID", 3 to "STALL", 4 to "EX", 5 to "MEM", 6 to "WB"),
            listOf(2 to "IF", 3 to "ID", 5 to "EX", 6 to "MEM"),
            listOf(4 to "IF", 5 to "ID", 6 to "EX")
        )
    }

    instStages.forEachIndexed { rIdx, stages ->
        val rowLabel = textMeasurer.measure("Inst ${rIdx + 1}", TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor))
        drawText(rowLabel, topLeft = Offset(12f, topPad + rIdx * cellH + (cellH - rowLabel.size.height) * 0.5f))

        for ((colIdx, stageName) in stages) {
            val x = leftPad + colIdx * cellW + 3f
            val y = topPad + rIdx * cellH + 5f
            val bw = cellW - 6f
            val bh = cellH - 10f
            val color = stageColors[stageName] ?: Color.Gray
            drawRoundRect(
                color = color.copy(alpha = 0.25f),
                topLeft = Offset(x, y),
                size = Size(bw, bh),
                cornerRadius = CornerRadius(10f, 10f)
            )
            drawRoundRect(
                color = color,
                topLeft = Offset(x, y),
                size = Size(bw, bh),
                cornerRadius = CornerRadius(10f, 10f),
                style = Stroke(width = 2.2f)
            )
            val txt = textMeasurer.measure(stageName, TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textColor))
            drawText(txt, topLeft = Offset(x + (bw - txt.size.width) * 0.5f, y + (bh - txt.size.height) * 0.5f))
        }
    }

    if (!hasHazardBubble) {
        val from = Offset(leftPad + 2.5f * cellW, topPad + cellH * 0.9f)
        val to = Offset(leftPad + 3.5f * cellW, topPad + cellH * 1.15f)
        drawLine(EmeraldSuccess, from, to, strokeWidth = 4f)
        drawArrowHead(to, atan2(to.y - from.y, to.x - from.x), EmeraldSuccess)
        val px = from.x + (to.x - from.x) * pulsePhase
        val py = from.y + (to.y - from.y) * pulsePhase
        drawCircle(Color.White, 5f, Offset(px, py))
    }

    val footerMsg = if (hasHazardBubble) {
        when (language) {
            AppLanguage.TELUGU -> "STALL బబుల్: Inst 2, Inst 1 ఫలితం కోసం 1 క్లాక్ ఆగింది"
            AppLanguage.HINGLISH -> "STALL Bubble: Inst 2 ko Inst 1 ke result ka wait karna pada"
            AppLanguage.ENGLISH -> "RAW Hazard: 1 clock stall inserted before EX"
        }
    } else {
        when (language) {
            AppLanguage.TELUGU -> "గ్రీన్ యారో: ఫార్వార్డింగ్ ద్వారా ఫలితం తక్షణమే అందింది!"
            AppLanguage.HINGLISH -> "Green Arrow: Forwarding se result bina ruke next instruction ko mil gaya!"
            AppLanguage.ENGLISH -> "Operand Forwarding bypasses WB -> zero stall cycles"
        }
    }
    val footRes = textMeasurer.measure(footerMsg, TextStyle(fontSize = 10.5.sp, fontWeight = FontWeight.Medium, color = if (hasHazardBubble) CoralError else EmeraldSuccess))
    drawText(footRes, topLeft = Offset(14f, h - 26f))
}

private fun DrawScope.drawKMapDiagram(
    textMeasurer: TextMeasurer,
    dontCareMode: Boolean,
    language: AppLanguage,
    textColor: Color,
    subColor: Color
) {
    val w = size.width
    val h = size.height
    val cols = 4
    val rows = 4
    val leftPad = w * 0.20f
    val topPad = h * 0.18f
    val gridW = w * 0.72f
    val gridH = h * 0.62f
    val cellW = gridW / cols
    val cellH = gridH / rows

    val headerLabel = textMeasurer.measure("AB \\ CD", TextStyle(fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = subColor))
    drawText(headerLabel, topLeft = Offset(leftPad - 58f, topPad - 24f))

    val grayLabels = listOf("00", "01", "11", "10")
    for (i in 0..3) {
        val colRes = textMeasurer.measure(grayLabels[i], TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BusDataColor))
        drawText(colRes, topLeft = Offset(leftPad + i * cellW + (cellW - colRes.size.width) * 0.5f, topPad - 24f))

        val rowRes = textMeasurer.measure(grayLabels[i], TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BusAddressColor))
        drawText(rowRes, topLeft = Offset(leftPad - 32f, topPad + i * cellH + (cellH - rowRes.size.height) * 0.5f))
    }

    // Standard minterm indices for 4-var K-Map in Gray order (AB rows, CD cols)
    val mintermIndices = arrayOf(
        intArrayOf(0, 1, 3, 2),
        intArrayOf(4, 5, 7, 6),
        intArrayOf(12, 13, 15, 14),
        intArrayOf(8, 9, 11, 10)
    )
    val ones = setOf(0, 1, 2, 3, 5, 7)
    val dontCares = if (dontCareMode) setOf(13, 15) else emptySet()

    for (r in 0 until rows) {
        for (c in 0 until cols) {
            val x = leftPad + c * cellW
            val y = topPad + r * cellH
            val m = mintermIndices[r][c]
            val valStr = when (m) {
                in ones -> "1"
                in dontCares -> "X"
                else -> "0"
            }
            drawRect(
                color = if (valStr == "1") BusDataColor.copy(alpha = 0.14f) else if (valStr == "X") BusAddressColor.copy(alpha = 0.16f) else Color.Transparent,
                topLeft = Offset(x, y),
                size = Size(cellW, cellH)
            )
            drawRect(
                color = subColor.copy(alpha = 0.4f),
                topLeft = Offset(x, y),
                size = Size(cellW, cellH),
                style = Stroke(width = 1.5f)
            )
            val valRes = textMeasurer.measure(
                valStr,
                TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (valStr) {
                        "1" -> textColor
                        "X" -> BusAddressColor
                        else -> subColor.copy(alpha = 0.55f)
                    }
                )
            )
            drawText(valRes, topLeft = Offset(x + (cellW - valRes.size.width) * 0.5f, y + (cellH - valRes.size.height) * 0.45f))

            val idxRes = textMeasurer.measure("m$m", TextStyle(fontSize = 8.5.sp, color = subColor.copy(alpha = 0.7f)))
            drawText(idxRes, topLeft = Offset(x + 4f, y + cellH - idxRes.size.height - 2f))
        }
    }

    // Group 1: Row 0 Quad (m0, m1, m3, m2) -> A'B'
    drawRoundRect(
        color = EmeraldSuccess,
        topLeft = Offset(leftPad + 4f, topPad + 4f),
        size = Size(gridW - 8f, cellH - 8f),
        cornerRadius = CornerRadius(14f, 14f),
        style = Stroke(width = 3.5f)
    )

    // Group 2: Col 1-2 Quad (m1, m3, m5, m7) OR with Don't Care (m13, m15) -> C'D? Wait m1(0001), m3(0011), m5(0101), m7(0111) -> A'D!
    val group2Height = if (dontCareMode) gridH - 8f else cellH * 2f - 8f
    drawRoundRect(
        color = BusAddressColor,
        topLeft = Offset(leftPad + cellW + 5f, topPad + 5f),
        size = Size(cellW * 2f - 10f, group2Height),
        cornerRadius = CornerRadius(14f, 14f),
        style = Stroke(width = 3.5f)
    )

    val footer = if (dontCareMode) {
        when (language) {
            AppLanguage.TELUGU -> "Don't Care (X) తో 8-సెల్/క్వాడ్ గ్రూప్: F = A'B' + D"
            AppLanguage.HINGLISH -> "Don't Care (X) ne bada Quad banaya -> F = A'B' + D"
            AppLanguage.ENGLISH -> "Don't Care (X) expands col-group -> F = A'B' + D"
        }
    } else {
        when (language) {
            AppLanguage.TELUGU -> "ఆకుపచ్చ Quad = A'B'  |  పసుపు Quad = A'D  =>  F = A'B' + A'D"
            AppLanguage.HINGLISH -> "Green Quad = A'B'  |  Amber Quad = A'D  =>  F = A'B' + A'D"
            AppLanguage.ENGLISH -> "Green Quad = A'B'  |  Amber Quad = A'D  =>  F = A'B' + A'D"
        }
    }
    val footRes = textMeasurer.measure(footer, TextStyle(fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess))
    drawText(footRes, topLeft = Offset(14f, h - 28f))
}

private fun DrawScope.drawPyramidSideAnnotations(
    textMeasurer: TextMeasurer,
    language: AppLanguage,
    missActive: Boolean,
    primaryColor: Color,
    textColor: Color
) {
    val w = size.width
    val h = size.height
    val leftTop = Offset(w * 0.04f, h * 0.14f)
    val leftBot = Offset(w * 0.04f, h * 0.86f)
    drawLine(primaryColor, leftBot, leftTop, strokeWidth = 3f)
    drawArrowHead(leftTop, -Math.PI.toFloat() / 2f, primaryColor)
    val speedLabel = when (language) {
        AppLanguage.TELUGU -> "వేగం ↑\nఖర్చు ↑"
        AppLanguage.HINGLISH -> "Speed ↑\nCost ↑"
        AppLanguage.ENGLISH -> "Speed ↑\nCost ↑"
    }
    val speedTxt = textMeasurer.measure(speedLabel, TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textColor))
    drawText(speedTxt, topLeft = Offset(w * 0.06f, h * 0.14f))

    val rightTop = Offset(w * 0.96f, h * 0.14f)
    val rightBot = Offset(w * 0.96f, h * 0.86f)
    val capCol = if (missActive) CoralError else EmeraldSuccess
    drawLine(capCol, rightTop, rightBot, strokeWidth = 3f)
    drawArrowHead(rightBot, Math.PI.toFloat() / 2f, capCol)
    val sizeLabel = if (language == AppLanguage.TELUGU) "సైజు ↑" else "Size ↑"
    val capTxt = textMeasurer.measure(sizeLabel, TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textColor))
    drawText(capTxt, topLeft = Offset(w * 0.86f, h * 0.14f))
}
