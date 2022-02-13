package com.valorin.network;

import java.util.List;

import com.valorin.Main;

public class Update {
    private String version;
    private List<String> context;
    private String downloadUrl;
    private String password;
    private int state;// 0 未变更 1 成功 2 失败（超时） 3 失败（其他）

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getContext() {
        return context;
    }

    public void setContext(List<String> context) {
        this.context = context;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isNew() {
        String versionNow = Main.getVersion();
        if (Integer.parseInt(versionNow.replace("EX-", "")) < Integer
                .parseInt(version)) {
            return false;
        } else {
            return true;
        }
    }
}
