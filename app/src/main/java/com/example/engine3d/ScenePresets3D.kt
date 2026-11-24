package com.example.engine3d

import androidx.compose.ui.graphics.Color
import com.example.domain.model.SceneType3D
import com.example.domain.model.SimulationStep

data class SceneAssembly3D(
    val boxes: List<Box3D>,
    val buses: List<BusLink3D>,
    val defaultYaw: Float = -34f,
    val defaultPitch: Float = 24f
)

object ScenePresets3D {

    private val CyanCpu = Color(0xFF0284C7)
    private val SkyRegister = Color(0xFF38BDF8)
    private val IndigoMemory = Color(0xFF4F46E5)
    private val VioletControl = Color(0xFF7C3AED)
    private val AmberAlu = Color(0xFFD97706)
    private val EmeraldIo = Color(0xFF059669)
    private val TealCache = Color(0xFF0D9488)
    private val CoralAlert = Color(0xFFE11D48)
    private val SlateFrame = Color(0xFF334155)

    fun buildScene(
        sceneType: SceneType3D,
        step: SimulationStep?,
        interactiveModeIndex: Int = 0
    ): SceneAssembly3D {
        val activeParts = step?.activePartIds ?: emptySet()
        val activeBuses = step?.activeBusIds ?: emptySet()

        DigitalScenePresets3D.buildScene(sceneType, activeParts, activeBuses, interactiveModeIndex)?.let {
            return it
        }

        return when (sceneType) {
            SceneType3D.VON_NEUMANN -> buildVonNeumann(activeParts, activeBuses)
            SceneType3D.HARVARD_VS_VON_NEUMANN -> buildHarvardVsVonNeumann(activeParts, activeBuses, interactiveModeIndex)
            SceneType3D.INSTRUCTION_CYCLE -> buildInstructionCycle(activeParts, activeBuses)
            SceneType3D.ALU_MICRO_OPS -> buildAluMicroOps(activeParts, activeBuses, interactiveModeIndex)
            SceneType3D.ADDRESSING_MODES -> buildAddressingModes(activeParts, activeBuses, interactiveModeIndex)
            SceneType3D.CONTROL_UNIT -> buildControlUnit(activeParts, activeBuses, interactiveModeIndex)
            SceneType3D.PIPELINE_5_STAGE -> buildPipeline5Stage(activeParts, activeBuses, interactiveModeIndex)
            SceneType3D.MEMORY_HIERARCHY -> buildMemoryHierarchy(activeParts, activeBuses)
            SceneType3D.CACHE_MAPPING -> buildCacheMapping(activeParts, activeBuses, interactiveModeIndex)
            SceneType3D.VIRTUAL_MEMORY_TLB -> buildVirtualMemoryTlb(activeParts, activeBuses, interactiveModeIndex)
            SceneType3D.IO_DMA_TRANSFER -> buildIoDmaTransfer(activeParts, activeBuses, interactiveModeIndex)
            SceneType3D.FLYNNS_MULTICORE -> buildFlynnsMulticore(activeParts, activeBuses, interactiveModeIndex)
            else -> buildVonNeumann(activeParts, activeBuses)
        }
    }

    private fun buildVonNeumann(activeParts: Set<String>, activeBuses: Set<String>): SceneAssembly3D {
        val boxes = listOf(
            Box3D("cpu_shell", "CPU Socket", "Central Processor", Vec3(-1.6f, 0.1f, 0f), Vec3(2.2f, 0.25f, 2.0f), SlateFrame, Vec3(-0.3f, -0.5f, 0f), alpha = 0.85f),
            Box3D("cu", "Control Unit", "CU + Timing", Vec3(-2.1f, 0.75f, -0.4f), Vec3(0.95f, 0.9f, 0.95f), VioletControl, Vec3(-0.5f, 0.6f, -0.4f), isEmphasized = "cu" in activeParts),
            Box3D("alu", "ALU", "Arithmetic & Logic", Vec3(-1.0f, 0.75f, -0.4f), Vec3(0.95f, 0.9f, 0.95f), AmberAlu, Vec3(-0.1f, 0.8f, -0.4f), isEmphasized = "alu" in activeParts),
            Box3D("regs", "Registers", "PC / IR / AC", Vec3(-1.55f, 0.75f, 0.55f), Vec3(1.9f, 0.65f, 0.7f), SkyRegister, Vec3(-0.3f, 0.45f, 0.55f), isEmphasized = "regs" in activeParts),
            Box3D("memory", "Main Memory", "Unified Code + Data", Vec3(1.75f, 0.7f, -0.55f), Vec3(1.6f, 1.35f, 1.3f), IndigoMemory, Vec3(0.55f, 0.4f, -0.3f), isEmphasized = "memory" in activeParts),
            Box3D("io", "I/O Interface", "Input / Output", Vec3(1.75f, 0.45f, 1.15f), Vec3(1.5f, 0.85f, 0.9f), EmeraldIo, Vec3(0.55f, 0.2f, 0.5f), isEmphasized = "io" in activeParts)
        )
        val buses = listOf(
            BusLink3D("addr_bus", Vec3(-0.5f, 0.95f, -0.35f), Vec3(0.95f, 0.95f, -0.35f), BusType.ADDRESS, "Address Bus", "addr_bus" in activeBuses),
            BusLink3D("data_bus", Vec3(-0.5f, 0.55f, 0.1f), Vec3(0.95f, 0.55f, -0.1f), BusType.DATA, "Data Bus", "data_bus" in activeBuses, bidirectional = true),
            BusLink3D("ctrl_bus", Vec3(-1.6f, 0.35f, -0.1f), Vec3(0.95f, 0.35f, 0.85f), BusType.CONTROL, "Control Bus", "ctrl_bus" in activeBuses),
            BusLink3D("io_bus", Vec3(0.3f, 0.45f, 0.1f), Vec3(1.0f, 0.45f, 1.15f), BusType.DATA, "I/O Link", "io_bus" in activeBuses, bidirectional = true)
        )
        return SceneAssembly3D(boxes, buses, defaultYaw = -32f, defaultPitch = 25f)
    }

