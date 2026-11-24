package com.example.data

import com.example.domain.model.ConceptTopic
import com.example.domain.model.HardwarePartInfo
import com.example.domain.model.SceneType3D
import com.example.domain.model.SimulationStep
import com.example.domain.model.VivaQuestion

object CbcsDigitalLogicTopics {

    val topics: List<ConceptTopic> = listOf(
        ConceptTopic(
            id = "number_systems_codes",
            unitId = "unit0",
            title = "Number Systems & Binary Codes",
            subtitle = "Binary, Octal, Hex conversions, BCD, Excess-3, Gray Code & 2's Complement",
            examWeightage = "High Yield (10-Mark)",
            difficulty = "Foundation",
            sceneType = SceneType3D.NUMBER_SYSTEMS_CODES,
            prefersStepByStep = true,
            interactiveModes = listOf("Bin / Oct / Hex", "BCD & Gray Code", "2's Complement"),
            summary = "Digital computers process data in Base-2 (Binary: 0, 1). Humans compact long binary strings using Base-8 (Octal: 3 bits per digit) and Base-16 (Hexadecimal: 4 bits per nibble). Special Binary Codes like BCD (8421), Excess-3, and Gray Code optimize decimal displays, self-complementing arithmetic, and rotary encoders.",
            keyPoints = listOf(
                "Binary (Base 2) ↔ Octal (Base 8): Group bits in 3s from the binary point (e.g., 101 110₂ = 56₈).",
                "Binary (Base 2) ↔ Hexadecimal (Base 16): Group bits in 4s (e.g., 1010 1111₂ = AF₁₆).",
                "BCD (8421): Each decimal digit 0–9 is encoded separately in 4 bits (1010–1111 are invalid).",
                "Excess-3 Code: BCD + 0011 (unweighted, self-complementing). Gray Code: Adjacent numbers differ by only 1 bit (unit-distance)."
            ),
            rtlOrFormulas = listOf(
                "Decimal → Base-r: Repeated division by r (integer) & multiply by r (fraction)",
                "Binary → Gray: G_n = B_n; G_i = B_(i+1) ⊕ B_i",
                "2's Complement (-N) = (1's Complement of N) + 1",
                "Example: (25)₁₀ = (11001)₂ = (31)₈ = (19)₁₆"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("dec_in", "Decimal Input (Base-10)", "DEC", "Human-readable Base-10 number (e.g., 25₁₀) converted via repeated division."),
                HardwarePartInfo("bin_reg", "Binary Register (Base-2)", "BIN", "Stores 0/1 voltage levels (11001₂) using weighted powers of 2."),
                HardwarePartInfo("oct_hex", "Octal (3-bit) & Hex (4-bit)", "OCT/HEX", "Compact grouping: 3 bits = 1 Octal digit; 4 bits = 1 Hex digit (0–9, A–F)."),
                HardwarePartInfo("code_enc", "BCD / Gray / 2's Comp Encoder", "CODE", "Transforms binary into 8421 BCD, unit-distance Gray, Excess-3, or signed 2's complement.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "1. Base Conversion Pipeline",
                    rtlAction = "(25)₁₀ → (0001 1001)₂ → (31)₈ / (19)₁₆",
                    description = "Toggle between Bin/Oct/Hex, BCD & Gray Code, and 2's Complement above to inspect each encoding path.",
                    activePartIds = setOf("dec_in", "bin_reg", "oct_hex", "code_enc"),
                    activeBusIds = setOf("dec_bin", "bin_oct", "bin_code"),
                    registerSnapshot = mapOf("DEC" to "25", "BIN" to "11001", "OCT" to "31", "HEX" to "19")
                ),
                SimulationStep(
                    title = "2. Binary Codes & Signed Range",
                    rtlAction = "Gray = B ⊕ (B >> 1) | BCD(25) = 0010 0101",
                    description = "In 8-bit 2's complement, MSB = 1 denotes negative; range is -128 to +127 with a single zero.",
                    activePartIds = setOf("bin_reg", "code_enc"),
                    activeBusIds = setOf("bin_code"),
                    registerSnapshot = mapOf("BCD" to "0010 0101", "GRAY" to "10101", "EX-3" to "0101 1000")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("Why is Gray Code called a unit-distance or reflected code?", "Because consecutive numbers in Gray code differ by exactly 1 bit, preventing false intermediate states in mechanical shaft encoders and K-Maps."),
                VivaQuestion("Why is 2's Complement preferred over Sign-Magnitude in CPUs?", "2's complement has only one representation for zero (+0/-0 problem eliminated) and allows addition and subtraction using the exact same Adder circuit.")
            )
        ),
        ConceptTopic(
            id = "logic_gates",
            unitId = "unit0",
            title = "Logic Gates & Universal Gates",
            subtitle = "AND, OR, NOT, NAND, NOR, XOR & XNOR symbols, truth tables & IC realization",
            examWeightage = "Foundation (8-Mark)",
            difficulty = "Foundation",
            sceneType = SceneType3D.LOGIC_GATES,
            interactiveModes = listOf("Basic (AND/OR/NOT)", "Universal (NAND/NOR)", "Derived (XOR/XNOR)"),
            summary = "Logic gates are the physical transistor building blocks of every CPU. Basic gates (AND, OR, NOT) implement Boolean algebra; NAND and NOR are Universal Gates because ANY digital circuit (AND, OR, NOT, XOR, Adders) can be built using only NAND or only NOR gates.",
            keyPoints = listOf(
                "AND (A·B): Output is 1 only when ALL inputs are 1. OR (A+B): Output is 1 if ANY input is 1.",
                "NOT (A'): Inverts input (0 ↔ 1). Buffer strengthens signal without inverting.",
                "NAND = (A·B)' and NOR = (A+B)': Both are Universal Gates (used heavily in CMOS silicon).",
                "XOR (A ⊕ B): Output is 1 when inputs are DIFFERENT (inequality detector, used in Half Adder Sum & Parity). XNOR = equality detector."
            ),
            rtlOrFormulas = listOf(
                "De Morgan's Law 1: (A + B)' = A' · B' (Bubble OR = NAND)",
                "De Morgan's Law 2: (A · B)' = A' + B' (Bubble AND = NOR)",
                "XOR: A ⊕ B = A'B + AB' | XNOR: (A ⊕ B)' = AB + A'B'"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("in_pins", "Boolean Inputs (A, B)", "IN", "Digital voltage levels: Logic 0 (LOW / 0V) and Logic 1 (HIGH / +V)."),
                HardwarePartInfo("gate_primary", "Primary Logic Gate", "GATE 1", "AND / NAND / XOR transistor stage evaluating inputs A and B."),
                HardwarePartInfo("gate_secondary", "Complementary Gate", "GATE 2", "OR / NOR / XNOR stage demonstrating De Morgan duality."),
                HardwarePartInfo("out_led", "Logic Output (Y)", "OUT", "Resulting Boolean output driving next combinational or latch stage.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "1. Evaluate Gate Truth Table",
                    rtlAction = "Y = f(A, B) via CMOS Gate Network",
                    description = "Switch between Basic, Universal, and Derived modes above to compare gate symbols, Boolean equations, and De Morgan equivalents.",
                    activePartIds = setOf("in_pins", "gate_primary", "gate_secondary", "out_led"),
                    activeBusIds = setOf("g_in_a", "g_in_b", "g_out"),
                    registerSnapshot = mapOf("A" to "1", "B" to "0", "AND" to "0", "OR" to "1", "XOR" to "1")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("Why are NAND and NOR called Universal Gates?", "Because NOT, AND, and OR (and therefore every possible Boolean circuit) can be constructed solely from NAND gates or solely from NOR gates."),
                VivaQuestion("How many NAND gates are needed to implement an XOR gate?", "4 NAND gates (or 5 NOR gates) are required to build a 2-input XOR gate.")
            )
        ),
        ConceptTopic(
            id = "karnaugh_map",
            unitId = "unit0",
            title = "Karnaugh Map (K-Map)",
            subtitle = "Visual Boolean minimization using Gray-code cells, Pairs, Quads & Octets",
            examWeightage = "Guaranteed 10-Mark",
            difficulty = "Core Exam",
            sceneType = SceneType3D.KARNAUGH_MAP,
            interactiveModes = listOf("4-Var SOP (Quad + Pair)", "Don't Care (X) Optimization"),
            summary = "A Karnaugh Map (K-Map) is a graphical grid method to simplify Boolean SOP/POS expressions without tedious algebra. Cells are ordered in Gray Code (00, 01, 11, 10) so physically adjacent cells (including wrap-around edges!) differ by only 1 variable.",
            keyPoints = listOf(
                "Cell Count: n variables need 2^n cells (2-var: 4, 3-var: 8, 4-var: 16 cells).",
                "Valid Group Sizes: Always powers of 2 -> 1, 2 (Pair), 4 (Quad), 8 (Octet), 16.",
                "Larger groups eliminate more variables: Pair drops 1 var, Quad drops 2 vars, Octet drops 3 vars.",
                "Don't Care (X): Can be treated as 1 inside a group to make it larger, or 0 if not needed."
            ),
            rtlOrFormulas = listOf(
                "Row/Col Order: 00 → 01 → 11 → 10 (Gray Code!)",
                "Example F(A,B,C,D) = Σm(0,1,2,3,5,7) → Quad(0,1,2,3) + Quad(1,3,5,7)",
                "Minimized SOP: F = A'B' + A'D"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("kmap_quad", "Quad Group (4 Cells)", "QUAD", "Groups 4 adjacent 1s (eliminates 2 variables -> 2-literal term)."),
                HardwarePartInfo("kmap_pair", "Pair / Overlap Group", "PAIR", "Groups 2 adjacent 1s or overlaps with Don't Care (X) to form a larger Quad."),
                HardwarePartInfo("kmap_grid", "Gray-Code 16-Cell Matrix", "4×4", "Adjacency wraps left-to-right and top-to-bottom like a torus."),
                HardwarePartInfo("min_sop", "Minimized Gate Output", "SOP", "Fewer product terms directly reduces IC gate count and propagation delay.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "1. Plot Minterms & Form Largest Power-of-2 Groups",
                    rtlAction = "F(A,B,C,D) = A'B' + A'D (Minimized)",
                    description = "Look at the 2D K-Map Grid: Gray Code (00, 01, 11, 10) keeps adjacent cells differing by 1 bit. Toggle Don't Care (X) above to see how X expands a Pair into a Quad!",
                    activePartIds = setOf("kmap_quad", "kmap_pair", "kmap_grid", "min_sop"),
                    activeBusIds = setOf("k_out"),
                    registerSnapshot = mapOf("VARS" to "4 (16 cells)", "QUAD" to "-2 vars", "PAIR" to "-1 var")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("Why do K-Map columns follow 00, 01, 11, 10 instead of 00, 01, 10, 11?", "Because 01 → 11 → 10 is Gray Code order, ensuring every neighboring cell differs in only 1 variable so X + X' = 1 simplification works visually."),
                VivaQuestion("What is a Prime Implicant vs Essential Prime Implicant?", "A Prime Implicant is the largest possible valid group of 1s; an Essential Prime Implicant covers at least one minterm '1' not covered by any other group.")
            )
        ),
        ConceptTopic(
            id = "mux_demux",
            unitId = "unit0",
            title = "Multiplexer & Demultiplexer",
            subtitle = "4:1 MUX (Many-to-One Data Selector) vs 1:4 DEMUX (One-to-Many Distributor)",
            examWeightage = "High Yield (8-Mark)",
            difficulty = "Core Exam",
            sceneType = SceneType3D.MUX_DEMUX,
            interactiveModes = listOf("4:1 MUX (Many → 1)", "1:4 DEMUX (1 → Many)"),
            summary = "A Multiplexer (MUX) acts like a digital rotary switch: it takes 2^n input lines and routes ONE selected input to a single output using n Select Lines. A Demultiplexer (DEMUX) does the reverse: routes 1 input line to one of 2^n output lines.",
            keyPoints = listOf(
                "MUX Ratio: 2^n inputs : 1 output, controlled by n select lines (e.g., 4:1 MUX needs 2 select lines S1, S0).",
                "MUX Equation (4:1): Y = S1'S0'·I0 + S1'S0·I1 + S1S0'·I2 + S1S0·I3.",
                "MUX as Universal Logic: An 8:1 MUX can implement any 3-variable (or even 4-variable!) Boolean function.",
                "DEMUX (1:2^n): Used for serial-to-parallel conversion and as a Binary Decoder when Data Input = 1."
            ),
            rtlOrFormulas = listOf(
                "MUX: 2^n Inputs + n Select Lines → 1 Output",
                "DEMUX: 1 Input + n Select Lines → 2^n Outputs",
                "If S1S0 = 10₂ (2), MUX outputs I2; DEMUX activates Y2"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("mux_inputs", "Data Channels (I0–I3 / Y0–Y3)", "CH 0–3", "4 parallel data lines entering MUX (or leaving DEMUX)."),
                HardwarePartInfo("select_lines", "Select Control (S1, S0)", "SEL", "2-bit binary selector choosing channel 00, 01, 10, or 11."),
                HardwarePartInfo("mux_core", "MUX / DEMUX Switch Core", "SWITCH", "AND-OR combinational decoder matrix connecting selected channel."),
                HardwarePartInfo("single_line", "Common Line (Y / Din)", "COM", "Single shared output (MUX) or single incoming serial data line (DEMUX).")
            ),
            steps = listOf(
                SimulationStep(
                    title = "1. Select Channel I0 (S1S0 = 00)",
                    rtlAction = "S1S0 = 00 → Active Channel = 0",
                    description = "Channel 0 is connected to the common line; channels 1–3 are blocked.",
                    activePartIds = setOf("mux_inputs", "select_lines", "mux_core", "single_line"),
                    activeBusIds = setOf("ch0_bus", "sel_bus"),
                    registerSnapshot = mapOf("S1:S0" to "00", "ACTIVE" to "CH 0")
                ),
                SimulationStep(
                    title = "2. Select Channel I2 (S1S0 = 10)",
                    rtlAction = "S1S0 = 10 → Active Channel = 2",
                    description = "Changing Select bits to 10 instantly switches the hardware route to Channel 2!",
                    activePartIds = setOf("mux_inputs", "select_lines", "mux_core", "single_line"),
                    activeBusIds = setOf("ch2_bus", "sel_bus"),
                    registerSnapshot = mapOf("S1:S0" to "10", "ACTIVE" to "CH 2")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("Why is a Multiplexer called a Data Selector?", "Because it selects one out of many input data lines based on the binary code on its Select lines and forwards it to the output."),
                VivaQuestion("How can a 1:4 DEMUX act as a 2-to-4 Decoder?", "By permanently tying the single Data Input (Din) to Logic 1; whichever output line is selected by S1S0 becomes 1 while others stay 0.")
            )
        ),
        ConceptTopic(
            id = "flip_flops",
            unitId = "unit0",
            title = "Flip-Flops (SR, JK, D & T)",
            subtitle = "1-bit sequential memory cells, clock edge triggering & race-around",
            examWeightage = "Guaranteed 10-Mark",
            difficulty = "Core Exam",
            sceneType = SceneType3D.FLIP_FLOPS,
            interactiveModes = listOf("SR & D Flip-Flop", "JK & T Flip-Flop"),
            summary = "Unlike combinational circuits, Sequential Circuits have memory! A Flip-Flop is a clock-synchronized 1-bit storage element (stores Q = 0 or 1). Registers, Counters, and SRAM-like latches inside a CPU are all built from Flip-Flops.",
            keyPoints = listOf(
                "SR Flip-Flop: S=1,R=0 → Set (Q=1); S=0,R=1 → Reset (Q=0); S=0,R=0 → Hold; S=1,R=1 → INVALID/Forbidden.",
                "D (Data/Delay) Flip-Flop: Eliminates invalid state by feeding D and D'. Simply stores Q(next) = D on Clock edge (used in CPU Registers!).",
                "JK Flip-Flop: Fixes SR's invalid state! J=1, K=1 → TOGGLE (Q' = complement). Avoids Race-Around using Edge Triggering or Master-Slave.",
                "T (Toggle) Flip-Flop: J and K tied together. T=0 → Hold; T=1 → Toggle every clock pulse (used in Binary Counters & Frequency Dividers)."
            ),
            rtlOrFormulas = listOf(
                "SR: Q(t+1) = S + R'·Q  (Condition: S·R = 0)",
                "D:  Q(t+1) = D",
                "JK: Q(t+1) = J·Q' + K'·Q",
                "T:  Q(t+1) = T ⊕ Q"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("ff_inputs", "Control Inputs (S/R, D, J/K, T)", "IN", "Determines whether next state will Hold, Set, Reset, or Toggle."),
                HardwarePartInfo("ff_clock", "Clock Edge Detector (CLK ↑)", "CLK", "Synchronizes state change strictly on rising/falling clock edge."),
                HardwarePartInfo("ff_latch", "Cross-Coupled Latch Core", "LATCH", "Feedback loop of NAND/NOR gates holding 1 bit indefinitely while powered."),
                HardwarePartInfo("ff_outputs", "Complementary Outputs (Q, Q')", "Q / Q'", "Stored bit Q and its inverted twin Q'.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "1. Hold State (Wait for Rising Clock Edge)",
                    rtlAction = "CLK = 0 → Q(t+1) = Q(t) (Memory Hold)",
                    description = "Until the clock pulse triggers, feedback keeps the stored bit locked inside the cross-coupled gates.",
                    activePartIds = setOf("ff_latch", "ff_outputs"),
                    activeBusIds = setOf("ff_fb"),
                    registerSnapshot = mapOf("CLK" to "LOW", "Q" to "1", "Q'" to "0")
                ),
                SimulationStep(
                    title = "2. Clocked Edge Trigger Update",
                    rtlAction = "CLK ↑ → Q(t+1) updates per Input",
                    description = "On the rising clock edge, D latches input directly or JK toggles on J=1, K=1.",
                    activePartIds = setOf("ff_inputs", "ff_clock", "ff_latch", "ff_outputs"),
                    activeBusIds = setOf("ff_in_bus", "ff_clk_bus", "ff_out_bus"),
                    registerSnapshot = mapOf("CLK" to "↑ EDGE", "D / J=K=1" to "LATCHED")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("What is the difference between a Latch and a Flip-Flop?", "A Latch is level-triggered (responds continuously while Enable=1), whereas a Flip-Flop is edge-triggered (changes state only at the instant of a rising or falling clock edge)."),
                VivaQuestion("What is the Race-Around Condition in a JK Flip-Flop and how is it solved?", "When J=K=1 in a level-triggered JK latch and the clock pulse width exceeds propagation delay, Q toggles uncontrollably. Solved using Edge-Triggered or Master-Slave JK Flip-Flops.")
            )
        ),
        ConceptTopic(
            id = "bus_structures",
            unitId = "unit1",
            title = "Bus Structures & Tri-State Logic",
            subtitle = "Single-Bus vs Multi-Bus organization, Tri-State buffers & Bus Arbitration",
            examWeightage = "High Yield (8-Mark)",
            difficulty = "Foundation",
            sceneType = SceneType3D.BUS_STRUCTURES,
            interactiveModes = listOf("Single Shared Bus", "Multi-Bus Hierarchy", "Tri-State & Arbitration"),
            summary = "A Bus is a shared communication highway of parallel wires connecting Registers, ALU, Memory, and I/O. Since multiple devices share the same wires, only ONE device may transmit at a time using Tri-State Buffers (0, 1, High-Z) and Bus Arbitration.",
            keyPoints = listOf(
                "Single-Bus Organization: Cheap & simple, connects CPU, RAM, and I/O on one bus, but only 1 transfer per clock cycle.",
                "Multi-Bus Organization: Separates high-speed CPU–Cache/RAM (System Bus / Front-Side) from slower I/O Expansion Bus (PCIe/USB) via a Bridge.",
                "Tri-State Buffer: 3 output states — Logic 0, Logic 1, and High-Impedance (Z = electrically disconnected) to prevent short circuits.",
                "Bus Arbitration: Resolves conflicts when CPU and DMA both request the bus (Daisy Chaining, Polling, Independent Request)."
            ),
            rtlOrFormulas = listOf(
                "Tri-State Enable E=1 → Out = In; E=0 → Out = High-Z (Open)",
                "Bus Width (bits) × Clock (Hz) = Throughput (bps)",
                "Multi-Bus: CPU ↔ Bridge ↔ {Memory Bus, High-Speed I/O, Legacy I/O}"
            ),
            hardwareParts = listOf(
                HardwarePartInfo("bus_master_cpu", "CPU Bus Master", "CPU", "Primary bus master initiating instruction & data transfers."),
                HardwarePartInfo("tristate_buf", "Tri-State Buffer Array", "TRI-Z", "Isolates non-transmitting devices using High-Impedance (Z) state."),
                HardwarePartInfo("bus_bridge", "Bus Bridge / Arbiter", "ARB", "Connects fast Processor/Memory bus to slower Peripheral I/O bus."),
                HardwarePartInfo("bus_slaves", "Memory & I/O Devices", "SLAVES", "Respond when their address is decoded and bus grant is active.")
            ),
            steps = listOf(
                SimulationStep(
                    title = "1. Single vs Multi-Bus Topology",
                    rtlAction = "Enable(Device_A)=1, Others=High-Z",
                    description = "Toggle Single Shared Bus, Multi-Bus Hierarchy, and Tri-State Arbitration above to see how bridges & High-Z prevent bus contention.",
                    activePartIds = setOf("bus_master_cpu", "tristate_buf", "bus_bridge", "bus_slaves"),
                    activeBusIds = setOf("sys_bus", "io_exp_bus"),
                    registerSnapshot = mapOf("MASTER" to "CPU", "OTHERS" to "HIGH-Z")
                )
            ),
            vivaQuestions = listOf(
                VivaQuestion("Why are Tri-State Buffers mandatory on a shared system bus?", "If two devices drove 1 and 0 onto the same wire simultaneously, it would short-circuit (bus contention). High-Z disconnects idle devices electrically."),
                VivaQuestion("Why do computers use a Bus Bridge instead of connecting USB/Keyboard directly to the CPU Memory Bus?", "Because slow I/O peripherals would drag down the clock speed and capacitive load of the ultra-fast CPU–RAM bus.")
            )
        )
    )
}
