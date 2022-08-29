package com.petya136900.rcebot.tools;

import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public class Properties {
    private final static String DEFAULT_BOT_PROPERTIES_FILE = "bot.ini";
    final static Set<Ini> iniList = new HashSet<>();
    static {
        try {
            iniList.add(new Ini(new File(checkParentFoldersFor(DEFAULT_BOT_PROPERTIES_FILE,2))));
        } catch (Exception ignored) {}
    }

    private static String checkParentFoldersFor(String file, int level) throws FileNotFoundException {
        File tFile = new File(RegexpTools.rString("../",level)+file);
        if(tFile.exists())
            return tFile.toString();
            if(level>0)
                return checkParentFoldersFor(file,level-1);
        throw new FileNotFoundException();
    }

    public static Ini addAdditionalProperties(File propertiesFile) throws IOException {
        Ini ini = new Ini(propertiesFile);
        return addAdditionalProperties(ini);
    }
    public static Ini addAdditionalProperties(Ini ini) {
        iniList.add(ini);
        return ini;
    }
    public static Profile.Section getSection(String sectionName) {
        for(Ini ini : iniList) {
            Profile.Section section = ini.get(sectionName);
            if(section!=null)
                return section;
        }
        return null;
    }
    public static String getProperty(String property) {
        for(Ini ini : iniList) {
            for(Map.Entry<String, Profile.Section> entry : ini.entrySet()) {
                String value = entry.getValue().get(property);
                if(value!=null) // return RegexpTools.replaceRegexp(value,";+.*$","",true).trim();
                     return value.trim();
            }
        }
        return null;
    }
    public static <T> T getProperty(String property, T defaultValue) {
        Class clazz = defaultValue.getClass();
        String propValue = getProperty(property);
        T value=null;
        if(propValue==null)
            return defaultValue;
        try {
            if (clazz.isAssignableFrom(String.class)) {
                value = (T) propValue;
            } else if (clazz.isAssignableFrom(Integer.class)) {
                value = (T) Integer.valueOf(propValue);
            } else if (clazz.isAssignableFrom(Boolean.class)) {
                value = (T) Boolean.valueOf(propValue);
            } else if (clazz.isAssignableFrom(Double.class)) {
                value = (T) Double.valueOf(propValue);
            }
        } catch (Exception ignore) {}
        if(value!=null)
            return value;
        return defaultValue;
    }
    public static String getProperty(String property, String defaultValue) {
        String rProperty = getProperty(property);
        return rProperty!=null?rProperty:defaultValue;
    }
}