    private fun buildHarvardVsVonNeumann(
        activeParts: Set<String>,
        activeBuses: Set<String>,
        modeIndex: Int
    ): SceneAssembly3D {
        // modeIndex 0 = Harvard (Separate Instruction & Data Memories + Dual Buses)
        // modeIndex 1 = Von Neumann (Single Shared Memory + Bottleneck)
        return if (modeIndex == 0) {
            val boxes = listOf(
                Box3D("inst_mem", "Instruction Mem", "ROM / Code Store", Vec3(-2.1f, 0.7f, 0f), Vec3(1.35f, 1.3f, 1.3f), AmberAlu, Vec3(-0.6f, 0.3f, 0f), isEmphasized = "inst_mem" in activeParts),
                Box3D("cpu_core", "Harvard CPU", "CU + ALU + Fetch", Vec3(0f, 0.8f, 0f), Vec3(1.5f, 1.45f, 1.4f), CyanCpu, Vec3(0f, 0.65f, 0f), isEmphasized = "cpu_core" in activeParts),
                Box3D("data_mem", "Data Memory", "RAM / Operands", Vec3(2.1f, 0.7f, 0f), Vec3(1.35f, 1.3f, 1.3f), IndigoMemory, Vec3(0.6f, 0.3f, 0f), isEmphasized = "data_mem" in activeParts)
            )
            val buses = listOf(
                BusLink3D("inst_bus", Vec3(-1.42f, 0.9f, 0f), Vec3(-0.75f, 0.9f, 0f), BusType.ADDRESS, "Instruction Bus", "inst_bus" in activeBuses),
                BusLink3D("data_bus", Vec3(0.75f, 0.65f, 0f), Vec3(1.42f, 0.65f, 0f), BusType.DATA, "Data Bus", "data_bus" in activeBuses, bidirectional = true),
                BusLink3D("ctrl_bus", Vec3(-0.75f, 0.4f, 0.3f), Vec3(1.42f, 0.4f, 0.3f), BusType.CONTROL, "Dual Control", "ctrl_bus" in activeBuses)
            )
            SceneAssembly3D(boxes, buses, defaultYaw = -26f, defaultPitch = 24f)
        } else {
            val boxes = listOf(
                Box3D("cpu_core", "Von Neumann CPU", "Single Port Core", Vec3(-1.6f, 0.8f, 0f), Vec3(1.6f, 1.4f, 1.4f), CyanCpu, Vec3(-0.45f, 0.5f, 0f), isEmphasized = "cpu_core" in activeParts),
                Box3D("shared_bus_bridge", "Shared Bus", "Single Bottleneck", Vec3(0f, 0.45f, 0f), Vec3(0.7f, 0.55f, 0.8f), CoralAlert, Vec3(0f, 0.2f, 0f), isEmphasized = true),
                Box3D("inst_mem", "Unified Memory", "Code + Data Together", Vec3(1.7f, 0.75f, 0f), Vec3(1.6f, 1.45f, 1.4f), IndigoMemory, Vec3(0.5f, 0.3f, 0f), isEmphasized = "data_mem" in activeParts || "inst_mem" in activeParts)
            )
            val buses = listOf(
                BusLink3D("inst_bus", Vec3(-0.8f, 0.65f, 0f), Vec3(0.9f, 0.65f, 0f), BusType.ALERT, "Shared Fetch/Data", true, bidirectional = true)
            )
            SceneAssembly3D(boxes, buses, defaultYaw = -28f, defaultPitch = 24f)
        }
    }

    private fun buildInstructionCycle(activeParts: Set<String>, activeBuses: Set<String>): SceneAssembly3D {
        val boxes = listOf(
            Box3D("pc", "PC", "Program Counter", Vec3(-2.1f, 1.25f, -0.7f), Vec3(0.9f, 0.55f, 0.7f), SkyRegister, Vec3(-0.4f, 0.6f, -0.3f), isEmphasized = "pc" in activeParts),
            Box3D("mar", "MAR", "Memory Addr Reg", Vec3(-0.6f, 1.25f, -0.7f), Vec3(0.95f, 0.55f, 0.7f), AmberAlu, Vec3(0f, 0.6f, -0.3f), isEmphasized = "mar" in activeParts),
            Box3D("ram", "Main RAM", "M[MAR] Store", Vec3(1.8f, 0.75f, 0f), Vec3(1.5f, 1.45f, 1.5f), IndigoMemory, Vec3(0.6f, 0.3f, 0f), isEmphasized = "ram" in activeParts),
            Box3D("mdr", "MDR", "Memory Data Reg", Vec3(-0.6f, 0.45f, 0.7f), Vec3(0.95f, 0.55f, 0.7f), TealCache, Vec3(0f, 0.25f, 0.35f), isEmphasized = "mdr" in activeParts),
            Box3D("ir", "IR", "Instruction Reg", Vec3(-2.1f, 0.45f, 0.7f), Vec3(0.95f, 0.55f, 0.7f), VioletControl, Vec3(-0.4f, 0.25f, 0.35f), isEmphasized = "ir" in activeParts),
            Box3D("cu_alu", "CU + ALU", "Decode & Execute", Vec3(-1.35f, 0.35f, -0.1f), Vec3(1.4f, 0.65f, 0.75f), CyanCpu, Vec3(-0.2f, -0.45f, 0f), isEmphasized = "cu_alu" in activeParts)
        )
        val buses = listOf(
            BusLink3D("pc_mar", Vec3(-1.65f, 1.25f, -0.7f), Vec3(-1.08f, 1.25f, -0.7f), BusType.ADDRESS, "PC → MAR", "pc_mar" in activeBuses),
            BusLink3D("mar_ram", Vec3(-0.12f, 1.15f, -0.6f), Vec3(1.05f, 1.0f, -0.2f), BusType.ADDRESS, "Addr Bus", "mar_ram" in activeBuses),
            BusLink3D("ram_mdr", Vec3(1.05f, 0.55f, 0.25f), Vec3(-0.12f, 0.48f, 0.65f), BusType.DATA, "Data Bus", "ram_mdr" in activeBuses, bidirectional = true),
            BusLink3D("mdr_ir", Vec3(-1.08f, 0.45f, 0.7f), Vec3(-1.62f, 0.45f, 0.7f), BusType.DATA, "MDR → IR", "mdr_ir" in activeBuses),
            BusLink3D("ir_cu", Vec3(-2.0f, 0.45f, 0.35f), Vec3(-1.6f, 0.45f, 0.05f), BusType.CONTROL, "Decode", "ir_cu" in activeBuses)
        )
        return SceneAssembly3D(boxes, buses, defaultYaw = -30f, defaultPitch = 28f)
    }

