package com.example.data

import com.example.domain.model.LocalizedProblemOverlay

object CbcsWorkedProblemsHinglish {

    val problems: Map<String, LocalizedProblemOverlay> = mapOf(
        "prob_num_conv" to LocalizedProblemOverlay(
            title = "(45.625)₁₀ ko Binary, Octal aur Hex mein badlo",
            statement = "Decimal number (45.625)₁₀ ko bina diagram ke step-by-step Binary, Octal aur Hexadecimal mein convert karein.",
            stepTitles = listOf(
                "Integer part (45) ko lagatar 2 se divide karo",
                "Fraction part (0.625) ko 2 se multiply karo",
                "Octal (Base-8) ke liye 3-3 bits ka group banao",
                "Hexadecimal (Base-16) ke liye 4-4 bits ka group banao"
            ),
            stepExplanations = listOf(
                "Remainders ko neeche se upar (MSB → LSB) padho: 45₁₀ = (101101)₂.",
                "Decimal se pehle wale integer carry ko upar se neeche padho: 0.625₁₀ = (.101)₂. Poora Binary = (101101.101)₂.",
                "Integer ko right se aur fraction ko left se 3-3 bits mein baanto: 101 = 5. Isliye Octal = (55.5)₈.",
                "4-4 bits ke nibble banane ke liye zero pad karo: 0010=2, 1101=13(D), 1010=10(A). Hex = (2D.A)₁₆."
            ),
            finalAnswer = "Binary = (101101.101)₂  |  Octal = (55.5)₈  |  Hex = (2D.A)₁₆"
        ),
        "prob_codes" to LocalizedProblemOverlay(
            title = "Binary → Gray Code, 8421 BCD aur Excess-3 Conversion",
            statement = "(29)₁₀ ko 8421 BCD aur Excess-3 mein badlo, aur (10110)₂ ka Gray Code nikalo.",
            stepTitles = listOf(
                "(29)₁₀ ka 8421 BCD nikalo",
                "(29)₁₀ ka Excess-3 Code nikalo",
                "Binary (10110)₂ se Gray Code banao"
            ),
            stepExplanations = listOf(
                "Har decimal digit ko alag 4-bit binary mein likho: 2 = 0010, 9 = 1001 → (0010 1001)BCD.",
                "Har BCD digit mein 3 (0011) add kar do → (0101 1100)EX-3.",
                "Pehli bit (MSB) same rakho (1), phir har padosi pair ka XOR karte jaao."
            ),
            finalAnswer = "BCD = 0010 1001  |  Excess-3 = 0101 1100  |  Gray = (11101)Gray"
        ),
        "prob_twos_comp" to LocalizedProblemOverlay(
            title = "8-Bit 2's Complement Subtraction: 14 - 9",
            statement = "8-bit 2's complement method se (14)₁₀ − (9)₁₀ step-by-step solve karein.",
            stepTitles = listOf(
                "Dono numbers ko 8-bit binary mein likho",
                "Subtrahend (+9) ka 2's complement (-9) nikalo",
                "(+14) aur (-9) ko jodo aur extra carry hatao"
            ),
            stepExplanations = listOf(
                "Positive numbers ke liye MSB 0 rakhte hue 8-bit binary likhein.",
                "9 ke saare bits ulte karke (1's complement) usme +1 jodo → -9 mil jaayega.",
                "Addition mein 9th carry bit = 1 aata hai, jise discard karne par 0000 0101₂ (+5) bachta hai."
            ),
            finalAnswer = "(0000 0101)₂ = +5₁₀"
        ),
        "prob_nand_impl" to LocalizedProblemOverlay(
            title = "Sirf NAND Gates se F = AB + CD banao",
            statement = "Boolean equation F = A·B + C·D ko minimum 2-input NAND gates se step-by-step implement karein.",
            stepTitles = listOf(
                "F par double complement lagao",
                "Andar wale bar par De Morgan's Law lagao",
                "Har (X·Y)' term ko ek NAND gate bana do"
            ),
            stepExplanations = listOf(
                "Do baar NOT lagane se value nahi badalti: (X')' = X.",
                "De Morgan ke hisaab se (X + Y)' = X' · Y' ban jaata hai.",
                "Ab teeno brackets seedha NAND gate hain — kul 3 NAND gates lagenge."
            ),
            finalAnswer = "F = NAND(NAND(A, B), NAND(C, D)) → Kul 3 NAND gates"
        ),
        "prob_kmap_min" to LocalizedProblemOverlay(
            title = "F(A,B,C,D) = Σm(0,1,2,3,5,7) ko Simplify karo",
            statement = "4-variable SOP function F(A,B,C,D) = Σm(0, 1, 2, 3, 5, 7) ko step-by-step minimize karein.",
            stepTitles = listOf(
                "Har minterm ko 4-bit binary (ABCD) mein likho",
                "{m0, m1, m2, m3} ka pehla Quad banao",
                "{m1, m3, m5, m7} ka doosra Quad banao"
            ),
            stepExplanations = listOf(
                "Sabhi 6 minterms ke binary combinations likho taaki common bits dikh sakein.",
                "In 4 cells mein C aur D badalte hain par A=0, B=0 fixed rehta hai → A'B'.",
                "In 4 cells mein B aur C badalte hain par A=0, D=1 fixed rehta hai → A'D."
            ),
            finalAnswer = "Minimized SOP: F(A,B,C,D) = A'B' + A'D"
        ),
        "prob_mux_fn" to LocalizedProblemOverlay(
            title = "4:1 MUX se F(A,B,C) = Σm(1,2,5,7) Implement karo",
            statement = "A aur B ko Select lines maankar F(A,B,C) = Σm(1, 2, 5, 7) ko 4:1 MUX se step-by-step solve karein.",
            stepTitles = listOf(
                "Minterms ko Select lines AB ke hisaab se baanto",
                "Har input (I0–I3) ki value C ke roop mein nikalo"
            ),
            stepExplanations = listOf(
                "Har AB pair ke andar 2 minterms aate hain: ek C=0 (even) aur ek C=1 (odd).",
                "I0 mein sirf m1 (C=1) hai → C. I1 mein sirf m2 (C=0) hai → C'. I2 mein m5 → C, I3 mein m7 → C."
            ),
            finalAnswer = "S1=A, S0=B  |  I0 = C, I1 = C', I2 = C, I3 = C"
        ),
        "prob_ff_counter" to LocalizedProblemOverlay(
            title = "Mod-12 Counter ke Flip-Flops aur Frequency Division",
            statement = "Mod-12 counter ke liye kitne Flip-Flops lagenge, aur 1.6 MHz clock ko 4 T-FF se divide karne par output kya hoga?",
            stepTitles = listOf(
                "Mod-12 ke liye minimum Flip-Flops nikalo",
                "4 toggle stages ke baad output frequency nikalo"
            ),
            stepExplanations = listOf(
                "3 Flip-Flops sirf 8 states (0–7) ginte hain; 12 states ke liye 2⁴ = 16 yaani 4 Flip-Flops chahiye.",
                "Har T Flip-Flop frequency ko aadha (/2) karta hai, to 4 stages ÷16 karenge."
            ),
            finalAnswer = "4 Flip-Flops chahiye  |  Output Frequency = 100 kHz"
        ),
        "prob_bus_bw" to LocalizedProblemOverlay(
            title = "System Bus Bandwidth Nikalo (64-bit @ 200 MHz)",
            statement = "Ek CPU mein 64-bit Data Bus 200 MHz par har clock cycle mein 1 word transfer karti hai. Peak Bandwidth nikalo.",
            stepTitles = listOf(
                "Bus width ko bits se Bytes mein badlo",
                "Bytes × Frequency se peak bandwidth nikalo"
            ),
            stepExplanations = listOf(
                "1 Byte = 8 bits hota hai, to 64-bit bus ek cycle mein 8 Bytes bhejti hai.",
                "8 Bytes ko 200 MHz (200 million cycles/sec) se multiply karein."
            ),
            finalAnswer = "Peak Bus Bandwidth = 1.6 GB/s (1,600 MB/s)"
        ),
        "prob_addr_space" to LocalizedProblemOverlay(
            title = "Address Bus se Maximum RAM Capacity Nikalo",
            statement = "Ek CPU mein 24-bit Address Bus aur 16-bit Data Bus (word-addressable) hai. Total addressable RAM MB mein nikalo.",
            stepTitles = listOf(
                "Total unique memory addresses nikalo",
                "Locations ko word size (2 Bytes) se multiply karo"
            ),
            stepExplanations = listOf(
                "24-bit address bus se 2²⁴ = 16 Million alag memory cells address hote hain.",
                "Har word 16 bits (2 Bytes) ka hai, isliye 16M × 2 Bytes = 32 MB."
            ),
            finalAnswer = "16 M words = 32 MB maximum memory"
        ),
        "prob_harvard_cycles" to LocalizedProblemOverlay(
            title = "10 Instructions ke Cycles: Harvard vs Von Neumann",
            statement = "Har instruction ko Fetch mein 1 cycle aur Data access mein 1 cycle lagta hai. 10 instructions mein dono architectures kitne cycles lenge?",
            stepTitles = listOf(
                "Von Neumann (Single Shared Bus)",
                "Harvard (Do Alag Buses)"
            ),
            stepExplanations = listOf(
                "Single bus par Fetch aur Data ek ke baad ek chalte hain, isliye 10 × 2 = 20 cycles.",
                "Jab ek instruction data read karta hai, agla usi cycle mein fetch ho jaata hai → sirf 11 cycles!"
            ),
            finalAnswer = "Von Neumann = 20 cycles  |  Harvard = 11 cycles (1.82× tez)"
        ),
        "prob_rtl_trace" to LocalizedProblemOverlay(
            title = "ADD R1, [300] ka Step-by-Step RTL Trace",
            statement = "PC = 100, Memory[300] = 45, R1 = 15 hai. ADD R1, [300] ke baad har register ki value step-by-step nikalo.",
            stepTitles = listOf(
                "T1–T3: Instruction Fetch",
                "T4: Memory se Operand Padhna",
                "T5: ALU Addition Execute Karna"
            ),
            stepExplanations = listOf(
                "MAR ko PC=100 milta hai, RAM se instruction IR mein aata hai aur PC badhkar 101 ho jaata hai.",
                "CU IR se address 300 nikalta hai aur M[300]=45 ko MDR mein laata hai.",
                "ALU R1 (15) aur MDR (45) ko jodkar result 60 wapas R1 mein store karta hai."
            ),
            finalAnswer = "Final State: PC = 101, MAR = 300, MDR = 45, R1 = 60"
        ),
        "prob_alu_overflow" to LocalizedProblemOverlay(
            title = "8-Bit Signed Addition aur Overflow Check: (+70) + (+80)",
            statement = "8-bit 2's complement mein (+70) + (+80) calculate karo aur Sign (N) wa Overflow (V) flags check karo.",
            stepTitles = listOf(
                "+70 aur +80 ko 8-bit binary mein badlo",
                "Dono 8-bit binary numbers ko jodo",
                "C_in ⊕ C_out se Overflow check karo"
            ),
            stepExplanations = listOf(
                "Dono positive hain isliye sign bit (MSB) = 0 rahegi.",
                "Jodne par result 1001 0110₂ aata hai jiska MSB 1 (negative -106) ho gaya!",
                "Do positive numbers jodne par negative answer aaya aur C_in ≠ C_out hai — matlab Overflow V = 1."
            ),
            finalAnswer = "Result = 1001 0110₂, N = 1, V = 1 (Overflow hua!)"
        ),
        "prob_ea_calc" to LocalizedProblemOverlay(
            title = "Har Addressing Mode ka Effective Address (EA) Nikalo",
            statement = "Address Field = 500, R1 = 400, IX = 50; M[400]=750, M[500]=800, M[550]=900, M[800]=950. Sabhi modes ke EA aur Operand nikalo.",
            stepTitles = listOf(
                "Immediate Mode",
                "Direct vs Indirect Mode",
                "Register Indirect aur Indexed Mode"
            ),
            stepExplanations = listOf(
                "Immediate mein 500 khud hi direct data value hai.",
                "Direct mein EA=500 (Op=800); Indirect mein M[500]=800 ko pointer maankar M[800]=950 padhte hain.",
                "Reg Indirect mein EA R1 (400) se milta hai; Indexed mein 500 + IX(50) = 550 banta hai."
            ),
            finalAnswer = "Immediate: 500 | Direct: 800 | Indirect: 950 | Reg-Ind: 750 | Indexed: 900"
        ),
        "prob_cu_rom" to LocalizedProblemOverlay(
            title = "Control Memory ROM Size aur CAR Bits Nikalo",
            statement = "Ek Microprogrammed CU mein 128 microinstructions hain aur har Control Word 24-bit ka hai. ROM size aur CAR width nikalo.",
            stepTitles = listOf(
                "CAR (Control Address Register) ki bit-width",
                "Total Control Memory ROM size"
            ),
            stepExplanations = listOf(
                "128 = 2⁷ hota hai, isliye har location address karne ke liye 7 bits chahiye.",
                "128 words × 24 bits = 3,072 bits = 384 Bytes."
            ),
            finalAnswer = "CAR = 7 bits  |  Control Memory = 3,072 bits (384 Bytes)"
        ),
        "prob_pipe_speedup" to LocalizedProblemOverlay(
            title = "100 Instructions par 5-Stage Pipeline Speedup",
            statement = "Non-pipelined CPU ek instruction mein 10 ns leta hai. 5-stage pipeline ka clock cycle 2 ns hai. 100 instructions ka Speedup nikalo.",
            stepTitles = listOf(
                "Non-Pipelined total time",
                "Pipelined total time: (k + n - 1) × Tp",
                "Actual Speedup aur Ideal Max Speedup"
            ),
            stepExplanations = listOf(
                "Bina pipeline ke 100 instructions ek ke baad ek 10 ns leti hain = 1,000 ns.",
                "Pehli instruction 5 cycles mein aur baaki 99 har 1 cycle mein complete hongi = 104 × 2 = 208 ns.",
                "1000 / 208 = 4.81× speedup mila; ideal limit k = 5× hoti hai."
            ),
            finalAnswer = "Speedup = 4.81× (208 ns vs 1,000 ns)  |  Max Speedup = 5×"
        ),
        "prob_pipe_cpi" to LocalizedProblemOverlay(
            title = "Load-Use RAW Hazard ke saath Effective CPI",
            statement = "5-stage pipeline ka Ideal CPI = 1 hai. Agar 20% instructions Load hain aur unme se 50% mein 1 stall aaye, to Effective CPI nikalo.",
            stepTitles = listOf(
                "Average stall cycles per instruction",
                "Ideal CPI mein stalls jodo"
            ),
            stepExplanations = listOf(
                "0.20 (Loads) × 0.50 (Stall chance) × 1 cycle = 0.10 extra cycle per instruction.",
                "Base CPI 1.0 + 0.10 stall = 1.10 Effective CPI."
            ),
            finalAnswer = "Effective CPI = 1.10 cycles/instruction"
        ),
        "prob_eat_cache" to LocalizedProblemOverlay(
            title = "95% Cache Hit Ratio par Effective Access Time (EAT)",
            statement = "Cache time Tc = 2 ns, RAM time Tm = 50 ns aur Hit Ratio h = 0.95 hai. Parallel aur Sequential lookup ka EAT nikalo.",
            stepTitles = listOf(
                "Miss Ratio (1 - h) nikalo",
                "Simultaneous (Parallel) Lookup EAT",
                "Sequential Lookup EAT"
            ),
            stepExplanations = listOf(
                "95% hit ka matlab 5% (0.05) requests miss hokar RAM par jaati hain.",
                "Parallel lookup mein Miss par seedha Tm = 50 ns lagta hai → 1.9 + 2.5 = 4.4 ns.",
                "Sequential lookup mein pehle Tc = 2 ns lagta hi hai, phir miss par Tm → 4.5 ns."
            ),
            finalAnswer = "EAT (Parallel) = 4.4 ns  |  EAT (Sequential) = 4.5 ns"
        ),
        "prob_cache_split" to LocalizedProblemOverlay(
            title = "32-Bit Address Split: Tag, Index & Offset Bits",
            statement = "32-bit address, 64 KB Direct-Mapped Cache aur 64-Byte block size ke liye Offset, Index aur Tag bits nikalo.",
            stepTitles = listOf(
                "Block Offset bits nikalo",
                "Cache Lines aur Index bits nikalo",
                "Bache hue Tag bits nikalo"
            ),
            stepExplanations = listOf(
                "Block size 64 B = 2⁶ hai, to byte select karne ke liye 6 Offset bits lagenge.",
                "64 KB / 64 B = 1024 (2¹⁰) lines hain, isliye Index = 10 bits.",
                "32 bits mein se Index (10) aur Offset (6) ghatane par Tag = 16 bits milta hai."
            ),
            finalAnswer = "Tag = 16 bits  |  Index = 10 bits  |  Block Offset = 6 bits"
        ),
        "prob_vm_split" to LocalizedProblemOverlay(
            title = "Virtual Page Number (VPN) & Physical Frame Bits",
            statement = "32-bit Virtual Address, 4 KB Page Size aur 256 MB Physical RAM ke liye VPN, Page Offset aur PFN bits nikalo.",
            stepTitles = listOf(
                "Page Size se Offset bits nikalo",
                "Virtual Page Number (VPN) bits",
                "Physical Frame Number (PFN) bits"
            ),
            stepExplanations = listOf(
                "4 KB = 2¹² Bytes hone se Page Offset = 12 bits rehta hai (translation mein nahi badalta).",
                "32 - 12 = 20 bits VPN ke liye bachte hain.",
                "256 MB RAM = 2²⁸ B (28-bit physical address) → 28 - 12 = 16 bits PFN."
            ),
            finalAnswer = "VPN = 20 bits | Offset = 12 bits | PFN = 16 bits"
        ),
        "prob_dma_cycles" to LocalizedProblemOverlay(
            title = "DMA vs Programmed I/O CPU Cycles Comparison",
            statement = "4 KB block (32-bit words) transfer mein PIO ko 20 cycles/word lagte hain, jabki DMA sirf 1 cycle/word steal karta hai (+100 setup). Dono compare karo.",
            stepTitles = listOf(
                "4 KB mein total 4-Byte words gino",
                "Programmed I/O ke CPU cycles",
                "DMA Cycle-Stealing ke CPU cycles"
            ),
            stepExplanations = listOf(
                "4096 Bytes ko 4 Bytes se divide karne par 1,024 words milte hain.",
                "PIO mein CPU har word ko poll aur copy karta hai = 20,480 cycles.",
                "DMA khud transfer karta hai aur CPU se sirf bus ke 1 cycle/word leta hai = 1,124 cycles."
            ),
            finalAnswer = "PIO = 20,480 cycles vs DMA = 1,124 cycles (~18.2× CPU bachat!)"
        ),
        "prob_amdahl" to LocalizedProblemOverlay(
            title = "Amdahl's Law: 4 Cores aur Infinite Cores Speedup",
            statement = "Program ka 75% hissa parallel (P = 0.75) aur 25% sequential hai. 4 cores par aur Infinite cores par Speedup nikalo.",
            stepTitles = listOf(
                "4 cores ke liye Amdahl's formula lagao",
                "Final 4-core Speedup calculate karo",
                "N → ∞ par Max Speedup limit"
            ),
            stepExplanations = listOf(
                "Sequential part 0.25 unchanged rehta hai aur parallel part 0.75/4 = 0.1875 ho jaata hai.",
                "1 / 0.4375 karne par 4-core speedup 2.29× aata hai.",
                "Infinite cores par bhi 25% sequential hissa speedup ko 4.0× par rok deta hai."
            ),
            finalAnswer = "4-Core Speedup = 2.29×  |  Max Possible Speedup = 4.0×"
        )
    )
}
