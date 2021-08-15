package com.valorin.dan;

public class CustomDan {
    private final int num;
    private final String editName;
    private final String displayName;
    private final int exp;

    public CustomDan(int num, String editName, String displayName, int exp) {
        this.num = num;
        this.editName = editName;
        this.displayName = displayName;
        this.exp = exp;
    }

    public int getNum() {
        return num;
    }

    public String getEditName() {
        return editName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getExp() {
        return exp;
    }
}