    private fun buildAluMicroOps(
        activeParts: Set<String>,
        activeBuses: Set<String>,
        modeIndex: Int
    ): SceneAssembly3D {
        // modeIndex: 0 = Arithmetic (ADD), 1 = Logic (XOR), 2 = Shift (SHL)
        val opTag = when (modeIndex) {
            1 -> "XOR Logic"
            2 -> "Barrel Shift"
            else -> "Ripple Adder"
        }
        val boxes = listOf(
            Box3D("reg_a", "Input Reg A", "Operand A", Vec3(-1.5f, 1.65f, -0.45f), Vec3(1.1f, 0.55f, 0.8f), SkyRegister, Vec3(-0.4f, 0.7f, -0.3f), isEmphasized = "reg_a" in activeParts),
            Box3D("reg_b", "Input Reg B", "Operand B", Vec3(1.5f, 1.65f, -0.45f), Vec3(1.1f, 0.55f, 0.8f), SkyRegister, Vec3(0.4f, 0.7f, -0.3f), isEmphasized = "reg_b" in activeParts),
            Box3D("arith_unit", "Arithmetic Unit", "Full Adder Array", Vec3(-1.2f, 0.85f, 0f), Vec3(1.25f, 0.7f, 1.0f), AmberAlu, Vec3(-0.5f, 0.2f, 0f), isEmphasized = ("arith_unit" in activeParts) || modeIndex == 0),
            Box3D("logic_unit", "Logic Gates", "AND/OR/XOR", Vec3(0f, 0.85f, 0f), Vec3(1.05f, 0.7f, 1.0f), VioletControl, Vec3(0f, 0.35f, 0.45f), isEmphasized = ("logic_unit" in activeParts) || modeIndex == 1),
            Box3D("shifter", "Shifter Unit", "SHL / SHR", Vec3(1.25f, 0.85f, 0f), Vec3(1.1f, 0.7f, 1.0f), TealCache, Vec3(0.5f, 0.2f, 0f), isEmphasized = ("shifter" in activeParts) || modeIndex == 2),
            Box3D("mux", "4:1 Output MUX", "Select: $opTag", Vec3(0f, 0.1f, 0.1f), Vec3(2.4f, 0.45f, 0.9f), CyanCpu, Vec3(0f, -0.35f, 0.2f), isEmphasized = "mux" in activeParts),
            Box3D("flags", "Status Flags", "Z | C | N | V", Vec3(1.85f, 0.1f, 0.6f), Vec3(0.95f, 0.5f, 0.7f), CoralAlert, Vec3(0.5f, -0.35f, 0.4f), isEmphasized = "flags" in activeParts)
        )
        val selectedSource = when (modeIndex) {
            1 -> Vec3(0f, 0.5f, 0f)
            2 -> Vec3(1.25f, 0.5f, 0f)
            else -> Vec3(-1.2f, 0.5f, 0f)
        }
        val buses = listOf(
            BusLink3D("op_a", Vec3(-1.5f, 1.38f, -0.4f), Vec3(-0.8f, 1.2f, 0f), BusType.DATA, "Bus A", "op_a" in activeBuses),
            BusLink3D("op_b", Vec3(1.5f, 1.38f, -0.4f), Vec3(0.8f, 1.2f, 0f), BusType.DATA, "Bus B", "op_b" in activeBuses),
            BusLink3D("alu_mux", selectedSource, Vec3(0f, 0.33f, 0.1f), BusType.CONTROL, opTag, "alu_mux" in activeBuses),
            BusLink3D("flag_bus", Vec3(1.1f, 0.1f, 0.2f), Vec3(1.38f, 0.1f, 0.5f), BusType.ALERT, "Flags", "flag_bus" in activeBuses)
        )
        return SceneAssembly3D(boxes, buses, defaultYaw = -24f, defaultPitch = 22f)
    }

