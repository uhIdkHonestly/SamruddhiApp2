package com.samruddhi.trading.equities.logic;

public enum OptionOrderFillStatus {

    ORDER_STATUS_FILLED ("FLL"),
    ORDER_STATUS_OPEN("OPN"),
    ORDER_STATUS_ACK("ACK"),
    ORDER_STATUS_FAILED("FAILED");

    String value;
    private OptionOrderFillStatus(String value) {
        this.value = value;
    }

    // Static method to create an enum instance from a string value
    public static OptionOrderFillStatus fromString(String text) {
        for (OptionOrderFillStatus optionOrderFillStatus : OptionOrderFillStatus.values()) {
            if (optionOrderFillStatus.name().equalsIgnoreCase(text)) {
                return optionOrderFillStatus;
            }
        }
        throw new IllegalArgumentException("No constant with string " + text + " found");
    }
}
