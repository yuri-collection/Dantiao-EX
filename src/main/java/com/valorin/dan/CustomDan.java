package com.valorin.dan;

public class CustomDan {
    private int num;
    private String editName;
    private String displayName;
    private int exp;

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
