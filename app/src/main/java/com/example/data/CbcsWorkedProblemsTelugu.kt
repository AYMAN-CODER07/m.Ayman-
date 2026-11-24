package com.example.data

import com.example.domain.model.LocalizedProblemOverlay

object CbcsWorkedProblemsTelugu {

    val problems: Map<String, LocalizedProblemOverlay> = mapOf(
        "prob_num_conv" to LocalizedProblemOverlay(
            title = "(45.625)₁₀ ను Binary, Octal & Hex కు మార్చండి",
            statement = "డయాగ్రామ్ అవసరం లేకుండా (45.625)₁₀ దశాంశ సంఖ్యను దశల వారీగా Binary, Octal మరియు Hexadecimal లోకి మార్చండి.",
            stepTitles = listOf(
                "పూర్ణాంక భాగం (45) ను 2 తో వరుసగా భాగించండి",
                "భిన్న భాగం (0.625) ను 2 తో గుణించండి",
                "Octal కోసం 3-బిట్ గ్రూపులు చేయండి",
                "Hex కోసం 4-బిట్ గ్రూపులు చేయండి"
            ),
            stepExplanations = listOf(
                "శేషాలను (Remainders) క్రింది నుండి పైకి చదవండి: 45₁₀ = (101101)₂.",
                "దశాంశం ముందున్న పూర్ణాంకాలను పై నుండి క్రిందికి చదవండి: 0.625₁₀ = (.101)₂. మొత్తం = (101101.101)₂.",
                "కుడి నుండి 3 బిట్లుగా విడగొట్టండి: 101 = 5, కాబట్టి Octal = (55.5)₈.",
                "4 బిట్ల నిబుల్స్ చేయండి: 0010=2, 1101=13(D), 1010=10(A). Hex = (2D.A)₁₆."
            ),
            finalAnswer = "Binary = (101101.101)₂  |  Octal = (55.5)₈  |  Hex = (2D.A)₁₆"
        ),
        "prob_codes" to LocalizedProblemOverlay(
            title = "Binary → Gray Code, 8421 BCD & Excess-3",
            statement = "(29)₁₀ ను 8421 BCD మరియు Excess-3 గా మార్చండి; (10110)₂ కు Gray Code కనుగొనండి.",
            stepTitles = listOf(
                "(29)₁₀ యొక్క 8421 BCD",
                "(29)₁₀ యొక్క Excess-3 కోడ్",
                "(10110)₂ నుండి Gray Code"
            ),
            stepExplanations = listOf(
                "ప్రతి అంకెను 4-బిట్ బైనరీగా రాయండి: 2 = 0010, 9 = 1001 → (0010 1001)BCD.",
                "ప్రతి BCD నిబుల్‌కు 3 (0011) కలపండి → (0101 1100)EX-3.",
                "మొదటి బిట్ (MSB=1) అలాగే ఉంచి పక్క బిట్లతో XOR చేయండి."
            ),
            finalAnswer = "BCD = 0010 1001  |  Excess-3 = 0101 1100  |  Gray = (11101)Gray"
        ),
        "prob_twos_comp" to LocalizedProblemOverlay(
            title = "8-Bit 2's Complement తీసివేత: 14 - 9",
            statement = "8-బిట్ 2's complement పద్ధతిలో (14)₁₀ − (9)₁₀ ను దశల వారీగా చేయండి.",
            stepTitles = listOf(
                "రెండు సంఖ్యలను 8-బిట్ బైనరీగా రాయండి",
                "+9 యొక్క 2's complement (-9) కనుగొనండి",
                "(+14) + (-9) కూడి అదనపు క్యారీని వదిలేయండి"
            ),
            stepExplanations = listOf(
                "ధన సంఖ్యలకు MSB = 0 ఉంచి 8 బిట్లుగా రాయండి.",
                "9 బిట్లను తిప్పి (1's comp) +1 కలిపితే -9 వస్తుంది.",
                "కూడినప్పుడు 9వ క్యారీ బిట్ = 1 వస్తుంది; దానిని వదిలేస్తే 0000 0101₂ (+5) మిగులుతుంది."
            ),
            finalAnswer = "(0000 0101)₂ = +5₁₀"
        ),
        "prob_nand_impl" to LocalizedProblemOverlay(
            title = "కేవలం NAND గేట్లతో F = AB + CD నిర్మించండి",
            statement = "F = A·B + C·D సమీకరణాన్ని కనీస 2-input NAND గేట్లతో దశల వారీగా రూపొందించండి.",
            stepTitles = listOf(
                "F పై డబుల్ కాంప్లిమెంట్ వేయండి",
                "లోపలి బార్ పై డి మోర్గాన్ సూత్రం వాడండి",
                "ప్రతి (X·Y)' ను NAND గేట్‌గా మార్చండి"
            ),
            stepExplanations = listOf(
                "రెండు సార్లు NOT చేస్తే విలువ మారదు: (X')' = X.",
                "డి మోర్గాన్ ప్రకారం (X + Y)' = X' · Y' అవుతుంది.",
                "మూడు భాగాలు నేరుగా NAND గేట్లే — మొత్తం 3 NAND గేట్లు సరిపోతాయి."
            ),
            finalAnswer = "F = NAND(NAND(A, B), NAND(C, D)) → మొత్తం 3 NAND గేట్లు"
        ),
        "prob_kmap_min" to LocalizedProblemOverlay(
            title = "F(A,B,C,D) = Σm(0,1,2,3,5,7) ను తగ్గించండి",
            statement = "4-వేరియబుల్ SOP ఫంక్షన్ F(A,B,C,D) = Σm(0, 1, 2, 3, 5, 7) ను దశల వారీగా సులభతరం చేయండి.",
            stepTitles = listOf(
                "ప్రతి మిన్‌టర్మ్‌ను 4-బిట్ బైనరీగా రాయండి",
                "{m0, m1, m2, m3} తో మొదటి Quad చేయండి",
                "{m1, m3, m5, m7} తో రెండో Quad చేయండి"
            ),
            stepExplanations = listOf(
                "మొత్తం 6 మిన్‌టర్మ్‌ల బైనరీ కోడ్‌లను రాసి కామన్ బిట్లను చూడండి.",
                "ఈ 4 గదుల్లో C, D మారినా A=0, B=0 స్థిరంగా ఉంటాయి → A'B'.",
                "ఈ 4 గదుల్లో B, C మారినా A=0, D=1 స్థిరంగా ఉంటాయి → A'D."
            ),
            finalAnswer = "తగ్గించిన SOP: F(A,B,C,D) = A'B' + A'D"
        ),
        "prob_mux_fn" to LocalizedProblemOverlay(
            title = "4:1 MUX తో F(A,B,C) = Σm(1,2,5,7) అమలు",
            statement = "A, B లను Select లైన్లుగా తీసుకొని 4:1 MUX తో F(A,B,C) = Σm(1, 2, 5, 7) ను సాధించండి.",
            stepTitles = listOf(
                "Select లైన్లు AB ప్రకారం మిన్‌టర్మ్‌లను విడగొట్టండి",
                "ప్రతి ఇన్‌పుట్ విలువను C రూపంలో కనుగొనండి"
            ),
            stepExplanations = listOf(
                "ప్రతి AB కలయికలో C=0 మరియు C=1 ఉండే రెండు మిన్‌టర్మ్‌లు వస్తాయి.",
                "I0 లో m1(C=1) ఉంది → C. I1 లో m2(C=0) ఉంది → C'. అలాగే I2=C, I3=C."
            ),
            finalAnswer = "S1=A, S0=B  |  I0 = C, I1 = C', I2 = C, I3 = C"
        ),
        "prob_ff_counter" to LocalizedProblemOverlay(
            title = "Mod-12 కౌంటర్ ఫ్లిప్-ఫ్లాప్‌లు & ఫ్రీక్వెన్సీ",
            statement = "Mod-12 కౌంటర్‌కు ఎన్ని Flip-Flops కావాలి? 1.6 MHz క్లాక్‌ను 4 T-FF లకు ఇస్తే అవుట్‌పుట్ ఫ్రీక్వెన్సీ ఎంత?",
            stepTitles = listOf(
                "Mod-12 కు కనీస ఫ్లిప్-ఫ్లాప్‌లు కనుగొనండి",
                "4 టోగుల్ దశల తర్వాత అవుట్‌పుట్ ఫ్రీక్వెన్సీ"
            ),
            stepExplanations = listOf(
                "3 ఫ్లిప్-ఫ్లాప్‌లు 8 స్థితులనే లెక్కిస్తాయి; 12 కోసం 2⁴ = 16 అంటే 4 ఫ్లిప్-ఫ్లాప్‌లు కావాలి.",
                "ప్రతి T-FF ఫ్రీక్వెన్సీని సగం (÷2) చేస్తుంది; 4 దశలు ÷16 చేస్తాయి."
            ),
            finalAnswer = "4 Flip-Flops అవసరం  |  అవుట్‌పుట్ ఫ్రీక్వెన్సీ = 100 kHz"
        ),
        "prob_bus_bw" to LocalizedProblemOverlay(
            title = "సిస్టమ్ బస్ బ్యాండ్‌విడ్త్ (64-bit @ 200 MHz)",
            statement = "64-బిట్ డేటా బస్సు 200 MHz వద్ద ప్రతి సైకిల్‌కు 1 పదం పంపితే గరిష్ట బ్యాండ్‌విడ్త్ కనుగొనండి.",
            stepTitles = listOf(
                "బస్సు వెడల్పును బైట్లలోకి మార్చండి",
                "బైట్లు × ఫ్రీక్వెన్సీ గుణించండి"
            ),
            stepExplanations = listOf(
                "1 బైట్ = 8 బిట్లు కాబట్టి 64-బిట్ బస్సు ఒక సైకిల్‌కు 8 బైట్లు పంపుతుంది.",
                "8 బైట్లను 200 MHz తో గుణిస్తే 1.6 GB/s వస్తుంది."
            ),
            finalAnswer = "గరిష్ట బస్ బ్యాండ్‌విడ్త్ = 1.6 GB/s"
        ),
        "prob_addr_space" to LocalizedProblemOverlay(
            title = "అడ్రస్ బస్ నుండి గరిష్ట RAM సామర్థ్యం",
            statement = "CPU కు 24-బిట్ Address Bus మరియు 16-బిట్ Data Bus (word-addressable) ఉంటే గరిష్ట మెమరీ ఎంత?",
            stepTitles = listOf(
                "మొత్తం మెమరీ స్థానాలను కనుగొనండి",
                "స్థానాలను పదం సైజు (2 బైట్లు) తో గుణించండి"
            ),
            stepExplanations = listOf(
                "24-బిట్ అడ్రస్ బస్సు 2²⁴ = 16 మిలియన్ స్థానాలను యాక్సెస్ చేస్తుంది.",
                "ప్రతి పదం 16 బిట్లు (2 బైట్లు) కాబట్టి మొత్తం 16M × 2 = 32 MB."
            ),
            finalAnswer = "16 M words = 32 MB గరిష్ట మెమరీ"
        ),
        "prob_harvard_cycles" to LocalizedProblemOverlay(
            title = "10 ఇన్‌స్ట్రక్షన్ల సైకిళ్లు: Harvard vs Von Neumann",
            statement = "ప్రతి ఇన్‌స్ట్రక్షన్‌కు 1 ఫెచ్ సైకిల్ మరియు 1 డేటా సైకిల్ పడితే, 10 ఇన్‌స్ట్రక్షన్లకు రెండింటిలో ఎన్ని సైకిళ్లు పడతాయి?",
            stepTitles = listOf(
                "వాన్ న్యూమాన్ (ఒకే షేర్డ్ బస్సు)",
                "హార్వర్డ్ (రెండు వేర్వేరు బస్సులు)"
            ),
            stepExplanations = listOf(
                "ఒకే బస్సు వల్ల ఫెచ్ మరియు డేటా ఒకదాని తర్వాత ఒకటి జరుగుతాయి = 20 సైకిళ్లు.",
                "ఒకటి డేటా చదువుతుండగానే తదుపరి ఇన్‌స్ట్రక్షన్ ఫెచ్ అవుతుంది = 11 సైకిళ్లు."
            ),
            finalAnswer = "వాన్ న్యూమాన్ = 20 సైకిళ్లు  |  హార్వర్డ్ = 11 సైకిళ్లు"
        ),
        "prob_rtl_trace" to LocalizedProblemOverlay(
            title = "ADD R1, [300] యొక్క RTL దశల ట్రేస్",
            statement = "PC = 100, M[300] = 45, R1 = 15 ఇచ్చినప్పుడు ADD R1, [300] తర్వాత రిజిస్టర్ విలువలు కనుగొనండి.",
            stepTitles = listOf(
                "T1–T3: ఇన్‌స్ట్రక్షన్ ఫెచ్",
                "T4: RAM నుండి ఆపరాండ్ తెచ్చుకోవడం",
                "T5: ALU కూడిక అమలు"
            ),
            stepExplanations = listOf(
                "MAR కు PC=100 వెళుతుంది, ఇన్‌స్ట్రక్షన్ IR లోకి వస్తుంది, PC = 101 అవుతుంది.",
                "IR లోని అడ్రస్ 300 ద్వారా M[300]=45 విలువ MDR లోకి వస్తుంది.",
                "ALU R1 (15) మరియు MDR (45) కూడి 60 ను R1 లో సేవ్ చేస్తుంది."
            ),
            finalAnswer = "తుది స్థితి: PC = 101, MAR = 300, MDR = 45, R1 = 60"
        ),
        "prob_alu_overflow" to LocalizedProblemOverlay(
            title = "8-Bit సైన్డ్ కూడిక & Overflow తనిఖీ: (+70) + (+80)",
            statement = "8-బిట్ 2's complement లో (+70) + (+80) చేసి Sign (N) మరియు Overflow (V) ఫ్లాగ్స్ చూడండి.",
            stepTitles = listOf(
                "+70 మరియు +80 లను 8-బిట్ బైనరీగా మార్చండి",
                "8-బిట్ బైనరీ కూడిక చేయండి",
                "C_in ⊕ C_out ద్వారా Overflow చూడండి"
            ),
            stepExplanations = listOf(
                "రెండూ ధన సంఖ్యలు కాబట్టి MSB = 0 ఉంటుంది.",
                "కూడితే 1001 0110₂ (-106) వచ్చింది; MSB = 1 (రుణ సంఖ్య) అయింది!",
                "+150 విలువ +127 పరిమితిని దాటింది మరియు C_in ≠ C_out కాబట్టి Overflow V = 1."
            ),
            finalAnswer = "ఫలితం = 1001 0110₂, N = 1, V = 1 (Overflow జరిగింది)"
        ),
        "prob_ea_calc" to LocalizedProblemOverlay(
            title = "అడ్రసింగ్ మోడ్ల ప్రకారం Effective Address (EA)",
            statement = "Address = 500, R1 = 400, IX = 50; M[400]=750, M[500]=800, M[550]=900, M[800]=950. అన్ని మోడ్ల EA కనుగొనండి.",
            stepTitles = listOf(
                "Immediate మోడ్",
                "Direct vs Indirect మోడ్",
                "Register Indirect & Indexed మోడ్"
            ),
            stepExplanations = listOf(
                "Immediate లో 500 అనేదే నేరుగా డేటా విలువ.",
                "Direct లో EA=500 (Op=800); Indirect లో M[500]=800 ద్వారా M[800]=950 వస్తుంది.",
                "Reg Indirect లో EA=R1(400); Indexed లో 500 + IX(50) = 550."
            ),
            finalAnswer = "Immediate: 500 | Direct: 800 | Indirect: 950 | Reg-Ind: 750 | Indexed: 900"
        ),
        "prob_cu_rom" to LocalizedProblemOverlay(
            title = "కంట్రోల్ మెమరీ ROM సైజు & CAR బిట్లు",
            statement = "128 మైక్రోఇన్‌స్ట్రక్షన్లు మరియు 24-బిట్ కంట్రోల్ వర్డ్ ఉన్న ROM సైజు మరియు CAR వెడల్పు కనుగొనండి.",
            stepTitles = listOf(
                "CAR బిట్ల సంఖ్య కనుగొనండి",
                "మొత్తం ROM మెమరీ సైజు"
            ),
            stepExplanations = listOf(
                "128 = 2⁷ కాబట్టి CAR కు 7 బిట్లు అవసరం.",
                "128 × 24 బిట్లు = 3,072 బిట్లు = 384 బైట్లు."
            ),
            finalAnswer = "CAR = 7 bits  |  Control Memory = 3,072 bits (384 Bytes)"
        ),
        "prob_pipe_speedup" to LocalizedProblemOverlay(
            title = "100 ఇన్‌స్ట్రక్షన్లకు 5-దశల పైప్‌లైన్ Speedup",
            statement = "నాన్-పైప్‌లైన్ సమయం 10 ns; 5-దశల పైప్‌లైన్ క్లాక్ 2 ns అయితే 100 ఇన్‌స్ట్రక్షన్లకు Speedup కనుగొనండి.",
            stepTitles = listOf(
                "నాన్-పైప్‌లైన్ మొత్తం సమయం",
                "పైప్‌లైన్ సమయం: (k + n - 1) × Tp",
                "Speedup & గరిష్ట పరిమితి"
            ),
            stepExplanations = listOf(
                "100 ఇన్‌స్ట్రక్షన్లు ఒకదాని తర్వాత ఒకటి 10 ns చొప్పున 1,000 ns తీసుకుంటాయి.",
                "మొదటిది 5 సైకిళ్లలో, మిగిలిన 99 సైకిల్‌కు 1 చొప్పున = 104 × 2 = 208 ns.",
                "1000 / 208 = 4.81× వేగం; గరిష్ట పరిమితి k = 5×."
            ),
            finalAnswer = "Speedup = 4.81× (208 ns vs 1,000 ns)  |  Max = 5×"
        ),
        "prob_pipe_cpi" to LocalizedProblemOverlay(
            title = "Load-Use హాజర్డ్‌తో Effective CPI గణన",
            statement = "ఆదర్శ CPI = 1. 20% Load ఇన్‌స్ట్రక్షన్లలో 50% ఒక సైకిల్ స్టాల్ ఇస్తే Effective CPI ఎంత?",
            stepTitles = listOf(
                "ప్రతి ఇన్‌స్ట్రక్షన్‌కు సగటు స్టాల్ సైకిళ్లు",
                "ఆదర్శ CPI కు స్టాల్ కలపండి"
            ),
            stepExplanations = listOf(
                "0.20 × 0.50 × 1 సైకిల్ = 0.10 అదనపు సైకిల్.",
                "మొత్తం CPI = 1.0 + 0.10 = 1.10."
            ),
            finalAnswer = "Effective CPI = 1.10 cycles/instruction"
        ),
        "prob_eat_cache" to LocalizedProblemOverlay(
            title = "95% హిట్ రేషియోతో Effective Access Time (EAT)",
            statement = "క్యాష్ Tc = 2 ns, RAM Tm = 50 ns, Hit h = 0.95 అయితే Parallel మరియు Sequential EAT కనుగొనండి.",
            stepTitles = listOf(
                "Miss Ratio (1 - h) కనుగొనండి",
                "సమాంతర (Parallel) EAT సూత్రం",
                "వరుస (Sequential) EAT సూత్రం"
            ),
            stepExplanations = listOf(
                "95% హిట్ అంటే 5% (0.05) మిస్ అయ్యి RAM కు వెళతాయి.",
                "సమాంతర శోధనలో EAT = 0.95×2 + 0.05×50 = 4.4 ns.",
                "వరుస శోధనలో మొదట క్యాష్ 2 ns తర్వాత మిస్ పెనాల్టీ = 4.5 ns."
            ),
            finalAnswer = "EAT (Parallel) = 4.4 ns  |  EAT (Sequential) = 4.5 ns"
        ),
        "prob_cache_split" to LocalizedProblemOverlay(
            title = "32-Bit అడ్రస్ విభజన: Tag, Index & Offset బిట్లు",
            statement = "32-bit అడ్రస్, 64 KB Direct-Mapped Cache, 64 B బ్లాక్ సైజుకు Offset, Index మరియు Tag బిట్లు కనుగొనండి.",
            stepTitles = listOf(
                "Block Offset బిట్లు",
                "క్యాష్ లైన్లు & Index బిట్లు",
                "మిగిలిన Tag బిట్లు"
            ),
            stepExplanations = listOf(
                "64 B = 2⁶ కాబట్టి Offset = 6 బిట్లు.",
                "64 KB / 64 B = 1024 (2¹⁰) లైన్లు → Index = 10 బిట్లు.",
                "32 - (10 + 6) = 16 బిట్లు Tag కోసం ఉంటాయి."
            ),
            finalAnswer = "Tag = 16 bits  |  Index = 10 bits  |  Block Offset = 6 bits"
        ),
        "prob_vm_split" to LocalizedProblemOverlay(
            title = "Virtual Page Number (VPN) & Physical Frame బిట్లు",
            statement = "32-bit Virtual Address, 4 KB పేజీ, 256 MB RAM కు VPN, Offset మరియు PFN బిట్లు కనుగొనండి.",
            stepTitles = listOf(
                "Page Offset బిట్లు",
                "Virtual Page Number (VPN) బిట్లు",
                "Physical Frame Number (PFN) బిట్లు"
            ),
            stepExplanations = listOf(
                "4 KB = 2¹² B కాబట్టి Page Offset = 12 బిట్లు.",
                "32 - 12 = 20 బిట్లు VPN కు ఉంటాయి.",
                "256 MB RAM = 2²⁸ B (28 బిట్లు) → PFN = 28 - 12 = 16 బిట్లు."
            ),
            finalAnswer = "VPN = 20 bits | Offset = 12 bits | PFN = 16 bits"
        ),
        "prob_dma_cycles" to LocalizedProblemOverlay(
            title = "DMA Cycle Stealing vs Programmed I/O సైకిళ్లు",
            statement = "4 KB బ్లాక్ బదిలీలో PIO కు 20 సైకిళ్లు/వర్డ్, DMA కు 1 సైకిల్/వర్డ్ (+100 సెటప్) పడితే రెండింటినీ పోల్చండి.",
            stepTitles = listOf(
                "4 KB లో మొత్తం 32-బిట్ పదాలు లెక్కించండి",
                "Programmed I/O CPU సైకిళ్లు",
                "DMA సైకిల్-స్టీలింగ్ ప్రభావం"
            ),
            stepExplanations = listOf(
                "4096 B / 4 B = 1,024 పదాలు.",
                "PIO లో CPU ప్రతి పదాన్ని చెక్ చేసి కాపీ చేస్తుంది = 20,480 సైకిళ్లు.",
                "DMA స్వయంగా పనిచేసి పదానికి 1 బస్ సైకిల్ మాత్రమే అప్పు తీసుకుంటుంది = 1,124 సైకిళ్లు."
            ),
            finalAnswer = "PIO = 20,480 సైకిళ్లు vs DMA = 1,124 సైకిళ్లు (~18.2× ఆదా!)"
        ),
        "prob_amdahl" to LocalizedProblemOverlay(
            title = "Amdahl's Law: 4 కోర్లు & అనంత కోర్ల Speedup",
            statement = "ప్రోగ్రామ్‌లో 75% సమాంతరంగా (P = 0.75) మరియు 25% వరుసగా నడిస్తే 4 కోర్లు మరియు N → ∞ వద్ద Speedup ఎంత?",
            stepTitles = listOf(
                "4 కోర్లకు Amdahl's సూత్రం వేయండి",
                "4-కోర్ల Speedup విలువ కనుగొనండి",
                "N → ∞ వద్ద గరిష్ట పరిమితి"
            ),
            stepExplanations = listOf(
                "వరుస భాగం 0.25 అలాగే ఉంటుంది; సమాంతర భాగం 0.75/4 = 0.1875 అవుతుంది.",
                "1 / 0.4375 = 2.29× వేగం లభిస్తుంది.",
                "కోర్లు ఎంత పెంచినా 25% వరుస భాగం వల్ల గరిష్ట వేగం 4.0× మించదు."
            ),
            finalAnswer = "4-కోర్ల Speedup = 2.29×  |  గరిష్ట Speedup = 4.0×"
        )
    )
}
