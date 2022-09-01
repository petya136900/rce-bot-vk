package com.petya136900.rcebot.lifecycle;

import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.handlers.HostNameHandler;
import com.petya136900.rcebot.rce.timetable.TimetableException;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.MessageSendResponse;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class DbStatusHandler implements HandlerInterface {
    private Boolean wNotify = false;
    private VK vkContent;
    private MessageSendResponse.MessageInfo mi;
    private ArrayList<Long> results;
    private static final Semaphore locker = new Semaphore(1);
    @Override
    public void handle(VK vkContent) {
        results = new ArrayList<>();
        this.vkContent=vkContent;
        String message = vkContent.getVK().getText();
        if(MainHandler.checkAdmin(vkContent.getVK().getFrom_id())) {
            if(!locker.tryAcquire()) {
                vkContent.reply(HostNameHandler.getUserHostname()+" | Test already running!");
                return;
            }
            vkContent.reply(MySqlConnector.toStringStatic());
            if(message!=null&& RegexpTools.checkRegexp("test",message)) {
                mi = vkContent.reply("Preparing MySQL test..");
                reply("Preparing MySQL test..");
                if(NotifyLoop.isRunning()) {
                    wNotify=true;
                    NotifyLoop.stopNotify();
                    reply("Notified stopped..");
                }
                reply("Waiting 5000ms..");
                try {Thread.sleep(5000); }catch (Exception ignored) {}
                try {
                    new Thread(() -> {
                        int s = 0;
                        while (true) {
                            if (s > 3)
                                s = 0;
                            reply("DB test is running" + anim(s));
                            s++;
                            synchronized (this) {
                                try {
                                    wait(350);
                                } catch (InterruptedException e) {
                                    break;
                                }
                            }
                        }
                    }).start();
                    check();
                    check();
                    check();
                    check();
                    check();
                    check();
                } finally {
                    locker.release();
                    VK.rerun();
                    if(wNotify)
                        new Thread(new NotifyLoop()).start();
                }
                reply("Results: \n\n"+(results.stream().map(String::valueOf).collect(Collectors.joining("ms\n")))+"ms");
            }
        }
    }
    private String anim(int s) {
        switch(s) {
            case(0):
                return "|";
            case(1):
                return "/";
            case(2):
                return "--";
            case(3):
                return "\\";
            default:
                return "@";
        }
    }
    private void check() {
        MySqlConnector.stop();
        long startTime;
        long endTime;
        startTime = System.currentTimeMillis();
        try {
            MySqlConnector.getBotSettings();
            MySqlConnector.getPeerSettings(vkContent.getVK().getPeer_id());
        } catch (TimetableException e) {
            vkContent.reply("Error! :"+e.getLocalizedMessage());
        }
        endTime = System.currentTimeMillis();
        results.add(endTime - startTime);
    }
    private void reply(String s) {
        mi = mi.editMessageOrDeleteAndReply("["+HostNameHandler.getUserHostname()+"] | "+s);
    }
}
