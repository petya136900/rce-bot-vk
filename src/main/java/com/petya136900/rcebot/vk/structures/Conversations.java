package com.petya136900.rcebot.vk.structures;

public class Conversations {
    private Integer count = 0;
    private Conversation[] items = new Conversation[]{};
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
        return (items == null) || (items.length < 1);
    }
    public static Conversations getEmpty() {
        return new Conversations();
    }
}
