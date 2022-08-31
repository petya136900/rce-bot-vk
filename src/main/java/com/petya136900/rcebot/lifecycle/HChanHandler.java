package com.petya136900.rcebot.lifecycle;

import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class HChanHandler implements HandlerInterface {
    @Override
    public boolean keyboardSupportStages() {
        return true;
    }
    private VK vkContent;
    @Override
    public void handle(VK vkContent) {
        this.vkContent=vkContent;
        Payload payload = JsonParser.fromJson(vkContent.getVK().getPayload(),Payload.class);
        String stage = vkContent.getVK().getStage();
        String message = vkContent.getVK().getText();
        if(payload==null) payload = vkContent.getVK().getCallbackPayload();
        if(!vkContent.isAdmin()) {
            vkContent.reply("Access denied ;)");
            return;
        }
        vkContent.reply("Stage: "+vkContent.getVK().getStage()+"\n" +
                "Payload: "+JsonParser.toJson(payload)+"\n" +
                "fromID: "+vkContent.getVK().getFrom_id()+"\n" +
                "peerID: "+vkContent.getVK().getPeer_id());
        if(payload==null) {
            if(RegexpTools.checkRegexp("exit|выход|назад|отмена",message)) {
                bye();
            }
        } else {
            stage = payload.getStage();
        }
        if(!vkContent.getVK().getPeer_id().equals(vkContent.getVK().getFrom_id())) {
            conversationsUnsupported();
            return;
        }
        switch (stage) {
            case("1"):
                mainMenu();
                break;
            case("new"):
                newManga();
                break;
            case("exit"):
                bye();
                break;
            default:
                unknownCommand();
                break;
        }
    }
    private void newManga() {
        AtomicReference<MessageSendResponse.MessageInfo> mi = new AtomicReference<>(vkContent.reply("Loading..."));
        try {
            HChanManga[] mangas = HChanParser.getNew(status->{
                mi.set(mi.get().editMessageOrDeleteAndReply(status));
            });
            if(mangas.length>0)
                mi.set(mi.get().editMessageOrDeleteAndReply(JsonParser.toJson(mangas[0])));
            else mi.get().editMessageOrDeleteAndReply("Нового нет..");
        } catch (IOException e) {
            vkContent.reply("Error: "+e.getLocalizedMessage());
        }
    }
    private void unknownCommand() {
        vkContent.reply("Error: Unknown command\n\nTry: exit, new");
    }

    private void conversationsUnsupported() {
        vkContent.reply("Sorry, HChan mode is not available in conversations :<");
    }
    private void mainMenu() {
        vkContent.reply("You're in hchan mode! ^_^",null,
                new Keyboard(
                        new KeyboardLine(
                                new Button(Button.Type.CALLBACK,"Выход",
                                        Button.COLOR_NEGATIVE).setPayload("hchan","exit"),
                                new Button(Button.Type.CALLBACK,"Новое",
                                        Button.COLOR_POSITIVE).setPayload("hchan","new")))
                        .setOne_time(false));
    }
    private void bye() {
        vkContent.getVK().removeStages();
        vkContent.reply("Bye, see you soon! :3",null,Keyboard.clear());
    }
}
