package com.example.data

object CbcsTeluguContent {

    private val coreTopics: Map<String, TopicHinglishData> = mapOf(
        "von_neumann" to TopicHinglishData(
            subtitle = "ఒకే షేర్డ్ మెమరీ మరియు 3 బస్సుల (Address, Data, Control) నిర్మాణం",
            summary = "జాన్ వాన్ న్యూమాన్ (1945) ప్రకారం ప్రోగ్రామ్ ఇన్‌స్ట్రక్షన్లు మరియు డేటా రెండూ ఒకే Main Memory (RAM) లో ఉంటాయి. CPU మూడు బస్సుల ద్వారా కమ్యూనికేట్ చేస్తుంది: Address Bus (ఏకదిశాత్మకం), Data Bus (ద్విదిశాత్మకం), మరియు Control Bus.",
            keyPoints = listOf(
                "Stored-Program Concept: కోడ్ మరియు డేటా రెండూ ఒకే RAM లో నిల్వ ఉంటాయి.",
                "Address Bus ఒక వైపు మాత్రమే వెళుతుంది (k లైన్లతో 2^k స్థానాలు యాక్సెస్ చేయవచ్చు).",
                "Data Bus రెండు వైపులా ప్రవహిస్తుంది; దీని వెడల్పు CPU word size ను నిర్ణయిస్తుంది.",
                "Control Bus Read/Write, Clock మరియు Interrupt సిగ్నల్స్‌ను సమన్వయం చేస్తుంది."
            ),
            stepDescriptions = listOf(
                "దశ 1: Control Unit PC నుండి అడ్రస్‌ను Address Bus పై ఉంచుతుంది.",
                "దశ 2: Control Bus పై MEM_READ సిగ్నల్ ఆన్ అవుతుంది.",
                "దశ 3: RAM నుండి డేటా/ఇన్‌స్ట్రక్షన్ Data Bus ద్వారా CPU రిజిస్టర్లకు వస్తుంది.",
                "దశ 4: ALU లెక్కింపు పూర్తి చేసి Accumulator లేదా I/O కు అవుట్‌పుట్ ఇస్తుంది."
            ),
            partRoles = mapOf(
                "cu" to "టైమింగ్ మరియు కంట్రోల్ సిగ్నల్స్ ఇచ్చి CPU మొత్తాన్ని నడిపే ప్రధాన భాగం.",
                "alu" to "అంకగణిత (+, -) మరియు లాజిక్ (AND, OR, XOR) గణనలు చేస్తుంది.",
                "regs" to "CPU లోపల అత్యంత వేగవంతమైన తాత్కాలిక మెమరీ (PC, IR, MAR, MDR, AC).",
                "memory" to "ఇన్‌స్ట్రక్షన్లు మరియు డేటా నిల్వ ఉండే ప్రధాన RAM.",
                "io" to "బయటి పరికరాలను సిస్టమ్ బస్సుకు అనుసంధానిస్తుంది."
            ),
            vivaAnswers = listOf(
                "ఏ మెమరీ సెల్ చదవాలో CPU (లేదా DMA) మాత్రమే నిర్దేశిస్తుంది; RAM ఎప్పుడూ అడ్రస్ పంపదు.",
                "కోడ్ మరియు డేటా ఒకే బస్సును పంచుకోవడం వల్ల ఒకేసారి ఇన్‌స్ట్రక్షన్ ఫెచ్ మరియు డేటా రీడ్ సాధ్యం కాదు — దీనినే Von Neumann Bottleneck అంటారు."
            )
        ),
        "harvard_vs_von" to TopicHinglishData(
            subtitle = "వేర్వేరు Instruction/Data బస్సులు vs సింగిల్ షేర్డ్ బస్సు",
            summary = "హార్వర్డ్ ఆర్కిటెక్చర్‌లో Instruction Memory మరియు Data Memory వేర్వేరుగా ఉండి స్వతంత్ర బస్సులను కలిగి ఉంటాయి. దీనివల్ల CPU ఒకే సైకిల్‌లో తదుపరి ఇన్‌స్ట్రక్షన్ ఫెచ్ చేస్తూనే డేటాను కూడా చదవగలదు.",
            keyPoints = listOf(
                "హార్వర్డ్‌లో రెండు వేర్వేరు బస్సుల వల్ల సమాంతర యాక్సెస్ మరియు అధిక వేగం లభిస్తుంది.",
                "ఇన్‌స్ట్రక్షన్ మెమరీ బిట్-వెడల్పు డేటా మెమరీ కంటే భిన్నంగా ఉండవచ్చు.",
                "వాన్ న్యూమాన్ తక్కువ ఖర్చుతో కూడినది మరియు RAM స్థలాన్ని సమర్థవంతంగా వాడుతుంది.",
                "ఆధునిక CPUలు Modified Harvard వాడతాయి: RAM ఒకటే కానీ L1 Cache I-Cache, D-Cache గా వేరుగా ఉంటుంది."
            ),
            stepDescriptions = listOf(
                "హార్వర్డ్ మోడ్: డయాగ్రామ్‌లో రెండు బస్సులు ఒకేసారి పనిచేయడం చూడండి.",
                "వాన్ న్యూమాన్ మోడ్: పైన టోగుల్ చేసి సింగిల్ బస్సు బాటిల్‌నెక్‌ను గమనించండి."
            ),
            partRoles = mapOf(
                "inst_mem" to "ప్రోగ్రామ్ కోడ్ మాత్రమే నిల్వ చేసే ప్రత్యేక మెమరీ.",
                "cpu_core" to "ఇన్‌స్ట్రక్షన్ ఫెచ్ మరియు డేటా యాక్సెస్ కోసం రెండు పోర్ట్‌లు ఉన్న కోర్.",
                "data_mem" to "వేరియబుల్స్ మరియు స్టాక్ నిల్వ చేసే ప్రత్యేక RAM."
            ),
            vivaAnswers = listOf(
                "మైక్రోకంట్రోలర్లు (AVR, ARM Cortex-M), DSP చిప్‌లు మరియు ఆధునిక CPU లలోని L1 స్ప్లిట్ క్యాష్‌లో.",
                "వాన్ న్యూమాన్ — ఒకే RAM లో కోడ్ మరియు డేటా అవసరాన్ని బట్టి స్థలాన్ని పంచుకోగలవు."
            )
        ),
        "instruction_cycle" to TopicHinglishData(
            subtitle = "PC, MAR, MDR, IR లతో Fetch → Decode → Execute చక్రం",
            summary = "ప్రతి మెషీన్ ఇన్‌స్ట్రక్షన్ మూడు దశల్లో నడుస్తుంది: RAM నుండి తీసుకురావడం (Fetch), Control Unit లో అర్థం చేసుకోవడం (Decode), మరియు ALU ద్వారా అమలు చేయడం (Execute).",
            keyPoints = listOf(
                "PC (Program Counter) ఎప్పుడూ తదుపరి ఇన్‌స్ట్రక్షన్ అడ్రస్‌ను కలిగి ఉంటుంది.",
                "MAR (Memory Address Register) RAM కు పంపాల్సిన అడ్రస్‌ను పట్టుకుంటుంది.",
                "MDR (Memory Data Register) RAM నుండి వచ్చిన లేదా వెళ్ళే డేటాను ఉంచుతుంది.",
                "IR (Instruction Register) డీకోడ్ అయ్యే వరకు ప్రస్తుత ఇన్‌స్ట్రక్షన్‌ను నిల్వ చేస్తుంది."
            ),
            stepDescriptions = listOf(
                "T0: PC తన అడ్రస్ (0x200) ను MAR కు పంపుతుంది: MAR ← PC.",
                "T1: RAM నుండి ఇన్‌స్ట్రక్షన్ Data Bus ద్వారా MDR కు వస్తుంది, PC +1 అవుతుంది.",
                "T2: ఇన్‌స్ట్రక్షన్ MDR నుండి IR కు మారుతుంది.",
                "T3: Control Unit IR లోని ఆప్‌కోడ్‌ను డీకోడ్ చేసి ALU తో అమలు చేస్తుంది."
            ),
            partRoles = mapOf(
                "pc" to "తదుపరి ఇన్‌స్ట్రక్షన్ అడ్రస్‌ను సూచిస్తుంది.",
                "mar" to "RAM యాక్సెస్ కోసం అడ్రస్‌ను లాచ్ చేస్తుంది.",
                "ram" to "M[MAR] స్థానంలోని బైనరీ పదాన్ని అందిస్తుంది.",
                "mdr" to "RAM నుండి చదివిన డేటా/ఇన్‌స్ట్రక్షన్‌ను బఫర్ చేస్తుంది.",
                "ir" to "డీకోడింగ్ సమయంలో ప్రస్తుత ఇన్‌స్ట్రక్షన్‌ను ఉంచుతుంది.",
                "cu_alu" to "డీకోడ్ చేసి ఆపరేషన్‌ను పూర్తి చేస్తుంది."
            ),
            vivaAnswers = listOf(
                "ప్రస్తుత ఇన్‌స్ట్రక్షన్ పూర్తయ్యేలోపే PC తదుపరి ఇన్‌స్ట్రక్షన్ అడ్రస్‌తో సిద్ధంగా ఉండటానికి.",
                "MAR లో మెమరీ అడ్రస్ ఉంటుంది; MDR లో ఆ అడ్రస్ వద్ద ఉన్న అసలు డేటా ఉంటుంది."
            )
        ),
        "alu_micro_ops" to TopicHinglishData(
            subtitle = "అరిథ్మెటిక్, లాజిక్, షిఫ్ట్ యూనిట్లు మరియు స్టేటస్ ఫ్లాగ్స్",
            summary = "ALU లోపల Arithmetic Unit (Full Adders), Logic Unit (AND/OR/XOR) మరియు Shifter సమాంతరంగా పనిచేస్తాయి. 4:1 MUX సెలెక్ట్ లైన్ల ఆధారంగా సరైన ఫలితాన్ని బయటకు పంపుతుంది.",
            keyPoints = listOf(
                "Arithmetic: కూడిక (A+B) మరియు 2's complement ద్వారా తీసివేత (A + B' + 1).",
                "Logic: బిట్-వైజ్ AND, OR, XOR, NOT ఆపరేషన్లు.",
                "Shift: Logical, Arithmetic మరియు Circular Rotate షిఫ్ట్‌లు.",
                "Status Flags: Zero (Z), Carry (C), Negative (N), Overflow (V)."
            ),
            stepDescriptions = listOf(
                "దశ 1: Reg A మరియు Reg B నుండి ఆపరాండ్లు అన్ని ALU యూనిట్లలోకి ప్రవేశిస్తాయి.",
                "దశ 2: పైన ADD / XOR / SHL మార్చి MUX ఏ మార్గాన్ని ఎంచుకుంటుందో చూడండి.",
                "దశ 3: ఫలితం Accumulator లోకి వెళ్లి Z, C, N, V ఫ్లాగ్స్ అప్‌డేట్ అవుతాయి."
            ),
            partRoles = mapOf(
                "reg_a" to "మొదటి ఆపరాండ్ A ని సరఫరా చేస్తుంది.",
                "reg_b" to "రెండవ ఆపరాండ్ B ని సరఫరా చేస్తుంది.",
                "arith_unit" to "బైనరీ కూడిక మరియు తీసివేత చేసే ఫుల్ యాడర్ గొలుసు.",
                "logic_unit" to "AND, OR, XOR గేట్ శ్రేణి.",
                "shifter" to "బిట్లను ఎడమ/కుడి వైపు షిఫ్ట్ చేస్తుంది.",
                "mux" to "సరైన అవుట్‌పుట్‌ను ఎంచుకునే 4:1 మల్టీప్లెక్సర్.",
                "flags" to "Z, C, N, V కండిషన్ ఫ్లాగ్ బిట్లను నిల్వ చేస్తుంది."
            ),
            vivaAnswers = listOf(
                "B యొక్క 1's complement (B') తీసుకొని Carry-In = 1 ఇవ్వడం ద్వారా A + B' + 1 = A - B లెక్కిస్తుంది.",
                "రెండు ధన సంఖ్యలను కూడినప్పుడు ఋణ గుర్తు వస్తే లేదా రెండు ఋణ సంఖ్యలను కూడినప్పుడు ధన గుర్తు వస్తే Overflow (V=1) జరుగుతుంది."
            )
        ),
        "addressing_modes" to TopicHinglishData(
            subtitle = "Immediate, Direct, Indirect & Indexed లలో Effective Address గణన",
            summary = "ఇన్‌స్ట్రక్షన్ లోని అడ్రస్ ఫీల్డ్ నుండి అసలు ఆపరాండ్ అడ్రస్ (EA) ను ఎలా కనుగొనాలో Addressing Modes చెప్తాయి. పైన 4 మోడ్‌లను మార్చి ఎన్ని మెమరీ హాప్స్ పడతాయో చూడండి!",
            keyPoints = listOf(
                "Immediate: విలువ నేరుగా ఇన్‌స్ట్రక్షన్‌లోనే ఉంటుంది (0 RAM రీడ్స్, అత్యంత వేగవంతం).",
                "Direct: ఇన్‌స్ట్రక్షన్‌లో నేరుగా అడ్రస్ ఉంటుంది (EA = Addr, 1 RAM రీడ్).",
                "Indirect: అడ్రస్ వద్ద మరో అడ్రస్ (పాయింటర్) ఉంటుంది (EA = M[Addr], 2 RAM రీడ్స్).",
                "Indexed: EA = Addr + Index Register (IX) — అర్రే లూప్‌లకు ఉత్తమం."
            ),
            stepDescriptions = listOf(
                "పైన Immediate, Direct, Indirect లేదా Indexed ఎంచుకుని డేటా వరకు వెళ్లే హాప్స్ చూడండి."
            ),
            partRoles = mapOf(
                "ir_field" to "ఆప్‌కోడ్ మరియు అడ్రస్/లిటరల్ ఉన్న ఇన్‌స్ట్రక్షన్ రిజిస్టర్.",
                "reg_file" to "Indexed మోడ్‌లో ఆఫ్‌సెట్ జోడించే Index Register (IX).",
                "mem_ptr" to "Indirect మోడ్‌లో అసలు అడ్రస్‌ను కలిగి ఉండే పాయింటర్ సెల్.",
                "mem_data" to "తుది ఆపరాండ్ విలువ ఉన్న మెమరీ స్థానం."
            ),
            vivaAnswers = listOf(
                "Indexed Addressing — బేస్ అడ్రస్ స్థిరంగా ఉండి ఇండెక్స్ రిజిస్టర్ IX ప్రతి లూప్‌కు పెరుగుతుంది.",
                "ముందు M[Addr] నుండి అసలు అడ్రస్ చదివి, ఆపై M[M[Addr]] నుండి డేటా చదవాలి కాబట్టి 2 మెమరీ యాక్సెస్‌లు పడతాయి."
            )
        ),
        "control_unit" to TopicHinglishData(
            subtitle = "Hardwired (వేగవంతమైన గేట్లు) vs Microprogrammed (ROM కంట్రోల్ మెమరీ)",
            summary = "Control Unit సమయబద్ధమైన కంట్రోల్ సిగ్నల్స్‌ను ఉత్పత్తి చేస్తుంది. Hardwired CU స్థిర లాజిక్ గేట్లను వాడుతుంది (చాలా వేగం, RISC), Microprogrammed CU ROM లో మైక్రోకోడ్‌ను నిల్వ చేస్తుంది (మార్చడం సులభం, CISC).",
            keyPoints = listOf(
                "Hardwired CU: PLA/లాజిక్ గేట్లతో వేగంగా పనిచేస్తుంది, కానీ మార్చడం కష్టం.",
                "Microprogrammed CU: CMAR -> Control Memory ROM -> CMDR ద్వారా కంట్రోల్ వర్డ్స్ ఇస్తుంది.",
                "Horizontal మైక్రోప్రోగ్రామింగ్‌లో ప్రతి సిగ్నల్‌కు ప్రత్యేక బిట్ ఉంటుంది; Vertical లో ఎన్కోడ్ చేసిన ఫీల్డ్స్ ఉంటాయి."
            ),
            stepDescriptions = listOf(
                "పైన Hardwired మరియు Microprogrammed మోడ్లను మార్చి కంట్రోల్ సిగ్నల్ తయారీని చూడండి."
            ),
            partRoles = mapOf(
                "ir_op" to "ఆప్‌కోడ్‌ను గేట్ మ్యాట్రిక్స్ లేదా ROM అడ్రస్‌కు పంపుతుంది.",
                "gate_matrix" to "వేగవంతమైన కాంబినేషనల్ PLA గేట్ సర్క్యూట్.",
                "ctrl_mem" to "మైక్రో-ఇన్‌స్ట్రక్షన్లను నిల్వ చేసే Control Memory ROM.",
                "ctrl_signals" to "ALU మరియు బస్సులను నడిపే కంట్రోల్ లైన్లు."
            ),
            vivaAnswers = listOf(
                "ఒక క్లాక్ సైకిల్‌లో ఏ మైక్రో-ఆపరేషన్లు జరగాలో సూచించే బిట్ నమూనానే కంట్రోల్ వర్డ్.",
                "RISC ఇన్‌స్ట్రక్షన్లు సరళంగా మరియు ఒకే పరిమాణంలో ఉంటాయి కాబట్టి Hardwired కంట్రోల్ వేగంగా సరిపోతుంది."
            )
        ),
        "pipeline_5_stage" to TopicHinglishData(
            subtitle = "IF → ID → EX → MEM → WB పైప్‌లైన్, స్టాల్స్ & ఫార్వార్డింగ్",
            summary = "పైప్‌లైనింగ్ ఒక అసెంబ్లీ లైన్ లాగా అనేక ఇన్‌స్ట్రక్షన్లను ఒకేసారి వేర్వేరు దశల్లో నడిపిస్తుంది. దీనివల్ల throughput ~5 రెట్లు పెరుగుతుంది. డేటా హాజార్డ్ వస్తే Forwarding లేదా STALL బబుల్ వాడతారు.",
            keyPoints = listOf(
                "5 దశలు: IF (Fetch), ID (Decode), EX (ALU), MEM (Data RAM), WB (WriteBack).",
                "దశల మధ్య Pipeline Latches డేటాను నిల్వ చేస్తాయి.",
                "Data Hazard (RAW): Forwarding (Bypass) ద్వారా స్టాల్ లేకుండా పరిష్కరించవచ్చు.",
                "Control Hazard: బ్రాంచ్ సూచనల వల్ల కలుగుతుంది — Branch Prediction ద్వారా తగ్గిస్తారు."
            ),
            stepDescriptions = listOf(
                "దశ 1: 2D Gantt చార్ట్‌లో 5 ఇన్‌స్ట్రక్షన్లు T1–T7 లో సమాంతరంగా నడవడం చూడండి.",
                "దశ 2: పైన 'RAW Hazard (Bubble)' మార్చి ఎరుపు STALL బబుల్‌ను చూడండి."
            ),
            partRoles = mapOf(
                "if_stage" to "I-Cache నుండి ఇన్‌స్ట్రక్షన్ ఫెచ్ చేసి PC+4 చేస్తుంది.",
                "id_stage" to "డీకోడ్ చేసి Rs1, Rs2 రిజిస్టర్లు చదువుతుంది.",
                "ex_stage" to "ALU లెక్కింపు లేదా బ్రాంచ్ అడ్రస్ గణిస్తుంది.",
                "mem_stage" to "LOAD/STORE కోసం Data RAM ను యాక్సెస్ చేస్తుంది.",
                "wb_stage" to "ఫలితాన్ని Rd రిజిస్టర్లో రాస్తుంది."
            ),
            vivaAnswers = listOf(
                "లేదు! ఒక ఇన్‌స్ట్రక్షన్ పట్టే సమయం (Latency) తగ్గదు, కానీ సెకనుకు పూర్తయ్యే ఇన్‌స్ట్రక్షన్ల సంఖ్య (Throughput) పెరుగుతుంది.",
                "1) Structural Hazard, 2) Data Hazard (RAW/WAR/WAW), 3) Control Hazard."
            )
        ),
        "memory_hierarchy" to TopicHinglishData(
            subtitle = "Registers నుండి L1/L2/L3 Cache, RAM & SSD వరకు వేగం vs సైజు పిరమిడ్",
            summary = "ఒకే మెమరీ అత్యంత వేగంగా, అతి పెద్దదిగా, చౌకగా ఉండలేదు. అందుకే Locality of Reference ఉపయోగించి పిరమిడ్ ఆకారంలో మెమరీ స్థాయిలను పేర్చుతారు.",
            keyPoints = listOf(
                "పిరమిడ్ పైకి వెళ్లేకొద్దీ: వేగం పెరుగుతుంది, ధర పెరుగుతుంది, సామర్థ్యం తగ్గుతుంది.",
                "Temporal Locality: ఇప్పుడే వాడిన డేటా మళ్ళీ త్వరలో వాడబడుతుంది (లూప్‌లు).",
                "Spatial Locality: పక్కపక్క అడ్రస్‌లు త్వరలో వాడబడతాయి (అర్రేలు).",
                "Hit Ratio (H) ఎక్కువైతే Effective Memory Access Time (EMAT) తగ్గుతుంది."
            ),
            stepDescriptions = listOf(
                "Cache Hit: డేటా L1/L2 SRAM లోనే దొరికితే (~1 ns) వెంటనే CPU కు వెళుతుంది.",
                "Cache Miss: దొరకకపోతే RAM/Disk నుండి 64-Byte బ్లాక్ పైకి లోడ్ అవుతుంది."
            ),
            partRoles = mapOf(
                "regs" to "CPU లోపల అత్యంత వేగవంతమైన రిజిస్టర్లు (~0.3 ns).",
                "l1_cache" to "కోర్ దగ్గర ఉన్న వేగవంతమైన SRAM క్యాష్ (1–4 ns).",
                "l3_cache" to "అన్ని కోర్లు పంచుకునే L3 క్యాష్ (~12 ns).",
                "ram" to "ప్రధాన DRAM మెమరీ (~60 ns, GBs).",
                "disk" to "శాశ్వత SSD/HDD స్టోరేజ్ (TBs)."
            ),
            vivaAnswers = listOf(
                "SRAM 6 ట్రాన్సిస్టర్లను వాడుతుంది (చాలా వేగం, రీఫ్రెష్ అక్కర్లేదు); DRAM 1 ట్రాన్సిస్టర్ + కెపాసిటర్ వాడుతుంది (చౌక, రీఫ్రెష్ అవసరం).",
                "EMAT = 0.9(10) + 0.1(10 + 100) = 9 + 11 = 20 ns."
            )
        ),
        "cache_mapping" to TopicHinglishData(
            subtitle = "Direct, Fully Associative & Set-Associative క్యాష్ మ్యాపింగ్",
            summary = "Main RAM లోని బ్లాక్ Cache లోని ఏ లైన్‌లో కూర్చోవాలో Cache Mapping నిర్ణయిస్తుంది. పైన 3 మోడ్లను మార్చి Direct మోడ్లో Conflict Miss ఎలా వస్తుందో చూడండి.",
            keyPoints = listOf(
                "Direct Mapping: Block j -> Line (j mod N). సరళమైన హార్డ్‌వేర్ కానీ Conflict Misses ఎక్కువ.",
                "Fully Associative: ఏ బ్లాక్ అయినా ఏ ఖాళీ లైన్‌లోనైనా కూర్చోవచ్చు. Conflict Miss ఉండదు.",
                "k-Way Set-Associative: రెండింటి మేలుకలయిక! S సెట్లలో k లైన్లు ఉంటాయి.",
                "పాత లైన్లను తొలగించడానికి LRU, FIFO పాలసీలు వాడతారు."
            ),
            stepDescriptions = listOf(
                "పైన Direct, Associative, 2-Way Set Assoc మార్చి Block 0 మరియు Block 4 ఎలా సర్దుబాటు అవుతాయో చూడండి."
            ),
            partRoles = mapOf(
                "cpu_addr" to "ఫిజికల్ అడ్రస్‌ను Tag | Index/Set | Offset బిట్లుగా విభజిస్తుంది.",
                "cache_b0" to "క్యాష్ లైన్ / వే 0.",
                "cache_b1" to "క్యాష్ లైన్ / వే 1.",
                "ram_blk0" to "RAM బ్లాక్ 0.",
                "ram_blk4" to "RAM బ్లాక్ 4 — Direct మోడ్‌లో బ్లాక్ 0 తో ఘర్షణ పడుతుంది!"
            ),
            vivaAnswers = listOf(
                "ఎక్కువగా వాడే రెండు RAM బ్లాకులు ఒకే క్యాష్ లైన్‌పైకి వచ్చి ఒకదానికొకటి బయటకు తోసేయడాన్ని Cache Thrashing అంటారు.",
                "Write-Through క్యాష్ మరియు RAM రెండింటినీ వెంటనే అప్‌డేట్ చేస్తుంది; Write-Back క్యాష్‌ను మాత్రమే అప్‌డేట్ చేసి Dirty Bit సెట్ చేస్తుంది."
            )
        ),
        "virtual_memory" to TopicHinglishData(
            subtitle = "Virtual-to-Physical అడ్రస్ మార్పిడి, TLB & Page Fault",
            summary = "Virtual Memory వల్ల పెద్ద ప్రోగ్రామ్‌లు తక్కువ RAM లోనూ నడుస్తాయి. MMU మెమరీని Pages (వర్చువల్) మరియు Frames (ఫిజికల్) గా విభజిస్తుంది; TLB ఈ మార్పిడిని వేగవంతం చేస్తుంది.",
            keyPoints = listOf(
                "Virtual Address = [ Page # | Offset ]. మార్పిడిలో Offset మారదు.",
                "TLB అనేది ఇటీవలి Page -> Frame మ్యాపింగ్‌లను నిల్వ చేసే వేగవంతమైన క్యాష్.",
                "Page Table లో Valid bit = 0 ఉంటే Page Fault జరిగి Swap Disk నుండి లోడ్ అవుతుంది.",
                "Belady's Anomaly FIFO లో రావచ్చు కానీ LRU లో రాదు."
            ),
            stepDescriptions = listOf(
                "పైన 'TLB Hit' మరియు 'Page Fault' మార్చి వేగవంతమైన TLB మార్గం vs డిస్క్ స్వాప్ చూడండి."
            ),
            partRoles = mapOf(
                "cpu_va" to "CPU ఇచ్చే వర్చువల్ అడ్రస్ [VPN | Offset].",
                "tlb" to "1 సైకిల్‌లో పేజీని ఫ్రేమ్‌గా మార్చే TLB క్యాష్.",
                "page_table" to "RAM లో ఉండే OS పేజీ టేబుల్.",
                "phys_ram" to "అసలు DRAM ఫ్రేమ్‌లు.",
                "swap_disk" to "పేజీ ఫాల్ట్ సమయంలో పేజీలను అందించే స్వాప్ డిస్క్."
            ),
            vivaAnswers = listOf(
                "వర్చువల్ పేజీ మరియు ఫిజికల్ ఫ్రేమ్ సైజు సమానంగా (ఉదా: 4 KB) ఉంటుంది కాబట్టి ఆఫ్‌సెట్ బిట్లు మారవు.",
                "CPU పనులు చేయడం కంటే RAM మరియు Disk మధ్య పేజీలను మార్చడంలోనే ఎక్కువ సమయం వృథా చేయడాన్ని Thrashing అంటారు."
            )
        ),
        "io_dma" to TopicHinglishData(
            subtitle = "HOLD/HLDA ద్వారా DMA బస్ మాస్టరింగ్ vs Programmed I/O",
            summary = "Programmed I/O లో CPU సమయం వృథా అవుతుంది. DMA (Direct Memory Access) లో DMA కంట్రోలర్ బస్సును తీసుకొని Disk/NIC నుండి నేరుగా RAM లోకి డేటాను వేగంగా పంపుతుంది.",
            keyPoints = listOf(
                "Programmed I/O: CPU స్థితిని పదేపదే చెక్ చేస్తూ బిజీగా ఉంటుంది.",
                "Interrupt I/O: పరికరం సిద్ధమైనప్పుడు IRQ పంపుతుంది.",
                "DMA Handshake: DMA 'HOLD' అడుగుతుంది -> CPU 'HLDA' ఇచ్చి బస్సును వదులుతుంది.",
                "DMA మోడ్‌లు: Burst Mode, Cycle Stealing, Transparent Mode."
            ),
            stepDescriptions = listOf(
                "పైన 'DMA Transfer' మరియు 'Programmed I/O' మార్చి CPU ఎలా ఫ్రీ అవుతుందో చూడండి."
            ),
            partRoles = mapOf(
                "cpu" to "HLDA ఇచ్చి బస్సును DMA కు అప్పగిస్తుంది.",
                "dma" to "నేరుగా I/O నుండి RAM కు డేటా పంపే బస్ మాస్టర్.",
                "ram" to "బల్క్ డేటాను స్వీకరించే ప్రధాన మెమరీ.",
                "disk_io" to "DREQ ప్రారంభించే హై-స్పీడ్ డిస్క్/నెట్‌వర్క్ పరికరం."
            ),
            vivaAnswers = listOf(
                "DMA ఒక పదం పంపడానికి CPU నుండి కేవలం ఒక్క మెమరీ సైకిల్ మాత్రమే అప్పుగా తీసుకొని బస్సును తిరిగి ఇవ్వడాన్ని Cycle Stealing అంటారు.",
                "1) Address Register, 2) Word Count Register, 3) Control/Status Register."
            )
        ),
        "flynns_multicore" to TopicHinglishData(
            subtitle = "SISD, SIMD (GPU), MIMD (మల్టీకోర్) & క్యాష్ కోహెరెన్స్",
            summary = "మైఖేల్ ఫ్లిన్ కంప్యూటర్లను SISD, SIMD, MISD, MIMD అని 4 రకాలుగా వర్గీకరించారు. ఆధునిక GPU లు SIMD ను, మల్టీకోర్ CPU లు MIMD ను ఉపయోగిస్తాయి.",
            keyPoints = listOf(
                "SISD: 1 కంట్రోల్ యూనిట్, 1 ALU, 1 డేటా స్ట్రీమ్.",
                "SIMD: 1 కంట్రోల్ యూనిట్ ఒకే ఇన్‌స్ట్రక్షన్‌ను అనేక ALU లకు పంపుతుంది (GPUs).",
                "MISD: ఒకే డేటాపై అనేక ఇన్‌స్ట్రక్షన్లు.",
                "MIMD: స్వతంత్ర కోర్లు తమ సొంత ఇన్‌స్ట్రక్షన్లను నడుపుతూ MESI కోహెరెన్స్ పాటిస్తాయి."
            ),
            stepDescriptions = listOf(
                "పైన SISD, SIMD, MIMD మార్చి 1 కోర్ నుండి 4-కోర్ మల్టీకోర్ నిర్మాణాన్ని చూడండి."
            ),
            partRoles = mapOf(
                "cu_master" to "ఇన్‌స్ట్రక్షన్ స్ట్రీమ్ ఇచ్చే కంట్రోల్ యూనిట్.",
                "pe1" to "సమాంతరంగా పనిచేసే ALU కోర్లు.",
                "shared_l3" to "కోర్ల మధ్య డేటాను సమన్వయం చేసే షేర్డ్ L3 క్యాష్ (MESI)."
            ),
            vivaAnswers = listOf(
                "గ్రాఫిక్స్ పిక్సెల్స్ మరియు AI మ్యాట్రిక్స్‌లపై ఒకే ఆపరేషన్ వేల డేటా పాయింట్లపై ఒకేసారి చేయాలి కాబట్టి.",
                "Core 0 తన L1 క్యాష్‌లో విలువ మార్చినప్పుడు Core 1 లో పాత విలువ ఉండకుండా MESI ప్రోటోకాల్ ద్వారా సరిచేస్తారు."
            )
        )
    )

