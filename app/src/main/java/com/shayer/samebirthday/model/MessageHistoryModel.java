package com.shayer.samebirthday.model;

import java.io.Serializable;

/**
 * Created by Shreeya Patel on 3/16/2016.
 */
public class MessageHistoryModel implements Serializable {

    private static final long serialVersionUID = 1;
    private String HistoryMessage;
    private String HistoryMessageUserName;
    private String HistoryMessageDate;
    private String HistoryMessageUserId;

    public String getHistoryMessage() {
        return this.HistoryMessage;
    }
    public void setHistoryMessage(String HistoryMessage) {
        this.HistoryMessage = HistoryMessage;
    }

    public String getHistoryMessageUserName() {
        return this.HistoryMessageUserName;
    }
    public void setHistoryMessageUserName(String HistoryMessageUserName) {
        this.HistoryMessageUserName = HistoryMessageUserName;
    }

    public String getHistoryMessageDate() {
        return this.HistoryMessageDate;
    }
    public void setHistoryMessageDate(String HistoryMessageDate) {
        this.HistoryMessageDate = HistoryMessageDate;
    }

    public String getHistoryMessageUserId() {
        return this.HistoryMessageUserId;
    }
    public void setHistoryMessageUserId(String HistoryMessageUserId) {
        this.HistoryMessageUserId = HistoryMessageUserId;
    }
}
