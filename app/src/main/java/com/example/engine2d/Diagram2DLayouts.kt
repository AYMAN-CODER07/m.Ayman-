package com.example.engine2d

import androidx.compose.ui.graphics.Color
import com.example.domain.model.AppLanguage
import com.example.domain.model.SceneType3D
import com.example.domain.model.SimulationStep
import com.example.engine3d.BusType

data class DiagramBox2D(
    val id: String,
    val title: String,
    val subtitle: String = "",
    // Normalized 0f..1f coordinates inside canvas draw area
    val x: Float,
    val y: Float,
    val w: Float,
    val h: Float,
    val color: Color,
    val isActive: Boolean = false
)

data class DiagramArrow2D(
    val id: String,
    val fromX: Float,
    val fromY: Float,
    val toX: Float,
    val toY: Float,
    val label: String = "",
    val busType: BusType = BusType.DATA,
    val isActive: Boolean = false,
    val bidirectional: Boolean = false
)

data class DiagramScene2D(
    val title: String,
    val boxes: List<DiagramBox2D>,
    val arrows: List<DiagramArrow2D>,
    val isPipelineGantt: Boolean = false,
    val pipelineHazardBubble: Boolean = false,
    val isHierarchyPyramid: Boolean = false,
    val pyramidMissActive: Boolean = false,
    val isKMapGrid: Boolean = false,
    val kMapDontCareMode: Boolean = false
)

object Diagram2DPresets {

    private val CyanCpu = Color(0xFF0284C7)
    private val SkyReg = Color(0xFF0EA5E9)
    private val IndigoMem = Color(0xFF4F46E5)
    private val VioletCtrl = Color(0xFF7C3AED)
    private val AmberAlu = Color(0xFFD97706)
    private val EmeraldIo = Color(0xFF059669)
    private val TealCache = Color(0xFF0D9488)
    private val CoralAlert = Color(0xFFE11D48)

