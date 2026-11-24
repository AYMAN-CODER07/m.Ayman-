package com.example.engine3d

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.theme.BusAddressColor
import com.example.ui.theme.BusControlColor
import com.example.ui.theme.BusDataColor
import com.example.ui.theme.UnitHazardColor

private sealed class RenderPrimitive(val depth: Float) {
    class FaceQuad(
        depth: Float,
        val boxId: String,
        val p0: Offset,
        val p1: Offset,
        val p2: Offset,
        val p3: Offset,
        val fillColor: Color,
        val strokeColor: Color,
        val strokeWidth: Float,
        val wireframeOnly: Boolean
    ) : RenderPrimitive(depth)

    class BusSegment(
        depth: Float,
        val p0: Offset,
        val p1: Offset,
        val color: Color,
        val isActive: Boolean,
        val bidirectional: Boolean,
        val packetOffset: Offset?,
        val reversePacketOffset: Offset?,
        val label: String,
        val midOffset: Offset
    ) : RenderPrimitive(depth)
}

@Composable
fun Interactive3DViewport(
    scene: SceneAssembly3D,
    selectedPartId: String?,
    onPartTapped: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var yaw by remember(scene.defaultYaw) { mutableFloatStateOf(scene.defaultYaw) }
    var pitch by remember(scene.defaultPitch) { mutableFloatStateOf(scene.defaultPitch) }
    var zoom by remember { mutableFloatStateOf(1.0f) }
    var explodeFactor by remember { mutableFloatStateOf(0f) }
    var showLabels by remember { mutableStateOf(true) }
    var wireframeMode by remember { mutableStateOf(false) }
    var autoOrbit by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "signal_flow")
    val packetPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1700, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "packet_phase"
    )

    val orbitDelta by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 22000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auto_orbit"
    )

    var lastHitTargets by remember { mutableStateOf<List<ProjectedBoxHitTarget>>(emptyList()) }
    val textMeasurer = rememberTextMeasurer()

    val effectiveYaw = if (autoOrbit) yaw + orbitDelta else yaw
    val camera = remember(effectiveYaw, pitch, zoom) {
        Camera3D(yawDeg = effectiveYaw, pitchDeg = pitch, zoom = zoom)
    }

    LaunchedEffect(scene.defaultYaw, scene.defaultPitch) {
        yaw = scene.defaultYaw
        pitch = scene.defaultPitch
    }

    val surfaceBg = MaterialTheme.colorScheme.surface
    val bgTop = MaterialTheme.colorScheme.background
    val gridColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
    val labelBg = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)
    val labelText = MaterialTheme.colorScheme.onSurface
    val labelBorder = MaterialTheme.colorScheme.outline

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(bgTop, surfaceBg)
                )
            )
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f), RoundedCornerShape(22.dp))
            .testTag("interactive_3d_viewport")
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        autoOrbit = false
                        yaw = (yaw + dragAmount.x * 0.42f) % 360f
                        pitch = (pitch + dragAmount.y * 0.36f).coerceIn(-68f, 68f)
                    }
                }
                .pointerInput(lastHitTargets) {
                    detectTapGestures { tapOffset ->
                        val hit = lastHitTargets
                            .sortedByDescending { it.depth }
                            .firstOrNull { it.bounds.contains(tapOffset) }
                        onPartTapped(hit?.id)
                    }
                }
        ) {
            val w = size.width
            val h = size.height
            if (w <= 0f || h <= 0f) return@Canvas

            // 1. Draw 3D perspective floor grid
            drawBlueprintGrid(camera, w, h, gridColor)

            // 2. Build Z-sorted 3D primitives (faces + buses)
            val primitives = mutableListOf<RenderPrimitive>()
            val hitTargets = mutableListOf<ProjectedBoxHitTarget>()
            val labelAnchors = mutableListOf<Triple<Box3D, Offset, Float>>()

            val lightDir = Vec3(0.45f, 0.82f, -0.52f).normalized()

            for (box in scene.boxes) {
                val explodedCenter = box.center + box.explodeOffset * explodeFactor
                val hx = box.size.x * 0.5f
                val hy = box.size.y * 0.5f
                val hz = box.size.z * 0.5f

                val worldVerts = arrayOf(
                    explodedCenter + Vec3(-hx, -hy, -hz), // 0: L B Back
                    explodedCenter + Vec3(hx, -hy, -hz),  // 1: R B Back
                    explodedCenter + Vec3(hx, hy, -hz),   // 2: R T Back
                    explodedCenter + Vec3(-hx, hy, -hz),  // 3: L T Back
                    explodedCenter + Vec3(-hx, -hy, hz),  // 4: L B Front
                    explodedCenter + Vec3(hx, -hy, hz),   // 5: R B Front
                    explodedCenter + Vec3(hx, hy, hz),    // 6: R T Front
                    explodedCenter + Vec3(-hx, hy, hz)    // 7: L T Front
                )

                val viewVerts = worldVerts.map { camera.toViewSpace(it) }
                val projVerts = worldVerts.map { camera.project(it, w, h) }

                // Compute 2D hit-test bounds
                var minX = Float.MAX_VALUE
                var minY = Float.MAX_VALUE
                var maxX = Float.MIN_VALUE
                var maxY = Float.MIN_VALUE
                var avgDepth = 0f
                for (pv in projVerts) {
                    minX = minOf(minX, pv.offset.x)
                    minY = minOf(minY, pv.offset.y)
                    maxX = maxOf(maxX, pv.offset.x)
                    maxY = maxOf(maxY, pv.offset.y)
                    avgDepth += pv.depth
                }
                avgDepth /= 8f
                hitTargets += ProjectedBoxHitTarget(
                    id = box.id,
                    bounds = Rect(minX - 6f, minY - 6f, maxX + 6f, maxY + 6f),
                    depth = avgDepth
                )

                val topProj = camera.project(explodedCenter + Vec3(0f, hy + 0.14f, 0f), w, h)
                labelAnchors += Triple(box, topProj.offset, topProj.depth)

                val isSelected = box.id == selectedPartId
                val isActive = box.isEmphasized

                val faces = arrayOf(
                    intArrayOf(4, 5, 6, 7), // Front (+Z)
                    intArrayOf(1, 0, 3, 2), // Back (-Z)
                    intArrayOf(3, 2, 6, 7), // Top (+Y) - wait check winding
                    intArrayOf(0, 1, 5, 4), // Bottom (-Y)
                    intArrayOf(5, 1, 2, 6), // Right (+X)
                    intArrayOf(0, 4, 7, 3)  // Left (-X)
                )

                val faceNormals = arrayOf(
                    Vec3(0f, 0f, 1f),
                    Vec3(0f, 0f, -1f),
                    Vec3(0f, 1f, 0f),
                    Vec3(0f, -1f, 0f),
                    Vec3(1f, 0f, 0f),
                    Vec3(-1f, 0f, 0f)
                )

                for (fIdx in faces.indices) {
                    val idx = faces[fIdx]
                    val v0 = viewVerts[idx[0]]
                    val v1 = viewVerts[idx[1]]
                    val v2 = viewVerts[idx[2]]
                    val e1 = v1 - v0
                    val e2 = v2 - v0
                    val viewNormal = e1.cross(e2).normalized()

                    // Backface culling in view space (+Z points toward camera)
                    val p0 = projVerts[idx[0]].offset
                    val p1 = projVerts[idx[1]].offset
                    val p2 = projVerts[idx[2]].offset
                    val p3 = projVerts[idx[3]].offset
                    val screenCross = (p1.x - p0.x) * (p2.y - p0.y) - (p1.y - p0.y) * (p2.x - p0.x)

                    if (!wireframeMode && screenCross > 0f) continue

                    val worldNormal = faceNormals[fIdx]
                    val diffuse = (worldNormal.dot(lightDir) * 0.4f + 0.65f).coerceIn(0.38f, 1.08f)
                    val shaded = shadeColor(
                        base = box.color.copy(alpha = box.alpha),
                        intensity = diffuse,
                        isActive = isActive,
                        isSelected = isSelected
                    )

                    val strokeCol = when {
                        isSelected -> Color(0xFFFFFFFF)
                        isActive -> Color(0xFF67E8F9)
                        else -> Color(0xFFFFFFFF).copy(alpha = 0.25f)
                    }
                    val strokeW = when {
                        isSelected -> 3.6f
                        isActive -> 2.4f
                        else -> 1.1f
                    }
                    val faceDepth = (
                        projVerts[idx[0]].depth +
                            projVerts[idx[1]].depth +
                            projVerts[idx[2]].depth +
                            projVerts[idx[3]].depth
                        ) * 0.25f

                    primitives += RenderPrimitive.FaceQuad(
                        depth = faceDepth,
                        boxId = box.id,
                        p0 = p0,
                        p1 = p1,
                        p2 = p2,
                        p3 = p3,
                        fillColor = shaded,
                        strokeColor = strokeCol,
                        strokeWidth = strokeW,
                        wireframeOnly = wireframeMode
                    )
                }
            }

            // 3. Project 3D buses and animated packets
            for (bus in scene.buses) {
                val pStart = camera.project(bus.from, w, h)
                val pEnd = camera.project(bus.to, w, h)
                val busColor = when (bus.busType) {
                    BusType.ADDRESS -> BusAddressColor
                    BusType.DATA -> BusDataColor
                    BusType.CONTROL -> BusControlColor
                    BusType.ALERT -> UnitHazardColor
                }
                val packetPt = if (bus.isActive) {
                    val worldPacket = bus.from.lerp(bus.to, packetPhase)
                    camera.project(worldPacket, w, h).offset
                } else null
                val revPacketPt = if (bus.isActive && bus.bidirectional) {
                    val worldPacket = bus.to.lerp(bus.from, packetPhase)
                    camera.project(worldPacket, w, h).offset
                } else null

                val midOffset = Offset(
                    (pStart.offset.x + pEnd.offset.x) * 0.5f,
                    (pStart.offset.y + pEnd.offset.y) * 0.5f
                )

                primitives += RenderPrimitive.BusSegment(
                    depth = (pStart.depth + pEnd.depth) * 0.5f + 0.08f,
                    p0 = pStart.offset,
                    p1 = pEnd.offset,
                    color = busColor,
                    isActive = bus.isActive,
                    bidirectional = bus.bidirectional,
                    packetOffset = packetPt,
                    reversePacketOffset = revPacketPt,
                    label = bus.label,
                    midOffset = midOffset
                )
            }

            // Save hit targets for tap inspection
            lastHitTargets = hitTargets

            // 4. Painter's Algorithm sort (far to near)
            primitives.sortBy { it.depth }

            for (prim in primitives) {
                when (prim) {
                    is RenderPrimitive.FaceQuad -> {
                        val path = Path().apply {
                            moveTo(prim.p0.x, prim.p0.y)
                            lineTo(prim.p1.x, prim.p1.y)
                            lineTo(prim.p2.x, prim.p2.y)
                            lineTo(prim.p3.x, prim.p3.y)
                            close()
                        }
                        if (!prim.wireframeOnly) {
                            drawPath(path = path, color = prim.fillColor)
                        }
                        drawPath(
                            path = path,
                            color = if (prim.wireframeOnly) prim.fillColor.copy(alpha = 0.9f) else prim.strokeColor,
                            style = Stroke(width = prim.strokeWidth)
                        )
                    }
                    is RenderPrimitive.BusSegment -> {
                        val lineAlpha = if (prim.isActive) 0.95f else 0.35f
                        val width = if (prim.isActive) 5.5f else 2.5f
                        if (prim.isActive) {
                            drawLine(
                                color = prim.color.copy(alpha = 0.28f),
                                start = prim.p0,
                                end = prim.p1,
                                strokeWidth = 14f,
                                cap = StrokeCap.Round
                            )
                        }
                        drawLine(
                            color = prim.color.copy(alpha = lineAlpha),
                            start = prim.p0,
                            end = prim.p1,
                            strokeWidth = width,
                            cap = StrokeCap.Round
                        )
                        prim.packetOffset?.let { pt ->
                            drawCircle(color = prim.color.copy(alpha = 0.38f), radius = 14f, center = pt)
                            drawCircle(color = Color.White, radius = 6.5f, center = pt)
                        }
                        prim.reversePacketOffset?.let { pt ->
                            drawCircle(color = prim.color.copy(alpha = 0.38f), radius = 12f, center = pt)
                            drawCircle(color = prim.color, radius = 5.5f, center = pt)
                        }
                    }
                }
            }

            // 5. Floating 3D labels (sorted far to near)
            if (showLabels) {
                labelAnchors.sortBy { it.third }
                for ((box, anchor, _) in labelAnchors) {
                    if (box.id.startsWith("pipe_reg_") || box.id == "cpu_shell") continue
                    drawFloatingLabel(
                        textMeasurer = textMeasurer,
                        text = box.label,
                        anchor = anchor,
                        isSelected = box.id == selectedPartId,
                        isActive = box.isEmphasized,
                        bgColor = labelBg,
                        textColor = labelText,
                        borderColor = if (box.id == selectedPartId) Color.White else if (box.isEmphasized) BusDataColor else labelBorder
                    )
                }
            }
        }

        // Top-Left Hint Pill + Bus Legend
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                shape = RoundedCornerShape(50),
                tonalElevation = 2.dp
            ) {
                Text(
                    text = "Drag to rotate 3D • Tap block to inspect",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                BusLegendPill("Addr", BusAddressColor)
                BusLegendPill("Data", BusDataColor)
                BusLegendPill("Ctrl", BusControlColor)
            }
        }

        // Top-Right 3D Viewport Quick Controls
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.End
        ) {
            ViewportMiniIconButton(
                icon = if (showLabels) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = "Toggle 3D Labels",
                active = showLabels,
                testTag = "toggle_3d_labels",
                onClick = { showLabels = !showLabels }
            )
            ViewportMiniIconButton(
                icon = Icons.Outlined.GridOn,
                contentDescription = "Toggle Wireframe",
                active = wireframeMode,
                testTag = "toggle_3d_wireframe",
                onClick = { wireframeMode = !wireframeMode }
            )
            ViewportMiniIconButton(
                icon = Icons.Default.Autorenew,
                contentDescription = "Auto Rotate 3D",
                active = autoOrbit,
                testTag = "toggle_3d_orbit",
                onClick = { autoOrbit = !autoOrbit }
            )
            ViewportMiniIconButton(
                icon = Icons.Default.Refresh,
                contentDescription = "Reset 3D View",
                active = false,
                testTag = "reset_3d_camera",
                onClick = {
                    autoOrbit = false
                    yaw = scene.defaultYaw
                    pitch = scene.defaultPitch
                    zoom = 1.0f
                    explodeFactor = 0f
                }
            )
        }

        // Bottom Controls: Explode Slider + Zoom +/-
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = "Exploded View",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Explode",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Slider(
                        value = explodeFactor,
                        onValueChange = { explodeFactor = it },
                        valueRange = 0f..1f,
                        modifier = Modifier
                            .weight(1f)
                            .height(30.dp)
                            .padding(horizontal = 6.dp)
                            .testTag("explode_slider"),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { zoom = (zoom - 0.15f).coerceAtLeast(0.65f) },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("zoom_out_btn")
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Zoom out", modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = { zoom = (zoom + 0.15f).coerceAtMost(1.65f) },
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("zoom_in_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Zoom in", modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ViewportMiniIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    active: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .testTag(testTag)
    ) {
        Surface(
            color = if (active) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface.copy(alpha = 0.86f),
            shape = CircleShape,
            tonalElevation = 2.dp,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = if (active) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun BusLegendPill(label: String, color: Color) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.82f),
        shape = RoundedCornerShape(50)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun DrawScope.drawBlueprintGrid(
    camera: Camera3D,
    w: Float,
    h: Float,
    gridColor: Color
) {
    val yFloor = -0.68f
    val extent = 3.6f
    val step = 0.9f
    var v = -extent
    while (v <= extent + 0.01f) {
        val p1 = camera.project(Vec3(v, yFloor, -extent), w, h).offset
        val p2 = camera.project(Vec3(v, yFloor, extent), w, h).offset
        drawLine(color = gridColor, start = p1, end = p2, strokeWidth = 1.1f)

        val p3 = camera.project(Vec3(-extent, yFloor, v), w, h).offset
        val p4 = camera.project(Vec3(extent, yFloor, v), w, h).offset
        drawLine(color = gridColor, start = p3, end = p4, strokeWidth = 1.1f)
        v += step
    }
}

private fun DrawScope.drawFloatingLabel(
    textMeasurer: TextMeasurer,
    text: String,
    anchor: Offset,
    isSelected: Boolean,
    isActive: Boolean,
    bgColor: Color,
    textColor: Color,
    borderColor: Color
) {
    val result = textMeasurer.measure(
        text = text,
        style = TextStyle(
            fontSize = 10.sp,
            fontWeight = if (isSelected || isActive) FontWeight.Bold else FontWeight.Medium,
            color = textColor
        )
    )
    val padX = 10f
    val padY = 5f
    val boxW = result.size.width + padX * 2f
    val boxH = result.size.height + padY * 2f
    val topLeft = Offset(
        (anchor.x - boxW * 0.5f).coerceIn(8f, size.width - boxW - 8f),
        (anchor.y - boxH - 6f).coerceIn(8f, size.height - boxH - 8f)
    )
    drawRoundRect(
        color = bgColor,
        topLeft = topLeft,
        size = Size(boxW, boxH),
        cornerRadius = CornerRadius(10f, 10f)
    )
    drawRoundRect(
        color = borderColor,
        topLeft = topLeft,
        size = Size(boxW, boxH),
        cornerRadius = CornerRadius(10f, 10f),
        style = Stroke(width = if (isSelected || isActive) 2.2f else 1f)
    )
    drawText(
        textLayoutResult = result,
        topLeft = Offset(topLeft.x + padX, topLeft.y + padY)
    )
}
