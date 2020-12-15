package com.example.demo.model.constant;

public enum HostEnum {
    NationLK("NationLK"),
    SundayOberver("SundayObserver"),
    AdaDerana("AdaDerana");

    private final String value;

    HostEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}