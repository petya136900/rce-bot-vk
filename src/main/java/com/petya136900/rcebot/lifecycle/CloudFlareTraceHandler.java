package com.petya136900.rcebot.lifecycle;

import com.petya136900.rcebot.vk.VK;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CloudFlareTraceHandler implements HandlerInterface {
    private final static String CLOURFLARE_TRACE_URL = "https://cloudflare.com/cdn-cgi/trace";
    private final HashMap<String, String> data = new HashMap<>();
    @Override
    public void handle(VK vkContent) {
        String message = vkContent.getVK().getText();
        if(!vkContent.isAdmin()) {
            vkContent.reply("Access denied");
            return;
        }
        try {
            URL url = new URL(CLOURFLARE_TRACE_URL);
            String response;
            try(Scanner s = new Scanner(url.openStream())) {
                response = s.useDelimiter("\\Z").next();
            }
            String[] entries = response.split("\n");
            try {
                for (String entry : entries) {
                    String[] parts = entry.split("=", 2);
                    data.put(parts[0], parts[1]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                vkContent.reply("Bad Data: "+response);
                return;
            }
            if(message.toLowerCase().contains("ip")) {
                vkContent.reply("IP: "+data.get("ip"));
            } else {
                StringBuilder sb = new StringBuilder("Trace data\n");
                for(Map.Entry<String,String> entry : data.entrySet()) {
                    sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
                vkContent.reply(sb.toString());
            }
        } catch (IOException e) {
            vkContent.reply("Error occurs: "+e.getLocalizedMessage());
        }
    }
}
