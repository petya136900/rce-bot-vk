package com.petya136900.rcebot.lifecycle;

import com.google.re2j.Matcher;
import com.petya136900.rcebot.tools.JsonParser;
import com.petya136900.rcebot.tools.RegexpTools;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HChanParser {
    private static final String HCHAN_SCHEME = "https://";
    private static final String HCHAN_URL = "hchan.live/";
    private static final String NEW  = "manga/new";
    private static final String TAGS = "tags";
    public static void test() {
        try {
            HChanManga[] mangas = getNew();
            for(HChanManga manga : mangas) {
                System.out.println(JsonParser.toJson(manga)+"\n\n");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static HChanManga[] getNew() throws IOException {
        return parseRows(getPage(HCHAN_SCHEME + HCHAN_URL + NEW));
    }
    public static HChanManga[] getByTags(String[] tags) throws IOException {
        String tagsString = Arrays.stream(tags)
                .filter(tag->tag!=null&&tag.trim().length()>0)
                .map(String::trim)
                .collect(Collectors.joining("+"));
        if(tagsString.length()<1)
            throw new IllegalArgumentException("Ошибка: тэги указаны некорректно");
        return parseRows(getPage(HCHAN_SCHEME + HCHAN_URL + TAGS + "/" + tagsString));
    }
    private static HChanManga[] parseRows(Document page) throws IOException {
        Elements content_rows = page.getElementsByClass("content_row");
        ArrayList<HChanManga> comics = new ArrayList<>();
        for(Element content_row: content_rows) {
            comics.add(parseRow(content_row));
        }
        return comics.toArray(new HChanManga[comics.size()]);
    }
    private static HChanManga parseRow(Element content_row) throws IOException {
        HChanManga comic = new HChanManga();
        Element manga_img = content_row.getElementsByClass("manga_images").get(0);
        if(manga_img!=null) {
            comic.setLink(manga_img.getElementsByTag("a").get(0).absUrl("href"));
            // TODO: CHECK IF EXIST by link in DB
            String thumbUrlBlur = manga_img.getElementsByTag("img").get(0).absUrl("src");
            if(thumbUrlBlur.trim().trim().length()>0)
                comic.setThumbImgUrl(thumbUrlBlur.replaceFirst("_blur",""));
        }
        Document comicPage;
        if(le(comic.getLink())) {
            comicPage = getPage(comic.getLink());
            Element cover = comicPage.getElementById("cover");
            if(cover!=null)
                comic.setCoverUrl(cover.attr("src"));
            Element manga_images = comicPage.getElementById("manga_images");
            if(manga_images!=null)
                comic.setReadLink(manga_images.getElementsByTag("a").get(0).absUrl("href"));
        }
        ArrayList<String> pages = new ArrayList<>();
        if(le(comic.getReadLink())) {
            Document readPage = getPage(comic.getReadLink());
            Matcher matcher = RegexpTools.getMatcher(readPage.getElementsByTag("script").get(2).toString(),
                    "(\\\"fullimg\\\":)+.*']");
            if(matcher.find()) {
                String result = matcher.group().replaceFirst("(\\\"fullimg\\\":)","");
                String[] pagesUrl = JsonParser.fromJson(result, String[].class);
                comic.setPageUrls(pagesUrl);
            }
        }
        comic.setTitle(content_row.getElementsByClass("manga_row1")
                .get(0).getElementsByClass("title_link").get(0).text().trim());
        content_row.select("row3_right").remove();
        Elements manga_row3s = content_row.getElementsByClass("manga_row3");
        Element manga_row3 = manga_row3s.get(0);
        comic.setItemName(manga_row3
                .getElementsByClass("item").text());
        Element item2 = manga_row3.getElementsByClass("item2").get(0);
        comic.setItemContent(item2.text().trim());
        if((comic.getItemName()!=null)&&(!comic.getItemName().equalsIgnoreCase("тип")))
            comic.setItemLink(item2.getElementsByTag("a").get(0).absUrl("href"));
        ArrayList<String> tags = new ArrayList<>();
        if(manga_row3s.size()>1)
            Arrays.stream(manga_row3s.get(1).getElementsByClass("item4").text().split(","))
                    .map(String::trim)
                    .filter(s->s.length()>0)
                    .forEach(tags::add);
        comic.setTags(tags.toArray(new String[tags.size()]));
        return comic;
    }
    private static boolean le(String link) {
        return ((link!=null)&&(link.trim().length()>0));
    }
    private static Document getPage(String url) throws IOException {
        String append = url.contains("?")?"&":"?";
        Connection con = Jsoup.connect(url+append+"development_access=true");
        con.header("Cookie","dle_restore_pass22=1");
        return con.get();
    }
}
