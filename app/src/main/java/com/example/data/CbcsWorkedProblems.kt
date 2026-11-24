package com.example.data

import com.example.domain.model.AppLanguage
import com.example.domain.model.LocalizedProblemOverlay
import com.example.domain.model.ProblemStep
import com.example.domain.model.WorkedProblem

object CbcsWorkedProblems {

    private val problemsByTopic: Map<String, List<WorkedProblem>> = mapOf(
        "number_systems_codes" to listOf(
            WorkedProblem(
                id = "prob_num_conv",
                title = "Convert (45.625)₁₀ to Binary, Octal & Hex",
                statement = "Convert the decimal number (45.625)₁₀ into Binary (Base-2), Octal (Base-8), and Hexadecimal (Base-16) step by step.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Divide integer part (45) repeatedly by 2",
                        expression = "45÷2=22 R1 | 22÷2=11 R0 | 11÷2=5 R1 | 5÷2=2 R1 | 2÷2=1 R0 | 1÷2=0 R1",
                        explanation = "Read remainders from bottom to top (MSB to LSB): 45₁₀ = (101101)₂."
                    ),
                    ProblemStep(
                        title = "Multiply fractional part (0.625) by 2",
                        expression = "0.625×2 = 1.25 (int 1) | 0.25×2 = 0.50 (int 0) | 0.50×2 = 1.00 (int 1)",
                        explanation = "Read integer carries from top to bottom until fraction becomes 0: 0.625₁₀ = (.101)₂. Combined Binary = (101101.101)₂."
                    ),
                    ProblemStep(
                        title = "Group bits into 3s for Octal (Base-8)",
                        expression = "(101 101 . 101)₂  →  5  5 . 5",
                        explanation = "Group integer bits from right and fractional bits from left in sets of 3: 101 = 5. So Octal = (55.5)₈."
                    ),
                    ProblemStep(
                        title = "Group bits into 4s for Hexadecimal (Base-16)",
                        expression = "(0010 1101 . 1010)₂  →  2  D . A",
                        explanation = "Pad leading/trailing zeros to make 4-bit nibbles: 0010=2, 1101=13(D), 1010=10(A). So Hex = (2D.A)₁₆."
                    )
                ),
                finalAnswer = "Binary = (101101.101)₂  |  Octal = (55.5)₈  |  Hex = (2D.A)₁₆"
            ),
            WorkedProblem(
                id = "prob_codes",
                title = "Binary → Gray Code, 8421 BCD & Excess-3",
                statement = "Convert (29)₁₀ to 8421 BCD and Excess-3, and convert (10110)₂ to Gray Code.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "8421 BCD of (29)₁₀",
                        expression = "Digit 2 → 0010  |  Digit 9 → 1001",
                        explanation = "Write each decimal digit separately in 4-bit binary. Result: (0010 1001)BCD."
                    ),
                    ProblemStep(
                        title = "Excess-3 of (29)₁₀",
                        expression = "0010 + 0011 = 0101 (5)  |  1001 + 0011 = 1100 (12)",
                        explanation = "Add 3 (0011) to each BCD nibble to get Excess-3: (0101 1100)EX-3."
                    ),
                    ProblemStep(
                        title = "Binary (10110)₂ to Gray Code",
                        expression = "G4=1 | G3=1⊕0=1 | G2=0⊕1=1 | G1=1⊕1=0 | G0=1⊕0=1",
                        explanation = "Keep MSB unchanged (G4=B4=1), then XOR each adjacent pair of binary bits from left to right."
                    )
                ),
                finalAnswer = "BCD = 0010 1001  |  Excess-3 = 0101 1100  |  Gray = (11101)Gray"
            ),
            WorkedProblem(
                id = "prob_twos_comp",
                title = "8-Bit 2's Complement Subtraction: 14 - 9",
                statement = "Perform (14)₁₀ − (9)₁₀ using 8-bit 2's complement arithmetic.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Convert both numbers to 8-bit binary",
                        expression = "+14 = 0000 1110₂   |   +9 = 0000 1001₂",
                        explanation = "Represent both operands using 8 bits with MSB = 0 for positive numbers."
                    ),
                    ProblemStep(
                        title = "Take 2's complement of subtrahend (+9 → -9)",
                        expression = "1's Comp(9) = 1111 0110  →  +1 = 1111 0111₂ (-9)",
                        explanation = "Invert all 8 bits of 9 and add 1 to obtain -9 in 2's complement."
                    ),
                    ProblemStep(
                        title = "Add (+14) + (-9) and discard end-around carry",
                        expression = "0000 1110 + 1111 0111 = (1) 0000 0101₂",
                        explanation = "Because carry-out = 1, discard the 9th bit. The remaining 8 bits 0000 0101₂ equal +5."
                    )
                ),
                finalAnswer = "(0000 0101)₂ = +5₁₀"
            )
        ),
        "logic_gates" to listOf(
            WorkedProblem(
                id = "prob_nand_impl",
                title = "Implement F = AB + CD using ONLY NAND Gates",
                statement = "Realize the Boolean SOP expression F = A·B + C·D using minimum 2-input NAND gates.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Apply double negation to F",
                        expression = "F = ((A·B + C·D)')'",
                        explanation = "Double complement leaves the Boolean function unchanged: (X')' = X."
                    ),
                    ProblemStep(
                        title = "Apply De Morgan's Law on the inner bar",
                        expression = "F = ((A·B)' · (C·D)')'",
                        explanation = "Break the inner OR (+) into AND (·) with inverted terms: (X + Y)' = X' · Y'."
                    ),
                    ProblemStep(
                        title = "Map each inverted AND to a NAND gate",
                        expression = "G1 = NAND(A,B), G2 = NAND(C,D), F = NAND(G1, G2)",
                        explanation = "Every term (X·Y)' is directly a NAND operation, requiring 3 NAND gates total."
                    )
                ),
                finalAnswer = "F = NAND(NAND(A, B), NAND(C, D)) → Total 3 NAND gates"
            )
        ),
        "karnaugh_map" to listOf(
            WorkedProblem(
                id = "prob_kmap_min",
                title = "Minimize F(A,B,C,D) = Σm(0,1,2,3,5,7)",
                statement = "Simplify the 4-variable SOP function F(A,B,C,D) = Σm(0, 1, 2, 3, 5, 7) step by step.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Convert each minterm to 4-bit binary (ABCD)",
                        expression = "m0=0000, m1=0001, m2=0010, m3=0011, m5=0101, m7=0111",
                        explanation = "Write the 4-bit binary code for every minterm in the sum-of-products list."
                    ),
                    ProblemStep(
                        title = "Form Quad 1 from {m0, m1, m2, m3}",
                        expression = "0000, 0001, 0010, 0011 → A=0, B=0 constant → A'B'",
                        explanation = "Both C and D change across these 4 adjacent cells, while A=0 and B=0 stay fixed, giving A'B'."
                    ),
                    ProblemStep(
                        title = "Form Quad 2 from {m1, m3, m5, m7}",
                        expression = "0001, 0011, 0101, 0111 → A=0, D=1 constant → A'D",
                        explanation = "B and C change across {m1, m3, m5, m7}, while A=0 and D=1 stay constant, giving A'D."
                    )
                ),
                finalAnswer = "Minimized SOP: F(A,B,C,D) = A'B' + A'D"
            )
        ),
        "mux_demux" to listOf(
            WorkedProblem(
                id = "prob_mux_fn",
                title = "Implement F(A,B,C) = Σm(1,2,5,7) with 4:1 MUX",
                statement = "Implement the 3-variable function F(A,B,C) = Σm(1, 2, 5, 7) using a 4:1 MUX with A, B as Select lines.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Group minterms by Select lines AB",
                        expression = "I0(AB=00): m0,m1 | I1(AB=01): m2,m3 | I2(AB=10): m4,m5 | I3(AB=11): m6,m7",
                        explanation = "Each of the 4 MUX inputs covers two minterms corresponding to C=0 (even) and C=1 (odd)."
                    ),
                    ProblemStep(
                        title = "Evaluate each input in terms of C",
                        expression = "I0={m1}=C | I1={m2}=C' | I2={m5}=C | I3={m7}=C",
                        explanation = "Since m1(C=1) is in F and m0(C=0) is not, I0 = C. Since m2(C=0) is in F and m3 is not, I1 = C'. Similarly I2 = C, I3 = C."
                    )
                ),
                finalAnswer = "S1=A, S0=B  |  I0 = C, I1 = C', I2 = C, I3 = C"
            )
        ),
        "flip_flops" to listOf(
            WorkedProblem(
                id = "prob_ff_counter",
                title = "Design Mod-8 Counter Stage Count & Frequency Division",
                statement = "How many Flip-Flops are needed for a Mod-12 counter, and what is the output frequency if Clock = 1.6 MHz feeds 4 T-FF stages?",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Find minimum Flip-Flops for Mod-12",
                        expression = "2^n ≥ 12 → 2³=8 (<12), 2⁴=16 (≥12) → n = 4 Flip-Flops",
                        explanation = "Each Flip-Flop stores 1 bit, so n Flip-Flops count up to 2^n states. 4 Flip-Flops are needed for Mod-12."
                    ),
                    ProblemStep(
                        title = "Calculate frequency division across 4 toggle stages",
                        expression = "f_out = f_clk / 2⁴ = 1,600,000 Hz / 16 = 100,000 Hz",
                        explanation = "Each T-Flip-Flop (with T=1) divides the input clock frequency by 2."
                    )
                ),
                finalAnswer = "4 Flip-Flops required  |  Output Frequency = 100 kHz"
            )
        ),
        "bus_structures" to listOf(
            WorkedProblem(
                id = "prob_bus_bw",
                title = "Calculate System Bus Bandwidth (64-bit @ 200 MHz)",
                statement = "A processor has a 64-bit Data Bus operating at 200 MHz, transferring 1 word per clock cycle. Find peak Bus Bandwidth.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Convert bus width from bits to Bytes",
                        expression = "Width in Bytes = 64 bits / 8 = 8 Bytes per transfer",
                        explanation = "Since 1 Byte = 8 bits, a 64-bit bus moves 8 Bytes in a single clock cycle."
                    ),
                    ProblemStep(
                        title = "Multiply Bytes per cycle by Bus Frequency",
                        expression = "Bandwidth = 8 B × 200 × 10⁶ Hz = 1.6 × 10⁹ B/s = 1.6 GB/s",
                        explanation = "Peak data transfer rate is bus width (in Bytes) multiplied by clock cycles per second."
                    )
                ),
                finalAnswer = "Peak Bus Bandwidth = 1.6 GB/s (1,600 MB/s)"
            )
        ),
        "von_neumann" to listOf(
            WorkedProblem(
                id = "prob_addr_space",
                title = "Addressable Memory from Address & Data Bus Width",
                statement = "A CPU has a 24-bit Address Bus and a 16-bit Data Bus (word-addressable). Calculate maximum addressable memory in Bytes and MB.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Compute total addressable memory locations",
                        expression = "Locations = 2²⁴ = 2⁴ × 2²⁰ = 16 × 1M = 16 M words",
                        explanation = "An n-bit unidirectional Address Bus uniquely selects 2^n memory addresses."
                    ),
                    ProblemStep(
                        title = "Multiply locations by word size (16 bits = 2 Bytes)",
                        expression = "Total Memory = 16 M words × 2 Bytes/word = 32 MB",
                        explanation = "With each word storing 2 Bytes (16-bit data bus), total capacity is 32 Megabytes."
                    )
                ),
                finalAnswer = "16 M words = 32 MB maximum addressable memory"
            )
        ),
        "harvard_vs_von" to listOf(
            WorkedProblem(
                id = "prob_harvard_cycles",
                title = "Cycles for 10 Load/Store Instructions: Harvard vs Von Neumann",
                statement = "Suppose every instruction needs 1 cycle to fetch and 1 cycle for data memory access. How many cycles do 10 instructions take if fetch and data access overlap in Harvard vs serialize in Von Neumann?",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Von Neumann (Single Shared Memory Bus)",
                        expression = "Cycles = 10 × (1 Fetch + 1 Data) = 20 cycles",
                        explanation = "Because code and data share one bus, each instruction stalls data access until fetch finishes."
                    ),
                    ProblemStep(
                        title = "Harvard (Dual Independent Buses)",
                        expression = "Cycles = 1 (initial fetch) + 10 = 11 cycles",
                        explanation = "While Instruction i accesses Data Memory on the Data Bus, Instruction i+1 fetches simultaneously on the Instruction Bus."
                    )
                ),
                finalAnswer = "Von Neumann = 20 cycles  |  Harvard = 11 cycles (1.82× faster)"
            )
        ),
        "instruction_cycle" to listOf(
            WorkedProblem(
                id = "prob_rtl_trace",
                title = "Step-by-Step RTL Trace for ADD R1, [300]",
                statement = "Given PC = 100 and Memory[300] = 45, R1 = 15. Trace the exact Register values after Fetch, Decode, and Execute of ADD R1, [300].",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "T1–T3: Fetch Phase",
                        expression = "MAR ← 100;  MDR ← M[100] (ADD R1,[300]);  IR ← MDR;  PC ← 101",
                        explanation = "MAR gets PC=100, instruction enters IR, and PC increments to 101 for the next instruction."
                    ),
                    ProblemStep(
                        title = "T4: Operand Fetch from Memory[300]",
                        expression = "MAR ← 300;  MDR ← M[300] = 45",
                        explanation = "The CU decodes operand address 300 from IR and loads 45 from RAM into MDR."
                    ),
                    ProblemStep(
                        title = "T5: Execute ALU Addition",
                        expression = "R1 ← R1 + MDR = 15 + 45 = 60",
                        explanation = "ALU adds R1 (15) and MDR (45) and stores 60 back into Register R1."
                    )
                ),
                finalAnswer = "Final State: PC = 101, MAR = 300, MDR = 45, R1 = 60"
            )
        ),
        "alu_micro_ops" to listOf(
            WorkedProblem(
                id = "prob_alu_overflow",
                title = "8-Bit Signed Addition & Overflow Check: (+70) + (+80)",
                statement = "Perform (+70) + (+80) in 8-bit 2's complement and check the Sign (N) and Overflow (V) flags.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Convert +70 and +80 to 8-bit binary",
                        expression = "+70 = 0100 0110₂   |   +80 = 0101 0000₂",
                        explanation = "Both numbers are positive, so their MSB (sign bit) is 0."
                    ),
                    ProblemStep(
                        title = "Perform 8-bit binary addition",
                        expression = "0100 0110 + 0101 0000 = 1001 0110₂ (-106 in 2's comp!)",
                        explanation = "Adding bit-by-bit gives carry into MSB (C_in = 1) and carry out of MSB (C_out = 0)."
                    ),
                    ProblemStep(
                        title = "Detect Overflow via C_in ⊕ C_out",
                        expression = "V = C_in ⊕ C_out = 1 ⊕ 0 = 1 (OVERFLOW!)",
                        explanation = "Two positive numbers produced MSB = 1 (negative). Since +150 exceeds +127 (max 8-bit signed), Overflow V = 1."
                    )
                ),
                finalAnswer = "Result = 1001 0110₂, N = 1, V = 1 (Signed Overflow Detected)"
            )
        ),
        "addressing_modes" to listOf(
            WorkedProblem(
                id = "prob_ea_calc",
                title = "Find Effective Address (EA) & Operand Across Modes",
                statement = "Instruction at PC=200 has Address Field = 500. Registers: R1 = 400, IX = 50. Memory: M[400]=750, M[500]=800, M[550]=900, M[800]=950. Find EA & Operand.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Immediate Mode",
                        expression = "Operand = 500 (no memory lookup needed)",
                        explanation = "The address field 500 itself is the literal operand value."
                    ),
                    ProblemStep(
                        title = "Direct vs Indirect Mode",
                        expression = "Direct: EA=500, Op=800  |  Indirect: EA=M[500]=800, Op=M[800]=950",
                        explanation = "Direct uses 500 as EA; Indirect reads M[500]=800 as the pointer to the operand."
                    ),
                    ProblemStep(
                        title = "Register Indirect & Indexed Mode",
                        expression = "Reg Indirect(R1): EA=400, Op=750  |  Indexed: EA=500+IX=550, Op=900",
                        explanation = "Register Indirect gets EA from R1; Indexed adds Address (500) + IX (50) = 550."
                    )
                ),
                finalAnswer = "Immediate: 500 | Direct: 800 | Indirect: 950 | Reg-Ind: 750 | Indexed: 900"
            )
        ),
        "control_unit" to listOf(
            WorkedProblem(
                id = "prob_cu_rom",
                title = "Calculate Control Memory ROM Size",
                statement = "A Microprogrammed Control Unit supports 128 microinstructions, each with a 24-bit Control Word. Calculate Control Memory size in Bits and Bytes, and CAR width.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Find Control Address Register (CAR) width",
                        expression = "128 = 2⁷ → CAR requires 7 bits",
                        explanation = "To uniquely address 128 rows in Control Memory, CAR needs log₂(128) = 7 bits."
                    ),
                    ProblemStep(
                        title = "Compute total ROM capacity",
                        expression = "Size = 128 × 24 bits = 3,072 bits = 3,072 / 8 = 384 Bytes",
                        explanation = "Multiply the number of microinstructions by control word width."
                    )
                ),
                finalAnswer = "CAR = 7 bits  |  Control Memory = 3,072 bits (384 Bytes)"
            )
        ),
        "pipeline_5_stage" to listOf(
            WorkedProblem(
                id = "prob_pipe_speedup",
                title = "5-Stage Pipeline Speedup for 100 Instructions",
                statement = "A non-pipelined CPU takes 10 ns per instruction. A 5-stage pipelined version has a 2 ns clock cycle. Find Speedup for n = 100 instructions and Ideal Max Speedup.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Compute Non-Pipelined Time",
                        expression = "T_nonpipe = n × T_inst = 100 × 10 ns = 1,000 ns",
                        explanation = "Each of the 100 instructions runs completely for 10 ns sequentially."
                    ),
                    ProblemStep(
                        title = "Compute Pipelined Time: (k + n - 1) × Tp",
                        expression = "T_pipe = (5 + 100 - 1) × 2 ns = 104 × 2 ns = 208 ns",
                        explanation = "The first instruction finishes in 5 cycles; the remaining 99 finish 1 per cycle."
                    ),
                    ProblemStep(
                        title = "Compute Speedup & Ideal Limit",
                        expression = "Speedup = 1000 / 208 = 4.81×   |   Ideal Max (n → ∞) = k = 5×",
                        explanation = "As n grows very large, speedup approaches k = 5."
                    )
                ),
                finalAnswer = "Speedup = 4.81× (208 ns vs 1,000 ns)  |  Max Speedup = 5×"
            ),
            WorkedProblem(
                id = "prob_pipe_cpi",
                title = "Effective CPI with Load-Use Data Hazard Stalls",
                statement = "A 5-stage pipeline has Ideal CPI = 1. If 20% of instructions are Loads and 50% of those cause a 1-cycle RAW stall, find Effective CPI.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Find stall cycles per instruction",
                        expression = "Stalls/inst = 0.20 × 0.50 × 1 cycle = 0.10 cycles",
                        explanation = "Only half of the 20% Load instructions incur 1 stall cycle."
                    ),
                    ProblemStep(
                        title = "Add stalls to Ideal CPI",
                        expression = "Effective CPI = 1.0 + 0.10 = 1.10",
                        explanation = "Total cycles per instruction equals base CPI plus average stall penalty."
                    )
                ),
                finalAnswer = "Effective CPI = 1.10 cycles/instruction"
            )
        ),
        "memory_hierarchy" to listOf(
            WorkedProblem(
                id = "prob_eat_cache",
                title = "Effective Access Time (EAT) with 95% Cache Hit Rate",
                statement = "A system has L1 Cache access time Tc = 2 ns and Main RAM time Tm = 50 ns. If Cache Hit Ratio h = 0.95 (95%), calculate Effective Access Time (simultaneous vs sequential lookup).",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Calculate Miss Ratio (1 - h)",
                        expression = "Miss Ratio = 1 - 0.95 = 0.05 (5%)",
                        explanation = "Only 5% of memory requests miss the cache and must access RAM."
                    ),
                    ProblemStep(
                        title = "Apply Simultaneous (Parallel) Lookup EAT formula",
                        expression = "EAT = h·Tc + (1-h)·Tm = (0.95 × 2) + (0.05 × 50) = 1.9 + 2.5 = 4.4 ns",
                        explanation = "Wait penalty on miss is Tm if cache & RAM are checked in parallel."
                    ),
                    ProblemStep(
                        title = "Apply Sequential Lookup EAT formula",
                        expression = "EAT = Tc + (1-h)·Tm = 2 + (0.05 × 50) = 2 + 2.5 = 4.5 ns",
                        explanation = "If RAM starts only after cache miss detection, every access pays Tc first."
                    )
                ),
                finalAnswer = "EAT (Parallel) = 4.4 ns  |  EAT (Sequential) = 4.5 ns"
            )
        ),
        "cache_mapping" to listOf(
            WorkedProblem(
                id = "prob_cache_split",
                title = "32-Bit Address Split: Tag, Index & Block Offset Bits",
                statement = "A byte-addressable CPU uses 32-bit addresses with a 64 KB Direct-Mapped Cache and 64-Byte blocks. Find Block Offset, Index, and Tag bits.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Find Block (Byte) Offset bits",
                        expression = "Block Size = 64 B = 2⁶ → Offset = 6 bits",
                        explanation = "Offset selects 1 byte within a 64-byte cache line."
                    ),
                    ProblemStep(
                        title = "Find number of cache lines & Index bits",
                        expression = "Lines = 64 KB / 64 B = 2¹⁶ / 2⁶ = 2¹⁰ = 1024 → Index = 10 bits",
                        explanation = "Direct mapping needs log₂(Lines) = 10 index bits to select one cache line."
                    ),
                    ProblemStep(
                        title = "Subtract Offset & Index from 32-bit address",
                        expression = "Tag = 32 - (10 + 6) = 16 bits",
                        explanation = "Remaining upper address bits form the Tag stored in the cache directory."
                    )
                ),
                finalAnswer = "Tag = 16 bits  |  Index = 10 bits  |  Block Offset = 6 bits"
            )
        ),
        "virtual_memory" to listOf(
            WorkedProblem(
                id = "prob_vm_split",
                title = "Virtual Page Number (VPN) & Page Table Entries",
                statement = "A CPU generates 32-bit Virtual Addresses with 4 KB pages and 256 MB Physical RAM. Find VPN bits, Page Offset bits, and Physical Frame (PFN) bits.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Find Page Offset bits from Page Size",
                        expression = "Page Size = 4 KB = 2¹² B → Page Offset = 12 bits",
                        explanation = "Offset bits stay identical during Virtual-to-Physical translation."
                    ),
                    ProblemStep(
                        title = "Find Virtual Page Number (VPN) bits",
                        expression = "VPN = 32 - 12 = 20 bits → 2²⁰ (~1 Million) virtual pages",
                        explanation = "Upper 20 bits index the Page Table."
                    ),
                    ProblemStep(
                        title = "Find Physical Frame Number (PFN) bits",
                        expression = "RAM = 256 MB = 2²⁸ B → Physical Addr = 28 bits → PFN = 28 - 12 = 16 bits",
                        explanation = "256 MB RAM holds 2¹⁶ (65,536) physical frames."
                    )
                ),
                finalAnswer = "VPN = 20 bits | Offset = 12 bits | PFN = 16 bits (28-bit Phys Addr)"
            )
        ),
        "io_dma" to listOf(
            WorkedProblem(
                id = "prob_dma_cycles",
                title = "CPU Cycles Saved: DMA Cycle Stealing vs Programmed I/O",
                statement = "A disk transfers a 4 KB block (32-bit words) into RAM. Programmed I/O takes 20 CPU cycles/word; DMA steals 1 memory cycle/word plus 100 setup/interrupt cycles. Compare CPU cycles spent.",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Count total 32-bit (4-Byte) words in 4 KB",
                        expression = "Words = 4,096 B / 4 B = 1,024 words",
                        explanation = "Each transfer moves 4 Bytes over the 32-bit bus."
                    ),
                    ProblemStep(
                        title = "Programmed I/O CPU cycles",
                        expression = "CPU Cycles (PIO) = 1,024 × 20 = 20,480 cycles",
                        explanation = "CPU polls status, reads register, and writes RAM for every word."
                    ),
                    ProblemStep(
                        title = "DMA Cycle-Stealing CPU impact",
                        expression = "DMA CPU Cycles = 100 + 1,024 × 1 = 1,124 cycles",
                        explanation = "DMA handles addressing/counting autonomously and only borrows 1 bus cycle per word."
                    )
                ),
                finalAnswer = "Programmed I/O = 20,480 cycles vs DMA = 1,124 cycles (~18.2× less CPU overhead)"
            )
        ),
        "flynns_multicore" to listOf(
            WorkedProblem(
                id = "prob_amdahl",
                title = "Amdahl's Law Speedup for 4-Core & Infinite-Core CPUs",
                statement = "75% of a program can run in parallel (P = 0.75) and 25% is strictly sequential. Compute Speedup on N = 4 cores and maximum theoretical speedup (N → ∞).",
                diagramNecessary = false,
                steps = listOf(
                    ProblemStep(
                        title = "Apply Amdahl's Law for N = 4 cores",
                        expression = "S(4) = 1 / ((1 - 0.75) + 0.75 / 4) = 1 / (0.25 + 0.1875) = 1 / 0.4375",
                        explanation = "Sequential time remains 0.25 while parallel time shrinks by 4× to 0.1875."
                    ),
                    ProblemStep(
                        title = "Compute 4-core Speedup value",
                        expression = "S(4) = 2.29×",
                        explanation = "Even with 4 cores, speedup is 2.29× rather than 4×."
                    ),
                    ProblemStep(
                        title = "Take limit as N → ∞",
                        expression = "S_max = 1 / (1 - 0.75) = 1 / 0.25 = 4.0×",
                        explanation = "The 25% sequential bottleneck caps speedup forever at 4×."
                    )
                ),
                finalAnswer = "4-Core Speedup = 2.29×  |  Max Possible Speedup = 4.0×"
            )
        )
    )

    fun getProblemsForTopic(topicId: String, language: AppLanguage): List<WorkedProblem> {
        val base = problemsByTopic[topicId].orEmpty()
        if (language == AppLanguage.ENGLISH) return base

        val overlayMap: Map<String, LocalizedProblemOverlay> = when (language) {
            AppLanguage.HINGLISH -> CbcsWorkedProblemsHinglish.problems
            AppLanguage.TELUGU -> CbcsWorkedProblemsTelugu.problems
            AppLanguage.ENGLISH -> emptyMap()
        }

        return base.map { problem ->
            val ov = overlayMap[problem.id] ?: return@map problem
            problem.copy(
                title = ov.title,
                statement = ov.statement,
                steps = problem.steps.mapIndexed { idx, step ->
                    step.copy(
                        title = ov.stepTitles.getOrNull(idx) ?: step.title,
                        explanation = ov.stepExplanations.getOrNull(idx) ?: step.explanation
                    )
                },
                finalAnswer = ov.finalAnswer
            )
        }
    }
}
