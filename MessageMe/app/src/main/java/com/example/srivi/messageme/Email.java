package com.example.srivi.messageme;

import java.io.Serializable;

/**
 * Created by srivi on 25-04-2018.
 */

public class Email implements Serializable{
    String text;
    String time;
    String senderId;
    String senderName;
    boolean isRead;
    String emailKey;

    public Email() {
    }

    public Email(String text, String time, String senderId, String senderName, boolean isRead) {
        this.text = text;
        this.time = time;
        this.senderId = senderId;
        this.senderName = senderName;
        this.isRead = isRead;
    }
}
