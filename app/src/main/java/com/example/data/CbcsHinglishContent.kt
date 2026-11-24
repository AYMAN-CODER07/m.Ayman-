package com.example.data

data class TopicHinglishData(
    val subtitle: String,
    val summary: String,
    val keyPoints: List<String>,
    val stepDescriptions: List<String>,
    val partRoles: Map<String, String>,
    val vivaAnswers: List<String>
)

object CbcsHinglishContent {

    private val coreTopics: Map<String, TopicHinglishData> = mapOf(
        "von_neumann" to TopicHinglishData(
            subtitle = "Ek hi shared memory aur 3 buses (Address, Data, Control) ka poora system",
            summary = "John von Neumann ne 1945 mein bataya ki Program Instructions aur Data dono ek hi Main Memory (RAM) mein binary form mein store hote hain. CPU teen system buses ke zariye Memory aur I/O se baat karta hai: Address Bus (sirf CPU se bahar jaati hai), Data Bus (dono taraf chalti hai), aur Control Bus (Read/Write signals bhejti hai).",
            keyPoints = listOf(
                "Stored-Program Concept: Code aur Data dono ek hi RAM mein store rehte hain.",
                "Address Bus unidirectional hoti hai — k lines se CPU total 2^k memory cells access kar sakta hai.",
                "Data Bus bidirectional hoti hai — isi ki width (32-bit / 64-bit) processor ka word size decide karti hai.",
                "Control Bus sabhi operations ko synchronize karti hai (MEM_READ, MEM_WRITE, Clock, Interrupt)."
            ),
            stepDescriptions = listOf(
                "Step 1: Control Unit sabse pehle PC se target address lekar unidirectional Address Bus par rakhta hai.",
                "Step 2: Control Unit Control Bus par MEM_READ signal ON karta hai taaki RAM data dene ke liye ready ho jaaye.",
                "Step 3: RAM us address ka word bidirectional Data Bus ke through CPU Registers mein bhejti hai.",
                "Step 4: ALU calculation complete karke result Accumulator mein rakhta hai ya I/O Interface par output deta hai."
            ),
            partRoles = mapOf(
                "cu" to "Poore CPU ka boss — timing aur control signals generate karke buses aur ALU ko chalata hai.",
                "alu" to "Yahan saari math (+, -) aur logic (AND, OR, XOR, Shift) calculations hoti hain.",
                "regs" to "CPU ke andar sabse tez temporary storage (PC, IR, MAR, MDR, AC).",
                "memory" to "Main RAM jahan instructions aur variables dono address ke hisaab se store hote hain.",
                "io" to "Keyboard, screen ya disk jaise external devices ko system bus se jodta hai."
            ),
            vivaAnswers = listOf(
                "Address sirf CPU (ya DMA master) generate karta hai ki kaunsa cell padhna hai; RAM kabhi CPU ko address nahi bhejti.",
                "Kyunki Code aur Data ek hi bus share karte hain, CPU ek saath instruction fetch aur data read/write nahi kar sakta — isi ko Von Neumann Bottleneck kehte hain."
            )
        ),
        "harvard_vs_von" to TopicHinglishData(
            subtitle = "Alag-alag Instruction/Data buses vs Single shared bus bottleneck",
            summary = "Harvard Architecture mein Instruction Memory aur Data Memory bilkul alag hoti hain aur dono ke paas apni independent bus hoti hai. Isse CPU ek hi clock cycle mein agla instruction bhi fetch kar sakta hai aur data bhi padh/likh sakta hai!",
            keyPoints = listOf(
                "Harvard mein Instruction Bus aur Data Bus alag hone se parallel access milta hai -> zyada speed!",
                "Instruction memory ki bit-width Data memory se alag ho sakti hai (jaise microcontrollers mein).",
                "Von Neumann sasta aur simple hai kyunki ek hi RAM mein code aur data flexibly adjust ho sakte hain.",
                "Aaj ke modern CPUs 'Modified Harvard' use karte hain: RAM unified hoti hai par L1 Cache I-Cache aur D-Cache mein split rehta hai."
            ),
            stepDescriptions = listOf(
                "Harvard Mode: Dekho diagram mein dono buses ek saath chal rahi hain — zero rukawat, full parallel speed!",
                "Von Neumann Mode: Upar toggle dabakar dekho kaise ek hi bridge par traffic jam (bottleneck) banta hai."
            ),
            partRoles = mapOf(
                "inst_mem" to "Sirf program ka code/opcodes store karne wali dedicated memory.",
                "cpu_core" to "Iske paas Instruction Fetch aur Data Load/Store ke liye do alag ports hote hain.",
                "data_mem" to "Variables, arrays aur stack store karne wali alag RAM."
            ),
            vivaAnswers = listOf(
                "Microcontrollers (Arduino/AVR, ARM Cortex-M), DSP chips aur modern CPUs ke split L1 I-Cache & D-Cache mein.",
                "Von Neumann memory space ko behtar use karta hai kyunki bacha hua space code ya data kisi ke bhi kaam aa sakta hai."
            )
        ),
        "instruction_cycle" to TopicHinglishData(
            subtitle = "Fetch -> Decode -> Execute cycle PC, MAR, MDR aur IR ke saath",
            summary = "Har instruction 3 main phases mein poora hota hai: pehle RAM se instruction lana (Fetch), phir Control Unit mein uska matlab samajhna (Decode), aur aakhir mein ALU se kaam karwana (Execute).",
            keyPoints = listOf(
                "PC (Program Counter) hamesha AGLE instruction ka address yaad rakhta hai.",
                "MAR (Memory Address Register) wo address hold karta hai jise RAM se access karna hai.",
                "MDR (Memory Data Register) RAM se aane-jaane wala data/instruction buffer karta hai.",
                "IR (Instruction Register) current instruction ko pakad kar rakhta hai jab tak CU use decode na kare."
            ),
            stepDescriptions = listOf(
                "T0: Sabse pehle PC apna address (0x200) MAR ko copy karta hai: MAR <- PC.",
                "T1: MAR RAM ko address deta hai; RAM se instruction Data Bus ke zariye MDR mein aata hai aur PC +1 hokar 0x201 ban jaata hai.",
                "T2: Fetched instruction MDR se IR mein shift hota hai taaki MDR agle data fetch ke liye free ho jaaye.",
                "T3: Control Unit IR ke opcode ko decode karta hai aur ALU/Registers ko execute karne ka signal deta hai."
            ),
            partRoles = mapOf(
                "pc" to "Agle execute hone wale instruction ka address store karta hai.",
                "mar" to "Address Bus par bhejne ke liye memory address latch karta hai.",
                "ram" to "M[MAR] location par rakha binary word return karta hai.",
                "mdr" to "RAM se padha gaya ya RAM mein likha jaane wala word hold karta hai.",
                "ir" to "Current instruction ko decode hone tak sambhal kar rakhta hai.",
                "cu_alu" to "IR ke opcode ko samajhkar actual calculation ya data movement execute karta hai."
            ),
            vivaAnswers = listOf(
                "Taaki current instruction ke decode/execute hote hi PC agle instruction par ready rahe (jab tak Jump/Branch PC ko badal na de).",
                "MAR mein memory ka ADDRESS hota hai, jabki MDR mein us address par rakhi VALUE (data/instruction) hoti hai."
            )
        ),
        "alu_micro_ops" to TopicHinglishData(
            subtitle = "Arithmetic, Logic, Shift circuits aur PSW Status Flags",
            summary = "ALU ke andar teen sub-units parallel kaam karte hain: Arithmetic Unit (Full Adders), Logic Unit (AND/OR/XOR gates) aur Shifter. Control signals (S1, S0) ke hisaab se 4:1 MUX sahi result ko output par bhejta hai.",
            keyPoints = listOf(
                "Arithmetic Micro-ops: Addition (A + B), aur Subtraction 2's complement se (A + B' + 1).",
                "Logic Micro-ops: Bitwise AND (bits mask karne ke liye), OR (set karne ke liye), XOR (toggle/compare).",
                "Shift Micro-ops: Logical Shift (0 fill), Arithmetic Shift (sign bit safe rakhta hai), Rotate.",
                "Status Flags: Result ke basis par Z (Zero), C (Carry), N (Negative), V (Overflow) flags update hote hain."
            ),
            stepDescriptions = listOf(
                "Step 1: Register A aur B ke operands ek saath Adder, Logic Gates aur Shifter teeno mein enter karte hain.",
                "Step 2: Upar ADD / XOR / SHL mode switch karke dekho kaise MUX sirf selected circuit ka output aage bhejta hai.",
                "Step 3: Final output Accumulator mein jaata hai aur Status Flags (Z, C, N, V) update ho jaate hain."
            ),
            partRoles = mapOf(
                "reg_a" to "ALU ke sabhi circuits ko pehla operand (A) supply karta hai.",
                "reg_b" to "Doosra operand (B) deta hai; subtraction mein iska 1's complement liya jaata hai.",
                "arith_unit" to "Full Adder chain jo binary addition aur subtraction karta hai.",
                "logic_unit" to "Har bit par AND, OR, XOR, NOT gates chalata hai.",
                "shifter" to "Bits ko left ya right shift karke multiply/divide by 2 fast karta hai.",
                "mux" to "Select lines ke hisaab se Adder, Logic ya Shifter mein se ek output chunta hai.",
                "flags" to "Branch decisions ke liye Zero, Carry, Sign aur Overflow flags set karta hai."
            ),
            vivaAnswers = listOf(
                "B ka 1's complement (B') lekar aur Carry-In (Cin) = 1 set karke: A + B' + 1 = A - B.",
                "Jab do positive numbers jodkar negative sign aaye, ya do negative numbers jodkar positive sign aaye (Cin_MSB XOR Cout_MSB = 1)."
            )
        ),
        "addressing_modes" to TopicHinglishData(
            subtitle = "Immediate, Direct, Indirect aur Indexed mein Effective Address kaise nikalta hai",
            summary = "Addressing Modes batate hain ki instruction ke Address field se asli operand (Effective Address - EA) kaise dhundha jaaye. Upar 4 modes toggle karke diagram mein pointer hops compare karo!",
            keyPoints = listOf(
                "Immediate (#value): Operand seedha instruction ke andar likha hota hai -> 0 memory read, sabse tez!",
                "Direct (Addr): Instruction mein seedha operand ka address hota hai (EA = Addr) -> 1 memory read.",
                "Indirect (@Addr): Instruction ek pointer cell dikhata hai jahan asli address rakha hai (EA = M[Addr]) -> 2 memory reads.",
                "Indexed (Addr + IX): Base address mein Index Register IX joda jaata hai -> arrays aur loops ke liye best!"
            ),
            stepDescriptions = listOf(
                "Upar Immediate, Direct, Indirect ya Indexed select karo aur dekho CPU ko operand tak pahunchne mein kitne hops lagte hain."
            ),
            partRoles = mapOf(
                "ir_field" to "Instruction ka hissa jisme Opcode, Mode bits aur Address/Constant hota hai.",
                "reg_file" to "Index/Base register (IX) jiski value Indexed mode mein Addr ke saath judti hai.",
                "mem_ptr" to "Indirect mode ka beech wala memory cell jo asli target ka address hold karta hai.",
                "mem_data" to "Final memory cell jahan hamara asli data operand store hota hai."
            ),
            vivaAnswers = listOf(
                "Indexed Addressing — kyunki base address fixed rehta hai aur Index Register (IX) har loop step par +1 hota jaata hai.",
                "Kyunki Indirect mode mein pehle M[Addr] se asli address padhna padta hai, phir us address M[M[Addr]] se data padhna padta hai (2 memory reads)."
            )
        ),
        "control_unit" to TopicHinglishData(
            subtitle = "Hardwired Logic Gates (Tez/RISC) vs Microprogrammed ROM (Flexible/CISC)",
            summary = "Control Unit poore processor ke timed control signals banata hai. Hardwired CU fixed logic gates use karta hai (super fast), jabki Microprogrammed CU ek Control Memory ROM mein micro-routines store karta hai (change karna aasaan).",
            keyPoints = listOf(
                "Hardwired CU: IR Opcode + Timing Counter ko PLA/logic gates se decode karta hai. Speed bahut tez par modify karna mushkil.",
                "Microprogrammed CU: Har instruction ek microprogram chalata hai jo Control Memory (CMAR -> ROM -> CMDR) mein stored hota hai.",
                "Horizontal Microprogramming mein har signal ke liye alag bit hoti hai (fast, wide word); Vertical mein encoded fields hote hain (compact)."
            ),
            stepDescriptions = listOf(
                "Upar Hardwired CU aur Microprogrammed CU toggle karke dekho kaise Control Word generate hota hai."
            ),
            partRoles = mapOf(
                "ir_op" to "Opcode ko gate matrix ya Control Memory ke starting address par map karta hai.",
                "gate_matrix" to "Hardwired combinational PLA circuit jo 1 cycle mein fast signals nikalta hai.",
                "ctrl_mem" to "ROM store jisme har complex instruction ke microinstructions likhe hote hain.",
                "ctrl_signals" to "ALU select, Register enable aur Memory Read/Write lines drive karta hai."
            ),
            vivaAnswers = listOf(
                "Ek binary pattern jiske bits yeh batate hain ki kisi ek clock cycle mein kaun-kaun se micro-operations chalenge.",
                "RISC mein instructions simple aur fixed-length hote hain, isliye Hardwired gates chhote, fast aur single-cycle pipeline ke liye best rehte hain."
            )
        ),
        "pipeline_5_stage" to TopicHinglishData(
            subtitle = "IF -> ID -> EX -> MEM -> WB assembly line, Stall Bubbles & Forwarding",
            summary = "Pipelining bilkul ek factory assembly line ki tarah kaam karti hai: jab ek instruction EX stage mein hota hai, tab doosra ID mein aur teesra IF mein parallel chalta hai! Isse throughput ~5x badh jaata hai.",
            keyPoints = listOf(
                "5 Stages: IF (Fetch), ID (Decode), EX (ALU Execute), MEM (RAM Load/Store), WB (Register WriteBack).",
                "Stages ke beech Pipeline Latches (IF/ID, ID/EX, EX/MEM, MEM/WB) data ko ek clock se agle clock tak pakadte hain.",
                "Data Hazard (RAW): Jab agle instruction ko pichle ka result pehle hi chahiye ho. Solution: Forwarding (Bypass) ya NOP Stall bubble.",
                "Control Hazard: Branch/Jump ki wajah se galat instruction fetch ho jaana — Branch Prediction se solve hota hai."
            ),
            stepDescriptions = listOf(
                "Step 1: Clock cycle badhne ke saath 5 instructions ek saath alag-alag stages mein overlap hokar chal rahi hain.",
                "Step 2: Upar 'RAW Hazard (Bubble)' toggle karke dekho kaise Forwarding bypass ki jagah Red STALL bubble banta hai."
            ),
            partRoles = mapOf(
                "if_stage" to "Instruction Cache se agla instruction fetch karta hai aur PC + 4 karta hai.",
                "id_stage" to "Instruction decode karke source registers Rs1, Rs2 ki values padhta hai.",
                "ex_stage" to "ALU mein calculation karta hai ya branch target address nikalta hai.",
                "mem_stage" to "Sirf LOAD/STORE instructions ke liye Data Memory read/write karta hai.",
                "wb_stage" to "Final result wapas destination register Rd mein save karta hai."
            ),
            vivaAnswers = listOf(
                "Nahi! Ek single instruction ka latency utna hi (ya latch ke kaaran thoda zyada) rehta hai; pipelining THROUGHPUT (instructions per second) badhati hai.",
                "1) Structural Hazard (hardware conflict), 2) Data Hazard (RAW/WAR/WAW dependency), 3) Control Hazard (branch/jump)."
            )
        ),
        "memory_hierarchy" to TopicHinglishData(
            subtitle = "Registers se L1/L2/L3 Cache, RAM aur SSD tak Speed vs Size pyramid",
            summary = "Koi bhi ek memory ek saath super-fast, bahut badi aur sasti nahi ho sakti. Isliye computer mein Memory Pyramid banaya jaata hai jo Locality of Reference ka faayda uthakar average speed L1 Cache jaisi rakhta hai.",
            keyPoints = listOf(
                "Pyramid mein UPAR jaane par: Speed badhti hai, Cost per bit badhti hai, Capacity ghatti hai.",
                "Temporal Locality: Jo data abhi use hua hai wo thodi der mein dobara use hoga (jaise loop variables).",
                "Spatial Locality: Jo address abhi use hua hai uske aas-paas ke addresses bhi jaldi use honge (jaise array elements).",
                "Hit Ratio (H) jitna 1 ke paas hoga, Effective Memory Access Time (EMAT) utna hi kam hoga."
            ),
            stepDescriptions = listOf(
                "Cache Hit: Data L1/L2 SRAM mein hi mil gaya (~1 ns) -> seedha CPU Registers ko mil jaata hai!",
                "Cache Miss: Data nahi mila -> RAM/Disk se poora 64-Byte block upar Cache mein laaya jaata hai."
            ),
            partRoles = mapOf(
                "regs" to "CPU core ke andar sabse tez flip-flops (~0.3 ns, kuch KB).",
                "l1_cache" to "Core ke bilkul paas super-fast SRAM (1–4 ns, KB–MB).",
                "l3_cache" to "Sabhi CPU cores ke beech shared bada cache (~12 ns, MBs).",
                "ram" to "Main working memory DRAM (~60 ns, GBs).",
                "disk" to "Permanent storage SSD/HDD (microseconds se ms, TBs)."
            ),
            vivaAnswers = listOf(
                "SRAM 6 transistors use karti hai (bahut tez, refresh nahi chahiye, mehngi); DRAM 1 transistor + 1 capacitor use karti hai (sasti, dense, par baar-baar refresh chahiye).",
                "EMAT = 0.9(10) + 0.1(10 + 100) = 9 + 11 = 20 ns."
            )
        ),
        "cache_mapping" to TopicHinglishData(
            subtitle = "Direct, Fully Associative aur Set-Associative mapping ka farq",
            summary = "Cache Mapping decide karta hai ki Main RAM ka ek block Cache ke kis line number mein baithega. Upar teeno modes toggle karke dekho kaise Direct mode mein collision (Conflict Miss) hota hai!",
            keyPoints = listOf(
                "Direct Mapping: Block j sirf Line (j mod N) par hi ja sakta hai. Hardware simple hai par Conflict Misses zyada hote hain.",
                "Fully Associative: RAM ka koi bhi block Cache ki KISI bhi khali line mein baith sakta hai. Zero conflict miss, par tag comparator mehnga!",
                "k-Way Set-Associative: Dono ka best mix! Cache ko S sets mein baanta jaata hai (har set mein k lines). Set = j mod S.",
                "LRU (Least Recently Used) aur FIFO replacement policies Associative caches mein purani line hatane ke kaam aati hain."
            ),
            stepDescriptions = listOf(
                "Upar Direct, Associative aur 2-Way Set Assoc switch karke dekho kaise Block 0 aur Block 4 same line par takrate ya adjust hote hain."
            ),
            partRoles = mapOf(
                "cpu_addr" to "Physical address ko [Tag | Index/Set | Block Offset] bits mein todta hai.",
                "cache_b0" to "Cache Line / Way 0 — isme Tag bits, Valid bit aur data block rehta hai.",
                "cache_b1" to "Cache Line / Way 1 — Set-Associative ya Associative mode mein doosra slot deta hai.",
                "ram_blk0" to "Main RAM ka Block 0 (0 mod 4 = Line 0).",
                "ram_blk4" to "Main RAM ka Block 4 (4 mod 4 = Line 0) — Direct mode mein Block 0 ko kick out kar deta hai!"
            ),
            vivaAnswers = listOf(
                "Jab do frequently use hone wale RAM blocks ek hi Cache Line par map hokar baar-baar ek doosre ko evict karte rahein, use Cache Thrashing kehte hain.",
                "Write-Through mein Cache aur RAM dono ek saath update hote hain; Write-Back mein sirf Cache update hota hai (Dirty bit = 1) aur eviction par RAM update hoti hai."
            )
        ),
        "virtual_memory" to TopicHinglishData(
            subtitle = "Virtual Address se Physical Frame translation, TLB aur Page Fault",
            summary = "Virtual Memory har program ko lagata hai ki uske paas bahut badi continuous RAM hai. MMU memory ko fixed-size Pages (Virtual) aur Frames (Physical) mein todta hai, aur TLB fast lookup karta hai.",
            keyPoints = listOf(
                "Virtual Address = [ Virtual Page Number (VPN) | Page Offset ]. Translation mein Offset kabhi nahi badalta.",
                "TLB (Translation Lookaside Buffer) ek super-fast cache hai jo recent VPN -> Physical Frame mappings yaad rakhta hai.",
                "Page Fault tab hota hai jab Page Table mein Valid bit = 0 ho (page RAM mein nahi, Swap Disk par hai). OS use disk se load karta hai.",
                "Belady's Anomaly FIFO page replacement mein ho sakti hai, LRU aur Optimal mein kabhi nahi."
            ),
            stepDescriptions = listOf(
                "Upar 'TLB Hit' aur 'Page Fault' toggle karke dekho: TLB Hit seedha RAM pahunchta hai, jabki Page Fault Disk swap trigger karta hai."
            ),
            partRoles = mapOf(
                "cpu_va" to "CPU dwara generate Virtual Address [Page # | Offset].",
                "tlb" to "Fast associative cache jo 1 cycle mein Page # ko Frame # mein badalta hai.",
                "page_table" to "RAM mein rakhi table jo har Virtual Page ka Physical Frame ya Disk status batati hai.",
                "phys_ram" to "Asli DRAM frames jahan active pages load hote hain.",
                "swap_disk" to "Hard disk/SSD ka hissa jahan RAM bharne par pages swap kiye jaate hain."
            ),
            vivaAnswers = listOf(
                "Kyunki Virtual Page aur Physical Frame dono ka size bilkul same hota hai (jaise 4 KB = 12-bit offset), isliye offset bits same rehti hain.",
                "Jab system instructions execute karne se zyada time RAM aur Disk ke beech pages swap karne (Page Faults) mein barbaad kare."
            )
        ),
        "io_dma" to TopicHinglishData(
            subtitle = "DMA Controller kaise HOLD/HLDA se bus lekar CPU ko free karta hai",
            summary = "Programmed I/O mein CPU baar-baar status check karke apna time waste karta hai. DMA (Direct Memory Access) mein ek DMA Controller bus ka master bankar Disk/Network se seedha RAM mein bulk data bhejta hai!",
            keyPoints = listOf(
                "Programmed I/O: CPU busy-wait polling loop mein phansa rehta hai.",
                "Interrupt-Driven I/O: Device ready hone par IRQ bhejta hai; CPU apna kaam rokkar ISR chalata hai.",
                "DMA Handshake: DMA 'HOLD' (Bus Request) bhejta hai; CPU current cycle poora karke 'HLDA' (Bus Grant) deta hai aur free ho jaata hai.",
                "DMA Modes: Burst Mode (poora block ek saath), Cycle Stealing (1 word per cycle), Transparent Mode."
            ),
            stepDescriptions = listOf(
                "Upar 'DMA Transfer' vs 'Programmed I/O' toggle karke dekho kaise DMA seedha I/O se RAM mein data dalta hai bina CPU ko roke!"
            ),
            partRoles = mapOf(
                "cpu" to "DMA ke dauran HLDA dekar system bus chhod deta hai aur apna cache kaam jaari rakhta hai.",
                "dma" to "Bus Master — khud Address aur Control signals chalakar I/O se RAM transfer karta hai.",
                "ram" to "Direct data buffer jo DMA se bulk bytes receive/send karta hai.",
                "disk_io" to "High-speed Disk ya Network card jo DREQ se DMA shuru karta hai."
            ),
            vivaAnswers = listOf(
                "Jab DMA har ek word transfer ke liye CPU se sirf ek memory cycle udhaar lekar wapas bus lauta de, use Cycle Stealing kehte hain.",
                "1) Address Register, 2) Word Count Register, 3) Control/Status Register."
            )
        ),
        "flynns_multicore" to TopicHinglishData(
            subtitle = "SISD, SIMD (GPU), MIMD (Multicore) aur Cache Coherence",
            summary = "Michael Flynn ne computers ko Instruction Streams aur Data Streams ke hisaab se 4 classes mein baanta: SISD (normal single-core), SIMD (GPU/vector), MISD (rare), aur MIMD (aaj ke multi-core processors).",
            keyPoints = listOf(
                "SISD: 1 Control Unit, 1 Processor, 1 Data stream (classic uniprocessor).",
                "SIMD: 1 Control Unit ek hi instruction ko bahut saare ALUs par broadcast karta hai alag-alag data ke liye (GPUs, AI).",
                "MISD: Ek hi data par multiple instructions (mostly theoretical / fault-tolerant systems).",
                "MIMD: Multiple independent cores apne-apne instructions aur data chalate hain aur Shared L3 Cache mein MESI Coherence follow karte hain."
            ),
            stepDescriptions = listOf(
                "Upar SISD, SIMD aur MIMD switch karke dekho kaise 1 core se parallel vector lanes aur 4-core coherent CPU banta hai."
            ),
            partRoles = mapOf(
                "cu_master" to "Instruction stream issue karne wali Control Unit(s).",
                "pe1" to "Parallel Arithmetic Logic Units jo ek saath data process karti hain.",
                "shared_l3" to "Sabhi cores ke beech data sync aur MESI cache coherence maintain karne wala Shared L3 Cache."
            ),
            vivaAnswers = listOf(
                "Kyunki graphics pixels ya AI matrix ke har element par bilkul wahi same calculation ek saath lagani hoti hai.",
                "Jab Core 0 kisi variable ko apne private L1 cache mein update kare aur Core 1 ke paas purani (stale) copy reh jaaye — ise MESI protocol se solve kiya jaata hai."
            )
        )
    )

