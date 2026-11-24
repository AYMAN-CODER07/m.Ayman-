package com.example.engine3d

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Vec3(val x: Float, val y: Float, val z: Float) {
    operator fun plus(o: Vec3) = Vec3(x + o.x, y + o.y, z + o.z)
    operator fun minus(o: Vec3) = Vec3(x - o.x, y - o.y, z - o.z)
    operator fun times(s: Float) = Vec3(x * s, y * s, z * s)

    fun dot(o: Vec3): Float = x * o.x + y * o.y + z * o.z

    fun cross(o: Vec3): Vec3 = Vec3(
        y * o.z - z * o.y,
        z * o.x - x * o.z,
        x * o.y - y * o.x
    )

    fun normalized(): Vec3 {
        val len = sqrt(x * x + y * y + z * z)
        return if (len > 1e-5f) Vec3(x / len, y / len, z / len) else Vec3(0f, 1f, 0f)
    }

    fun lerp(target: Vec3, t: Float): Vec3 = Vec3(
        x + (target.x - x) * t,
        y + (target.y - y) * t,
        z + (target.z - z) * t
    )
}

enum class BusType {
    ADDRESS,
    DATA,
    CONTROL,
    ALERT
}

data class Box3D(
    val id: String,
    val label: String,
    val subLabel: String = "",
    val center: Vec3,
    val size: Vec3,
    val color: Color,
    val explodeOffset: Vec3 = Vec3(0f, 0f, 0f),
    val alpha: Float = 0.94f,
    val isEmphasized: Boolean = false
)

data class BusLink3D(
    val id: String,
    val from: Vec3,
    val to: Vec3,
    val busType: BusType,
    val label: String = "",
    val isActive: Boolean = false,
    val bidirectional: Boolean = false
)

data class ProjectedPoint(
    val offset: Offset,
    val depth: Float,
    val scaleFactor: Float
)

data class Camera3D(
    val yawDeg: Float = -34f,
    val pitchDeg: Float = 24f,
    val zoom: Float = 1.0f,
    val distance: Float = 9.5f
) {
    private val yawRad = Math.toRadians(yawDeg.toDouble()).toFloat()
    private val pitchRad = Math.toRadians(pitchDeg.toDouble()).toFloat()
    private val cosY = cos(yawRad)
    private val sinY = sin(yawRad)
    private val cosP = cos(pitchRad)
    private val sinP = sin(pitchRad)

    fun toViewSpace(world: Vec3): Vec3 {
        // Rotate around Y axis (yaw)
        val x1 = world.x * cosY - world.z * sinY
        val z1 = world.x * sinY + world.z * cosY
        // Rotate around X axis (pitch)
        val y2 = world.y * cosP - z1 * sinP
        val z2 = world.y * sinP + z1 * cosP
        return Vec3(x1, y2, z2)
    }

    fun project(world: Vec3, viewportWidth: Float, viewportHeight: Float): ProjectedPoint {
        val v = toViewSpace(world)
        val cameraZ = (distance / zoom).coerceAtLeast(3.2f)
        val perspectiveDenom = (cameraZ - v.z).coerceAtLeast(1.0f)
        val fovScale = (minOf(viewportWidth, viewportHeight) * 0.85f) / perspectiveDenom
        val sx = viewportWidth * 0.5f + v.x * fovScale
        // Invert Y so +Y points upward in 3D space
        val sy = viewportHeight * 0.52f - v.y * fovScale
        return ProjectedPoint(
            offset = Offset(sx, sy),
            depth = v.z,
            scaleFactor = fovScale
        )
    }
}

data class ProjectedBoxHitTarget(
    val id: String,
    val bounds: Rect,
    val depth: Float
)

fun shadeColor(base: Color, intensity: Float, isActive: Boolean, isSelected: Boolean): Color {
    val boost = when {
        isSelected -> 0.22f
        isActive -> 0.14f
        else -> 0f
    }
    val factor = (intensity + boost).coerceIn(0.32f, 1.25f)
    return Color(
        red = (base.red * factor).coerceIn(0f, 1f),
        green = (base.green * factor).coerceIn(0f, 1f),
        blue = (base.blue * factor).coerceIn(0f, 1f),
        alpha = base.alpha
    )
}
