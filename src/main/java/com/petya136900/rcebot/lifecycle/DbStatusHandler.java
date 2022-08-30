package com.petya136900.rcebot.lifecycle;

import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.vk.VK;

public class DbStatusHandler implements HandlerInterface {
    @Override
    public void handle(VK vkContent) {
        if(MainHandler.checkAdmin(vkContent.getVK().getFrom_id())) {
            vkContent.reply(MySqlConnector.toStringStatic());
        }
    }
}
