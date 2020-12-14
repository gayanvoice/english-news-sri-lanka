package com.example.demo.model.constant;

public enum HostEnum {
    EconomyNext("EconomyNext"),
    BBCNews("BBCNews"),
    NewsFirst("NewsFirst"),
    NethNews("NethNews"),
    Silumina("Silumina"),
    Dinamina("Dinamina"),
    ITN("ITN"),
    ColomboTimes("ColomboTimes");

    private final String value;

    HostEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}