package com.petya136900.rcebot.tools;

import org.ini4j.Ini;
import org.ini4j.Profile;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public class Properties {
    private final static String DEFAULT_BOT_PROPERTIES_FILE = "bot.ini";
    final static Set<Ini> iniList = new HashSet<>();
    static {
        try {
            iniList.add(new Ini(new File(DEFAULT_BOT_PROPERTIES_FILE)));
        } catch (Exception ignored) {}
    }
    public static Ini addAdditionalProperties(File propertiesFile) throws IOException {
        Ini ini = new Ini(new File(DEFAULT_BOT_PROPERTIES_FILE));
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
                if(value!=null) return value;
            }
        }
        return null;
    }
    public static String getProperty(String property, String defaultValue) {
        String rProperty = getProperty(property);
        return rProperty!=null?rProperty:defaultValue;
    }
}
