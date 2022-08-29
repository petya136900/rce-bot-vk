package com.petya136900.rcebot.tools;

import java.util.Arrays;

public class Settings {
    private Settings() {}
    public Settings(String groupToken) {
        setGroupToken(groupToken);
    }
    public static Settings getInternalInstance() {
        return new Settings();
    }
    @Override
    public String toString() {
        int l = groupToken!=null?groupToken.length():0;
        int replaced = 0;
        if(l>1)
            replaced=l/2;
        return (useLongPoll?"Long-Poll":"Call-Back")+" mode\n"
                +"GROUP_BOT_TOKEN: "+(groupToken!=null?(groupToken.replaceFirst(".{"+replaced+"}$",
                RegexpTools.rString("*",replaced))):null)+"\n"
                + "TestMode: "+testMode+"\n"
                + "Admin ID: "+adminID+"\n"
                + "Names: "+ Arrays.toString(names)+"\n"
                + "API-Version: "+apiVersion;
    }
    private String[] consoleArgs=new String[]{};
    private String groupToken;
    private String pathToConfigFile;
    private Integer callBackPort=80;
    private Boolean useLongPoll=true;
    private String confirmCode="bb3ef7c2";
    private Boolean testMode=false;
    private String[] names = new String[] {};
    private Integer adminID=550940196;
    private String apiVersion= "5.130";
    public String getGroupToken() {
        return groupToken;
    }
    public String getPathToConfigFile() { return pathToConfigFile; }
    public void setPathToConfigFile(String pathToConfigFile) { this.pathToConfigFile = pathToConfigFile; }

    public void setGroupToken(String groupToken) {
        this.groupToken = groupToken;
    }

    public Boolean getUseLongPoll() {
        return useLongPoll;
    }

    public void setUseLongPoll(Boolean useLongPoll) {
        this.useLongPoll = useLongPoll;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public Boolean getTestMode() {
        return testMode;
    }

    public void setTestMode(Boolean testMode) {
        this.testMode = testMode;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }
    public Integer getAdminID() {
        return adminID;
    }
    public void setAdminID(Integer adminID) {
        this.adminID = adminID;
    }
    public String getApiVersion() {
        return apiVersion;
    }
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
    public void addBotName(String addedName) {
        addBotNames(new String[] {addedName});
    }
    public void addBotNames(String[] addedNames) {
        if(addedNames!=null&&addedNames.length>0)
            names = ArrayTools.concatenate(names, addedNames);
    }

    public String[] getConsoleArgs() {
        return consoleArgs;
    }

    public void setConsoleArgs(String[] consoleArgs) {
        this.consoleArgs = consoleArgs;
    }

    public Integer getCallBackPort() {
        return callBackPort;
    }

    public void setCallBackPort(Integer callBackPort) {
        this.callBackPort = callBackPort;
    }
}
