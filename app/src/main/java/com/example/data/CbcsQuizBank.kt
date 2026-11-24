package com.example.data

import com.example.domain.model.QuizQuestion

object CbcsQuizBank {
    val questions: List<QuizQuestion> = listOf(
        // Unit 0 - Digital Logic & Number Systems
        QuizQuestion(
            id = "q_dig_1",
            unitId = "unit0",
            question = "What is the Hexadecimal equivalent of the binary number (10111101)₂?",
            options = listOf("AD₁₆", "BD₁₆", "BC₁₆", "DB₁₆"),
            correctIndex = 1,
            explanation = "Group bits into 4s from right: 1011 = 11 (B) and 1101 = 13 (D), giving BD₁₆."
        ),
        QuizQuestion(
            id = "q_dig_2",
            unitId = "unit0",
            question = "Which binary code has consecutive numbers differing by only a single bit (unit-distance code)?",
            options = listOf("8421 BCD", "Excess-3 Code", "Gray Code", "ASCII"),
            correctIndex = 2,
            explanation = "Gray Code changes only 1 bit between adjacent values, eliminating transition errors in encoders and K-Maps."
        ),
        QuizQuestion(
            id = "q_dig_3",
            unitId = "unit0",
            question = "Which pair of logic gates are called Universal Gates because each alone can build any Boolean circuit?",
            options = listOf("AND and OR", "XOR and XNOR", "NAND and NOR", "NOT and Buffer"),
            correctIndex = 2,
            explanation = "Any combinational or sequential circuit (AND, OR, NOT, Flip-Flops) can be built using only NAND or only NOR gates."
        ),
        QuizQuestion(
            id = "q_dig_4",
            unitId = "unit0",
            question = "In a 4-variable K-Map, forming a Quad (group of 4 adjacent 1s) eliminates how many variables?",
            options = listOf("1 variable", "2 variables", "3 variables", "4 variables"),
            correctIndex = 1,
            explanation = "A group of 2^k cells in a K-Map eliminates k variables. Since 4 = 2², a Quad eliminates 2 variables."
        ),
        QuizQuestion(
            id = "q_dig_5",
            unitId = "unit0",
            question = "How many Select lines are required for a 16:1 Multiplexer?",
            options = listOf("2", "3", "4", "8"),
            correctIndex = 2,
            explanation = "For a 2^n : 1 Multiplexer, n select lines are needed. Since 16 = 2⁴, 4 select lines are required."
        ),
        QuizQuestion(
            id = "q_dig_6",
            unitId = "unit0",
            question = "In a JK Flip-Flop, what happens to output Q on the clock edge when J = 1 and K = 1?",
            options = listOf("Hold (No Change)", "Reset to 0", "Set to 1", "Toggle (Q')"),
            correctIndex = 3,
            explanation = "Unlike SR's invalid state, J=1 and K=1 toggles the JK Flip-Flop output to its complement Q'."
        ),
        QuizQuestion(
            id = "q_dig_7",
            unitId = "unit1",
            question = "What is the third state of a Tri-State Buffer used to isolate idle devices on a shared system bus?",
            options = listOf("Logic 0", "Logic 1", "High-Impedance (High-Z)", "Clock Pulse"),
            correctIndex = 2,
            explanation = "High-Impedance (High-Z) electrically disconnects the device output from the shared bus, preventing short-circuits."
        ),
        // Unit I
        QuizQuestion(
            id = "q1",
            unitId = "unit1",
            question = "Which register holds the address of the NEXT instruction to be fetched from memory?",
            options = listOf("Instruction Register (IR)", "Program Counter (PC)", "Memory Data Register (MDR)", "Accumulator (AC)"),
            correctIndex = 1,
            explanation = "The Program Counter (PC) stores the memory address of the next instruction and increments automatically during fetch."
        ),
        QuizQuestion(
            id = "q2",
            unitId = "unit1",
            question = "If a CPU has a 20-bit Address Bus, how many unique memory locations can it address?",
            options = listOf("64 KB (2^16)", "1 MB (2^20)", "4 GB (2^32)", "20 KB"),
            correctIndex = 1,
            explanation = "With k = 20 address lines, the addressable space is 2^20 = 1,048,576 locations (1 Mega locations)."
        ),
        QuizQuestion(
            id = "q3",
            unitId = "unit1",
            question = "What is the key advantage of Harvard Architecture over Von Neumann Architecture?",
            options = listOf(
                "Uses a single shared memory for simplicity",
                "Allows simultaneous instruction fetch and data access via separate buses",
                "Eliminates the need for an ALU",
                "Requires no Program Counter"
            ),
            correctIndex = 1,
            explanation = "Harvard separates Instruction and Data memories with independent buses, eliminating the Von Neumann bottleneck."
        ),
        QuizQuestion(
            id = "q4",
            unitId = "unit1",
            question = "In the Fetch Cycle, what is the correct first RTL step?",
            options = listOf("IR ← MDR", "PC ← M[MAR]", "MAR ← PC", "AC ← AC + MDR"),
            correctIndex = 2,
            explanation = "First, the address in PC is copied to MAR (MAR ← PC) so the memory unit knows which cell to read."
        ),
        // Unit II
        QuizQuestion(
            id = "q5",
            unitId = "unit2",
            question = "In which addressing mode is the operand included directly inside the instruction itself?",
            options = listOf("Direct Addressing", "Indirect Addressing", "Immediate Addressing", "Indexed Addressing"),
            correctIndex = 2,
            explanation = "Immediate addressing embeds the constant value directly in the instruction's address field—requiring zero extra memory reads."
        ),
        QuizQuestion(
            id = "q6",
            unitId = "unit2",
            question = "How does a Ripple Adder compute A - B in 2's complement arithmetic?",
            options = listOf("A + B + 0", "A + B' + 1", "A' + B + 0", "A ⊕ B"),
            correctIndex = 1,
            explanation = "Taking the 1's complement of B (B') and setting Carry-In = 1 yields A + (~B + 1) = A - B."
        ),
        QuizQuestion(
            id = "q7",
            unitId = "unit2",
            question = "Which Control Unit design stores control words in a ROM and is easier to modify?",
            options = listOf("Hardwired Control Unit", "Microprogrammed Control Unit", "DMA Controller", "Barrel Shifter"),
            correctIndex = 1,
            explanation = "A Microprogrammed Control Unit stores microinstructions in Control Memory (ROM), making complex CISC instruction sets easier to update."
        ),
        QuizQuestion(
            id = "q8",
            unitId = "unit2",
            question = "Which addressing mode computes Effective Address as EA = Address + Index Register (IX)?",
            options = listOf("Register Indirect", "Immediate", "Indexed Addressing", "Implied"),
            correctIndex = 2,
            explanation = "Indexed addressing adds an index register to the base address field, making it ideal for iterating through arrays."
        ),
        // Unit III
        QuizQuestion(
            id = "q9",
            unitId = "unit3",
            question = "What is the ideal maximum speedup of a 5-stage RISC pipeline (ignoring latch overhead)?",
            options = listOf("1x", "2.5x", "5x", "10x"),
            correctIndex = 2,
            explanation = "For a k-stage pipeline with a large number of instructions, ideal speedup approaches k = 5."
        ),
        QuizQuestion(
            id = "q10",
            unitId = "unit3",
            question = "Which technique resolves a Read-After-Write (RAW) data hazard WITHOUT stalling the pipeline?",
            options = listOf("Inserting NOP bubbles", "Operand Forwarding (Bypassing)", "Turning off the clock", "Increasing RAM size"),
            correctIndex = 1,
            explanation = "Operand forwarding routes the computed ALU result directly from EX/MEM or MEM/WB latches to the next instruction's EX input."
        ),
        QuizQuestion(
            id = "q11",
            unitId = "unit3",
            question = "In a 5-stage pipeline (IF, ID, EX, MEM, WB), which stage accesses Data Memory for LOAD/STORE?",
            options = listOf("IF", "ID", "EX", "MEM"),
            correctIndex = 3,
            explanation = "IF fetches instructions from Instruction Cache, while MEM reads/writes operands in Data Cache."
        ),
        // Unit IV
        QuizQuestion(
            id = "q12",
            unitId = "unit4",
            question = "Which cache mapping technique suffers most from Conflict Misses due to fixed modulo placement?",
            options = listOf("Fully Associative", "Direct Mapping", "4-Way Set Associative", "8-Way Set Associative"),
            correctIndex = 1,
            explanation = "In Direct Mapping, Block j can only sit in Line (j mod N), causing thrashing if two hot blocks share the same line."
        ),
        QuizQuestion(
            id = "q13",
            unitId = "unit4",
            question = "What hardware structure caches recent Virtual-to-Physical page translations to avoid extra RAM reads?",
            options = listOf("MDR", "TLB (Translation Lookaside Buffer)", "Barrel Shifter", "DMA Controller"),
            correctIndex = 1,
            explanation = "The TLB is a fast associative cache inside the MMU that accelerates virtual address translation."
        ),
        QuizQuestion(
            id = "q14",
            unitId = "unit4",
            question = "Looping repeatedly over a single variable primarily demonstrates which principle?",
            options = listOf("Spatial Locality", "Temporal Locality", "Page Faulting", "Cycle Stealing"),
            correctIndex = 1,
            explanation = "Temporal locality means an item referenced recently is likely to be referenced again soon."
        ),
        QuizQuestion(
            id = "q15",
            unitId = "unit4",
            question = "In a Write-Back cache, which flag bit indicates a cache line must be written to RAM before eviction?",
            options = listOf("Valid Bit", "Dirty (Modified) Bit", "Sign Bit", "Carry Bit"),
            correctIndex = 1,
            explanation = "The Dirty bit is set to 1 whenever the CPU modifies data in cache without immediately updating Main Memory."
        ),
        // Unit V
        QuizQuestion(
            id = "q16",
            unitId = "unit5",
            question = "Which two handshake signals allow a DMA Controller to take control of the system bus?",
            options = listOf("READ / WRITE", "HOLD / HLDA", "IF / ID", "PUSH / POP"),
            correctIndex = 1,
            explanation = "The DMA Controller asserts HOLD (Bus Request); the CPU finishes its bus cycle and asserts HLDA (Hold Acknowledge)."
        ),
        QuizQuestion(
            id = "q17",
            unitId = "unit5",
            question = "Under Flynn's Taxonomy, modern GPUs executing one kernel across thousands of pixels are classified as:",
            options = listOf("SISD", "SIMD", "MISD", "Single-Cycle"),
            correctIndex = 1,
            explanation = "SIMD (Single Instruction, Multiple Data) broadcasts one instruction stream across many parallel ALU lanes."
        ),
        QuizQuestion(
            id = "q18",
            unitId = "unit5",
            question = "Which I/O mode wastes the most CPU cycles in a busy-wait polling loop?",
            options = listOf("Direct Memory Access (DMA)", "Interrupt-Driven I/O", "Programmed I/O", "Channel I/O"),
            correctIndex = 2,
            explanation = "In Programmed I/O, the CPU continuously checks the device status register until the device becomes ready."
        ),
        QuizQuestion(
            id = "q19",
            unitId = "unit5",
            question = "According to Amdahl's Law, what limits the speedup of a multicore processor?",
            options = listOf("The size of the L1 cache", "The sequential (non-parallelizable) fraction (1 - P) of the program", "The color of the bus", "The number of registers"),
            correctIndex = 1,
            explanation = "Speedup = 1 / ((1 - P) + P/N). Even with infinite cores N, speedup can never exceed 1 / (1 - P)."
        )
    )

    fun getQuestionsForUnit(unitId: String): List<QuizQuestion> =
        if (unitId == "all") questions else questions.filter { it.unitId == unitId }
}
