package com.valorin.network;

import java.util.List;

import com.valorin.Main;

public class Update {

    public enum UpdateState {
        NOCHANGE,
        SUCCESS,
        FAILURE_TIMEOUT,
        FAILURE_OTHER
    }

    private int version;
    private List<String> context;
    private String downloadUrl;
    private String password;
    private UpdateState state;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
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

    public UpdateState getState() {
        return state;
    }

    public void setState(UpdateState state) {
        this.state = state;
    }

    public boolean isNew() {
        String versionNow = Main.getVersion();
        return Integer.parseInt(versionNow.replace("EX-", "")) >= version;
    }
}
