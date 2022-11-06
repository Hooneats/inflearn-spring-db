package org.example.code;

public enum JavaEnumClass {

    KOREA("한국"),
    AMERICA("미국");

    private final String code;

    JavaEnumClass(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
