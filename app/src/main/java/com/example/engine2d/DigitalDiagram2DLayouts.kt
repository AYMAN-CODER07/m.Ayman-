package com.example.engine2d

import androidx.compose.ui.graphics.Color
import com.example.domain.model.AppLanguage
import com.example.domain.model.SceneType3D
import com.example.domain.model.SimulationStep
import com.example.engine3d.BusType

object DigitalDiagram2DLayouts {

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
    ): DiagramScene2D? {
        val activeParts = step?.activePartIds ?: emptySet()
        val activeBuses = step?.activeBusIds ?: emptySet()
        val hi = language == AppLanguage.HINGLISH
        val te = language == AppLanguage.TELUGU

        return when (sceneType) {
            SceneType3D.NUMBER_SYSTEMS_CODES -> {
                val title = when {
                    te -> "సంఖ్య వ్యవస్థలు (Bin/Oct/Hex) & బైనరీ కోడ్స్ (BCD, Gray)"
                    hi -> "Number Systems (Bin/Oct/Hex) aur Binary Codes (BCD, Gray, 2's Comp)"
                    else -> "Number Systems (Bin / Oct / Hex) & Binary Codes"
                }
                val codeBoxTitle = when (modeIndex) {
                    1 -> "BCD: 0010 0101"
                    2 -> "2's Comp (-25)"
                    else -> "Oct (31)₈ | Hex (19)₁₆"
                }
                val codeBoxSub = when (modeIndex) {
                    1 -> "Gray: 10101 | Ex-3"
                    2 -> "1's Comp + 1 = 100111"
                    else -> "3-bit Oct | 4-bit Hex"
                }
                DiagramScene2D(
                    title = title,
                    boxes = listOf(
                        DiagramBox2D("dec_in", "Decimal (25)₁₀", "Base 10", 0.04f, 0.30f, 0.22f, 0.36f, AmberAlu, "dec_in" in activeParts || true),
                        DiagramBox2D("bin_reg", "Binary (11001)₂", "16+8+0+0+1", 0.35f, 0.28f, 0.26f, 0.40f, SkyReg, true),
                        DiagramBox2D("oct_hex", "Oct / Hex Grouping", "011 001 | 0001 1001", 0.69f, 0.14f, 0.27f, 0.28f, TealCache, modeIndex == 0),
                        DiagramBox2D("code_enc", codeBoxTitle, codeBoxSub, 0.69f, 0.54f, 0.27f, 0.32f, VioletCtrl, modeIndex >= 1)
                    ),
                    arrows = listOf(
                        DiagramArrow2D("dec_bin", 0.26f, 0.48f, 0.35f, 0.48f, "÷ 2", BusType.DATA, true),
                        DiagramArrow2D("bin_oct", 0.61f, 0.38f, 0.69f, 0.28f, "3/4 Bits", BusType.ADDRESS, modeIndex == 0),
                        DiagramArrow2D("bin_code", 0.61f, 0.60f, 0.69f, 0.68f, "Encode", BusType.CONTROL, modeIndex >= 1 || "bin_code" in activeBuses)
                    )
                )
            }

            SceneType3D.LOGIC_GATES -> {
                val (g1, g1Sub, g2, g2Sub) = when (modeIndex) {
                    1 -> listOf("NAND: (A·B)'", "Universal Gate 1", "NOR: (A+B)'", "Universal Gate 2")
                    2 -> listOf("XOR: A ⊕ B", "1 when A ≠ B", "XNOR: (A ⊕ B)'", "1 when A = B")
                    else -> listOf("AND: A · B", "1 only if A=1, B=1", "OR: A + B", "1 if A=1 or B=1")
                }
                DiagramScene2D(
                    title = when {
                        te -> "లాజిక్ గేట్స్ & ట్రూత్ టేబుల్ లాజిక్ ($g1 / $g2)"
                        hi -> "Logic Gates Schematic ($g1 aur $g2)"
                        else -> "Logic Gates Schematic ($g1 & $g2)"
                    },
                    boxes = listOf(
                        DiagramBox2D("in_pins", "Inputs A, B", "A = 1, B = 0", 0.05f, 0.26f, 0.22f, 0.48f, SkyReg, true),
                        DiagramBox2D("gate_primary", g1, g1Sub, 0.37f, 0.16f, 0.28f, 0.28f, AmberAlu, true),
                        DiagramBox2D("gate_secondary", g2, g2Sub, 0.37f, 0.56f, 0.28f, 0.28f, VioletCtrl, true),
                        DiagramBox2D("out_led", "Output Y", "De Morgan Dual", 0.74f, 0.28f, 0.21f, 0.44f, EmeraldIo, true)
                    ),
                    arrows = listOf(
                        DiagramArrow2D("g1a", 0.27f, 0.38f, 0.37f, 0.30f, "A, B", BusType.DATA, true),
                        DiagramArrow2D("g2a", 0.27f, 0.62f, 0.37f, 0.70f, "A, B", BusType.DATA, true),
                        DiagramArrow2D("y1", 0.65f, 0.30f, 0.74f, 0.40f, "Y1", BusType.CONTROL, true),
                        DiagramArrow2D("y2", 0.65f, 0.70f, 0.74f, 0.60f, "Y2", BusType.CONTROL, true)
                    )
                )
            }

            SceneType3D.KARNAUGH_MAP -> DiagramScene2D(
                title = when {
                    te -> "4-వేరియబుల్ K-Map (గ్రే కోడ్ 00, 01, 11, 10 & గ్రూపింగ్)"
                    hi -> "4-Var K-Map Grid (Gray Code 00, 01, 11, 10 & Quad Grouping)"
                    else -> "4-Variable K-Map Grid (Gray Code Simplification)"
                },
                boxes = emptyList(),
                arrows = emptyList(),
                isKMapGrid = true,
                kMapDontCareMode = modeIndex == 1
            )

            SceneType3D.MUX_DEMUX -> {
                val isMux = modeIndex == 0
                DiagramScene2D(
                    title = if (isMux) {
                        when {
                            te -> "4:1 మల్టీప్లెక్సర్ (MUX: అనేక ఇన్‌పుట్‌ల నుండి 1 అవుట్‌పుట్)"
                            hi -> "4:1 Multiplexer (Many Inputs → 1 Output Data Selector)"
                            else -> "4:1 Multiplexer (Data Selector)"
                        }
                    } else {
                        when {
                            te -> "1:4 డీమల్టీప్లెక్సర్ (DEMUX: 1 ఇన్‌పుట్ నుండి 4 అవుట్‌పుట్‌లు)"
                            hi -> "1:4 Demultiplexer (1 Input → Selected Output Line)"
                            else -> "1:4 Demultiplexer (Data Distributor)"
                        }
                    },
                    boxes = listOf(
                        DiagramBox2D("mux_inputs", if (isMux) "I0, I1, I2, I3" else "Y0, Y1, Y2, Y3", "4 Channels", if (isMux) 0.05f else 0.70f, 0.14f, 0.25f, 0.56f, SkyReg, true),
                        DiagramBox2D("mux_core", if (isMux) "4:1 MUX" else "1:4 DEMUX", "Switch Core", 0.38f, 0.18f, 0.24f, 0.46f, VioletCtrl, true),
                        DiagramBox2D("select_lines", "Select (S1, S0)", "00 / 01 / 10 / 11", 0.36f, 0.74f, 0.28f, 0.18f, AmberAlu, true),
                        DiagramBox2D("single_line", if (isMux) "Output Y" else "Input Din", "1 Common Line", if (isMux) 0.71f else 0.05f, 0.28f, 0.24f, 0.28f, EmeraldIo, true)
                    ),
                    arrows = if (isMux) {
                        listOf(
                            DiagramArrow2D("ch0_bus", 0.30f, 0.36f, 0.38f, 0.36f, "Active CH", BusType.DATA, true),
                            DiagramArrow2D("ch2_bus", 0.62f, 0.42f, 0.71f, 0.42f, "Y = I_sel", BusType.DATA, true),
                            DiagramArrow2D("sel_bus", 0.50f, 0.74f, 0.50f, 0.64f, "S1:S0", BusType.CONTROL, true)
                        )
                    } else {
                        listOf(
                            DiagramArrow2D("ch0_bus", 0.29f, 0.42f, 0.38f, 0.42f, "Din", BusType.DATA, true),
                            DiagramArrow2D("ch2_bus", 0.62f, 0.42f, 0.70f, 0.42f, "Y_sel", BusType.DATA, true),
                            DiagramArrow2D("sel_bus", 0.50f, 0.74f, 0.50f, 0.64f, "S1:S0", BusType.CONTROL, true)
                        )
                    }
                )
            }

            SceneType3D.FLIP_FLOPS -> DiagramScene2D(
                title = if (modeIndex == 0) {
                    when {
                        te -> "SR & D ఫ్లిప్-ఫ్లాప్ (1-బిట్ క్లాక్డ్ మెమరీ సెల్)"
                        hi -> "SR & D Flip-Flop (Clocked 1-Bit Memory Cell)"
                        else -> "SR & D Flip-Flop Schematic"
                    }
                } else {
                    when {
                        te -> "JK & T ఫ్లిప్-ఫ్లాప్ (J=K=1 వద్ద TOGGLE, రేస్-అరౌండ్ నివారణ)"
                        hi -> "JK & T Flip-Flop (J=K=1 Toggle State & Edge Trigger)"
                        else -> "JK & T Flip-Flop Schematic"
                    }
                },
                boxes = listOf(
                    DiagramBox2D("ff_inputs", if (modeIndex == 0) "S, R / D" else "J, K / T", "Control In", 0.05f, 0.18f, 0.22f, 0.28f, SkyReg, "ff_inputs" in activeParts || true),
                    DiagramBox2D("ff_clock", "CLK ↑", "Edge Pulse", 0.05f, 0.58f, 0.22f, 0.24f, AmberAlu, "ff_clock" in activeParts),
                    DiagramBox2D("ff_latch", "Latch Core", "1-Bit Storage", 0.37f, 0.22f, 0.26f, 0.54f, VioletCtrl, true),
                    DiagramBox2D("ff_outputs", "Q  &  Q'", "Stored Output", 0.73f, 0.24f, 0.22f, 0.50f, EmeraldIo, true)
                ),
                arrows = listOf(
                    DiagramArrow2D("ff_in_bus", 0.27f, 0.32f, 0.37f, 0.36f, "In", BusType.DATA, true),
                    DiagramArrow2D("ff_clk_bus", 0.27f, 0.70f, 0.37f, 0.60f, "Edge ↑", BusType.CONTROL, "ff_clk_bus" in activeBuses),
                    DiagramArrow2D("ff_out_bus", 0.63f, 0.48f, 0.73f, 0.48f, "Q(t+1)", BusType.DATA, true),
                    DiagramArrow2D("ff_fb", 0.73f, 0.28f, 0.50f, 0.22f, "Hold Feedback", BusType.CONTROL, "ff_fb" in activeBuses)
                )
            )

            SceneType3D.BUS_STRUCTURES -> {
                val isMulti = modeIndex >= 1
                DiagramScene2D(
                    title = when (modeIndex) {
                        1 -> if (te) "మల్టీ-బస్ నిర్మాణం (CPU బస్సు + బ్రిడ్జి + I/O బస్సు)" else if (hi) "Multi-Bus Hierarchy: Bridge separates Fast RAM & Slow I/O" else "Multi-Bus Hierarchy with Bus Bridge"
                        2 -> if (te) "ట్రై-స్టేట్ బఫర్ (0, 1, High-Z) & బస్ ఆర్బిట్రేషన్" else if (hi) "Tri-State Buffers (High-Z) & Bus Arbitration" else "Tri-State Buffers & Bus Arbitration"
                        else -> if (te) "సింగిల్ షేర్డ్ బస్సు (ఒక సైకిల్‌లో 1 ట్రాన్స్‌ఫర్)" else if (hi) "Single Shared Bus (Simple par 1 Transfer/Cycle)" else "Single Shared Bus Organization"
                    },
                    boxes = listOf(
                        DiagramBox2D("bus_master_cpu", "CPU Master", "Drives Bus", 0.05f, 0.20f, 0.25f, 0.34f, CyanCpu, true),
                        DiagramBox2D("tristate_buf", "Tri-State Buffer", "0 | 1 | High-Z", 0.36f, 0.16f, 0.24f, 0.28f, AmberAlu, modeIndex == 2 || true),
                        DiagramBox2D("bus_bridge", if (isMulti) "Bus Bridge / Arb" else "Shared Bus", if (isMulti) "Isolates I/O" else "Single Lane", 0.36f, 0.56f, 0.24f, 0.28f, if (isMulti) TealCache else CoralAlert, true),
                        DiagramBox2D("bus_slaves", "RAM & I/O Slaves", "Targets", 0.70f, 0.24f, 0.25f, 0.52f, IndigoMem, true)
                    ),
                    arrows = listOf(
                        DiagramArrow2D("sys_bus", 0.30f, 0.32f, 0.36f, 0.30f, "Ctrl", BusType.CONTROL, true),
                        DiagramArrow2D("sys_bus2", 0.60f, 0.34f, 0.70f, 0.38f, "Fast RAM", BusType.DATA, true, bidirectional = true),
                        DiagramArrow2D("io_exp_bus", 0.60f, 0.70f, 0.70f, 0.66f, if (isMulti) "I/O Bus" else "Wait", if (isMulti) BusType.CONTROL else BusType.ALERT, true, bidirectional = true)
                    )
                )
            }

            else -> null
        }
    }
}
