package com.petya136900.rcebot.handlers;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.petya136900.rcebot.vk.structures.ResponseGen;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.petya136900.rcebot.other.Tokens;
import com.petya136900.rcebot.lifecycle.HandlerInterface;
import com.petya136900.rcebot.vk.VK;
import com.petya136900.rcebot.vk.structures.VKAttachment;
import com.petya136900.rcebot.vk.structures.VKMessage;
import com.petya136900.rcebot.vk.structures.MessageSendResponse.MessageInfo;
import com.petya136900.rcebot.vk.structures.VKAttachment.Photo.Size;

public class FurryHandler implements HandlerInterface {
	private static final String SAUCENAO_USER = Tokens.SAUCENAO_USER_ID;
	private static final String SAUCENAO_TOKEN = Tokens.SAUCENAO_TOKEN;
	private static final String SAUCENAO_AUTH = Tokens.SAUCENAO_AUTH;
	private static final String SAUCENAO_ENGINE_URI = "https://saucenao.com/search.php";
	private static final String IGNORE_LINK = "saucenao.com/info.php";
	private static final long REQUEST_TIMEOUT = 750;
	private static final int MAX_UPLOAD_IMAGES = 10;
	private Boolean nf=true;
	static class SauceNaoResult { // .result
		private String resultTitle; // .resultimage img[title] +
		private String resultImageUrl; // .resultimage img[title] +
		private String similarityInfo; // .resultsimilarityinfo +
		private final ArrayList<String> srcUrl=new ArrayList<>(); // .resultmatchinfo .resultmiscinfo -> array of a[href] +
		private Content content; //
		public String getResultTitle() {
			return resultTitle;
		}
		public String getResultImageUrl() {
			return resultImageUrl;
		}
		public String getSimilarityInfo() {
			return similarityInfo;
		}
		public ArrayList<String> getSrcUrl() {
			return srcUrl;
		}
		public Content getContent() {
			return content;
		}
		public SauceNaoResult(Element result) {
			Elements resultImages = result.getElementsByClass("resultimage");
			if(resultImages.size()>0) {
				Elements selectorResultTitle = resultImages.get(0).select("img[title]");
				if(selectorResultTitle.size()>0) {
					resultTitle = selectorResultTitle.get(0).attr("title");
					resultImageUrl = selectorResultTitle.get(0).attr("src");
				}
			}
			Elements resultSimilarityInfo = result.getElementsByClass("resultsimilarityinfo");
			if(resultSimilarityInfo.size()>0)
				similarityInfo=resultSimilarityInfo.get(0).text();
			Elements resultSrcUrl = result.getElementsByClass("resultmiscinfo");
			resultSrcUrl.select("a").forEach(a->srcUrl.add(a.attr("href")));
			Elements resultContent = result.getElementsByClass("resultcontent");
			if(resultContent.size()>0)
				content= new Content(resultContent.get(0));
		}
		static class Content { // .resultcontent +
			private String strongTitle; // .resulttitle %text% & a[href] +
			private final ArrayList<Part> parts = new ArrayList<>(); // .resultcontentcolumn !raw! -> split array by <br> (ignore .trim().length<1) +
			public Content(Element element) {
				Elements rTitle = element.getElementsByClass("resulttitle");
				if(rTitle.size()>0)
					strongTitle = rTitle.get(0).text();
				element.select(".resulttitle").remove();
				String rawParts = element.children().html();
				Arrays.stream(rawParts.split("<br>")).forEach(part->{
					part=part.trim();
					if(part.length()>1)
						parts.add(new Part(part));
				});
			}
			public String getStrongTitle() {
				return strongTitle;
			}
			public ArrayList<Part> getParts() {
				return parts;
			}
			private static class Part {
				private final String description; // +
				private final ArrayList<String> urls = new ArrayList<>(); // check != IGNORE_LINK +
				public Part(String part) {
					Document html = Jsoup.parse(part);
					description = html.text();
					html.getElementsByTag("a").forEach(a->{
						String link = a.attr("href");
						if(!link.toLowerCase().contains(IGNORE_LINK))
							urls.add(link);
					});
				}
				public String getDescription() {
					return description;
				}
				public ArrayList<String> getUrls() {
					return urls;
				}
			}
		}
	}
	@Override
	public  void     handle(VK vkContent) {
		ArrayList<String> urls = new ArrayList<>();
		VKMessage[] messages = VK.getByConversationMessageId(vkContent.getVK().getPeer_id(), vkContent.getVK().getConversation_message_id());
		analyzeAll(urls,messages);
		if(nf) {
			vkContent.reply("Я не увидел изображения в твоем сообщении :с");
		} else {
			try {
				findFurC(urls, vkContent);
			} catch (Exception e) {
				vkContent.reply(
						String.format("[ERROR] Ресурс недоступен | %s" +
								"\nПопробуйте позднее или уменьшите количество изображений",
								(e instanceof HttpStatusException)?
									"Status: "+((HttpStatusException)e).getStatusCode():""
								));
			}
		}
	}
	private void analyzeAll(ArrayList<String> urls, VKMessage[] messages) {
		for(VKMessage message : messages) {
			analyzeAll(urls,message);
		}
	}
	private void analyzeAll(ArrayList<String> urls, VKMessage message) {
		if(message.getAttachments()!=null) {
			for(VKAttachment attach :message.getAttachments()) {
				if(attach.getType().equals("photo")) {
					Size photo = attach.getPhoto().getMaxSize();
					if(photo!=null) {
						nf=false;
						urls.add(photo.getUrl());
					} 
				}
			}
		}		
		if(message.getReply_message()!=null) { 
			analyzeAll(urls, message.getReply_message());
		}
		if(message.getFwd_messages()!=null) {
			for(VKMessage fwd : message.getFwd_messages()) {
				analyzeAll(urls,fwd);
			}
		}		
	}	
	private void     findFurC(ArrayList<String> urls, VK vkContent) throws Exception {
		if(urls.size()>0) {
			nf=false;
			for(String url : urls) {
				findFur(url,vkContent);
				if(urls.iterator().hasNext()) {
					synchronized (SAUCENAO_AUTH) {
						try {SAUCENAO_AUTH.wait(REQUEST_TIMEOUT);}catch(Exception ignored){}
					}}
			}				
		} else {
			nf=true;
		}
	}
	private void     findFur(String imgURL, VK vk) throws Exception {
		MessageInfo sentMessage = vk.reply("Загрузка..");
		Document page = loadURL(imgURL);
		page.select("[class=\"result hidden\"]").remove();
		page.select("[id=\"result-hidden-notification\"]").remove();
		Elements results = page.getElementsByClass("result");
		sentMessage.editMessageOrDeleteAndReply("Парсинг результатов.. [Найдено "+results.size()+"]");
		ArrayList<SauceNaoResult> sauces=new ArrayList<>();
		results.forEach(x->
			sauces.add(new SauceNaoResult(x))
		);
		ResponseGen<Integer> intStore = new ResponseGen<>();
		ResponseGen<MessageInfo> miS = new ResponseGen<>();
		miS.setResponse(sentMessage);
		intStore.setResponse(0);
		StringBuilder sb=new StringBuilder();
		String[] previewURLs = sauces
				.stream()
				.map(sauce -> {
					intStore.setResponse(intStore.getResponse()+1);
					SauceNaoResult.Content content = sauce.getContent();
					//noinspection StringConcatenationInsideStringBufferAppend
					sb.append("#"+(intStore.getResponse())+" [Совпадение "+sauce.getSimilarityInfo()+"]" + "\n" +
							(sauce.getResultTitle()!=null?(sauce.getResultTitle().length()>0?("Файл: "+sauce.getResultTitle()+"\n"):""):"") +
							(content.getStrongTitle()!=null?(content.getStrongTitle().length()>0?content.getStrongTitle()+"\n":""):"") +
							content.getParts()
									.stream()
									.map(part-> (part.getDescription()!=null?(part.getDescription().length()>0?part.getDescription()+"\n":""):"") +
										part.getUrls()
											.stream()
											.filter(link->link!=null&&link.length()>0)
											.collect(Collectors.joining("\n")))
										.filter(c->c.length()>0)
										.collect(Collectors.joining("\n")) + "\n" +
											(sauce.getSrcUrl().size()>0?"Источник: "+sauce.getSrcUrl()
											.stream()
											.filter(url->url!=null&&url.length()>0)
											.limit(MAX_UPLOAD_IMAGES)
											.collect(Collectors.joining("\n"))
											:"")+"\n\n");
					return sauce.getResultImageUrl();
				})
				.filter(Objects::nonNull)
				.limit(10)
				.toArray(String[]::new);
		intStore.setResponse(0);
		String[] attachs = Stream.of(previewURLs)
			.map((String x) ->{
				try {
					intStore.setResponse(intStore.getResponse()+1);
					miS.getResponse()
						.editMessageOrDeleteAndReply(
				"Загрузка превью ["+intStore.getResponse()+"/"+previewURLs.length+"]");
					return VK.getUploadedPhoto(vk.getVK().getPeer_id(), new URL(x)).toStringAttachment();
				} catch (Exception e) {
					return null;
				}
			})
			.filter(Objects::nonNull)
			.limit(10)
			.toArray(String[]::new);
		if(attachs.length>0) {
			sb.append("[Превью]");
		}
		if(sb.length()>1) {
			miS.getResponse().editMessageOrDeleteAndReply(sb.toString(),attachs.length>0?attachs:null);
		} else {
			miS.getResponse().editMessageOrDeleteAndReply("Арт не найден");
		}
	}
	private Document loadURL(String imgUrl) throws Exception {
		int retryCount = 0;
		Exception lastException = null;
		while (++retryCount < 7) {
			try {
				Connection conJsoup = Jsoup.connect(SAUCENAO_ENGINE_URI);
				conJsoup.cookie("token", SAUCENAO_TOKEN);
				conJsoup.cookie("user", SAUCENAO_USER);
				conJsoup.cookie("auth", SAUCENAO_AUTH);
				conJsoup.cookie("hide","0");
				conJsoup.cookie("database","999");
				conJsoup.data("url", imgUrl);
				conJsoup.data("frame", "1");
				conJsoup.data("hide", "0");
				conJsoup.data("database", "999");
				return conJsoup.get();
			} catch (HttpStatusException e) {
				try {
					Thread.sleep(REQUEST_TIMEOUT * (retryCount));
				} catch (Exception ignored) {}
				lastException = e;
			}
		}
		throw lastException;
	}	
}