    private val coreQuizExplanations: Map<String, String> = mapOf(
        "q1" to "Program Counter (PC) లో ఎప్పుడూ తదుపరి ఇన్‌స్ట్రక్షన్ అడ్రస్ నిల్వ ఉంటుంది.",
        "q2" to "20-బిట్ Address Bus తో 2^20 = 1,048,576 (1 MB) మెమరీ స్థానాలను యాక్సెస్ చేయవచ్చు.",
        "q3" to "హార్వర్డ్‌లో ఇన్‌స్ట్రక్షన్ మరియు డేటా బస్సులు వేర్వేరుగా ఉండటం వల్ల రెండూ ఒకేసారి పనిచేస్తాయి.",
        "q4" to "ఫెచ్ సైకిల్‌లో మొదటి దశ MAR ← PC.",
        "q5" to "Immediate అడ్రసింగ్‌లో విలువ నేరుగా ఇన్‌స్ట్రక్షన్‌లోనే ఉంటుంది.",
        "q6" to "2's complement తీసివేతకు A + B' + 1 సూత్రాన్ని వాడతారు.",
        "q7" to "Microprogrammed CU కంట్రోల్ వర్డ్స్‌ను ROM లో నిల్వ చేస్తుంది కాబట్టి మార్చడం సులభం.",
        "q8" to "Indexed అడ్రసింగ్‌లో EA = Address + Index Register (IX).",
        "q9" to "5-దశల పైప్‌లైన్ యొక్క ఆదర్శ గరిష్ట వేగం 5x.",
        "q10" to "Operand Forwarding స్టాల్ లేకుండానే ఫలితాన్ని తదుపరి ఇన్‌స్ట్రక్షన్‌కు పంపుతుంది.",
        "q11" to "LOAD/STORE కోసం Data Memory ను MEM దశ యాక్సెస్ చేస్తుంది.",
        "q12" to "Direct Mapping లో స్థిర మోడ్యులో స్థానం వల్ల Conflict Misses ఎక్కువగా వస్తాయి.",
        "q13" to "TLB ఇటీవలి వర్చువల్-టు-ఫిజికల్ పేజీ మార్పిడులను క్యాష్ చేస్తుంది.",
        "q14" to "ఒకే వేరియబుల్‌ను లూప్‌లో మళ్ళీ మళ్ళీ వాడటం Temporal Locality.",
        "q15" to "Write-Back క్యాష్‌లో Dirty bit = 1 ఉంటే ఆ లైన్‌ను RAM లో రాయాలని అర్థం.",
        "q16" to "DMA HOLD పంపుతుంది మరియు CPU HLDA ఇచ్చి బస్సును అప్పగిస్తుంది.",
        "q17" to "GPU లు వేల పిక్సెల్స్‌పై ఒకే ఇన్‌స్ట్రక్షన్ నడుపుతాయి కాబట్టి అది SIMD.",
        "q18" to "Programmed I/O లో CPU పోలింగ్ చేస్తూ ఎక్కువ సమయం వృథా చేస్తుంది.",
        "q19" to "Amdahl's Law ప్రకారం ప్రోగ్రామ్ లోని సీక్వెన్షియల్ భాగం (1 - P) వేగాన్ని పరిమితం చేస్తుంది."
    )

    val topics: Map<String, TopicHinglishData> = coreTopics + CbcsTeluguDigitalContent.topics

    val quizExplanations: Map<String, String> = coreQuizExplanations + CbcsTeluguDigitalContent.quizExplanations
}
