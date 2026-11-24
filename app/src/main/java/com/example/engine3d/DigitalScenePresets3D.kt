package com.example.engine3d

import androidx.compose.ui.graphics.Color
import com.example.domain.model.SceneType3D

object DigitalScenePresets3D {

    private val CyanCpu = Color(0xFF0284C7)
    private val SkyReg = Color(0xFF38BDF8)
    private val IndigoMem = Color(0xFF4F46E5)
    private val VioletCtrl = Color(0xFF7C3AED)
    private val AmberAlu = Color(0xFFD97706)
    private val EmeraldIo = Color(0xFF059669)
    private val TealCache = Color(0xFF0D9488)
    private val CoralAlert = Color(0xFFE11D48)

    fun buildScene(
        sceneType: SceneType3D,
        activeParts: Set<String>,
        activeBuses: Set<String>,
        modeIndex: Int
    ): SceneAssembly3D? = when (sceneType) {
        SceneType3D.NUMBER_SYSTEMS_CODES -> {
            val modeLabel = when (modeIndex) {
                1 -> "BCD / Gray / Ex-3"
                2 -> "2's Complement"
                else -> "Bin / Oct / Hex"
            }
            val boxes = listOf(
                Box3D("dec_in", "Decimal (25)₁₀", "Base-10 Input", Vec3(-2.1f, 0.9f, 0f), Vec3(1.1f, 0.8f, 1.0f), AmberAlu, Vec3(-0.5f, 0.35f, 0f), isEmphasized = "dec_in" in activeParts || true),
                Box3D("bin_reg", "Binary 11001₂", "Weighted 2^n", Vec3(-0.45f, 0.9f, 0f), Vec3(1.25f, 0.95f, 1.15f), SkyReg, Vec3(0f, 0.5f, 0f), isEmphasized = true),
                Box3D("oct_hex", "Oct (31)₈ / Hex (19)₁₆", "3-bit & 4-bit Group", Vec3(1.5f, 1.35f, -0.55f), Vec3(1.3f, 0.65f, 0.9f), TealCache, Vec3(0.45f, 0.55f, -0.35f), isEmphasized = modeIndex == 0),
                Box3D("code_enc", modeLabel, "Code Encoder", Vec3(1.5f, 0.45f, 0.6f), Vec3(1.3f, 0.7f, 0.95f), VioletCtrl, Vec3(0.45f, -0.2f, 0.4f), isEmphasized = modeIndex >= 1)
            )
            val buses = listOf(
                BusLink3D("dec_bin", Vec3(-1.55f, 0.9f, 0f), Vec3(-1.08f, 0.9f, 0f), BusType.DATA, "÷ 2", true),
                BusLink3D("bin_oct", Vec3(0.18f, 1.1f, -0.2f), Vec3(0.85f, 1.35f, -0.5f), BusType.ADDRESS, "3/4 Bits", modeIndex == 0),
                BusLink3D("bin_code", Vec3(0.18f, 0.7f, 0.2f), Vec3(0.85f, 0.5f, 0.55f), BusType.CONTROL, modeLabel, modeIndex >= 1 || "bin_code" in activeBuses)
            )
            SceneAssembly3D(boxes, buses, defaultYaw = -28f, defaultPitch = 24f)
        }

        SceneType3D.LOGIC_GATES -> {
            val g1Name = when (modeIndex) {
                1 -> "NAND Gate"
                2 -> "XOR Gate"
                else -> "AND Gate"
            }
            val g2Name = when (modeIndex) {
                1 -> "NOR Gate"
                2 -> "XNOR Gate"
                else -> "OR / NOT"
            }
            val boxes = listOf(
                Box3D("in_pins", "Inputs A, B", "Logic 0 / 1", Vec3(-2.0f, 0.85f, 0f), Vec3(0.95f, 1.1f, 1.1f), SkyReg, Vec3(-0.5f, 0.3f, 0f), isEmphasized = true),
                Box3D("gate_primary", g1Name, "CMOS Array", Vec3(-0.15f, 1.35f, -0.45f), Vec3(1.3f, 0.65f, 0.95f), AmberAlu, Vec3(0f, 0.55f, -0.35f), isEmphasized = true),
                Box3D("gate_secondary", g2Name, "Dual Logic", Vec3(-0.15f, 0.35f, 0.45f), Vec3(1.3f, 0.65f, 0.95f), VioletCtrl, Vec3(0f, -0.25f, 0.35f), isEmphasized = true),
                Box3D("out_led", "Output Y", "Truth State", Vec3(1.85f, 0.85f, 0f), Vec3(1.05f, 1.0f, 1.0f), EmeraldIo, Vec3(0.5f, 0.3f, 0f), isEmphasized = true)
            )
            val buses = listOf(
                BusLink3D("g_in_a", Vec3(-1.52f, 1.1f, -0.15f), Vec3(-0.8f, 1.35f, -0.45f), BusType.DATA, "A, B", true),
                BusLink3D("g_in_b", Vec3(-1.52f, 0.6f, 0.15f), Vec3(-0.8f, 0.35f, 0.45f), BusType.DATA, "A, B", true),
                BusLink3D("g_out1", Vec3(0.5f, 1.35f, -0.4f), Vec3(1.32f, 1.0f, -0.1f), BusType.CONTROL, "Y1", true),
                BusLink3D("g_out2", Vec3(0.5f, 0.35f, 0.4f), Vec3(1.32f, 0.7f, 0.1f), BusType.CONTROL, "Y2", true)
            )
            SceneAssembly3D(boxes, buses, defaultYaw = -26f, defaultPitch = 24f)
        }

        SceneType3D.KARNAUGH_MAP -> {
            val boxes = listOf(
                Box3D("kmap_grid", "Gray 4×4 K-Map", "00 01 11 10", Vec3(-1.1f, 0.25f, 0f), Vec3(2.3f, 0.35f, 2.3f), IndigoMem, Vec3(-0.3f, -0.45f, 0f), isEmphasized = true),
                Box3D("kmap_quad", "Quad Group (4)", "Drops 2 Vars -> A'B'", Vec3(-1.55f, 0.9f, -0.55f), Vec3(1.0f, 0.55f, 0.9f), EmeraldIo, Vec3(-0.45f, 0.55f, -0.35f), isEmphasized = true),
                Box3D("kmap_pair", if (modeIndex == 1) "Don't Care Quad" else "Overlap Pair/Quad", "Drops Vars", Vec3(-0.6f, 0.9f, 0.45f), Vec3(1.0f, 0.55f, 0.9f), AmberAlu, Vec3(0f, 0.7f, 0.35f), isEmphasized = true),
                Box3D("min_sop", "Minimized SOP", "F = A'B' + A'D", Vec3(1.75f, 0.8f, 0f), Vec3(1.3f, 0.9f, 1.1f), CyanCpu, Vec3(0.55f, 0.35f, 0f), isEmphasized = true)
            )
            val buses = listOf(
                BusLink3D("k1", Vec3(-1.05f, 0.9f, -0.45f), Vec3(1.1f, 0.9f, -0.15f), BusType.CONTROL, "Term 1", true),
                BusLink3D("k2", Vec3(-0.1f, 0.9f, 0.4f), Vec3(1.1f, 0.7f, 0.15f), BusType.DATA, "Term 2", true)
            )
            SceneAssembly3D(boxes, buses, defaultYaw = -32f, defaultPitch = 28f)
        }

        SceneType3D.MUX_DEMUX -> {
            val isMux = modeIndex == 0
            val boxes = listOf(
                Box3D("mux_inputs", if (isMux) "4 Inputs (I0–I3)" else "4 Outputs (Y0–Y3)", "Parallel Lines", Vec3(if (isMux) -2.0f else 2.0f, 0.9f, 0f), Vec3(1.1f, 1.55f, 1.25f), SkyReg, Vec3(if (isMux) -0.5f else 0.5f, 0.3f, 0f), isEmphasized = true),
                Box3D("mux_core", if (isMux) "4:1 MUX Core" else "1:4 DEMUX Core", "Switch Matrix", Vec3(0f, 0.9f, 0f), Vec3(1.35f, 1.2f, 1.2f), VioletCtrl, Vec3(0f, 0.45f, 0f), isEmphasized = true),
                Box3D("select_lines", "Select (S1, S0)", "2-Bit Control", Vec3(0f, 0.05f, 1.15f), Vec3(1.2f, 0.45f, 0.75f), AmberAlu, Vec3(0f, -0.35f, 0.5f), isEmphasized = true),
                Box3D("single_line", if (isMux) "Single Output Y" else "Single Input Din", "Common Line", Vec3(if (isMux) 2.0f else -2.0f, 0.9f, 0f), Vec3(1.05f, 0.85f, 0.95f), EmeraldIo, Vec3(if (isMux) 0.5f else -0.5f, 0.3f, 0f), isEmphasized = true)
            )
            val buses = listOf(
                BusLink3D("ch0_bus", Vec3(-1.45f, 1.25f, 0f), Vec3(-0.68f, 1.15f, 0f), BusType.DATA, "I0/D", isMux || true),
                BusLink3D("ch2_bus", Vec3(0.68f, 0.9f, 0f), Vec3(1.45f, 0.9f, 0f), BusType.DATA, "Out", true),
                BusLink3D("sel_bus", Vec3(0f, 0.28f, 0.9f), Vec3(0f, 0.4f, 0.35f), BusType.CONTROL, "S1:S0", true)
            )
            SceneAssembly3D(boxes, buses, defaultYaw = -26f, defaultPitch = 24f)
        }

        SceneType3D.FLIP_FLOPS -> {
            val ffTitle = if (modeIndex == 0) "SR / D Latch Core" else "JK / T Toggle Core"
            val boxes = listOf(
                Box3D("ff_inputs", if (modeIndex == 0) "S, R / D Input" else "J, K / T Input", "Next-State Ctrl", Vec3(-2.0f, 1.05f, -0.35f), Vec3(1.05f, 0.85f, 0.9f), SkyReg, Vec3(-0.5f, 0.35f, -0.25f), isEmphasized = "ff_inputs" in activeParts),
                Box3D("ff_clock", "Clock (CLK ↑)", "Edge Trigger", Vec3(-2.0f, 0.25f, 0.75f), Vec3(1.05f, 0.5f, 0.75f), AmberAlu, Vec3(-0.5f, -0.2f, 0.4f), isEmphasized = "ff_clock" in activeParts),
                Box3D("ff_latch", ffTitle, "1-Bit Memory Cell", Vec3(0f, 0.85f, 0f), Vec3(1.45f, 1.25f, 1.25f), VioletCtrl, Vec3(0f, 0.45f, 0f), isEmphasized = true),
                Box3D("ff_outputs", "Outputs Q & Q'", "Stored Bit", Vec3(2.0f, 0.85f, 0f), Vec3(1.05f, 1.05f, 1.0f), EmeraldIo, Vec3(0.5f, 0.35f, 0f), isEmphasized = true)
            )
            val buses = listOf(
                BusLink3D("ff_in_bus", Vec3(-1.48f, 1.05f, -0.3f), Vec3(-0.72f, 1.0f, -0.15f), BusType.DATA, "Input", "ff_in_bus" in activeBuses || true),
                BusLink3D("ff_clk_bus", Vec3(-1.48f, 0.3f, 0.65f), Vec3(-0.72f, 0.55f, 0.35f), BusType.CONTROL, "CLK ↑", "ff_clk_bus" in activeBuses),
                BusLink3D("ff_out_bus", Vec3(0.72f, 0.85f, 0f), Vec3(1.48f, 0.85f, 0f), BusType.DATA, "Q / Q'", "ff_out_bus" in activeBuses || true),
                BusLink3D("ff_fb", Vec3(1.48f, 1.25f, -0.3f), Vec3(-0.2f, 1.48f, -0.3f), BusType.CONTROL, "Feedback", "ff_fb" in activeBuses)
            )
            SceneAssembly3D(boxes, buses, defaultYaw = -28f, defaultPitch = 25f)
        }

        SceneType3D.BUS_STRUCTURES -> {
            val isMulti = modeIndex >= 1
            val boxes = listOf(
                Box3D("bus_master_cpu", "CPU Master", "High Speed", Vec3(-1.9f, 1.0f, -0.45f), Vec3(1.25f, 1.0f, 1.0f), CyanCpu, Vec3(-0.5f, 0.4f, -0.3f), isEmphasized = true),
                Box3D("tristate_buf", "Tri-State Buffers", "0 | 1 | High-Z", Vec3(-0.35f, 0.95f, -0.35f), Vec3(0.95f, 0.65f, 0.85f), AmberAlu, Vec3(-0.1f, 0.55f, -0.25f), isEmphasized = modeIndex == 2 || true),
                Box3D("bus_bridge", if (isMulti) "Bus Bridge / Arbiter" else "Shared System Bus", if (isMulti) "Speed Isolation" else "1 Transfer/Cycle", Vec3(0.55f, 0.35f, 0.45f), Vec3(1.35f, 0.55f, 0.9f), if (isMulti) TealCache else CoralAlert, Vec3(0.15f, -0.25f, 0.3f), isEmphasized = true),
                Box3D("bus_slaves", "RAM & I/O Slaves", "Decoded Targets", Vec3(2.0f, 0.95f, -0.2f), Vec3(1.25f, 1.1f, 1.15f), IndigoMem, Vec3(0.55f, 0.35f, -0.2f), isEmphasized = true)
            )
            val buses = listOf(
                BusLink3D("sys_bus", Vec3(-1.28f, 1.0f, -0.4f), Vec3(1.38f, 1.0f, -0.3f), BusType.DATA, "System Bus", true, bidirectional = true),
                BusLink3D("io_exp_bus", Vec3(0.55f, 0.62f, 0.45f), Vec3(1.85f, 0.55f, 0.3f), if (isMulti) BusType.CONTROL else BusType.ALERT, if (isMulti) "PCI/I/O Bus" else "Shared", true)
            )
            SceneAssembly3D(boxes, buses, defaultYaw = -30f, defaultPitch = 25f)
        }

        else -> null
    }
}