    private fun buildAddressingModes(
        activeParts: Set<String>,
        activeBuses: Set<String>,
        modeIndex: Int
    ): SceneAssembly3D {
        // modeIndex: 0=Immediate, 1=Direct, 2=Indirect, 3=Indexed
        val boxes = listOf(
            Box3D("ir_field", "IR [Opcode|Addr]", "Instruction Field", Vec3(-2.0f, 1.1f, 0f), Vec3(1.45f, 0.75f, 1.0f), VioletControl, Vec3(-0.5f, 0.4f, 0f), isEmphasized = true),
            Box3D("reg_file", "Index / Base Reg", "R1 / IX Register", Vec3(-0.35f, 1.35f, -0.8f), Vec3(1.1f, 0.65f, 0.8f), SkyRegister, Vec3(0f, 0.6f, -0.45f), isEmphasized = modeIndex == 3 || "reg_file" in activeParts),
            Box3D("mem_ptr", "Memory [Addr]", "Pointer Cell", Vec3(0.3f, 0.45f, 0.45f), Vec3(1.15f, 0.8f, 0.9f), AmberAlu, Vec3(0.15f, 0.15f, 0.35f), isEmphasized = modeIndex >= 1 || "mem_ptr" in activeParts),
            Box3D("mem_data", "Target Operand", "Final Data Value", Vec3(2.1f, 0.7f, 0f), Vec3(1.3f, 1.25f, 1.1f), EmeraldIo, Vec3(0.6f, 0.3f, 0f), isEmphasized = modeIndex >= 1 || "mem_data" in activeParts)
        )
        val buses = when (modeIndex) {
            0 -> listOf(
                BusLink3D("imm_hop", Vec3(-1.28f, 1.1f, 0f), Vec3(-0.3f, 0.7f, 0f), BusType.DATA, "Immediate Value", true)
            )
            1 -> listOf(
                BusLink3D("dir_hop", Vec3(-1.28f, 1.0f, 0f), Vec3(1.45f, 0.8f, 0f), BusType.ADDRESS, "EA = Addr", true)
            )
            2 -> listOf(
                BusLink3D("ind_1", Vec3(-1.28f, 0.9f, 0.15f), Vec3(-0.28f, 0.55f, 0.45f), BusType.ADDRESS, "Hop 1: Addr", true),
                BusLink3D("ind_2", Vec3(0.88f, 0.55f, 0.35f), Vec3(1.45f, 0.7f, 0.1f), BusType.DATA, "Hop 2: M[Addr]", true)
            )
            else -> listOf(
                BusLink3D("idx_1", Vec3(-1.28f, 1.2f, -0.2f), Vec3(-0.9f, 1.35f, -0.7f), BusType.CONTROL, "Addr + R1", true),
                BusLink3D("idx_2", Vec3(0.2f, 1.28f, -0.65f), Vec3(1.45f, 0.85f, -0.1f), BusType.ADDRESS, "Effective Addr", true)
            )
        }
        return SceneAssembly3D(boxes, buses, defaultYaw = -28f, defaultPitch = 24f)
    }

    private fun buildControlUnit(
        activeParts: Set<String>,
        activeBuses: Set<String>,
        modeIndex: Int
    ): SceneAssembly3D {
        // 0 = Hardwired Control, 1 = Microprogrammed Control
        return if (modeIndex == 0) {
            val boxes = listOf(
                Box3D("ir_op", "IR Opcode", "Instruction Bits", Vec3(-1.9f, 0.9f, 0f), Vec3(1.1f, 0.8f, 1.0f), VioletControl, Vec3(-0.45f, 0.4f, 0f), isEmphasized = "ir_op" in activeParts),
                Box3D("ring_cnt", "Step Counter", "T0..Tn Timing", Vec3(-1.9f, 0.2f, 0.9f), Vec3(1.0f, 0.5f, 0.7f), SkyRegister, Vec3(-0.45f, -0.1f, 0.5f), isEmphasized = "ring_cnt" in activeParts),
                Box3D("gate_matrix", "PLA Gate Matrix", "Hardwired Logic", Vec3(0f, 0.7f, 0f), Vec3(1.55f, 1.2f, 1.35f), AmberAlu, Vec3(0f, 0.45f, 0f), isEmphasized = true),
                Box3D("ctrl_signals", "Control Signals", "C0..Ck Out", Vec3(2.0f, 0.7f, 0f), Vec3(1.1f, 1.1f, 1.1f), EmeraldIo, Vec3(0.5f, 0.3f, 0f), isEmphasized = "ctrl_signals" in activeParts)
            )
            val buses = listOf(
                BusLink3D("b1", Vec3(-1.35f, 0.9f, 0f), Vec3(-0.78f, 0.85f, 0f), BusType.ADDRESS, "Opcode", true),
                BusLink3D("b2", Vec3(-1.4f, 0.25f, 0.8f), Vec3(-0.78f, 0.4f, 0.35f), BusType.CONTROL, "Clock T", true),
                BusLink3D("b3", Vec3(0.78f, 0.7f, 0f), Vec3(1.45f, 0.7f, 0f), BusType.CONTROL, "Fast Signals", true)
            )
            SceneAssembly3D(boxes, buses)
        } else {
            val boxes = listOf(
                Box3D("ir_op", "Sequencer", "Next Addr Logic", Vec3(-2.0f, 0.8f, 0f), Vec3(1.05f, 0.9f, 0.95f), VioletControl, Vec3(-0.5f, 0.35f, 0f), isEmphasized = "ir_op" in activeParts),
                Box3D("cmar", "CMAR", "Control Mem Addr", Vec3(-0.75f, 1.15f, -0.35f), Vec3(0.9f, 0.55f, 0.8f), SkyRegister, Vec3(-0.2f, 0.5f, -0.3f), isEmphasized = "cmar" in activeParts),
                Box3D("ctrl_mem", "Control Memory", "Microprogram ROM", Vec3(0.55f, 0.75f, 0f), Vec3(1.35f, 1.3f, 1.3f), TealCache, Vec3(0.15f, 0.4f, 0f), isEmphasized = true),
                Box3D("cmdr", "CMDR / CW", "Control Word Out", Vec3(2.1f, 0.75f, 0f), Vec3(1.05f, 0.9f, 1.0f), EmeraldIo, Vec3(0.55f, 0.25f, 0f), isEmphasized = "ctrl_signals" in activeParts)
            )
            val buses = listOf(
                BusLink3D("b1", Vec3(-1.48f, 1.05f, -0.15f), Vec3(-1.2f, 1.15f, -0.35f), BusType.ADDRESS, "CAR", true),
                BusLink3D("b2", Vec3(-0.3f, 1.1f, -0.25f), Vec3(-0.12f, 0.95f, -0.15f), BusType.ADDRESS, "Lookup", true),
                BusLink3D("b3", Vec3(1.22f, 0.75f, 0f), Vec3(1.58f, 0.75f, 0f), BusType.CONTROL, "Micro-op", true)
            )
            SceneAssembly3D(boxes, buses)
        }
    }