    private val coreQuizExplanations: Map<String, String> = mapOf(
        "q1" to "Program Counter (PC) mein hamesha AGLE execute hone wale instruction ka address store rehta hai.",
        "q2" to "20-bit Address Bus se total 2^20 = 1,048,576 (1 MB) unique memory locations address ho sakti hain.",
        "q3" to "Harvard mein Instruction aur Data memories alag buses par hoti hain, isliye dono ka fetch ek saath ho sakta hai.",
        "q4" to "Fetch cycle ka pehla RTL step MAR ← PC hai taaki RAM ko instruction ka address mile.",
        "q5" to "Immediate addressing mein constant value seedha instruction ke andar hoti hai — koi extra RAM read nahi lagta.",
        "q6" to "2's complement subtraction ke liye B ka 1's complement (B') aur Carry-In = 1 dekar A + B' + 1 calculate kiya jaata hai.",
        "q7" to "Microprogrammed CU mein control words Control Memory ROM mein rehte hain, isliye modify karna aasaan hota hai.",
        "q8" to "Indexed addressing mein Effective Address = Base Address + Index Register (IX) hota hai.",
        "q9" to "k-stage pipeline ka ideal maximum speedup k ke barabar hota hai, yaani 5-stage mein 5x.",
        "q10" to "Operand Forwarding (Bypassing) result ko EX/MEM latch se seedha agle instruction ke ALU input mein bhej deta hai bina stall kiye.",
        "q11" to "IF instruction fetch karta hai, jabki MEM stage LOAD/STORE ke liye Data Memory access karta hai.",
        "q12" to "Direct Mapping mein har block sirf ek fixed line (j mod N) par ja sakta hai, isliye Conflict Misses sabse zyada hote hain.",
        "q13" to "TLB (Translation Lookaside Buffer) recent Virtual-to-Physical page mappings cache karke RAM lookup bachata hai.",
        "q14" to "Ek hi variable ko loop mein baar-baar access karna Temporal Locality ka classic example hai.",
        "q15" to "Write-Back cache mein Dirty (Modified) bit = 1 batata hai ki RAM ko eviction se pehle update karna zaroori hai.",
        "q16" to "DMA Controller HOLD signal bhejta hai aur CPU bus chhodne ke liye HLDA (Hold Acknowledge) signal deta hai.",
        "q17" to "GPUs ek hi shader instruction ko hazaron pixels par parallel chalate hain — yeh SIMD (Single Instruction, Multiple Data) hai.",
        "q18" to "Programmed I/O mein CPU lagatar status register poll karke apna time waste karta hai.",
        "q19" to "Amdahl's Law ke hisaab se program ka sequential hissa (1 - P) parallel speedup ko limit karta hai."
    )

    val topics: Map<String, TopicHinglishData> = coreTopics + CbcsDigitalHinglishContent.topics

    val quizExplanations: Map<String, String> = coreQuizExplanations + CbcsDigitalHinglishContent.quizExplanations
}
