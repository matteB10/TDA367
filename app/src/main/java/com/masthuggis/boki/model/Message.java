package com.masthuggis.boki.model;

import java.util.Date;

public class Message implements iMessage {
    private String content;
    private Date dateSent;



    public String getContent() {
        return this.content;
    }

    public Date getDateSent() {
        return this.dateSent;
    }
}
