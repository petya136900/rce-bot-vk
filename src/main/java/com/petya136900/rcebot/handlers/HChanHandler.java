package com.petya136900.rcebot.handlers;

import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.other.hchan.HChanManga;
import com.petya136900.rcebot.other.hchan.HChanParser;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
    private boolean fromCallback=false;
    private boolean busy=false;
    private Thread loadingThread;
    @Override
    public void handle(VK vkContent) {
        this.vkContent=vkContent;
        payload = JsonParser.fromJson(vkContent.getVK().getPayload(),Payload.class);
        String stage = vkContent.getVK().getStage();
        String message = vkContent.getVK().getText();
        if(payload==null) { payload = vkContent.getVK().getCallbackPayload(); fromCallback=true; }
        /*
        if(!vkContent.isAdmin()) {
            vkContent.reply("Access denied ;)");
            return;
        }
        */
        if(!vkContent.getVK().getPeer_id().equals(vkContent.getVK().getFrom_id())) {
            conversationsUnsupported();
            return;
        }
        if(payload==null) {
            if(RegexpTools.checkRegexp("exit|выход|назад|отмена",message)) {
                stopLoading();
                bye();
                return;
            }
        } else if(payload.getStage()!=null) {
            stage=payload.getStage();
        }
        lockStage();
        if(busy) {
            markCBRead("Please wait..");
            vkContent.reply("Please wait, loading in progress..");
            return;
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
    private void stopLoading() {
        if(loadingThread!=null&&loadingThread.isAlive()) {
            vkContent.reply("Stopping..");
            loadingThread.interrupt();
            try {
                loadingThread.join();
            } catch (InterruptedException ignored) {}
        }
    }
    private void markCBRead(String message) {
        if(fromCallback) {
            EventData event_data = new EventData(EventData.Type.SHOW_SNACKBAR, "HCHan: " + message);
            vkContent.replyMessageEventAnswer(event_data);
        }
    }
    private void watchResult(Integer offset) {
        if(offset>=results.length||offset<0) {
            badIndex();
            return;
        }
        HChanManga result = results[offset];
        if(result==null) {
            badIndex();
            return;
        }
        try {
            busy();
            markCBRead(results[offset].getTitle());
            MessageSendResponse.MessageInfo mi = vkContent.reply(results[offset].getTitle());
            ArrayList<String> attachs = new ArrayList<>();
            if(!result.isParsed()) {
                mi.editMessageOrDeleteAndReply("Новый комикс, загрузка в БД..");
                int i=1;
                for(String page : result.getPageUrls()) {
                    if(Thread.currentThread().isInterrupted())
                        throw new InterruptedException();
                    String attach = upload(page);
                    if(attach!=null)
                        attachs.add(attach);
                    mi.editMessageOrDeleteAndReply(String.format("Новый комикс, загрузка в БД..[%d/%d]",i,result.getPageUrls().length));
                    i++;
                }
                String[] attachesArray = getArray(attachs);
                result.setAttachs(attachesArray);
                result.setParsed(true);
                MySqlConnector.saveHCHan(result,true);
            }
            if(result.getAttachs()==null||result.getAttachs().length<1) {
                vkContent.reply("Хмм.. у этого комикса ещё нет страниц, выберите другой");
                unbusy();
                return;
            }
            attachs = new ArrayList<>();
            for(int y=0;y<result.getAttachs().length;y++) {
                attachs.add(result.getAttachs()[y]);
                if(attachs.size()==10) {
                    vkContent.reply("",getArray(attachs));
                    attachs = new ArrayList<>();
                }
            }
            if(attachs.size()>0)
                vkContent.reply("",getArray(attachs));
        } catch (InterruptedException e) {
            mainMenu("Loading interrupted");
        } catch (Exception e) {
            mainMenu("Error: "+e.getLocalizedMessage());
        } finally {
            unbusy();
        }
    }
    public static String[] getArray(ArrayList<String> s) {
        return s.toArray(new String[s.size()]);
    }
    private void badIndex() {
        markCBRead("Bad index");
        mainMenu("Ошибка, индекс за пределами результатов");
    }
    private void busy() {
        loadingThread=Thread.currentThread();
        busy=true;
    }
    private Integer getOffset() {
        int offset=0;
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
        markCBRead("Fetching updates..");
        if(offset<0)
            offset=0;
        AtomicReference<MessageSendResponse.MessageInfo> mi = new AtomicReference<>(vkContent.reply("Loading...",null,Keyboard.clear()));
        try {
            busy();
            HChanManga[] mangas = new HChanParser().setCoverToAttachConverter(this::upload).getNew(offset,status->
                mi.set(mi.get().editMessageOrDeleteAndReply(status))
            );
            if(mangas.length>0) {
                lastSearchOffset = offset;
                results = mangas;
                scrollResult(resultOffset);
            }
            else mi.get().editMessageOrDeleteAndReply("Нового нет..");
        } catch (InterruptedException e) {
            mainMenu("Loading interrupted");
        } catch (Exception e) {
            mainMenu("Error: "+e.getLocalizedMessage());
        } finally {
            unbusy();
        }
    }
    public String upload(String s) {
        try {
            if(s==null||s.trim().length()<1)
                return null;
            return vkContent.getUploadedPhoto(new URL(s)).toStringAttachment();} catch (Exception e) {e.printStackTrace();
            return null;}
    }
    private void unbusy() {
        busy=false;
    }

    private void scrollResult(Integer resultOffset) {
        if(resultOffset>=results.length||resultOffset<0) {
            markCBRead("Bad index");
            mainMenu("Ошибка, индекс за пределами результатов");
            return;
        }
        markCBRead("Result "+(resultOffset+1)+" of "+results.length+"\n"+results[resultOffset].getTitle());
        String cover = results[resultOffset].getCoverAttach();
        vkContent.reply("#"+(resultOffset+1)+" | "+results[resultOffset].getTitle()+(cover==null?"\n[Нет обложки]":"")+"\n"
                +desc(results[resultOffset]),cover!=null?new String[]{cover}:null,
            new Keyboard(
                new KeyboardLine(
                        new Button(Button.Type.CALLBACK,"Смотреть",Button.COLOR_POSITIVE).setHandler("hchan").setStage("watch").setData(String.valueOf(resultOffset))
                ),
                new KeyboardLine(
                                new Button(Button.Type.CALLBACK,"Меню",Button.COLOR_SECONDARY).setHandler("hchan").setStage("1"),
                        resultOffset>0?new Button(Button.Type.CALLBACK,"Назад",Button.COLOR_POSITIVE).setHandler("hchan").setStage("scroll").setData((resultOffset-1)+""):null,
        (resultOffset+1)<results.length?new Button(Button.Type.CALLBACK,"Вперед",Button.COLOR_POSITIVE).setHandler("hchan").setStage("scroll").setData((resultOffset+1)+""):null
                ),
(resultOffset+1)==results.length?new KeyboardLine(
                        new Button(Button.Type.CALLBACK,"Загрузить ещё",Button.COLOR_POSITIVE).setHandler("hchan").setStage("new").setData(String.valueOf(lastSearchOffset+20))
                ):null).setOne_time(true)
            );

    }
    private String desc(HChanManga result) {
        return result.getItemName() + ": " + result.getItemContent() + "\n" +
                "Тэги: " + String.join(", ", result.getTags()) + "\n" +
                check(result.getDescription()) + "\n";
    }

    private String check(String string) {
        if(string!=null)
            if(string.trim().length()>0)
                return string;
        return "";
    }

    private void lockStage() {
        vkContent.getVK().setStage("custom");
    }

    private void unknownCommand() {
        vkContent.reply("Error: Unknown command\n\nTry: exit, new");
    }
    private void conversationsUnsupported() {
        vkContent.reply("Sorry, HChan mode is not available in conversations :<");
    }
    private void mainMenu(String message) {
        markCBRead("Loaded main menu");
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
        markCBRead("Bye! ^_^");
        vkContent.getVK().removeStages();
        vkContent.reply("Bye, see you soon! :3",null,Keyboard.clear());
    }
}
