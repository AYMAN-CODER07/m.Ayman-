package com.example.data

import com.example.domain.model.CbcsUnit
import com.example.domain.model.ConceptTopic
import com.example.domain.model.HardwarePartInfo
import com.example.domain.model.SceneType3D
import com.example.domain.model.SimulationStep
import com.example.domain.model.VivaQuestion

object CbcsCurriculumRepository {

    val units: List<CbcsUnit> = listOf(
        CbcsUnit(
            id = "unit0",
            numberRoman = "Digital Logic",
            title = "Digital Systems & Logic Design",
            subtitle = "Number Systems, Binary Codes, Logic Gates, K-Map, MUX/DEMUX & Flip-Flops",
            creditsHours = "14 Hours • Foundation"
        ),
        CbcsUnit(
            id = "unit1",
            numberRoman = "Unit I",
            title = "Basic Computer Organization",
            subtitle = "Functional Units, System Buses, Stored Program & Instruction Cycle",
            creditsHours = "12 Hours • 20% Weight"
        ),
        CbcsUnit(
            id = "unit2",
            numberRoman = "Unit II",
            title = "CPU, ALU & Control Unit",
            subtitle = "RTL Micro-operations, Addressing Modes & Control Design",
            creditsHours = "14 Hours • 25% Weight"
        ),
        CbcsUnit(
            id = "unit3",
            numberRoman = "Unit III",
            title = "Pipelining & Parallelism",
            subtitle = "5-Stage RISC Pipeline, Speedup & Hazard Resolution",
            creditsHours = "10 Hours • 18% Weight"
        ),
        CbcsUnit(
            id = "unit4",
            numberRoman = "Unit IV",
            title = "Memory & Cache Systems",
            subtitle = "Memory Hierarchy, Cache Mapping, Virtual Memory & TLB",
            creditsHours = "14 Hours • 22% Weight"
        ),
        CbcsUnit(
            id = "unit5",
            numberRoman = "Unit V",
            title = "I/O & Multiprocessors",
            subtitle = "Programmed I/O, Interrupts, DMA & Flynn's Taxonomy",
            creditsHours = "10 Hours • 15% Weight"
        )
    )

