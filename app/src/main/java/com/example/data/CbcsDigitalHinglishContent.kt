package com.example.data

object CbcsDigitalHinglishContent {

    val topics: Map<String, TopicHinglishData> = mapOf(
        "number_systems_codes" to TopicHinglishData(
            subtitle = "Binary, Octal, Hex conversions, BCD, Gray Code, Excess-3 aur 2's Complement",
            summary = "Computer sirf 0 aur 1 (Binary, Base-2) samajhta hai. Lambe binary numbers ko chhota likhne ke liye Octal (Base-8: 3 bits ka group) aur Hexadecimal (Base-16: 4 bits ka group) use hota hai. Digital systems mein alag-alag kaam ke liye BCD (8421), Excess-3 aur Gray Code jaise Binary Codes bhi use hote hain.",
            keyPoints = listOf(
                "Binary ↔ Octal: Right se 3-3 bits ke group banao (jaise 101 110₂ = 56₈).",
                "Binary ↔ Hexadecimal: Right se 4-4 bits ke group banao (jaise 1010 1111₂ = AF₁₆).",
                "BCD (8421): Har decimal digit (0–9) ko alag 4-bit binary mein likhte hain (1010 se 1111 invalid hote hain).",
                "Excess-3 = BCD + 3 (self-complementing code). Gray Code mein lagatar numbers mein sirf 1 bit change hoti hai!"
            ),
            stepDescriptions = listOf(
                "Step 1: Upar 'Bin / Oct / Hex', 'BCD & Gray Code', aur '2's Complement' switch karke dekho kaise ek hi number alag codes mein convert hota hai.",
                "Step 2: 2's Complement mein 1's complement + 1 karke negative numbers store hote hain — isse +0 aur -0 ka confusion khatam ho jaata hai."
            ),
            partRoles = mapOf(
                "dec_in" to "Insan ke samajhne wala Base-10 number (jaise 25₁₀) jise base se divide karke convert karte hain.",
                "bin_reg" to "CPU register jo 0/1 voltage levels (11001₂) store karta hai.",
                "oct_hex" to "3-bit group se Octal aur 4-bit group se Hexadecimal shortcut representation.",
                "code_enc" to "Binary ko 8421 BCD, Gray Code, Excess-3 ya signed 2's Complement mein badalta hai."
            ),
            vivaAnswers = listOf(
                "Kyunki Gray Code mein har agla number pichle se sirf 1 bit alag hota hai — isse shaft encoders aur K-Map mein galti nahi hoti.",
                "2's Complement mein Zero sirf ek baar (0000) hota hai aur Addition + Subtraction dono ek hi Adder circuit se ho jaate hain."
            )
        ),
        "logic_gates" to TopicHinglishData(
            subtitle = "AND, OR, NOT, NAND, NOR, XOR aur XNOR gates ke symbols aur truth tables",
            summary = "Logic Gates poore computer hardware ki sabse chhoti eent (building block) hain. AND, OR, NOT basic gates hain, jabki NAND aur NOR ko Universal Gates kehte hain kyunki akela NAND ya akela NOR gate duniya ka koi bhi digital circuit bana sakta hai!",
            keyPoints = listOf(
                "AND (A·B): Output 1 tabhi hoga jab SAARE inputs 1 hon. OR (A+B): Koi EK bhi input 1 ho to output 1.",
                "NOT (A'): Input ko ulta karta hai (0 ko 1, 1 ko 0).",
                "NAND & NOR = Universal Gates: Inse NOT, AND, OR, XOR, Adder sab kuch ban sakta hai.",
                "XOR (A ⊕ B): Jab dono inputs ALAG hon (0,1 ya 1,0) tab output 1. XNOR: Jab dono inputs SAME hon tab output 1."
            ),
            stepDescriptions = listOf(
                "Upar 'Basic', 'Universal (NAND/NOR)', aur 'Derived (XOR/XNOR)' toggle karke gates ka behaviour aur De Morgan's Law dekho."
            ),
            partRoles = mapOf(
                "in_pins" to "Digital inputs A aur B (0 = LOW, 1 = HIGH).",
                "gate_primary" to "Primary CMOS gate (AND / NAND / XOR) jo A aur B par logic lagata hai.",
                "gate_secondary" to "Complementary gate (OR / NOR / XNOR) jo duality dikhata hai.",
                "out_led" to "Final Boolean output Y jo agle gate ya Flip-Flop ko jaata hai."
            ),
            vivaAnswers = listOf(
                "Kyunki sirf NAND gates (ya sirf NOR gates) ko jodkar hum NOT, AND aur OR teeno basic gates bana sakte hain.",
                "Ek 2-input XOR gate banane ke liye 4 NAND gates (ya 5 NOR gates) lagte hain."
            )
        ),
        "karnaugh_map" to TopicHinglishData(
            subtitle = "Gray-Code K-Map grid, Pair/Quad/Octet grouping aur Boolean simplification",
            summary = "Karnaugh Map (K-Map) ek visual grid method hai jisse bade Boolean expressions bina lambi algebra lagaye aasaani se chhote (minimize) ho jaate hain. Isme rows/columns Gray Code (00, 01, 11, 10) order mein hote hain taaki paas wale cells mein sirf 1 variable badle.",
            keyPoints = listOf(
                "Cells = 2^n (2 variables = 4 cells, 3 variables = 8 cells, 4 variables = 16 cells).",
                "Group Size hamesha power of 2 hota hai: 1, 2 (Pair), 4 (Quad), 8 (Octet), 16.",
                "Jitna bada group, utne zyada variables khatam: Pair se 1, Quad se 2, Octet se 3 variables kam hote hain!",
                "Don't Care (X): Group bada banane ke liye X ko 1 maan sakte hain, warna ignore kar do."
            ),
            stepDescriptions = listOf(
                "2D K-Map Grid mein dekho: 00, 01, 11, 10 Gray Code hai aur edges wrap-around hote hain! Upar 'Don't Care (X)' toggle karke optimization dekho."
            ),
            partRoles = mapOf(
                "kmap_quad" to "4 adjacent 1s ka Quad group jo 2 variables eliminate karta hai.",
                "kmap_pair" to "2 adjacent 1s ka Pair ya Don't Care (X) ke saath mila hua bada Quad.",
                "kmap_grid" to "4×4 Gray-Code matrix jisme left-right aur top-bottom edges jude hote hain.",
                "min_sop" to "Minimized SOP equation jisse circuit mein kam logic gates lagte hain."
            ),
            vivaAnswers = listOf(
                "Kyunki 00, 01, 11, 10 Gray Code order hai — har paas wale cell mein sirf 1 bit badalti hai taaki A + A' = 1 cancel ho sake.",
                "Prime Implicant sabse bada valid group hota hai; Essential Prime Implicant mein kam se kam ek aisa '1' hota hai jo kisi aur group mein na aata ho."
            )
        ),
        "mux_demux" to TopicHinglishData(
            subtitle = "4:1 MUX (Many-to-One Data Selector) aur 1:4 DEMUX (One-to-Many)",
            summary = "Multiplexer (MUX) ek digital switch hai jo kai saare inputs (2^n) mein se Select Lines (n) ke hisaab se kisi EK input ko single output par bhejta hai. Demultiplexer (DEMUX) iska bilkul ulta karta hai: 1 input ko select kiye gaye 2^n outputs mein se kisi ek par bhejta hai.",
            keyPoints = listOf(
                "4:1 MUX mein 4 inputs (I0–I3), 2 Select lines (S1, S0) aur 1 Output (Y) hota hai.",
                "Formula: Y = S1'S0'·I0 + S1'S0·I1 + S1S0'·I2 + S1S0·I3.",
                "MUX se koi bhi Boolean function implement kiya ja sakta hai (Data Selector = Universal logic block!).",
                "1:4 DEMUX mein 1 Data Input (Din) aur 4 outputs (Y0–Y3) hote hain; Din = 1 rakhne par yeh 2-to-4 Decoder ban jaata hai."
            ),
            stepDescriptions = listOf(
                "Step 1: Jab Select S1S0 = 00 hota hai, sirf Channel 0 connect hota hai; baaki sab block rehte hain.",
                "Step 2: Select S1S0 = 10 hote hi switch turant Channel 2 par shift ho jaata hai!"
            ),
            partRoles = mapOf(
                "mux_inputs" to "4 parallel data channels (MUX mein inputs I0–I3, DEMUX mein outputs Y0–Y3).",
                "select_lines" to "2-bit control selector (S1, S0) jo batata hai kaunsa channel active hoga.",
                "mux_core" to "AND-OR combinational switch jo sirf selected path open karta hai.",
                "single_line" to "MUX ka single output Y (ya DEMUX ka single input Din)."
            ),
            vivaAnswers = listOf(
                "Kyunki yeh Select lines ke binary code ke hisaab se kai inputs mein se ek data line ko output ke liye select karta hai.",
                "DEMUX ke single Data Input (Din) ko permanent Logic 1 par set kar do — ab jo output line select hogi sirf wahi 1 hogi (2-to-4 Decoder)."
            )
        ),
        "flip_flops" to TopicHinglishData(
            subtitle = "SR, JK, D aur T Flip-Flops — Clocked 1-Bit Memory aur Race-Around",
            summary = "Flip-Flop ek 1-bit memory cell hai jo clock signal ke saath sync hokar 0 ya 1 store karta hai. CPU ke saare Registers, Counters aur fast hardware buffers Flip-Flops se hi bante hain!",
            keyPoints = listOf(
                "SR Flip-Flop: S=1 Set (Q=1), R=1 Reset (Q=0), dono 0 par Hold, par S=1, R=1 INVALID state banata hai.",
                "D (Data) Flip-Flop: Invalid state khatam! Clock edge par Q = D ho jaata hai (Registers mein sabse zyada use).",
                "JK Flip-Flop: J=1, K=1 par INVALID nahi hota balki TOGGLE (Q ka ulta) ho jaata hai!",
                "T (Toggle) Flip-Flop: T=1 par har clock pulse ke saath output flip hota hai (Binary Counters ke liye best)."
            ),
            stepDescriptions = listOf(
                "Step 1: Clock LOW rehne tak feedback loop purana bit Q pakad kar (Hold) rakhta hai.",
                "Step 2: Clock ke rising edge (CLK ↑) par hi input ke hisaab se Q update hota hai!"
            ),
            partRoles = mapOf(
                "ff_inputs" to "Control inputs (S/R, D, J/K, T) jo decide karte hain Hold hoga, Set, Reset ya Toggle.",
                "ff_clock" to "Clock Edge Detector jo sirf rising/falling edge par gate open karta hai.",
                "ff_latch" to "Cross-coupled NAND/NOR feedback loop jo 1 bit ko store rakhta hai.",
                "ff_outputs" to "Stored output Q aur uska inverted complement Q'."
            ),
            vivaAnswers = listOf(
                "Latch level-triggered hota hai (Enable=1 rehne tak badalta rehta hai), jabki Flip-Flop edge-triggered hota hai (sirf clock edge par change hota hai).",
                "Jab level-triggered JK mein J=K=1 ho aur clock pulse lambi ho, to Q baar-baar 0↔1 oscillate karta hai — ise Race-Around kehte hain. Edge-triggering ya Master-Slave se solve hota hai."
            )
        ),
        "bus_structures" to TopicHinglishData(
            subtitle = "Single-Bus vs Multi-Bus Architecture, Tri-State Buffers aur Arbitration",
            summary = "Bus parallel wires ka ek common highway hai jo CPU, RAM aur I/O ko jodta hai. Kyunki ek bus ko kai devices share karte hain, ek waqt par sirf EK device data bhej sakta hai — baaki sab ko Tri-State Buffer High-Z (disconnect) state mein rakhta hai!",
            keyPoints = listOf(
                "Single-Bus Structure: Sasta aur simple par ek cycle mein sirf ek data transfer ho sakta hai (slow).",
                "Multi-Bus Structure: Fast CPU–RAM bus ko slow I/O peripherals (USB, Disk) se ek Bus Bridge ke zariye alag rakha jaata hai.",
                "Tri-State Buffer ke 3 states: 0 (LOW), 1 (HIGH), aur High-Impedance (Z = electrically cut off) taaki short-circuit na ho.",
                "Bus Arbitration: Jab CPU aur DMA dono bus maangein to kaun chalega (Daisy Chaining, Polling, Independent Request)."
            ),
            stepDescriptions = listOf(
                "Upar 'Single Shared Bus', 'Multi-Bus Hierarchy' aur 'Tri-State & Arbitration' switch karke dekho kaise Bridge aur High-Z bus traffic handle karte hain."
            ),
            partRoles = mapOf(
                "bus_master_cpu" to "Primary Bus Master jo address aur control signals drive karta hai.",
                "tristate_buf" to "Jo device abhi transmit nahi kar raha use High-Impedance (Z) par rakhkar bus clash rokta hai.",
                "bus_bridge" to "Fast System Bus aur slow I/O Expansion Bus ke beech speed matching Bridge/Arbiter.",
                "bus_slaves" to "RAM aur I/O devices jo apna address decode hone par respond karte hain."
            ),
            vivaAnswers = listOf(
                "Agar do devices ek hi wire par ek saath 1 aur 0 bhej dein to short-circuit ho jaayega. Tri-State ka High-Z idle devices ko wire se disconnect rakhta hai.",
                "Kyunki slow I/O devices direct fast CPU–RAM bus par lagne se poore system bus ki frequency aur speed kam kar denge."
            )
        )
    )

    val quizExplanations: Map<String, String> = mapOf(
        "q_dig_1" to "Binary se Hexadecimal mein badalne ke liye 4-4 bits ke group bante hain: 1011 = B aur 1101 = D -> BD₁₆.",
        "q_dig_2" to "Gray Code mein har lagatar number ke beech sirf 1 bit ka farq hota hai, isliye use unit-distance code kehte hain.",
        "q_dig_3" to "NAND aur NOR dono Universal Gates hain kyunki akela NAND ya akela NOR koi bhi logic circuit bana sakta hai.",
        "q_dig_4" to "4-variable K-Map mein 4 cells ka Quad group banane se 2 variables cancel (eliminate) ho jaate hain.",
        "q_dig_5" to "16:1 Multiplexer mein 16 inputs mein se 1 chunne ke liye log₂(16) = 4 Select lines (16 = 2⁴) lagti hain.",
        "q_dig_6" to "JK Flip-Flop mein J=1 aur K=1 dene par output pichle state ka complement (TOGGLE) ho jaata hai.",
        "q_dig_7" to "Tri-State Buffer ka teesra state High-Impedance (High-Z) hota hai jo idle device ko bus se electrically disconnect rakhta hai."
    )
}