    private fun buildPipeline5Stage(
        activeParts: Set<String>,
        activeBuses: Set<String>,
        modeIndex: Int
    ): SceneAssembly3D {
        // modeIndex: 0 = Normal / Forwarding Path, 1 = Pipeline Stall Bubble
        val stageColors = listOf(SkyRegister, VioletControl, AmberAlu, IndigoMemory, EmeraldIo)
        val stageIds = listOf("if_stage", "id_stage", "ex_stage", "mem_stage", "wb_stage")
        val stageTitles = listOf("IF", "ID", "EX", "MEM", "WB")
        val stageSubs = listOf("Fetch", "Decode", "Execute", "Memory", "WriteBack")

        val boxes = mutableListOf<Box3D>()
        for (i in 0 until 5) {
            val x = -2.2f + i * 1.1f
            val isStallEx = modeIndex == 1 && i == 2
            boxes += Box3D(
                id = stageIds[i],
                label = stageTitles[i],
                subLabel = if (isStallEx) "NOP Bubble" else stageSubs[i],
                center = Vec3(x, 0.75f, 0f),
                size = Vec3(0.82f, 1.15f, 1.05f),
                color = if (isStallEx) CoralAlert else stageColors[i],
                explodeOffset = Vec3((i - 2) * 0.25f, (i % 2) * 0.35f, 0f),
                isEmphasized = stageIds[i] in activeParts || isStallEx
            )
            if (i < 4) {
                boxes += Box3D(
                    id = "pipe_reg_$i",
                    label = "Latch",
                    subLabel = "IF/ID..MEM/WB",
                    center = Vec3(x + 0.55f, 0.75f, 0f),
                    size = Vec3(0.16f, 1.25f, 1.15f),
                    color = SlateFrame,
                    explodeOffset = Vec3((i - 1.5f) * 0.25f, -0.25f, 0.35f)
                )
            }
        }

        val buses = mutableListOf(
            BusLink3D("pipe_flow_1", Vec3(-2.2f, 0.75f, 0f), Vec3(-1.1f, 0.75f, 0f), BusType.DATA, "Inst", "pipe_flow_1" in activeBuses || true),
            BusLink3D("pipe_flow_2", Vec3(-1.1f, 0.75f, 0f), Vec3(0f, 0.75f, 0f), BusType.DATA, "Op", true),
            BusLink3D("pipe_flow_3", Vec3(0f, 0.75f, 0f), Vec3(1.1f, 0.75f, 0f), BusType.DATA, "ALU Out", true),
            BusLink3D("pipe_flow_4", Vec3(1.1f, 0.75f, 0f), Vec3(2.2f, 0.75f, 0f), BusType.CONTROL, "WB", true)
        )
        if (modeIndex == 0) {
            buses += BusLink3D("forwarding_bypass", Vec3(0.25f, 1.45f, 0.3f), Vec3(-0.15f, 1.45f, 0.3f), BusType.CONTROL, "Forwarding", true)
        } else {
            buses += BusLink3D("hazard_stall", Vec3(-1.1f, 1.45f, 0.3f), Vec3(0f, 1.45f, 0.3f), BusType.ALERT, "RAW Hazard Stall", true)
        }
        return SceneAssembly3D(boxes, buses, defaultYaw = -22f, defaultPitch = 24f)
    }

    private fun buildMemoryHierarchy(activeParts: Set<String>, activeBuses: Set<String>): SceneAssembly3D {
        val boxes = listOf(
            Box3D("regs", "CPU Registers", "~0.3 ns | Bytes", Vec3(0f, 2.15f, 0f), Vec3(0.95f, 0.42f, 0.95f), AmberAlu, Vec3(0f, 0.95f, 0f), isEmphasized = "regs" in activeParts),
            Box3D("l1_cache", "L1 / L2 Cache", "1–4 ns | KB–MB", Vec3(0f, 1.55f, 0f), Vec3(1.65f, 0.48f, 1.55f), TealCache, Vec3(0f, 0.55f, 0f), isEmphasized = "l1_cache" in activeParts),
            Box3D("l3_cache", "L3 Shared Cache", "~12 ns | MBs", Vec3(0f, 0.95f, 0f), Vec3(2.3f, 0.48f, 2.15f), CyanCpu, Vec3(0f, 0.15f, 0f), isEmphasized = "l3_cache" in activeParts),
            Box3D("ram", "Main RAM (DRAM)", "~60 ns | GBs", Vec3(0f, 0.35f, 0f), Vec3(3.05f, 0.52f, 2.75f), IndigoMemory, Vec3(0f, -0.25f, 0f), isEmphasized = "ram" in activeParts),
            Box3D("disk", "SSD / Secondary", "μs–ms | TBs", Vec3(0f, -0.25f, 0f), Vec3(3.8f, 0.52f, 3.35f), SlateFrame, Vec3(0f, -0.65f, 0f), isEmphasized = "disk" in activeParts)
        )
        val buses = listOf(
            BusLink3D("hit_path", Vec3(0f, 1.55f, 0.8f), Vec3(0f, 2.15f, 0.5f), BusType.CONTROL, "Cache Hit", "hit_path" in activeBuses),
            BusLink3D("miss_path", Vec3(0f, -0.2f, 1.65f), Vec3(0f, 1.55f, 0.85f), BusType.ALERT, "Cache Miss Fill", "miss_path" in activeBuses)
        )
        return SceneAssembly3D(boxes, buses, defaultYaw = -36f, defaultPitch = 20f)
    }

