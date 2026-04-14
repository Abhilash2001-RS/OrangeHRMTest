package org.com.webUI.enums;

public enum BrowserType {

    CHROME("chrome"),
    EDGE("Edge");

    private String value;

     BrowserType(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
