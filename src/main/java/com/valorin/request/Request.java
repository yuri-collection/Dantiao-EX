﻿package com.valorin.request;

public class Request {
    private String sender;
    private String receiver;
    private String arenaEditName;

    public Request(String sender, String receiver, String arenaEditName) {
        this.sender = sender;
        this.receiver = receiver;
        this.arenaEditName = arenaEditName;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getArenaEditName() {
        return arenaEditName;
    }
}
