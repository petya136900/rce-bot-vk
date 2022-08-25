package com.petya136900.rcebot.vk.structures;

public class Conversations {
    private Integer count;
    private Conversation[] items;
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Conversation[] getItems() {
        return items;
    }

    public void setItems(Conversation[] items) {
        this.items = items;
    }

    public boolean isEmpty() {
        if(items==null||items.length<1)
            return true;
        return false;
    }
}