    val topics: List<ConceptTopic> = CbcsDigitalLogicTopics.topics + listOf(
        ConceptTopic(
            id = "von_neumann",
            unitId = "unit1",
            title = "Von Neumann & System Bus",
            subtitle = "Stored-program architecture with unified memory & tri-bus",
            examWeightage = "High Yield",
            difficulty = "Foundation",
            sceneType = SceneType3D.VON_NEUMANN,
            summary = "Proposed by John von Neumann (1945), both program instructions and data reside in the same shared Main Memory. The CPU communicates with Memory and I/O via three system buses: Address Bus (unidirectional), Data Bus (bidirectional), and Control Bus.",
            keyPoints = listOf(
                "Stored-Program Concept: Instructions are encoded as binary numbers and stored alongside data in RAM.",
                "Address Bus carries memory/I/O addresses from CPU to Memory (n lines address 2^n locations).",
                "Data Bus is bidirectional and determines the word size of the processor (e.g., 32-bit or 64-bit).",
                "Control Bus synchronizes operations using Read/Write, Clock, Interrupt, and Bus Grant signals."
            ),
            rtlOrFormulas = listOf(
                "Addressable Space = 2^k words (k = Address Bus width)",
                "Bus Bandwidth = Bus Width (Bytes) × Bus Clock Frequency",
                "Bottleneck: Single shared bus limits simultaneous code + data fetch"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("cu", "Control Unit (CU)", "CU", "Generates timing & control signals to orchestrate all buses and ALU operations."),
                HardwarePartInfo("alu", "Arithmetic Logic Unit", "ALU", "Performs binary addition, subtraction, bitwise logic, and shifting."),
                HardwarePartInfo("regs", "Register Array", "REG", "Ultra-fast flip-flop storage inside CPU (PC, IR, MAR, MDR, AC)."),
                HardwarePartInfo("memory", "Main Memory (RAM)", "MEM", "Stores both machine instructions and operands in numbered cells."),
                HardwarePartInfo("io", "I/O Interface", "I/O", "Bridges external peripherals (keyboard, disk, display) to the system bus.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "1. Address Placement",
                    rtlAction = "Address Bus ← PC",
                    description = "The Control Unit drives the target memory location onto the unidirectional Address Bus.",
                    activePartIds = setOf("cu", "regs", "memory"),
                    activeBusIds = setOf("addr_bus"),
                    registerSnapshot = mapOf("PC" to "0x1004", "BUS" to "ADDR_OUT")
                ),
                SimulationStep(
                    title = "2. Memory Read Control",
                    rtlAction = "Control Bus ← MEM_READ",
                    description = "The Control Unit asserts the MEM_READ signal on the Control Bus so RAM prepares the word.",
                    activePartIds = setOf("cu", "memory"),
                    activeBusIds = setOf("ctrl_bus"),
                    registerSnapshot = mapOf("CTRL" to "RD=1", "MEM" to "READY")
                ),
                SimulationStep(
                    title = "3. Data Transfer to CPU",
                    rtlAction = "Regs ← Data Bus ← M[0x1004]",
                    description = "The instruction or operand travels across the bidirectional Data Bus into CPU registers.",
                    activePartIds = setOf("regs", "memory", "alu"),
                    activeBusIds = setOf("data_bus"),
                    registerSnapshot = mapOf("DATA" to "ADD R1,R2", "ALU" to "ARMED")
                ),
                SimulationStep(
                    title = "4. ALU Execution & I/O",
                    rtlAction = "AC ← AC + R1; I/O ← Out",
                    description = "ALU computes the result and stores it in Accumulator or sends output via I/O Interface.",
                    activePartIds = setOf("alu", "io", "cu"),
                    activeBusIds = setOf("io_bus", "ctrl_bus"),
                    registerSnapshot = mapOf("AC" to "0x002A", "I/O" to "UPDATED")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("Why is the Address Bus unidirectional?", "Only the CPU (or a DMA bus master) specifies which memory address to access; memory never addresses the CPU."),
                VivaQuestion("What is the Von Neumann bottleneck?", "Because instructions and data share a single memory bus, the CPU cannot fetch an instruction and read/write data simultaneously.")
            )
        ),
        ConceptTopic(
            id = "harvard_vs_von",
            unitId = "unit1",
            title = "Harvard vs Von Neumann",
            subtitle = "Dual separated buses vs unified single memory bottleneck",
            examWeightage = "Frequent 5-Mark",
            difficulty = "Foundation",
            sceneType = SceneType3D.HARVARD_VS_VON_NEUMANN,
            interactiveModes = listOf("Harvard (Dual Bus)", "Von Neumann (Shared)"),
            summary = "Harvard Architecture physically separates Instruction Memory and Data Memory with independent buses. This allows fetching the next instruction while reading/writing data in the same clock cycle—ideal for DSPs, microcontrollers, and modern L1 split caches.",
            keyPoints = listOf(
                "Harvard uses separate Instruction Bus and Data Bus -> parallel access and higher throughput.",
                "Instruction memory can have a different word width (e.g., 14-bit PIC instructions) than Data memory (8-bit).",
                "Von Neumann is simpler and cheaper in hardware, allowing flexible dynamic memory allocation between code and data.",
                "Modern CPUs use Modified Harvard: unified L2/L3/RAM (Von Neumann) + split L1 I-Cache & D-Cache (Harvard)."
            ),
            rtlOrFormulas = listOf(
                "Harvard: I-Fetch || Data-Access (1 cycle)",
                "Von Neumann: I-Fetch then Data-Access (2+ cycles)",
                "Switch modes above in 3D to compare bus paths!"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("inst_mem", "Instruction Memory", "I-MEM", "Dedicated read-only/code memory storing program opcodes."),
                HardwarePartInfo("cpu_core", "Processor Core", "CPU", "Contains independent Instruction Fetch and Data Load/Store ports."),
                HardwarePartInfo("data_mem", "Data Memory", "D-MEM", "Dedicated RAM holding variables, arrays, and stack frames.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "1. Parallel Instruction & Data Access",
                    rtlAction = "IR ← I-Mem[PC]  ||  R1 ← D-Mem[Addr]",
                    description = "In Harvard mode, both buses fire concurrently without contending for the same wires.",
                    activePartIds = setOf("inst_mem", "cpu_core", "data_mem"),
                    activeBusIds = setOf("inst_bus", "data_bus", "ctrl_bus"),
                    registerSnapshot = mapOf("I-BUS" to "ACTIVE", "D-BUS" to "ACTIVE")
                ),
                SimulationStep(
                    title = "2. Shared Bus Contention (Toggle Mode)",
                    rtlAction = "Wait: Data stalls Instruction Fetch",
                    description = "Switch to 'Von Neumann (Shared)' above to inspect the single-bridge bottleneck.",
                    activePartIds = setOf("cpu_core", "shared_bus_bridge"),
                    activeBusIds = setOf("inst_bus"),
                    registerSnapshot = mapOf("BUS" to "CONTENTION", "STALL" to "1 CYC")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("Where is Harvard architecture used today?", "In microcontrollers (AVR, ARM Cortex-M), DSPs, and inside the L1 split cache (L1i + L1d) of modern desktop/mobile CPUs."),
                VivaQuestion("Which architecture uses memory space more efficiently?", "Von Neumann, because unused code space can be used for data and vice versa in one unified RAM.")
            )
        ),
        ConceptTopic(
            id = "instruction_cycle",
            unitId = "unit1",
            title = "Instruction Cycle & Registers",
            subtitle = "Step-by-step Fetch, Decode & Execute across PC, MAR, MDR, IR",
            examWeightage = "Must Know (10-Mark)",
            difficulty = "Foundation",
            sceneType = SceneType3D.INSTRUCTION_CYCLE,
            summary = "Every machine instruction executes through a repeating Instruction Cycle: Fetch from RAM using PC/MAR/MDR, Decode the opcode in IR, and Execute inside the Control Unit & ALU.",
            keyPoints = listOf(
                "PC (Program Counter) holds the address of the NEXT instruction to be fetched.",
                "MAR (Memory Address Register) buffers the address sent onto the Address Bus.",
                "MDR (Memory Data Register) holds the data/instruction read from or written to RAM.",
                "IR (Instruction Register) holds the current instruction while CU decodes its opcode."
            ),
            rtlOrFormulas = listOf(
                "T0: MAR ← PC",
                "T1: MDR ← M[MAR], PC ← PC + 1",
                "T2: IR ← MDR",
                "T3: Decode IR[Opcode] & Execute"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("pc", "Program Counter", "PC", "Points to the memory address of the next instruction."),
                HardwarePartInfo("mar", "Memory Address Register", "MAR", "Latches the address for the RAM read/write cycle."),
                HardwarePartInfo("ram", "Main Memory", "RAM", "Returns the stored binary word at address M[MAR]."),
                HardwarePartInfo("mdr", "Memory Data Register", "MDR", "Receives the fetched instruction or operand word from RAM."),
                HardwarePartInfo("ir", "Instruction Register", "IR", "Holds the active instruction during decoding."),
                HardwarePartInfo("cu_alu", "CU & ALU Core", "CU", "Decodes IR opcode and executes the micro-operations.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "T0: Copy PC to MAR",
                    rtlAction = "MAR ← PC",
                    description = "The Program Counter transfers the next instruction address (0x200) into MAR.",
                    activePartIds = setOf("pc", "mar"),
                    activeBusIds = setOf("pc_mar"),
                    registerSnapshot = mapOf("PC" to "0x200", "MAR" to "0x200")
                ),
                SimulationStep(
                    title = "T1: Fetch from RAM & Increment PC",
                    rtlAction = "MDR ← M[MAR]; PC ← PC + 1",
                    description = "MAR addresses RAM; RAM places the instruction onto the Data Bus into MDR while PC advances to 0x201.",
                    activePartIds = setOf("mar", "ram", "mdr", "pc"),
                    activeBusIds = setOf("mar_ram", "ram_mdr"),
                    registerSnapshot = mapOf("PC" to "0x201", "MDR" to "LOAD 500")
                ),
                SimulationStep(
                    title = "T2: Transfer to Instruction Register",
                    rtlAction = "IR ← MDR",
                    description = "The fetched instruction word moves from MDR into IR so MDR is free for operand fetches.",
                    activePartIds = setOf("mdr", "ir"),
                    activeBusIds = setOf("mdr_ir"),
                    registerSnapshot = mapOf("IR" to "LOAD 500", "MDR" to "FREE")
                ),
                SimulationStep(
                    title = "T3: Decode & Execute",
                    rtlAction = "CU Decodes IR[Opcode] → Execute",
                    description = "Control Unit decodes 'LOAD 500', sets MAR ← 500, and routes operand into ALU/Accumulator.",
                    activePartIds = setOf("ir", "cu_alu"),
                    activeBusIds = setOf("ir_cu"),
                    registerSnapshot = mapOf("IR" to "LOAD 500", "STATE" to "EXEC")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("Why is PC incremented during T1 rather than after Execute?", "So PC already points to the next sequential instruction while the current instruction finishes decoding/executing (unless a Branch overwrites PC)."),
                VivaQuestion("What is the difference between MAR and MDR?", "MAR holds the memory address (width = address bus), whereas MDR holds the actual data/instruction value (width = data bus).")
            )
        ),
        ConceptTopic(
            id = "alu_micro_ops",
            unitId = "unit2",
            title = "ALU & Micro-operations",
            subtitle = "3D Arithmetic, Logic, Shift stages & Status Flags",
            examWeightage = "High Yield",
            difficulty = "Intermediate",
            sceneType = SceneType3D.ALU_MICRO_OPS,
            interactiveModes = listOf("Arithmetic (ADD)", "Logic (XOR)", "Shift (SHL)"),
            summary = "An Arithmetic Logic Unit combines an Arithmetic Circuit (Full Adders), a Logic Unit (AND/OR/XOR/NOT), and a Shifter under one multiplexer controlled by selection lines S0–S2.",
            keyPoints = listOf(
                "Arithmetic Micro-operations: Addition (R3 ← R1 + R2), Subtraction via 2's complement (R1 + R2' + 1), Increment/Decrement.",
                "Logic Micro-operations: Bitwise AND (masking), OR (setting bits), XOR (toggling/comparing), Complement.",
                "Shift Micro-operations: Logical Shift (0 fill), Arithmetic Shift (preserves sign bit), Circular Rotate.",
                "Status Register Flags: Zero (Z), Carry (C), Negative/Sign (N), Overflow (V)."
            ),
            rtlOrFormulas = listOf(
                "Add: F = A + B + Cin",
                "Subtract: F = A + B' + 1 (2's Complement)",
                "Overflow V = C_in_MSB ⊕ C_out_MSB"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("reg_a", "Input Register A", "REG A", "Supplies the first n-bit binary operand to all ALU sub-units."),
                HardwarePartInfo("reg_b", "Input Register B", "REG B", "Supplies the second operand (inverted for subtraction)."),
                HardwarePartInfo("arith_unit", "Ripple/Carry Adder", "ADD", "Full Adder chain computing binary sum and carry."),
                HardwarePartInfo("logic_unit", "Bitwise Gate Array", "LOGIC", "Parallel AND, OR, XOR, NOT gates per bit slice."),
                HardwarePartInfo("shifter", "Barrel Shifter", "SHIFT", "Shifts bits left/right by 1 or k positions."),
                HardwarePartInfo("mux", "4:1 Output MUX", "MUX", "Selects Arithmetic, Logic, or Shift output based on Control S1:S0."),
                HardwarePartInfo("flags", "Status Flag Flip-Flops", "PSW", "Updates Zero (Z), Carry (C), Sign (N), and Overflow (V) flags.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "1. Latch Operands A & B",
                    rtlAction = "Bus A ← R1; Bus B ← R2",
                    description = "Operands enter the parallel Arithmetic, Logic, and Shifter sub-circuits simultaneously.",
                    activePartIds = setOf("reg_a", "reg_b"),
                    activeBusIds = setOf("op_a", "op_b"),
                    registerSnapshot = mapOf("A" to "0101 (5)", "B" to "0011 (3)")
                ),
                SimulationStep(
                    title = "2. Multiplexer Operation Select",
                    rtlAction = "MUX ← Select(S1, S0)",
                    description = "Switch the mode pills above (ADD / XOR / SHL) to route that 3D unit through the Output MUX.",
                    activePartIds = setOf("arith_unit", "logic_unit", "shifter", "mux"),
                    activeBusIds = setOf("alu_mux"),
                    registerSnapshot = mapOf("ADD" to "1000 (8)", "XOR" to "0110 (6)")
                ),
                SimulationStep(
                    title = "3. Latch Output & Status Flags",
                    rtlAction = "AC ← F; Flags ← {Z, C, N, V}",
                    description = "The final result updates the Accumulator and sets condition flags for branch instructions.",
                    activePartIds = setOf("mux", "flags"),
                    activeBusIds = setOf("flag_bus"),
                    registerSnapshot = mapOf("Z" to "0", "C" to "0", "V" to "0")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("How does an ALU perform subtraction using an Adder?", "By taking the 1's complement of B (B') and setting Carry-In (Cin) = 1, which computes A + (~B + 1) = A - B."),
                VivaQuestion("When does signed Overflow (V) occur?", "When adding two positive numbers yields a negative sign bit, or adding two negative numbers yields a positive sign bit (Cin_MSB XOR Cout_MSB = 1).")
            )
        ),
        ConceptTopic(
            id = "addressing_modes",
            unitId = "unit2",
            title = "Addressing Modes & Formats",
            subtitle = "How Effective Address (EA) resolves in 3D Register & Memory space",
            examWeightage = "Guaranteed Question",
            difficulty = "Intermediate",
            sceneType = SceneType3D.ADDRESSING_MODES,
            interactiveModes = listOf("Immediate", "Direct", "Indirect", "Indexed"),
            summary = "Addressing modes define how the operand's Effective Address (EA) is calculated from the instruction's address field. Toggle the 4 modes above to watch the 3D pointer hops change!",
            keyPoints = listOf(
                "Immediate (#val): Operand is embedded directly inside the instruction. Zero memory reads, fastest!",
                "Direct (Addr): Instruction contains the exact memory address (EA = Addr). 1 memory read.",
                "Indirect (@Addr): Instruction points to a memory cell that stores the real address (EA = M[Addr]). 2 memory reads.",
                "Indexed (Addr + R1): Effective Address adds a base/index register (EA = Addr + IX), ideal for array loops."
            ),
            rtlOrFormulas = listOf(
                "Immediate: Operand = IR[Addr]",
                "Direct: EA = IR[Addr]",
                "Indirect: EA = M[IR[Addr]]",
                "Indexed: EA = IR[Addr] + IX"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("ir_field", "IR [Opcode | Addr]", "IR", "Instruction word containing opcode, mode bits, and address/literal."),
                HardwarePartInfo("reg_file", "Index / Base Register", "IX", "Holds offset index (e.g., array index i) added to Addr."),
                HardwarePartInfo("mem_ptr", "Pointer Cell M[Addr]", "PTR", "Intermediate memory word holding the target address in Indirect mode."),
                HardwarePartInfo("mem_data", "Target Operand M[EA]", "DATA", "Final memory location containing the actual operand value.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "Compare Pointer Hops in 3D",
                    rtlAction = "Operand ← M[EA]",
                    description = "Tap Immediate, Direct, Indirect, or Indexed above to see how many hops the CPU takes to reach the operand.",
                    activePartIds = setOf("ir_field", "mem_data"),
                    activeBusIds = setOf("imm_hop", "dir_hop", "ind_1", "ind_2", "idx_1", "idx_2"),
                    registerSnapshot = mapOf("ADDR" to "100", "IX" to "04", "M[100]" to "500")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("Which addressing mode is best for accessing array elements in a loop?", "Indexed (or Base-Register) addressing, where the base address stays fixed and the index register increments each iteration."),
                VivaQuestion("Why does Indirect Addressing take longer?", "It requires two memory accesses: first to read the pointer at M[Addr], and second to fetch the actual operand at M[M[Addr]].")
            )
        ),
        ConceptTopic(
            id = "control_unit",
            unitId = "unit2",
            title = "Hardwired vs Microprogrammed CU",
            subtitle = "Combinational logic matrix vs Control Memory micro-routines",
            examWeightage = "Important 8-Mark",
            difficulty = "Intermediate",
            sceneType = SceneType3D.CONTROL_UNIT,
            interactiveModes = listOf("Hardwired CU", "Microprogrammed CU"),
            summary = "The Control Unit generates timed control signals. Hardwired CU uses fixed combinational logic gates (fast, used in RISC). Microprogrammed CU stores control words in a Control Memory ROM (flexible, used in CISC).",
            keyPoints = listOf(
                "Hardwired CU: Decodes IR + Step Counter through PLA/logic gates. Very fast, difficult to modify after fabrication.",
                "Microprogrammed CU: Each machine instruction triggers a microprogram stored in Control Memory (CMAR -> Control Store -> CMDR).",
                "Horizontal Microprogramming: 1 bit per control signal (wide word, fast, parallel). Vertical: Encoded fields (compact, needs decoder)."
            ),
            rtlOrFormulas = listOf(
                "Hardwired: Control = f(IR_Opcode, T_step, Flags)",
                "Microprogrammed: CMDR ← Control_Mem[CMAR]",
                "RISC prefers Hardwired; CISC often uses Microprogrammed"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("ir_op", "IR / Sequencer", "SEQ", "Maps instruction opcode to logic gates or starting micro-address."),
                HardwarePartInfo("gate_matrix", "PLA Gate Matrix", "PLA", "High-speed combinational logic array (Hardwired)."),
                HardwarePartInfo("ctrl_mem", "Control Memory ROM", "C-ROM", "Stores sequences of microinstructions (Microprogrammed)."),
                HardwarePartInfo("ctrl_signals", "Control Signals Out", "CTRL", "Drives ALU select, Register load, and Memory Read/Write lines.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "Generate Timed Control Word",
                    rtlAction = "CTRL_OUT ← Decode(Opcode, T)",
                    description = "Toggle between Hardwired CU and Microprogrammed CU above to compare gate matrix vs Control Memory lookup.",
                    activePartIds = setOf("ir_op", "gate_matrix", "ctrl_mem", "ctrl_signals"),
                    activeBusIds = setOf("b1", "b2", "b3"),
                    registerSnapshot = mapOf("CYCLE" to "T2", "WORD" to "1011001")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("What is a Control Word?", "A binary string where individual bits or encoded fields specify which micro-operations execute in a single clock cycle."),
                VivaQuestion("Why do RISC processors favor Hardwired control?", "RISC has a small, uniform instruction set, making combinational logic simple, compact, and fast enough for single-cycle pipelining.")
            )
        ),
        ConceptTopic(
            id = "pipeline_5_stage",
            unitId = "unit3",
            title = "5-Stage RISC Pipeline & Hazards",
            subtitle = "IF → ID → EX → MEM → WB concurrency, stalls & data forwarding",
            examWeightage = "High Yield (10-Mark)",
            difficulty = "Core Exam",
            sceneType = SceneType3D.PIPELINE_5_STAGE,
            interactiveModes = listOf("Forwarding (No Stall)", "RAW Hazard (Bubble)"),
            summary = "Pipelining overlaps the execution of multiple instructions like a 3D assembly line. Ideally 1 instruction finishes every clock cycle (CPI → 1), unless Structural, Data (RAW), or Control (Branch) hazards cause a stall bubble.",
            keyPoints = listOf(
                "5 Classic RISC Stages: IF (Fetch), ID (Decode/Reg Read), EX (ALU), MEM (Data RAM), WB (Write Back).",
                "Pipeline Latches (IF/ID, ID/EX, EX/MEM, MEM/WB) hold intermediate values between clock edges.",
                "Data Hazard (RAW): An instruction needs a register value not yet written back. Solved by Operand Forwarding (Bypass) or inserting NOP stalls.",
                "Control Hazard: Branches alter PC; mitigated by Branch Prediction & Delayed Branching."
            ),
            rtlOrFormulas = listOf(
                "Non-Pipelined Time = n × k × Tp",
                "Pipelined Time = (k + n - 1) × Tp",
                "Ideal Speedup (n → ∞) = k (number of stages = 5)"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("if_stage", "IF: Instruction Fetch", "IF", "Fetches instruction from I-Cache using PC and increments PC + 4."),
                HardwarePartInfo("id_stage", "ID: Decode & Read", "ID", "Decodes opcode and reads source registers Rs1, Rs2."),
                HardwarePartInfo("ex_stage", "EX: ALU Execute", "EX", "Computes arithmetic result or branch target address."),
                HardwarePartInfo("mem_stage", "MEM: Memory Access", "MEM", "Reads/writes D-Cache for Load/Store instructions."),
                HardwarePartInfo("wb_stage", "WB: Write Back", "WB", "Writes final ALU or Load result back to destination register Rd.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "1. Concurrent Instruction Fill",
                    rtlAction = "I1(WB) | I2(MEM) | I3(EX) | I4(ID) | I5(IF)",
                    description = "All 5 hardware stages work simultaneously on 5 different instructions—5× ideal throughput!",
                    activePartIds = setOf("if_stage", "id_stage", "ex_stage", "mem_stage", "wb_stage"),
                    activeBusIds = setOf("pipe_flow_1", "pipe_flow_2", "pipe_flow_3", "pipe_flow_4"),
                    registerSnapshot = mapOf("CPI" to "1.0", "SPEEDUP" to "~5x")
                ),
                SimulationStep(
                    title = "2. Data Forwarding vs Stall Bubble",
                    rtlAction = "EX/MEM.Result → EX.ALU_In (Bypass)",
                    description = "Toggle 'RAW Hazard (Bubble)' above to see an EX stage stall turn coral red vs bypassing via Forwarding.",
                    activePartIds = setOf("id_stage", "ex_stage"),
                    activeBusIds = setOf("forwarding_bypass", "hazard_stall"),
                    registerSnapshot = mapOf("HAZARD" to "RAW", "BYPASS" to "ON")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("Does pipelining reduce the latency of a single instruction?", "No—latency of one instruction stays ~5 stages (slightly higher due to latch overhead). Pipelining increases THROUGHPUT (instructions completed per second)."),
                VivaQuestion("Name the 3 types of pipeline hazards.", "1) Structural (hardware resource conflict), 2) Data (RAW/WAR/WAW dependencies), 3) Control (branch/jump uncertainty).")
            )
        ),
        ConceptTopic(
            id = "memory_hierarchy",
            unitId = "unit4",
            title = "Memory Hierarchy Pyramid",
            subtitle = "Speed vs Cost vs Capacity from Registers down to Disk",
            examWeightage = "Must Know",
            difficulty = "Foundation",
            sceneType = SceneType3D.MEMORY_HIERARCHY,
            summary = "No single memory technology is simultaneously ultra-fast, huge, and cheap. Computer systems stack memories in a 3D pyramid exploiting Locality of Reference (Temporal & Spatial) so average access time approaches L1 Cache speed at Disk cost.",
            keyPoints = listOf(
                "Moving UP the pyramid: Speed increases, Cost per bit increases, Capacity decreases.",
                "Temporal Locality: Recently accessed items are likely to be accessed again soon (loops).",
                "Spatial Locality: Nearby memory addresses are likely to be accessed soon (arrays, sequential code).",
                "Hit Ratio (H) = Cache Hits / Total Accesses; Miss Ratio = 1 - H."
            ),
            rtlOrFormulas = listOf(
                "EMAT = H × T_cache + (1 - H) × (T_cache + T_mem)",
                "Speedup ↑ as Hit Ratio H → 1.0",
                "SRAM (Cache) vs DRAM (Main RAM) vs Flash/HDD"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("regs", "CPU Registers", "REG", "Flip-flops inside CPU core (~0.3 ns, ~1 KB total)."),
                HardwarePartInfo("l1_cache", "L1 / L2 SRAM Cache", "L1/L2", "Fast static RAM on-die (1–4 ns, 32 KB – 1 MB per core)."),
                HardwarePartInfo("l3_cache", "L3 Shared Cache", "L3", "Last-level cache shared across cores (~12 ns, 8–64 MB)."),
                HardwarePartInfo("ram", "Main Memory (DRAM)", "RAM", "Capacitor-based dynamic RAM (~60 ns, 8–64 GB)."),
                HardwarePartInfo("disk", "Secondary Storage", "SSD", "Non-volatile NVMe/HDD storage (microseconds–ms, TBs).")
            ),
            steps = listOf(
                SimulationStep(
                    title = "1. Cache Hit (Fast Path)",
                    rtlAction = "CPU ← L1 Cache (Hit! 1 ns)",
                    description = "Requested word is found in L1/L2 SRAM; data returns straight up to CPU Registers.",
                    activePartIds = setOf("regs", "l1_cache"),
                    activeBusIds = setOf("hit_path"),
                    registerSnapshot = mapOf("HIT" to "96%", "LATENCY" to "1.2 ns")
                ),
                SimulationStep(
                    title = "2. Cache Miss (Hierarchy Fill)",
                    rtlAction = "Disk/RAM → L3 → L1 → CPU",
                    description = "On a miss, an entire cache block (e.g., 64 Bytes) is promoted up the pyramid for future spatial hits.",
                    activePartIds = setOf("disk", "ram", "l3_cache", "l1_cache"),
                    activeBusIds = setOf("miss_path"),
                    registerSnapshot = mapOf("MISS" to "4%", "BLOCK" to "64 B")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("Why is SRAM used for Cache while DRAM is used for Main RAM?", "SRAM uses 6 transistors per bit (fast, no refresh needed, expensive/large); DRAM uses 1 transistor + 1 capacitor (dense, cheap, needs periodic refresh)."),
                VivaQuestion("Calculate EMAT if H = 0.9, T_cache = 10ns, T_mem = 100ns.", "EMAT = 0.9(10) + 0.1(10 + 100) = 9 + 11 = 20 ns (simultaneous check: 0.9×10 + 0.1×100 = 19 ns).")
            )
        ),
        ConceptTopic(
            id = "cache_mapping",
            unitId = "unit4",
            title = "Cache Mapping Techniques",
            subtitle = "Direct, Fully Associative & Set-Associative 3D address mapping",
            examWeightage = "High Yield (10-Mark)",
            difficulty = "Core Exam",
            sceneType = SceneType3D.CACHE_MAPPING,
            interactiveModes = listOf("Direct Mapped", "Fully Associative", "2-Way Set Assoc"),
            summary = "Cache Mapping defines which Cache Line can hold a given Main Memory block. Switch between the 3 mapping modes above to visualize conflict misses vs parallel tag search in 3D!",
            keyPoints = listOf(
                "Direct Mapping: Block j maps only to Cache Line (j mod N). Fast hardware, high Conflict Misses.",
                "Fully Associative: Any RAM block can go into ANY cache line. Zero conflict misses, expensive parallel comparators.",
                "k-Way Set-Associative: Hybrid! Cache is divided into S sets of k lines each. Set = (j mod S), associative within the set.",
                "Replacement Policies (LRU, FIFO, Random) are needed in Associative & Set-Associative caches."
            ),
            rtlOrFormulas = listOf(
                "Direct: [ Tag | Line Index | Block Offset ]",
                "Associative: [ Tag | Block Offset ]",
                "Set-Assoc: Set Index = (Block Addr) mod (# of Sets)"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("cpu_addr", "Physical Address Split", "ADDR", "Splits address bits into Tag, Index/Set, and Byte Offset."),
                HardwarePartInfo("cache_b0", "Cache Line / Way 0", "WAY 0", "Stores Tag bits, Valid bit, and cached data block."),
                HardwarePartInfo("cache_b1", "Cache Line / Way 1", "WAY 1", "Second slot in a 2-way set or associative pool."),
                HardwarePartInfo("ram_blk0", "RAM Block 0", "BLK 0", "Main memory block whose Block # mod N = 0."),
                HardwarePartInfo("ram_blk4", "RAM Block 4", "BLK 4", "Shares same modulo index as Block 0 -> causes Conflict Miss in Direct mode!")
            ),
            steps = listOf(
                SimulationStep(
                    title = "Compare Mapping Topologies",
                    rtlAction = "Tag_Compare(CPU_Addr.Tag, Cache[Idx].Tag)",
                    description = "Toggle Direct / Associative / 2-Way Set Assoc above to observe how Block 0 and Block 4 collide or coexist.",
                    activePartIds = setOf("cpu_addr", "cache_b0", "cache_b1", "ram_blk0", "ram_blk4"),
                    activeBusIds = setOf("addr_chk", "map_direct", "conflict_line", "assoc_1", "assoc_2", "set_idx", "way_0", "way_1"),
                    registerSnapshot = mapOf("LINE" to "2^r", "BLK_SZ" to "64B")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("What is Thrashing in Direct-Mapped Cache?", "When two frequently accessed blocks map to the exact same cache line, repeatedly evicting each other."),
                VivaQuestion("What is the difference between Write-Through and Write-Back?", "Write-Through updates Cache and RAM simultaneously; Write-Back updates only Cache (marking a Dirty Bit) and writes to RAM on eviction.")
            )
        ),
        ConceptTopic(
            id = "virtual_memory",
            unitId = "unit4",
            title = "Virtual Memory, Paging & TLB",
            subtitle = "Virtual-to-Physical address translation & Page Fault handling",
            examWeightage = "High Yield",
            difficulty = "Core Exam",
            sceneType = SceneType3D.VIRTUAL_MEMORY_TLB,
            interactiveModes = listOf("TLB Hit (Fast)", "Page Fault (Disk)"),
            summary = "Virtual Memory lets programs use a large contiguous virtual address space regardless of physical RAM size. The MMU divides memory into fixed-size Pages (virtual) and Frames (physical), accelerated by the TLB cache.",
            keyPoints = listOf(
                "Virtual Address = [ Virtual Page Number (VPN) | Page Offset ]. Offset remains unchanged during translation.",
                "TLB (Translation Lookaside Buffer) is a fast associative hardware cache storing recent VPN → PFN mappings.",
                "Page Fault occurs when the Page Table Valid bit = 0 (page is on Swap Disk, not in RAM). OS traps and loads it.",
                "Belady's Anomaly can happen in FIFO page replacement, never in LRU or Optimal."
            ),
            rtlOrFormulas = listOf(
                "Page Size = Frame Size = 2^offset bytes",
                "EMAT_VM = h_TLB(T_TLB + T_mem) + (1 - h_TLB)(T_TLB + 2·T_mem)",
                "TLB Hit skips the RAM Page Table read!"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("cpu_va", "MMU Virtual Address", "VA", "Generated by CPU: splits into Page Number and Offset."),
                HardwarePartInfo("tlb", "TLB Hardware Cache", "TLB", "Associative cache mapping VPN -> Physical Frame # in 1 cycle."),
                HardwarePartInfo("page_table", "OS Page Table", "PT", "In-RAM table mapping every Virtual Page to a Frame or Disk."),
                HardwarePartInfo("phys_ram", "Physical RAM Frames", "PFN", "Actual DRAM frames holding active program pages."),
                HardwarePartInfo("swap_disk", "Swap / Backing Store", "SWAP", "Disk area holding pages swapped out during memory pressure.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "Trace TLB Hit vs Page Fault",
                    rtlAction = "Physical Addr = Frame# + Offset",
                    description = "Toggle 'TLB Hit' and 'Page Fault' above to see how TLB bypasses RAM lookup vs how a Page Fault invokes Swap Disk.",
                    activePartIds = setOf("cpu_va", "tlb", "phys_ram"),
                    activeBusIds = setOf("va_tlb", "tlb_ram", "va_pt", "pt_disk", "disk_ram"),
                    registerSnapshot = mapOf("PAGE" to "4 KB", "TLB_HIT" to "98%")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("Why does Page Offset stay unchanged during translation?", "Because Virtual Pages and Physical Frames have the exact same size (e.g., 4 KB = 12-bit offset)."),
                VivaQuestion("What is Thrashing in Virtual Memory?", "When the system spends more time swapping pages between RAM and Disk (page faults) than executing instructions.")
            )
        ),
        ConceptTopic(
            id = "io_dma",
            unitId = "unit5",
            title = "I/O Modes & DMA Transfer",
            subtitle = "Direct Memory Access bus mastering vs CPU busy-waiting",
            examWeightage = "Guaranteed Question",
            difficulty = "Intermediate",
            sceneType = SceneType3D.IO_DMA_TRANSFER,
            interactiveModes = listOf("DMA Transfer (Fast)", "Programmed I/O"),
            summary = "Slow peripherals connect via I/O Interfaces. In Programmed I/O, the CPU polls status flags endlessly. Interrupt I/O notifies CPU only when ready. DMA goes further: a DMA Controller becomes Bus Master to move large blocks directly between Disk/NIC and RAM.",
            keyPoints = listOf(
                "Programmed I/O: CPU wastes cycles polling the Ready bit (busy wait).",
                "Interrupt-Driven I/O: Device asserts IRQ; CPU saves PC/PSW, runs ISR, then resumes.",
                "DMA Handshake: DMA asserts HOLD (Bus Request); CPU completes current cycle, floats buses, and replies HLDA (Bus Grant).",
                "DMA Modes: Burst Mode (block transfer), Cycle Stealing (1 word/cycle), Transparent Mode."
            ),
            rtlOrFormulas = listOf(
                "DMA Registers: Address Reg + Word Count + Control Reg",
                "Handshake: DMA --[HOLD]--> CPU --[HLDA]--> DMA",
                "Interrupt Priority: Daisy Chaining (Serial) / Polling"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("cpu", "CPU Core", "CPU", "Relinquishes system buses during DMA by granting HLDA."),
                HardwarePartInfo("dma", "DMA Controller (DMAC)", "DMA", "Drives Address & Control buses directly during block transfers."),
                HardwarePartInfo("ram", "Main RAM Buffer", "RAM", "Receives or supplies bulk data directly from/to I/O."),
                HardwarePartInfo("disk_io", "High-Speed I/O Device", "I/O", "Disk/Network controller initiating DMA request (DREQ).")
            ),
            steps = listOf(
                SimulationStep(
                    title = "Compare DMA vs Programmed I/O",
                    rtlAction = "HOLD=1 → HLDA=1 → M[DMA_AR] ← I/O_Data",
                    description = "Toggle between DMA Transfer (direct I/O→RAM path, CPU free) and Programmed I/O (CPU bottleneck).",
                    activePartIds = setOf("cpu", "dma", "ram", "disk_io"),
                    activeBusIds = setOf("hold_ack", "dma_io", "dma_ram", "pio_poll", "pio_write"),
                    registerSnapshot = mapOf("HOLD" to "1", "WC" to "1024 B")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("What is Cycle Stealing in DMA?", "The DMA controller takes over the system bus for just one memory cycle to transfer one word while the CPU pauses briefly, then returns control."),
                VivaQuestion("What are the 3 registers inside a DMA controller?", "1) Address Register (target RAM address), 2) Word Count Register (bytes left), 3) Control/Status Register (R/W direction & mode).")
            )
        ),
        ConceptTopic(
            id = "flynns_multicore",
            unitId = "unit5",
            title = "Flynn's Taxonomy & Multicore",
            subtitle = "SISD, SIMD, MIMD & Cache Coherence in parallel processors",
            examWeightage = "Frequent 5-Mark",
            difficulty = "Intermediate",
            sceneType = SceneType3D.FLYNNS_MULTICORE,
            interactiveModes = listOf("SISD (Single)", "SIMD (Vector/GPU)", "MIMD (Multicore)"),
            summary = "Michael Flynn (1966) classified parallel computers by Instruction Streams and Data Streams: SISD (traditional CPU), SIMD (GPUs/vector units), MISD (rare fault-tolerant pipelines), and MIMD (modern multi-core CPUs & clusters).",
            keyPoints = listOf(
                "SISD: 1 Control Unit, 1 Processor, 1 Data stream (classic uniprocessor).",
                "SIMD: 1 Control Unit broadcasts the SAME instruction to N parallel ALUs processing different data (GPUs, AVX).",
                "MISD: Multiple instructions operate on a single data stream (theoretical / specialized systolic arrays).",
                "MIMD: Multiple autonomous cores execute independent instruction streams on independent data, sharing coherent L3/RAM (MESI protocol)."
            ),
            rtlOrFormulas = listOf(
                "Amdahl's Law: Speedup = 1 / [ (1 - P) + P / N ]",
                "SISD: 1×CU, 1×ALU | SIMD: 1×CU, N×ALU",
                "MIMD: N×CU, N×ALU + Cache Coherence (MESI)"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("cu_master", "Control Unit(s)", "CU", "Issues 1 instruction stream (SISD/SIMD) or N independent streams (MIMD)."),
                HardwarePartInfo("pe1", "Processing Elements", "ALU", "Execute arithmetic/vector operations in parallel."),
                HardwarePartInfo("shared_l3", "Shared Interconnect / L3", "L3", "Maintains cache coherence (Modified/Exclusive/Shared/Invalid) across cores.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "Explore SISD → SIMD → MIMD in 3D",
                    rtlAction = "Speedup ≤ 1 / ((1 - P) + P/N)",
                    description = "Switch between SISD, SIMD, and MIMD above to watch the 3D processor topology scale from 1 core to vector lanes to 4 coherent cores.",
                    activePartIds = setOf("cu0", "pe0", "cu_master", "pe1", "pe2", "pe3", "core0", "core1", "core2", "core3", "shared_l3"),
                    activeBusIds = setOf("sisd_1", "sisd_2", "simd_c1", "simd_c2", "simd_c3", "simd_d1", "simd_d2", "simd_d3", "m0", "m1", "m2", "m3"),
                    registerSnapshot = mapOf("CORES" to "4", "PROTO" to "MESI")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("Why does SIMD excel at graphics and AI matrix math?", "Every pixel or matrix element needs the exact same arithmetic operation applied across thousands of data items simultaneously."),
                VivaQuestion("What is the Cache Coherence problem in MIMD?", "When Core 0 updates a shared variable in its private L1 cache, Core 1 might still hold a stale copy in its L1 unless snooping/directory coherence (MESI) invalidates or updates it.")
            )
        )
    )

    fun getTopicsForUnit(unitId: String?): List<ConceptTopic> =
        if (unitId == null || unitId == "all") topics else topics.filter { it.unitId == unitId }

    fun getTopicById(topicId: String): ConceptTopic? = topics.find { it.id == topicId }

    fun getUnitById(unitId: String): CbcsUnit? = units.find { it.id == unitId }
}
