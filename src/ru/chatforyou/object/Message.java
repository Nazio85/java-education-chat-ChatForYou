package ru.chatforyou.object;

import java.io.Serializable;
import java.util.Calendar;

public class Message implements Serializable {
    private String message;
    private Calendar calendar;
    private String userName;

    public Message(String userName, String message) {
        this.message = message;
        this.calendar = Calendar.getInstance();
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public Calendar getData() {
        return calendar;
    }

    public String getUserName() {
        return userName;
    }
}
