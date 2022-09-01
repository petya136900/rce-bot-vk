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
    private Payload payload;
    private Integer lastSearchOffset = 0; //
    private Integer resultOffset = 0;
    //private Integer pageOffset = 0;
    private HChanManga[] results;
    @Override
    public void handle(VK vkContent) {
        this.vkContent=vkContent;
        payload = JsonParser.fromJson(vkContent.getVK().getPayload(),Payload.class);
        String stage = vkContent.getVK().getStage();
        String message = vkContent.getVK().getText();
        if(payload==null) payload = vkContent.getVK().getCallbackPayload();
        if(!vkContent.isAdmin()) {
            vkContent.reply("Access denied ;)");
            return;
        }
        if(!vkContent.getVK().getPeer_id().equals(vkContent.getVK().getFrom_id())) {
            conversationsUnsupported();
            return;
        }
        if(payload==null) {
            if(RegexpTools.checkRegexp("exit|выход|назад|отмена",message)) {
                bye();
            }
        }
        switch (stage) {
            case("1"):
                mainMenu("You're in hchan mode! ^_^");
                break;
            case("scroll"):
                scrollResult(getOffset());
                break;
            case("watch"):
                watchResult(getOffset());
                break;
            case("new"):
                newManga(getOffset());
                break;
            case("tags_select"):
                // TODO:
                break;
            case("tags"):
                // TODO:
                break;
            case("exit"):
                bye();
                break;
            default:
                unknownCommand();
                break;
        }
    }
    private void watchResult(Integer offset) {
        vkContent.reply("U a trying to watch: "+results[offset].getTitle());
    }
    private Integer getOffset() {
        Integer offset=0;
        String raw;
        if(payload!=null) {
            raw = payload.getData();
            if(raw!=null)
                try {
                    offset = Integer.parseInt(raw);
                } catch (Exception ignored) {}
        }
        return offset;
    }
    private void newManga(Integer offset) {
        if(offset<0)
            offset=0;
        AtomicReference<MessageSendResponse.MessageInfo> mi = new AtomicReference<>(vkContent.reply("Loading..."));
        try {
            HChanManga[] mangas = new HChanParser().setCoverToAttachConverter(
                    cover -> {try {
                        String coverAttach = vkContent.getUploadedPhoto(cover).toStringAttachment();
                        System.out.println("Cover uploaded: "+coverAttach);
                        return coverAttach;} catch (Exception e) {e.printStackTrace();
                        return "";}}
            ).getNew(status->{
                mi.set(mi.get().editMessageOrDeleteAndReply(status));
            });
            if(mangas.length>0) {
                lastSearchOffset = offset;
                results = mangas;
                scrollResult(resultOffset);
            }
            else mi.get().editMessageOrDeleteAndReply("Нового нет..");
        } catch (IOException e) {
            vkContent.reply("Error: "+e.getLocalizedMessage());
        }
    }
    private void scrollResult(Integer resultOffset) {
        if(resultOffset>=results.length||resultOffset<0) {
            mainMenu("Ошибка, индекс за пределами результатов");
            return;
        }
        String cover = results[resultOffset].getCoverAttach();
        vkContent.reply("",cover.trim().length()>0?new String[]{cover}:null,

            new Keyboard(
                new KeyboardLine(

                        new Button(Button.Type.CALLBACK,"Меню",Button.COLOR_POSITIVE).setStage("watch"),
                        resultOffset>0?new Button(Button.Type.CALLBACK,"Назад",Button.COLOR_POSITIVE).setStage("watch").setData((resultOffset-1)+""):null,
                        (resultOffset+1)<results.length?new Button(Button.Type.CALLBACK,"Вперед",Button.COLOR_POSITIVE).setStage("watch").setData((resultOffset+1)+""):null

                ),
                new KeyboardLine(

                                new Button(Button.Type.CALLBACK,"Меню",Button.COLOR_SECONDARY).setStage("1"),
                        resultOffset>0?new Button(Button.Type.CALLBACK,"Назад",Button.COLOR_POSITIVE).setStage("scroll").setData((resultOffset-1)+""):null,
        (resultOffset+1)<results.length?new Button(Button.Type.CALLBACK,"Вперед",Button.COLOR_POSITIVE).setStage("scroll").setData((resultOffset+1)+""):null

                )).setOne_time(true)
            );

    }
    private void unknownCommand() {
        vkContent.reply("Error: Unknown command\n\nTry: exit, new");
    }
    private void conversationsUnsupported() {
        vkContent.reply("Sorry, HChan mode is not available in conversations :<");
    }
    private void mainMenu(String message) {
        vkContent.reply(message,null,
                new Keyboard(
                        new KeyboardLine(
                                new Button(Button.Type.CALLBACK,"Выход",
                                        Button.COLOR_SECONDARY).setPayload("hchan","exit"),
                                new Button(Button.Type.CALLBACK,"Новое",
                                        Button.COLOR_POSITIVE).setPayload("hchan","new")))
                        .setOne_time(true));
    }
    private void bye() {
        vkContent.getVK().removeStages();
        vkContent.reply("Bye, see you soon! :3",null,Keyboard.clear());
    }
}
