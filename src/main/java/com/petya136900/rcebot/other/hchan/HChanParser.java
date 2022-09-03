package com.petya136900.rcebot.other.hchan;

import com.google.re2j.Matcher;
import com.petya136900.rcebot.db.MySqlConnector;
import com.petya136900.rcebot.rce.timetable.TimetableException;
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HChanParser {
    private static final String HCHAN_SCHEME = "https://";
    private static final String HCHAN_URL = "hchan.live/";
    private static final String NEW  = "manga/new";
    private static final String TAGS = "tags";
    private int foundTotal=0;
    private int parsed=0;
    private int failed=0;
    private int imagesTotal=0;
    private int imagesUpload=0;
    private int fromDB=0;
    private Function<String, String> coverToAttachConverter;

    public HChanParser setCoverToAttachConverter(Function<String, String> coverToAttachConverter) {
        this.coverToAttachConverter = coverToAttachConverter;
        return this;
    }
    public static void test() {
        try {
            HChanManga[] mangas = new HChanParser().getNew();
            for(HChanManga manga : mangas) {
                System.out.println(JsonParser.toJson(manga)+"\n\n");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public HChanManga[] getNew() throws IOException, InterruptedException {
        return getNew(0,null);
    }
    public HChanManga[] getNew(Integer offset,Consumer<String> statusConsumer) throws IOException, InterruptedException {
        return parseRows(getPage(HCHAN_SCHEME + HCHAN_URL + NEW + "?offset="+offset), statusConsumer);
    }
    public HChanManga[] getByTags(Integer offset, String tagsByPlus, Consumer<String> statusConsumer) throws IOException, InterruptedException {
        if(tagsByPlus.length()<1)
            throw new IllegalArgumentException("Ошибка: тэги указаны некорректно");
        return parseRows(getPage(HCHAN_SCHEME + HCHAN_URL + TAGS + "/" + tagsByPlus+ "?offset="+offset),statusConsumer);
    }
    public HChanManga[] getByTags(Integer offset, String[] tags, Consumer<String> statusConsumer) throws IOException, InterruptedException {
        String tagsString = Arrays.stream(tags)
                .filter(tag->tag!=null&&tag.trim().length()>0)
                .map(String::trim)
                .collect(Collectors.joining("+"));
        if(tagsString.length()<1)
            throw new IllegalArgumentException("Ошибка: тэги указаны некорректно");
        return getByTags(offset,tagsString, statusConsumer);
    }
    private HChanManga[] parseRows(Document page, Consumer<String> statusConsumer) throws IOException, InterruptedException {
        Elements content_rows = page.getElementsByClass("content_row");
        ArrayList<HChanManga> comics = new ArrayList<>();
        for(Element content_row: content_rows) {
            try {
                comics.add(parseRow(content_row, statusConsumer));
                parsed++;
            } catch (IOException e) {
                failed++;
            }
            statusConsumer.accept(("Parsed: "+parsed+"/"+content_rows.size())+(fromDB>0?" ("+fromDB+" from DB)":"")+(failed>0?" ("+failed+" failed)":""));
        }
        return comics.toArray(new HChanManga[comics.size()]);
    }
    private HChanManga parseRow(Element content_row, Consumer<String> statusConsumer) throws IOException, InterruptedException {
        if(Thread.currentThread().isInterrupted())
            throw new InterruptedException();
        HChanManga comic = new HChanManga();
        Element manga_img = content_row.getElementsByClass("manga_images").get(0);
        if(manga_img!=null) {
            comic.setLink(manga_img.getElementsByTag("a").get(0).absUrl("href").trim());
            HChanManga comicFromDb = MySqlConnector.getHChanByLink(comic.getLink());
            if(comicFromDb.existInDB()) {
                fromDB++;
                return comicFromDb;
            }
            String thumbUrlBlur = manga_img.getElementsByTag("img").get(0).absUrl("src");
            if(thumbUrlBlur.trim().trim().length()>0)
                comic.setThumbImgUrl(thumbUrlBlur.replaceFirst("_blur",""));
        }
        Elements descriptionContainer = content_row.getElementsByClass("tags");
        if(descriptionContainer.size()>0)
            comic.setDescription(descriptionContainer.get(0).text());
        Document comicPage;
        if(le(comic.getLink())) {
            comicPage = getPage(comic.getLink());
            Element cover = comicPage.getElementById("cover");
            if(cover!=null) {
                comic.setCoverUrl(cover.attr("src"));
                if(coverToAttachConverter!=null) {
                    comic.setCoverAttach(coverToAttachConverter.apply(comic.getCoverUrl()));
                }
            }
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
        try {
            MySqlConnector.saveHCHan(comic,false);
        } catch (TimetableException e) {
            System.err.println("Can't save HChan");
            e.printStackTrace();
        }
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