    private fun buildCacheMapping(
        activeParts: Set<String>,
        activeBuses: Set<String>,
        modeIndex: Int
    ): SceneAssembly3D {
        // modeIndex: 0 = Direct Mapped, 1 = Fully Associative, 2 = 2-Way Set Associative
        val boxes = mutableListOf(
            Box3D("cpu_addr", "CPU Address", "Tag | Index | Offset", Vec3(-2.1f, 1.15f, 0f), Vec3(1.2f, 0.7f, 1.0f), SkyRegister, Vec3(-0.45f, 0.45f, 0f), isEmphasized = true),
            Box3D("cache_b0", "Cache Line 0", "Way 0 / Tag Check", Vec3(-0.2f, 1.4f, -0.55f), Vec3(1.25f, 0.52f, 0.85f), TealCache, Vec3(0f, 0.55f, -0.35f), isEmphasized = modeIndex == 0 || "cache_lines" in activeParts),
            Box3D("cache_b1", "Cache Line 1", "Way 1 / Tag Check", Vec3(-0.2f, 0.6f, 0.55f), Vec3(1.25f, 0.52f, 0.85f), TealCache, Vec3(0f, 0.1f, 0.35f), isEmphasized = modeIndex >= 1 || "cache_lines" in activeParts),
            Box3D("ram_blk0", "RAM Block 0", "Addr % N = 0", Vec3(1.9f, 1.45f, -0.6f), Vec3(1.2f, 0.55f, 0.85f), IndigoMemory, Vec3(0.55f, 0.5f, -0.35f), isEmphasized = "ram_blocks" in activeParts),
            Box3D("ram_blk4", "RAM Block 4", "Maps to Same Line!", Vec3(1.9f, 0.55f, 0.6f), Vec3(1.2f, 0.55f, 0.85f), AmberAlu, Vec3(0.55f, 0.1f, 0.35f), isEmphasized = "ram_blocks" in activeParts)
        )
        val buses = when (modeIndex) {
            0 -> listOf(
                BusLink3D("addr_chk", Vec3(-1.5f, 1.2f, -0.15f), Vec3(-0.82f, 1.4f, -0.5f), BusType.ADDRESS, "Mod N Index", true),
                BusLink3D("map_direct", Vec3(1.3f, 1.45f, -0.6f), Vec3(0.42f, 1.4f, -0.55f), BusType.DATA, "Fixed Line 0", true),
                BusLink3D("conflict_line", Vec3(1.3f, 0.55f, 0.6f), Vec3(0.42f, 1.32f, -0.45f), BusType.ALERT, "Conflict Miss", "conflict_line" in activeBuses || true)
            )
            1 -> listOf(
                BusLink3D("assoc_1", Vec3(1.3f, 1.45f, -0.6f), Vec3(0.42f, 1.4f, -0.55f), BusType.CONTROL, "Any Line", true),
                BusLink3D("assoc_2", Vec3(1.3f, 0.55f, 0.6f), Vec3(0.42f, 0.6f, 0.55f), BusType.CONTROL, "Parallel Tag Search", true)
            )
            else -> listOf(
                BusLink3D("set_idx", Vec3(-1.5f, 1.15f, 0f), Vec3(-0.82f, 1.0f, 0f), BusType.ADDRESS, "Set Index", true),
                BusLink3D("way_0", Vec3(1.3f, 1.45f, -0.6f), Vec3(0.42f, 1.4f, -0.55f), BusType.DATA, "Set 0 Way 0", true),
                BusLink3D("way_1", Vec3(1.3f, 0.55f, 0.6f), Vec3(0.42f, 0.6f, 0.55f), BusType.DATA, "Set 0 Way 1", true)
            )
        }
        return SceneAssembly3D(boxes, buses, defaultYaw = -30f, defaultPitch = 24f)
    }

