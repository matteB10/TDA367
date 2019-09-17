package com.masthuggis.boki.model;

import java.util.List;

public class Chat implements iChat {
    private List<iMessage> messages;


    public List<iMessage> getMessages() {
        return this.messages;
    }
}
