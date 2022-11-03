package com.petya136900.rcebot.vk.structures;

import com.petya136900.rcebot.vk.VK;

import java.util.concurrent.atomic.AtomicReference;

public class MessageInfoAtomicNonImmutable {
    private AtomicReference<MessageSendResponse.MessageInfo> internalMessageInfo;
    private MessageSendResponse.MessageInfo get() {
        return internalMessageInfo.get();
    }
    private void set(MessageSendResponse.MessageInfo mi) {
        internalMessageInfo.set(mi);
    }
    public MessageInfoAtomicNonImmutable(MessageSendResponse.MessageInfo mi) {
        if(mi==null)
            throw new IllegalArgumentException("MessageInfo can't be null");
        this.internalMessageInfo = new AtomicReference<>(mi);
    }
    public void deleteMessage() {
        get().deleteMessage();
    }
    public void editMessageOrDeleteAndReply(String new_message, String[] attachs, Keyboard keyboard) {
        set(get().editMessageOrDeleteAndReply(new_message, attachs, keyboard));
    }
    public void editMessageOrDeleteAndReply(String new_message, String[] attachs) {
        set(get().editMessageOrDeleteAndReply(new_message,attachs,null));
    }
    public void editMessageOrDeleteAndReply(String new_message) {
        set(get().editMessageOrDeleteAndReply(new_message,null));
    }
    public boolean editMessage(String new_message) {
        return get().editMessage(new_message);
    }
    public boolean editMessage(String new_message, String[] attachs) {
        return get().editMessage(new_message, attachs);
    }
    public boolean editMessage(String new_message, String[] attachs, Keyboard keyboard) {
        return get().editMessage(new_message, attachs, keyboard);
    }
}