    private fun buildVirtualMemoryTlb(
        activeParts: Set<String>,
        activeBuses: Set<String>,
        modeIndex: Int
    ): SceneAssembly3D {
        // 0 = TLB Hit (Fast Path), 1 = Page Fault (Disk Swap)
        val boxes = listOf(
            Box3D("cpu_va", "Virtual Addr", "Page # + Offset", Vec3(-2.1f, 1.0f, 0f), Vec3(1.15f, 0.85f, 0.95f), SkyRegister, Vec3(-0.5f, 0.45f, 0f), isEmphasized = "cpu_va" in activeParts),
            Box3D("tlb", "TLB Cache", "Fast Page Lookup", Vec3(-0.45f, 1.5f, -0.55f), Vec3(1.05f, 0.6f, 0.85f), TealCache, Vec3(-0.1f, 0.65f, -0.35f), isEmphasized = modeIndex == 0 || "tlb" in activeParts),
            Box3D("page_table", "Page Table", "RAM Mapping Table", Vec3(-0.45f, 0.45f, 0.55f), Vec3(1.15f, 0.75f, 0.95f), VioletControl, Vec3(-0.1f, 0.1f, 0.4f), isEmphasized = modeIndex == 1 || "page_table" in activeParts),
            Box3D("phys_ram", "Physical RAM", "Frame # + Offset", Vec3(1.45f, 0.95f, -0.25f), Vec3(1.25f, 1.2f, 1.15f), IndigoMemory, Vec3(0.45f, 0.35f, -0.2f), isEmphasized = "phys_ram" in activeParts),
            Box3D("swap_disk", "Swap Disk", "Page Fault Store", Vec3(1.45f, 0.15f, 1.1f), Vec3(1.25f, 0.55f, 0.9f), CoralAlert, Vec3(0.45f, -0.35f, 0.5f), isEmphasized = modeIndex == 1 || "swap_disk" in activeParts)
        )
        val buses = if (modeIndex == 0) {
            listOf(
                BusLink3D("va_tlb", Vec3(-1.52f, 1.2f, -0.15f), Vec3(-0.98f, 1.5f, -0.5f), BusType.ADDRESS, "Check TLB", true),
                BusLink3D("tlb_ram", Vec3(0.08f, 1.45f, -0.5f), Vec3(0.82f, 1.2f, -0.35f), BusType.CONTROL, "TLB Hit!", true)
            )
        } else {
            listOf(
                BusLink3D("va_pt", Vec3(-1.52f, 0.85f, 0.15f), Vec3(-1.02f, 0.55f, 0.45f), BusType.ADDRESS, "TLB Miss -> PT", true),
                BusLink3D("pt_disk", Vec3(0.12f, 0.35f, 0.65f), Vec3(0.82f, 0.25f, 0.95f), BusType.ALERT, "Page Fault!", true),
                BusLink3D("disk_ram", Vec3(1.45f, 0.42f, 0.95f), Vec3(1.45f, 0.7f, 0.32f), BusType.DATA, "Load Page", true)
            )
        }
        return SceneAssembly3D(boxes, buses, defaultYaw = -30f, defaultPitch = 25f)
    }

    private fun buildIoDmaTransfer(
        activeParts: Set<String>,
        activeBuses: Set<String>,
        modeIndex: Int
    ): SceneAssembly3D {
        // 0 = DMA Mode (CPU Freed, Bus Master DMA -> RAM)
        // 1 = Programmed I/O (CPU Busy Loop)
        val boxes = listOf(
            Box3D("cpu", "CPU Core", if (modeIndex == 0) "Free (HOLD/HLDA)" else "Stalled Polling", Vec3(-1.9f, 1.15f, -0.55f), Vec3(1.3f, 1.1f, 1.0f), CyanCpu, Vec3(-0.5f, 0.45f, -0.3f), isEmphasized = "cpu" in activeParts),
            Box3D("dma", "DMA Controller", "Bus Master", Vec3(-0.25f, 0.45f, 0.7f), Vec3(1.25f, 0.75f, 0.95f), AmberAlu, Vec3(0f, 0.2f, 0.45f), isEmphasized = modeIndex == 0 || "dma" in activeParts),
            Box3D("ram", "Main RAM", "Direct Buffer", Vec3(1.8f, 1.15f, -0.55f), Vec3(1.35f, 1.2f, 1.1f), IndigoMemory, Vec3(0.55f, 0.45f, -0.3f), isEmphasized = "ram" in activeParts),
            Box3D("disk_io", "Disk / Network I/O", "High-Speed Device", Vec3(1.8f, 0.35f, 0.85f), Vec3(1.35f, 0.75f, 0.9f), EmeraldIo, Vec3(0.55f, -0.2f, 0.45f), isEmphasized = "disk_io" in activeParts)
        )
        val buses = if (modeIndex == 0) {
            listOf(
                BusLink3D("hold_ack", Vec3(-1.25f, 0.95f, -0.2f), Vec3(-0.85f, 0.6f, 0.5f), BusType.CONTROL, "HOLD / HLDA", true),
                BusLink3D("dma_io", Vec3(1.12f, 0.4f, 0.85f), Vec3(0.38f, 0.45f, 0.75f), BusType.DATA, "Burst Read", true),
                BusLink3D("dma_ram", Vec3(0.38f, 0.65f, 0.45f), Vec3(1.12f, 1.0f, -0.25f), BusType.DATA, "Direct Write", true)
            )
        } else {
            listOf(
                BusLink3D("pio_poll", Vec3(1.12f, 0.45f, 0.65f), Vec3(-1.25f, 1.05f, -0.35f), BusType.ALERT, "CPU Busy Poll", true),
                BusLink3D("pio_write", Vec3(-1.25f, 1.15f, -0.55f), Vec3(1.12f, 1.15f, -0.55f), BusType.DATA, "Word-by-Word", true)
            )
        }
        return SceneAssembly3D(boxes, buses, defaultYaw = -32f, defaultPitch = 25f)
    }

