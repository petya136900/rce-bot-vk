package com.petya136900.rcebot.tools;

import org.fusesource.jansi.Ansi;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsoleParser {
    private Settings consoleSettings = Settings.getInternalInstance();
    public static Settings parseArgs(String[] args) {
        return new ConsoleParser().localParseArgs(args);
    }
    private Settings localParseArgs(String[] args) {
        consoleSettings.setConsoleArgs(args);
        for(int i=0;i<args.length;i++) {
            StoredString ss = RegexpTools.storeString(args[i]);
            if(ss.check("^(-?-?he?l?p?)$")) {
                System.out.println(s("--help",sa("-h"),false,false,"Show this message"));
                System.out.println(s("--token",sa("-t"),true,false,"Group token (Permissions: messages, offline)","--token 6b8e...53ef18f4"));
                System.out.println(s("--longpoll",sa("-lp"),false,false,"Use Long Polling to receive a messages (by default)"));
                System.out.println(s("--callback",sa("-cb"),true,false,"Use Call Back to receive a message","--callback 8080"));
                System.out.println(s("--confirmcode",sa("-cd"),true,false,"Provide confirmation code for CallBack","--confirmcode b46df2y"));
                System.out.println(s("--testmode",sa("--test","-tm"),false,false,"Test Mode, don't sends logs, reply only to admin"));
                System.out.println(s("--names",sa("-n"),true,false,"Path to file with the names that the bot responds to, separated by commas","--names names.txt\n\t\n\tContent of names.txt: \n\t\t\t\trce,^bot,^l*l"));
                System.out.println(s("--adminid",sa("-aid"),true,false,"Admin ID, only Integer","-aid 550940196"));
                System.out.println(s("--apiversion",sa("-v"),true,false,"API version, by default 5.130"));
                System.out.println(Ansi.ansi().fgDefault());
                System.exit(0);
            } else if(ss.check("^(-t|--token)$")) {
                if(args.length>(i+1)) {
                    consoleSettings.setGroupToken(args[i+1].trim()); i++;
                } else {
                    throw new IllegalArgumentException("You have not specified a --token");
                }
            } else if(ss.check("^(-lp|--longpoll)$")) {
                consoleSettings.setUseLongPoll(true);
            } else if(ss.check("^(-cb|--callback)$")) {
                consoleSettings.setUseLongPoll(false);
                if(args.length>(i+1)) {
                    consoleSettings.setCallBackPort(Integer.parseInt(args[i+1].trim())); i++;
                } else {
                    throw new IllegalArgumentException("You have not specified a callback port");
                }
            } else if(ss.check("^(-cd|--confirmcode)$")) {
                if(args.length>(i+1)) {
                    consoleSettings.setConfirmCode(args[i+1].trim()); i++;
                } else {
                    throw new IllegalArgumentException("You have not specified a --confirmcode");
                }
            } else if(ss.check("^(-tm|--testmode|--test)$")) {
                consoleSettings.setTestMode(true);
            } else if(ss.check("^(-aid|--adminid)$")) {
                if(args.length>(i+1)) {
                    try {
                        consoleSettings.setAdminID(Integer.parseInt(args[i+1].trim())); i++;
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Bad --adminid");
                    }
                } else {
                    throw new IllegalArgumentException("You have not specified a --adminid");
                }
            } else if(ss.check("^(-v|--apiversion)$")) {
                if(args.length>(i+1)) {
                    consoleSettings.setApiVersion(args[i+1].trim()); i++;
                } else {
                    throw new IllegalArgumentException("You have not specified a --apiVersion");
                }
            } else if(ss.check("^(-n|--names)$")) {
                if(args.length>(i+1)) {
                    try {
                        Stream<String> stream = Stream.of(
                            String.join(",", Files.readAllLines(
                                    new File(args[i + 1].trim()).toPath(),
                                    StandardCharsets.UTF_8)).split(","));
                        String[] names2 = stream
                                .filter(x->x!=null&&x.trim().length()>0)
                                .toArray(String[]::new);
                        consoleSettings.setNames(ArrayTools.concatenate(consoleSettings.getNames(), names2));
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new IllegalArgumentException("Bad input file for --name");
                    }
                } else {
                    throw new IllegalArgumentException("You have not specified a --names");
                }
            }
        }
        return consoleSettings;
    }
    private static Ansi s(String argName, String[] altNames, Boolean hasValue,Boolean isArrayValues, String desc) {
        return s(argName,altNames,hasValue,isArrayValues,desc,null);
    }
    private static Ansi s(String argName, String[] altNames, Boolean hasValue,Boolean isArrayValues, String desc, String example) {
        String altNamesString ="";
        if(altNames!=null&&altNames.length>0) {
            altNamesString = Stream.of(altNames)
                    .filter(x->x!=null&&x.length()>0)
                    .map(x->"OR "+x.trim()+" ")
                    .collect(Collectors.joining());
        }
        return Ansi.ansi().a("\n").fgBrightYellow().a(argName).a(" ")
                .fgYellow().a(altNamesString)
                .fgBrightBlue().a((hasValue?(isArrayValues?"[value1,value2,..,valueN] ":"[value] "):""))
                .fgDefault()
                .a("| ")
                .fgBrightGreen()
                .a(desc)
                .fgCyan()
                .a(((example!=null&&example.length()>0)?"\n\tFor example: "+example:""));
    }
    private static String[] sa(String ...elements) {
        return elements;
    }
}