    fun buildDiagram(
        sceneType: SceneType3D,
        step: SimulationStep?,
        modeIndex: Int,
        language: AppLanguage
    ): DiagramScene2D {
        DigitalDiagram2DLayouts.buildDiagram(sceneType, step, modeIndex, language)?.let {
            return it
        }
        val activeParts = step?.activePartIds ?: emptySet()
        val activeBuses = step?.activeBusIds ?: emptySet()
        val hinglish = language != AppLanguage.ENGLISH

        return when (sceneType) {
            SceneType3D.VON_NEUMANN -> DiagramScene2D(
                title = if (hinglish) "Von Neumann Block Diagram (Shared Memory + 3 Buses)" else "Von Neumann Architecture Diagram",
                boxes = listOf(
                    DiagramBox2D("cu", "Control Unit (CU)", if (hinglish) "Timing & Signals" else "Control Signals", 0.06f, 0.14f, 0.28f, 0.24f, VioletCtrl, "cu" in activeParts),
                    DiagramBox2D("alu", "ALU", if (hinglish) "Math & Logic" else "Arithmetic/Logic", 0.06f, 0.44f, 0.28f, 0.22f, AmberAlu, "alu" in activeParts),
                    DiagramBox2D("regs", "Registers", "PC | MAR | MDR | IR", 0.06f, 0.72f, 0.28f, 0.20f, SkyReg, "regs" in activeParts),
                    DiagramBox2D("memory", "Main RAM", if (hinglish) "Code + Data Ek Saath" else "Unified Code + Data", 0.64f, 0.16f, 0.30f, 0.36f, IndigoMem, "memory" in activeParts),
                    DiagramBox2D("io", "I/O Interface", if (hinglish) "Keyboard / Display" else "Input / Output", 0.64f, 0.60f, 0.30f, 0.30f, EmeraldIo, "io" in activeParts)
                ),
                arrows = listOf(
                    DiagramArrow2D("addr_bus", 0.34f, 0.24f, 0.64f, 0.24f, "Address Bus", BusType.ADDRESS, "addr_bus" in activeBuses),
                    DiagramArrow2D("data_bus", 0.34f, 0.44f, 0.64f, 0.44f, "Data Bus", BusType.DATA, "data_bus" in activeBuses, bidirectional = true),
                    DiagramArrow2D("ctrl_bus", 0.34f, 0.34f, 0.64f, 0.66f, "Control Bus", BusType.CONTROL, "ctrl_bus" in activeBuses),
                    DiagramArrow2D("io_bus", 0.34f, 0.80f, 0.64f, 0.78f, "I/O Data", BusType.DATA, "io_bus" in activeBuses, bidirectional = true)
                )
            )

            SceneType3D.HARVARD_VS_VON_NEUMANN -> if (modeIndex == 0) {
                DiagramScene2D(
                    title = if (hinglish) "Harvard: Alag Instruction aur Data Bus (Parallel)" else "Harvard Architecture (Dual Independent Buses)",
                    boxes = listOf(
                        DiagramBox2D("inst_mem", "Instruction Mem", "Program Code", 0.04f, 0.26f, 0.26f, 0.48f, AmberAlu, "inst_mem" in activeParts),
                        DiagramBox2D("cpu_core", "Harvard CPU", "Fetch || Execute", 0.37f, 0.22f, 0.26f, 0.56f, CyanCpu, "cpu_core" in activeParts),
                        DiagramBox2D("data_mem", "Data Memory", "Variables / RAM", 0.70f, 0.26f, 0.26f, 0.48f, IndigoMem, "data_mem" in activeParts)
                    ),
                    arrows = listOf(
                        DiagramArrow2D("inst_bus", 0.30f, 0.44f, 0.37f, 0.44f, "Inst", BusType.ADDRESS, true),
                        DiagramArrow2D("data_bus", 0.63f, 0.44f, 0.70f, 0.44f, "Data", BusType.DATA, true, bidirectional = true),
                        DiagramArrow2D("ctrl_bus", 0.37f, 0.66f, 0.70f, 0.66f, "Parallel Control", BusType.CONTROL, true)
                    )
                )
            } else {
                DiagramScene2D(
                    title = if (hinglish) "Von Neumann: Single Shared Bus (Bottleneck!)" else "Von Neumann (Single Bus Contention)",
                    boxes = listOf(
                        DiagramBox2D("cpu_core", "CPU Core", "Single Port", 0.06f, 0.26f, 0.28f, 0.48f, CyanCpu, true),
                        DiagramBox2D("shared_bus", "Shared Bus", if (hinglish) "Traffic Jam!" else "Bottleneck", 0.42f, 0.36f, 0.16f, 0.28f, CoralAlert, true),
                        DiagramBox2D("inst_mem", "Unified RAM", "Code + Data", 0.66f, 0.26f, 0.28f, 0.48f, IndigoMem, true)
                    ),
                    arrows = listOf(
                        DiagramArrow2D("b1", 0.34f, 0.50f, 0.42f, 0.50f, "1 at a time", BusType.ALERT, true, bidirectional = true),
                        DiagramArrow2D("b2", 0.58f, 0.50f, 0.66f, 0.50f, "Stall", BusType.ALERT, true, bidirectional = true)
                    )
                )
            }

            SceneType3D.INSTRUCTION_CYCLE -> DiagramScene2D(
                title = if (hinglish) "Fetch → Decode → Execute Cycle Flow" else "Instruction Cycle Register Flow",
                boxes = listOf(
                    DiagramBox2D("pc", "PC", "0x200 -> +1", 0.05f, 0.14f, 0.22f, 0.22f, SkyReg, "pc" in activeParts),
                    DiagramBox2D("mar", "MAR", "Addr Latch", 0.38f, 0.14f, 0.22f, 0.22f, AmberAlu, "mar" in activeParts),
                    DiagramBox2D("ram", "Main RAM", "M[MAR]", 0.71f, 0.22f, 0.24f, 0.54f, IndigoMem, "ram" in activeParts),
                    DiagramBox2D("mdr", "MDR", "Data Buffer", 0.38f, 0.54f, 0.22f, 0.22f, TealCache, "mdr" in activeParts),
                    DiagramBox2D("ir", "IR", "Opcode Hold", 0.05f, 0.54f, 0.22f, 0.22f, VioletCtrl, "ir" in activeParts),
                    DiagramBox2D("cu_alu", "CU + ALU", if (hinglish) "Decode & Run" else "Decode & Exec", 0.05f, 0.80f, 0.55f, 0.16f, CyanCpu, "cu_alu" in activeParts)
                ),
                arrows = listOf(
                    DiagramArrow2D("pc_mar", 0.27f, 0.25f, 0.38f, 0.25f, "MAR<-PC", BusType.ADDRESS, "pc_mar" in activeBuses),
                    DiagramArrow2D("mar_ram", 0.60f, 0.28f, 0.71f, 0.32f, "Addr", BusType.ADDRESS, "mar_ram" in activeBuses),
                    DiagramArrow2D("ram_mdr", 0.71f, 0.64f, 0.60f, 0.64f, "M[MAR]", BusType.DATA, "ram_mdr" in activeBuses),
                    DiagramArrow2D("mdr_ir", 0.38f, 0.65f, 0.27f, 0.65f, "IR<-MDR", BusType.DATA, "mdr_ir" in activeBuses),
                    DiagramArrow2D("ir_cu", 0.16f, 0.76f, 0.16f, 0.80f, "Decode", BusType.CONTROL, "ir_cu" in activeBuses)
                )
            )

            SceneType3D.ALU_MICRO_OPS -> {
                val opName = when (modeIndex) {
                    1 -> "XOR"
                    2 -> "SHL"
                    else -> "ADD"
                }
                DiagramScene2D(
                    title = if (hinglish) "ALU Internal Block Diagram (Active: $opName)" else "ALU Schematic (Selected: $opName)",
                    boxes = listOf(
                        DiagramBox2D("reg_a", "Reg A", "0101 (5)", 0.12f, 0.10f, 0.26f, 0.16f, SkyReg, "reg_a" in activeParts),
                        DiagramBox2D("reg_b", "Reg B", "0011 (3)", 0.62f, 0.10f, 0.26f, 0.16f, SkyReg, "reg_b" in activeParts),
                        DiagramBox2D("arith_unit", "Adder", "A + B + Cin", 0.05f, 0.38f, 0.26f, 0.22f, AmberAlu, modeIndex == 0 || "arith_unit" in activeParts),
                        DiagramBox2D("logic_unit", "Logic Gates", "AND / OR / XOR", 0.37f, 0.38f, 0.26f, 0.22f, VioletCtrl, modeIndex == 1 || "logic_unit" in activeParts),
                        DiagramBox2D("shifter", "Shifter", "SHL / SHR", 0.69f, 0.38f, 0.26f, 0.22f, TealCache, modeIndex == 2 || "shifter" in activeParts),
                        DiagramBox2D("mux", "4:1 Output MUX", "Select: $opName", 0.18f, 0.73f, 0.44f, 0.18f, CyanCpu, "mux" in activeParts),
                        DiagramBox2D("flags", "Flags", "Z | C | N | V", 0.70f, 0.73f, 0.24f, 0.18f, CoralAlert, "flags" in activeParts)
                    ),
                    arrows = listOf(
                        DiagramArrow2D("op_a", 0.25f, 0.26f, 0.18f, 0.38f, "Bus A", BusType.DATA, true),
                        DiagramArrow2D("op_b", 0.75f, 0.26f, 0.82f, 0.38f, "Bus B", BusType.DATA, true),
                        DiagramArrow2D("alu_mux", when (modeIndex) { 1 -> 0.50f; 2 -> 0.82f; else -> 0.18f }, 0.60f, 0.40f, 0.73f, opName, BusType.CONTROL, true),
                        DiagramArrow2D("flag_bus", 0.62f, 0.82f, 0.70f, 0.82f, "PSW", BusType.ALERT, "flag_bus" in activeBuses)
                    )
                )
            }

            SceneType3D.ADDRESSING_MODES -> DiagramScene2D(
                title = when (modeIndex) {
                    0 -> if (hinglish) "Immediate: Value seedha Instruction mein (0 RAM Read)" else "Immediate Mode: Operand Inside IR"
                    1 -> if (hinglish) "Direct: IR mein seedha Target Address (1 RAM Read)" else "Direct Mode: EA = IR[Addr]"
                    2 -> if (hinglish) "Indirect: Double Hop Pointer M[M[Addr]] (2 RAM Reads)" else "Indirect Mode: EA = M[Addr]"
                    else -> if (hinglish) "Indexed: EA = Addr + Index Register IX (Arrays)" else "Indexed Mode: EA = Addr + IX"
                },
                boxes = listOf(
                    DiagramBox2D("ir_field", "IR [Op | Addr]", if (modeIndex == 0) "Value = #25" else "Addr = 100", 0.05f, 0.18f, 0.28f, 0.26f, VioletCtrl, true),
                    DiagramBox2D("reg_file", "Index Reg (IX)", "IX = 04", 0.05f, 0.58f, 0.28f, 0.24f, SkyReg, modeIndex == 3),
                    DiagramBox2D("mem_ptr", "RAM [100]", "Ptr = 500", 0.40f, 0.58f, 0.24f, 0.24f, AmberAlu, modeIndex == 2),
                    DiagramBox2D("mem_data", "Operand Data", if (modeIndex == 0) "Used Directly" else "Final Value", 0.70f, 0.24f, 0.25f, 0.46f, EmeraldIo, true)
                ),
                arrows = when (modeIndex) {
                    0 -> listOf(DiagramArrow2D("a1", 0.33f, 0.31f, 0.70f, 0.38f, "0 Mem Reads!", BusType.DATA, true))
                    1 -> listOf(DiagramArrow2D("a1", 0.33f, 0.31f, 0.70f, 0.34f, "EA = 100 (1 Read)", BusType.ADDRESS, true))
                    2 -> listOf(
                        DiagramArrow2D("a1", 0.33f, 0.35f, 0.46f, 0.58f, "Hop 1: 100", BusType.ADDRESS, true),
                        DiagramArrow2D("a2", 0.64f, 0.68f, 0.70f, 0.52f, "Hop 2: 500", BusType.DATA, true)
                    )
                    else -> listOf(
                        DiagramArrow2D("a1", 0.19f, 0.44f, 0.19f, 0.58f, "100 + IX", BusType.CONTROL, true),
                        DiagramArrow2D("a2", 0.33f, 0.68f, 0.70f, 0.48f, "EA = 104", BusType.ADDRESS, true)
                    )
                }
            )

            SceneType3D.CONTROL_UNIT -> if (modeIndex == 0) {
                DiagramScene2D(
                    title = if (hinglish) "Hardwired CU: Fast Combinational Gates (RISC)" else "Hardwired Control Unit",
                    boxes = listOf(
                        DiagramBox2D("ir_op", "IR Opcode", "Instruction", 0.05f, 0.16f, 0.24f, 0.24f, VioletCtrl, true),
                        DiagramBox2D("ring_cnt", "Step Counter", "T0..Tn", 0.05f, 0.54f, 0.24f, 0.24f, SkyReg, true),
                        DiagramBox2D("gate_matrix", "PLA Gate Matrix", "Hardwired Logic", 0.38f, 0.26f, 0.26f, 0.44f, AmberAlu, true),
                        DiagramBox2D("ctrl_signals", "Control Signals", "C0..Ck Out", 0.72f, 0.26f, 0.23f, 0.44f, EmeraldIo, true)
                    ),
                    arrows = listOf(
                        DiagramArrow2D("b1", 0.29f, 0.28f, 0.38f, 0.38f, "Opcode", BusType.ADDRESS, true),
                        DiagramArrow2D("b2", 0.29f, 0.64f, 0.38f, 0.56f, "Clock", BusType.CONTROL, true),
                        DiagramArrow2D("b3", 0.64f, 0.48f, 0.72f, 0.48f, "Fast Out", BusType.CONTROL, true)
                    )
                )
            } else {
                DiagramScene2D(
                    title = if (hinglish) "Microprogrammed CU: Control Memory ROM (CISC)" else "Microprogrammed Control Unit",
                    boxes = listOf(
                        DiagramBox2D("ir_op", "Sequencer", "Next Addr", 0.04f, 0.28f, 0.20f, 0.38f, VioletCtrl, true),
                        DiagramBox2D("cmar", "CMAR", "ROM Addr", 0.28f, 0.28f, 0.18f, 0.38f, SkyReg, true),
                        DiagramBox2D("ctrl_mem", "Control Memory", "Microcode ROM", 0.50f, 0.22f, 0.24f, 0.50f, TealCache, true),
                        DiagramBox2D("ctrl_signals", "CMDR / CW", "Control Word", 0.78f, 0.28f, 0.18f, 0.38f, EmeraldIo, true)
                    ),
                    arrows = listOf(
                        DiagramArrow2D("b1", 0.24f, 0.47f, 0.28f, 0.47f, "", BusType.ADDRESS, true),
                        DiagramArrow2D("b2", 0.46f, 0.47f, 0.50f, 0.47f, "", BusType.ADDRESS, true),
                        DiagramArrow2D("b3", 0.74f, 0.47f, 0.78f, 0.47f, "Micro-op", BusType.CONTROL, true)
                    )
                )
            }

            SceneType3D.PIPELINE_5_STAGE -> DiagramScene2D(
                title = if (modeIndex == 0) {
                    if (hinglish) "5-Stage Pipeline Gantt Chart (Forwarding ON — Zero Stall)" else "5-Stage Pipeline Timing (Forwarding)"
                } else {
                    if (hinglish) "RAW Data Hazard: EX Stage mein Red STALL Bubble!" else "RAW Data Hazard (1-Cycle Stall Bubble)"
                },
                boxes = emptyList(),
                arrows = emptyList(),
                isPipelineGantt = true,
                pipelineHazardBubble = modeIndex == 1
            )

            SceneType3D.MEMORY_HIERARCHY -> DiagramScene2D(
                title = if (hinglish) "Memory Hierarchy Pyramid (Upar Tez & Mehnga, Neeche Bada)" else "Memory Hierarchy Pyramid",
                boxes = listOf(
                    DiagramBox2D("regs", "Registers", "~0.3 ns | Bytes", 0.36f, 0.10f, 0.28f, 0.12f, AmberAlu, "regs" in activeParts),
                    DiagramBox2D("l1_cache", "L1 / L2 Cache", "1–4 ns | KB–MB", 0.29f, 0.26f, 0.42f, 0.13f, TealCache, "l1_cache" in activeParts),
                    DiagramBox2D("l3_cache", "L3 Shared Cache", "~12 ns | MBs", 0.22f, 0.43f, 0.56f, 0.13f, CyanCpu, "l3_cache" in activeParts),
                    DiagramBox2D("ram", "Main RAM (DRAM)", "~60 ns | GBs", 0.15f, 0.60f, 0.70f, 0.14f, IndigoMem, "ram" in activeParts),
                    DiagramBox2D("disk", "SSD / Secondary", "ms | TBs", 0.08f, 0.78f, 0.84f, 0.14f, Color(0xFF475569), "disk" in activeParts)
                ),
                arrows = emptyList(),
                isHierarchyPyramid = true,
                pyramidMissActive = "miss_path" in activeBuses
            )

            SceneType3D.CACHE_MAPPING -> DiagramScene2D(
                title = when (modeIndex) {
                    0 -> if (hinglish) "Direct Mapping: Block j -> Line (j mod N) [Conflict Miss!]" else "Direct Cache Mapping"
                    1 -> if (hinglish) "Fully Associative: Koi bhi Block kisi bhi Line mein" else "Fully Associative Cache Mapping"
                    else -> if (hinglish) "2-Way Set Associative: Har Set mein 2 Lines" else "2-Way Set-Associative Mapping"
                },
                boxes = listOf(
                    DiagramBox2D("cpu_addr", "Address Bits", when (modeIndex) { 1 -> "Tag | Offset"; 2 -> "Tag | Set | Off"; else -> "Tag | Line | Off" }, 0.04f, 0.12f, 0.92f, 0.16f, SkyReg, true),
                    DiagramBox2D("cache_b0", "Cache Line 0", "Tag | Valid | Data", 0.06f, 0.38f, 0.36f, 0.22f, TealCache, true),
                    DiagramBox2D("cache_b1", "Cache Line 1", "Tag | Valid | Data", 0.06f, 0.66f, 0.36f, 0.22f, TealCache, modeIndex >= 1),
                    DiagramBox2D("ram_blk0", "RAM Block 0", "0 mod 4 = 0", 0.58f, 0.38f, 0.36f, 0.22f, IndigoMem, true),
                    DiagramBox2D("ram_blk4", "RAM Block 4", "4 mod 4 = 0", 0.58f, 0.66f, 0.36f, 0.22f, AmberAlu, true)
                ),
                arrows = when (modeIndex) {
                    0 -> listOf(
                        DiagramArrow2D("m1", 0.58f, 0.49f, 0.42f, 0.49f, "Maps Line 0", BusType.DATA, true),
                        DiagramArrow2D("m2", 0.58f, 0.77f, 0.42f, 0.54f, "Conflict!", BusType.ALERT, true)
                    )
                    1 -> listOf(
                        DiagramArrow2D("m1", 0.58f, 0.49f, 0.42f, 0.49f, "Any Free Line", BusType.CONTROL, true),
                        DiagramArrow2D("m2", 0.58f, 0.77f, 0.42f, 0.77f, "No Conflict", BusType.CONTROL, true)
                    )
                    else -> listOf(
                        DiagramArrow2D("m1", 0.58f, 0.49f, 0.42f, 0.49f, "Set 0 Way 0", BusType.DATA, true),
                        DiagramArrow2D("m2", 0.58f, 0.77f, 0.42f, 0.77f, "Set 0 Way 1", BusType.DATA, true)
                    )
                }
            )

            SceneType3D.VIRTUAL_MEMORY_TLB -> DiagramScene2D(
                title = if (modeIndex == 0) {
                    if (hinglish) "TLB Hit: Page# seedha Frame# mein convert (Fast!)" else "Virtual Memory: TLB Hit Fast Path"
                } else {
                    if (hinglish) "Page Fault: Page RAM mein nahi -> Swap Disk se Load!" else "Virtual Memory: Page Fault Trap"
                },
                boxes = listOf(
                    DiagramBox2D("cpu_va", "Virtual Addr", "VPN | Offset", 0.04f, 0.34f, 0.22f, 0.32f, SkyReg, true),
                    DiagramBox2D("tlb", "TLB Cache", "Fast Lookup", 0.34f, 0.14f, 0.26f, 0.26f, TealCache, modeIndex == 0),
                    DiagramBox2D("page_table", "Page Table", "Valid Bit = ${if (modeIndex == 0) 1 else 0}", 0.34f, 0.56f, 0.26f, 0.26f, VioletCtrl, modeIndex == 1),
                    DiagramBox2D("phys_ram", "Physical RAM", "Frame # + Off", 0.70f, 0.14f, 0.26f, 0.32f, IndigoMem, true),
                    DiagramBox2D("swap_disk", "Swap Disk", "Backing Store", 0.70f, 0.58f, 0.26f, 0.26f, CoralAlert, modeIndex == 1)
                ),
                arrows = if (modeIndex == 0) {
                    listOf(
                        DiagramArrow2D("v1", 0.26f, 0.44f, 0.34f, 0.27f, "Check TLB", BusType.ADDRESS, true),
                        DiagramArrow2D("v2", 0.60f, 0.27f, 0.70f, 0.27f, "TLB Hit!", BusType.CONTROL, true)
                    )
                } else {
                    listOf(
                        DiagramArrow2D("v1", 0.26f, 0.56f, 0.34f, 0.68f, "Miss -> PT", BusType.ADDRESS, true),
                        DiagramArrow2D("v2", 0.60f, 0.70f, 0.70f, 0.70f, "Fault!", BusType.ALERT, true),
                        DiagramArrow2D("v3", 0.83f, 0.58f, 0.83f, 0.46f, "Load Page", BusType.DATA, true)
                    )
                }
            )

            SceneType3D.IO_DMA_TRANSFER -> if (modeIndex == 0) {
                DiagramScene2D(
                    title = if (hinglish) "DMA Transfer: HOLD/HLDA se Bus lekar Seedha I/O -> RAM" else "DMA Block Transfer Diagram",
                    boxes = listOf(
                        DiagramBox2D("cpu", "CPU Core", if (hinglish) "Free (HLDA=1)" else "Bus Released", 0.06f, 0.14f, 0.28f, 0.28f, CyanCpu, true),
                        DiagramBox2D("dma", "DMA Controller", "Bus Master", 0.06f, 0.58f, 0.28f, 0.28f, AmberAlu, true),
                        DiagramBox2D("ram", "Main RAM", "Buffer", 0.64f, 0.14f, 0.30f, 0.28f, IndigoMem, true),
                        DiagramBox2D("disk_io", "Disk / NIC I/O", "DREQ Burst", 0.64f, 0.58f, 0.30f, 0.28f, EmeraldIo, true)
                    ),
                    arrows = listOf(
                        DiagramArrow2D("h", 0.20f, 0.42f, 0.20f, 0.58f, "HOLD/HLDA", BusType.CONTROL, true, bidirectional = true),
                        DiagramArrow2D("d1", 0.64f, 0.72f, 0.34f, 0.72f, "Read I/O", BusType.DATA, true),
                        DiagramArrow2D("d2", 0.34f, 0.64f, 0.64f, 0.30f, "Direct RAM Write", BusType.DATA, true)
                    )
                )
            } else {
                DiagramScene2D(
                    title = if (hinglish) "Programmed I/O: CPU Polling mein Busy (Slow!)" else "Programmed I/O Polling Diagram",
                    boxes = listOf(
                        DiagramBox2D("cpu", "CPU Core", if (hinglish) "Busy Polling!" else "Busy Waiting", 0.06f, 0.26f, 0.30f, 0.44f, CoralAlert, true),
                        DiagramBox2D("ram", "Main RAM", "Slow Copy", 0.64f, 0.14f, 0.30f, 0.28f, IndigoMem, true),
                        DiagramBox2D("disk_io", "I/O Device", "Ready Bit?", 0.64f, 0.56f, 0.30f, 0.28f, EmeraldIo, true)
                    ),
                    arrows = listOf(
                        DiagramArrow2D("p1", 0.36f, 0.64f, 0.64f, 0.68f, "Poll Ready?", BusType.ALERT, true, bidirectional = true),
                        DiagramArrow2D("p2", 0.36f, 0.34f, 0.64f, 0.28f, "1 Word/Time", BusType.DATA, true)
                    )
                )
            }

            SceneType3D.FLYNNS_MULTICORE -> when (modeIndex) {
                0 -> DiagramScene2D(
                    title = if (hinglish) "SISD: 1 Control Unit + 1 ALU + 1 Data Stream" else "Flynn's SISD Architecture",
                    boxes = listOf(
                        DiagramBox2D("cu_master", "1 Control Unit", "1 Inst Stream", 0.05f, 0.34f, 0.24f, 0.32f, VioletCtrl, true),
                        DiagramBox2D("pe1", "1 ALU Core", "Scalar Ops", 0.38f, 0.34f, 0.24f, 0.32f, AmberAlu, true),
                        DiagramBox2D("shared_l3", "1 Memory", "1 Data Stream", 0.71f, 0.34f, 0.24f, 0.32f, IndigoMem, true)
                    ),
                    arrows = listOf(
                        DiagramArrow2D("s1", 0.29f, 0.50f, 0.38f, 0.50f, "Inst", BusType.CONTROL, true),
                        DiagramArrow2D("s2", 0.62f, 0.50f, 0.71f, 0.50f, "Data", BusType.DATA, true, bidirectional = true)
                    )
                )
                1 -> DiagramScene2D(
                    title = if (hinglish) "SIMD (GPU): 1 CU Broadcasts Same Inst to Many ALUs" else "Flynn's SIMD Vector Architecture",
                    boxes = listOf(
                        DiagramBox2D("cu_master", "Single CU", "1 Inst Broadcast", 0.04f, 0.22f, 0.24f, 0.56f, VioletCtrl, true),
                        DiagramBox2D("pe1", "ALU Lane 0", "D[0]", 0.38f, 0.14f, 0.22f, 0.18f, AmberAlu, true),
                        DiagramBox2D("pe2", "ALU Lane 1", "D[1]", 0.38f, 0.41f, 0.22f, 0.18f, AmberAlu, true),
                        DiagramBox2D("pe3", "ALU Lane 2", "D[2]", 0.38f, 0.68f, 0.22f, 0.18f, AmberAlu, true),
                        DiagramBox2D("shared_l3", "Vector RAM", "Parallel Array", 0.72f, 0.20f, 0.24f, 0.60f, IndigoMem, true)
                    ),
                    arrows = listOf(
                        DiagramArrow2D("c1", 0.28f, 0.34f, 0.38f, 0.23f, "", BusType.CONTROL, true),
                        DiagramArrow2D("c2", 0.28f, 0.50f, 0.38f, 0.50f, "Same Inst", BusType.CONTROL, true),
                        DiagramArrow2D("c3", 0.28f, 0.66f, 0.38f, 0.77f, "", BusType.CONTROL, true),
                        DiagramArrow2D("d1", 0.60f, 0.23f, 0.72f, 0.30f, "D0", BusType.DATA, true),
                        DiagramArrow2D("d2", 0.60f, 0.50f, 0.72f, 0.50f, "D1", BusType.DATA, true),
                        DiagramArrow2D("d3", 0.60f, 0.77f, 0.72f, 0.70f, "D2", BusType.DATA, true)
                    )
                )
                else -> DiagramScene2D(
                    title = if (hinglish) "MIMD Multicore: 4 Autonomous Cores + Shared Coherent L3" else "Flynn's MIMD Multicore Architecture",
                    boxes = listOf(
                        DiagramBox2D("core0", "Core 0", "CU + ALU + L1", 0.06f, 0.16f, 0.20f, 0.26f, CyanCpu, true),
                        DiagramBox2D("core1", "Core 1", "CU + ALU + L1", 0.29f, 0.16f, 0.20f, 0.26f, SkyReg, true),
                        DiagramBox2D("core2", "Core 2", "CU + ALU + L1", 0.52f, 0.16f, 0.20f, 0.26f, TealCache, true),
                        DiagramBox2D("core3", "Core 3", "CU + ALU + L1", 0.75f, 0.16f, 0.20f, 0.26f, VioletCtrl, true),
                        DiagramBox2D("shared_l3", "Shared L3 Cache & Coherent Bus (MESI)", "All Cores Synced", 0.08f, 0.62f, 0.84f, 0.24f, IndigoMem, true)
                    ),
                    arrows = listOf(
                        DiagramArrow2D("m0", 0.16f, 0.42f, 0.16f, 0.62f, "", BusType.DATA, true, bidirectional = true),
                        DiagramArrow2D("m1", 0.39f, 0.42f, 0.39f, 0.62f, "MESI", BusType.DATA, true, bidirectional = true),
                        DiagramArrow2D("m2", 0.62f, 0.42f, 0.62f, 0.62f, "Sync", BusType.CONTROL, true, bidirectional = true),
                        DiagramArrow2D("m3", 0.85f, 0.42f, 0.85f, 0.62f, "", BusType.CONTROL, true, bidirectional = true)
                    )
                )
            }
            else -> DiagramScene2D(
                title = "System Architecture",
                boxes = emptyList(),
                arrows = emptyList()
            )
        }
    }
}