    private fun buildFlynnsMulticore(
        activeParts: Set<String>,
        activeBuses: Set<String>,
        modeIndex: Int
    ): SceneAssembly3D {
        // 0 = SISD, 1 = SIMD, 2 = MIMD (Multi-Core)
        return when (modeIndex) {
            0 -> {
                val boxes = listOf(
                    Box3D("cu0", "1 Control Unit", "Single Inst Stream", Vec3(-1.5f, 0.9f, 0f), Vec3(1.1f, 0.9f, 1.0f), VioletControl, Vec3(-0.4f, 0.4f, 0f), isEmphasized = true),
                    Box3D("pe0", "1 Processor", "Single ALU", Vec3(0f, 0.9f, 0f), Vec3(1.05f, 0.9f, 1.0f), AmberAlu, Vec3(0f, 0.4f, 0f), isEmphasized = true),
                    Box3D("mem0", "1 Data Pool", "Single Data Stream", Vec3(1.5f, 0.9f, 0f), Vec3(1.1f, 0.9f, 1.0f), IndigoMemory, Vec3(0.4f, 0.4f, 0f), isEmphasized = true)
                )
                val buses = listOf(
                    BusLink3D("sisd_1", Vec3(-0.95f, 0.9f, 0f), Vec3(-0.52f, 0.9f, 0f), BusType.CONTROL, "1 Inst", true),
                    BusLink3D("sisd_2", Vec3(0.52f, 0.9f, 0f), Vec3(0.95f, 0.9f, 0f), BusType.DATA, "1 Data", true, bidirectional = true)
                )
                SceneAssembly3D(boxes, buses)
            }
            1 -> {
                val boxes = listOf(
                    Box3D("cu_master", "Single CU", "Broadcasts 1 Inst", Vec3(-2.0f, 0.9f, 0f), Vec3(1.1f, 1.3f, 1.2f), VioletControl, Vec3(-0.55f, 0.35f, 0f), isEmphasized = true),
                    Box3D("pe1", "ALU Lane 0", "Data[0]", Vec3(0f, 1.55f, -0.7f), Vec3(0.95f, 0.55f, 0.8f), AmberAlu, Vec3(0f, 0.65f, -0.35f), isEmphasized = true),
                    Box3D("pe2", "ALU Lane 1", "Data[1]", Vec3(0f, 0.85f, 0f), Vec3(0.95f, 0.55f, 0.8f), AmberAlu, Vec3(0f, 0.25f, 0f), isEmphasized = true),
                    Box3D("pe3", "ALU Lane 2", "Data[2]", Vec3(0f, 0.15f, 0.7f), Vec3(0.95f, 0.55f, 0.8f), AmberAlu, Vec3(0f, -0.25f, 0.35f), isEmphasized = true),
                    Box3D("vec_mem", "Vector Memory", "Parallel Data", Vec3(1.9f, 0.85f, 0f), Vec3(1.15f, 1.6f, 1.5f), IndigoMemory, Vec3(0.55f, 0.2f, 0f), isEmphasized = true)
                )
                val buses = listOf(
                    BusLink3D("simd_c1", Vec3(-1.45f, 1.2f, -0.2f), Vec3(-0.48f, 1.55f, -0.7f), BusType.CONTROL, "Broadcast", true),
                    BusLink3D("simd_c2", Vec3(-1.45f, 0.9f, 0f), Vec3(-0.48f, 0.85f, 0f), BusType.CONTROL, "Broadcast", true),
                    BusLink3D("simd_c3", Vec3(-1.45f, 0.6f, 0.2f), Vec3(-0.48f, 0.15f, 0.7f), BusType.CONTROL, "Broadcast", true),
                    BusLink3D("simd_d1", Vec3(0.48f, 1.55f, -0.7f), Vec3(1.32f, 1.35f, -0.4f), BusType.DATA, "D0", true),
                    BusLink3D("simd_d2", Vec3(0.48f, 0.85f, 0f), Vec3(1.32f, 0.85f, 0f), BusType.DATA, "D1", true),
                    BusLink3D("simd_d3", Vec3(0.48f, 0.15f, 0.7f), Vec3(1.32f, 0.35f, 0.4f), BusType.DATA, "D2", true)
                )
                SceneAssembly3D(boxes, buses)
            }
            else -> {
                val boxes = listOf(
                    Box3D("core0", "Core 0 (CU+ALU)", "Thread A", Vec3(-1.55f, 1.45f, -0.65f), Vec3(1.15f, 0.75f, 0.95f), CyanCpu, Vec3(-0.45f, 0.55f, -0.35f), isEmphasized = true),
                    Box3D("core1", "Core 1 (CU+ALU)", "Thread B", Vec3(1.55f, 1.45f, -0.65f), Vec3(1.15f, 0.75f, 0.95f), SkyRegister, Vec3(0.45f, 0.55f, -0.35f), isEmphasized = true),
                    Box3D("core2", "Core 2 (CU+ALU)", "Thread C", Vec3(-1.55f, 1.45f, 0.65f), Vec3(1.15f, 0.75f, 0.95f), TealCache, Vec3(-0.45f, 0.55f, 0.35f), isEmphasized = true),
                    Box3D("core3", "Core 3 (CU+ALU)", "Thread D", Vec3(1.55f, 1.45f, 0.65f), Vec3(1.15f, 0.75f, 0.95f), VioletControl, Vec3(0.45f, 0.55f, 0.35f), isEmphasized = true),
                    Box3D("shared_l3", "Shared L3 + Interconnect", "Coherent Cache", Vec3(0f, 0.35f, 0f), Vec3(3.2f, 0.55f, 2.0f), IndigoMemory, Vec3(0f, -0.45f, 0f), isEmphasized = true)
                )
                val buses = listOf(
                    BusLink3D("m0", Vec3(-1.55f, 1.08f, -0.65f), Vec3(-0.8f, 0.62f, -0.4f), BusType.DATA, "Coherence", true, bidirectional = true),
                    BusLink3D("m1", Vec3(1.55f, 1.08f, -0.65f), Vec3(0.8f, 0.62f, -0.4f), BusType.DATA, "Coherence", true, bidirectional = true),
                    BusLink3D("m2", Vec3(-1.55f, 1.08f, 0.65f), Vec3(-0.8f, 0.62f, 0.4f), BusType.CONTROL, "Sync", true, bidirectional = true),
                    BusLink3D("m3", Vec3(1.55f, 1.08f, 0.65f), Vec3(0.8f, 0.62f, 0.4f), BusType.CONTROL, "Sync", true, bidirectional = true)
                )
                SceneAssembly3D(boxes, buses, defaultYaw = -34f, defaultPitch = 26f)
            }
        }
    }
}
