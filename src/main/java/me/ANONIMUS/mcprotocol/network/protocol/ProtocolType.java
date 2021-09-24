package me.ANONIMUS.mcprotocol.network.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ProtocolType {
    PROTOCOL_UNKNOWN(0, "UNKNOWN"),
    PROTOCOL_1_8_X(47, "1.8.X"),
    PROTOCOL_1_9(107, "1.9"),
    PROTOCOL_1_9_1(108, "1.9.1"),
    PROTOCOL_1_9_2(109, "1.9.2"),
    PROTOCOL_1_9_3_AND_4(110, "1.9.3/4"),
    PROTOCOL_1_10_X(210, "1.10.X"),
    PROTOCOL_1_11(315, "1.11"),
    PROTOCOL_1_11_X(316, "1.11.X"),
    PROTOCOL_1_12(335, "1.12"),
    PROTOCOL_1_12_1(338, "1.12.1"),
    PROTOCOL_1_12_2(340, "1.12.2");

   /*
        NOT TESTED (ONLY MOTD WORKING)

        PROTOCOL_1_17_1(756, "1.17.1"),
        PROTOCOL_1_17(755, "1.17"),
        PROTOCOL_1_16_4(754, "1.16.4"),
        PROTOCOL_1_16_3(753, "1.16.3"),
        PROTOCOL_1_16_2(751, "1.16.2"),
        PROTOCOL_1_16_1(736, "1.16.1"),
        PROTOCOL_1_16(735, "1.16"),
        PROTOCOL_1_15_2(578, "1.15.2"),
        PROTOCOL_1_15_1(575, "1.15.1"),
        PROTOCOL_1_15(573, "1.15"),
        PROTOCOL_1_14_4(498, "1.14.4"),
        PROTOCOL_1_14_3(490, "1.14.3"),
        PROTOCOL_1_14_2(485, "1.14.2"),
        PROTOCOL_1_14_1(480, "1.14.1"),
        PROTOCOL_1_14(477, "1.14"),
        PROTOCOL_1_13_2(404, "1.13.2"),
        PROTOCOL_1_13_1(401, "1.13.1"),
        PROTOCOL_1_13(393, "1.13"),
        PROTOCOL_1_7_10(5, "1.7.10"),
        PROTOCOL_1_7_5(4, "1.7.5"),
        PROTOCOL_1_6_4(78, "1.6.4"),
        PROTOCOL_1_6_2(74, "1.6.2"),
        PROTOCOL_1_6_1(73, "1.6.1"),
        PROTOCOL_1_5_2(61, "1.5.2"),
        PROTOCOL_1_5_1(60, "1.5.1"),
        PROTOCOL_1_4_7(51, "1.4.7");
    */

    private final int protocol;
    private final String prefix;

    public static boolean isSupported(int protocol) {
        return getByProtocolID(protocol) != PROTOCOL_UNKNOWN;
    }

    public static ProtocolType getByProtocolID(int protocol) {
        return Arrays.stream(values()).filter(v -> v.protocol == protocol).findFirst().orElse(PROTOCOL_UNKNOWN);
    }
}