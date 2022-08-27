package com.petya136900.rcebot.handlers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.petya136900.rcebot.other.Tokens;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.petya136900.rcebot.lifecycle.HandlerInterface;
//import com.petya136900.rcebot.tools.KeyboardAnimations;
import com.petya136900.rcebot.tools.RegexpTools;
import com.petya136900.rcebot.vk.VK;
//import com.petya136900.rcebot.vk.structures.Field;
import com.petya136900.rcebot.vk.structures.ResponseGen;
//import com.petya136900.rcebot.vk.structures.User;
import com.petya136900.rcebot.vk.structures.VKAttachment;
import com.petya136900.rcebot.vk.structures.VKMessage;
import com.petya136900.rcebot.vk.structures.MessageSendResponse.MessageInfo;
import com.petya136900.rcebot.vk.structures.VKAttachment.Photo.Size;

public class FurryHandler implements HandlerInterface {
	private static final String SAUCENAO_USER = Tokens.SAUCENAO_USER_ID;
	private static final String SAUCENAO_TOKEN = Tokens.SAUCENAO_TOKEN;
	private static final String SAUCENAO_AUTH = Tokens.SAUCENAO_AUTH;
	private static final long REQUEST_TIMEOUT = 750;
	private Boolean nf=true;
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
		MessageInfo mi1 = vk.reply("Загрузка..");
		Document page = loadURL(imgURL, null,vk);
		mi1 = mi1.editMessageOrDeleteAndReply("Парсинг результатов..");
		page.select("[class=\"result hidden\"]").remove();
		page.select("[id=\"result-hidden-notification\"]").remove();
		//String picUrl;
		String accuracy;
		String creator;
		String sourceUrl;
		String previewURL;
		String material;
		String character;
		String drawr;
		String tweet;
		String twitter;
		String thread;
		String devianID;
		String devianCreator;
		String pixivID;
		String member;
		String replyMessage="";
		if(page!=null) {
			//vk.reply("Searching for: "+imgURL);
			//System.out.println("Searching for: "+imgURL);
			Elements results = page.getElementsByClass("result");
			mi1 = mi1.editMessageOrDeleteAndReply("Парсинг результатов.. [Найдено "+results.size()+"]");
			//replyMessage+="Бот#2\nАртов найдено: "+results.size()+"\n";
			//for(Element result : results) {
			ArrayList<String> previewURLs = new ArrayList<String>();
			for(int resCount=0;resCount<results.size();resCount++) {	
				Element result = results.get(resCount);
				//replyMessage+="Арт#"+(resCount+1);
				//System.out.println("Art#"+(resCount+1));
				//picUrl=result.getElementsByClass("resultimage").get(0).
				//		getElementsByTag("a").get(0).getElementsByTag("img").get(0).attr("src");
				accuracy=null;
				creator=null;
				sourceUrl=null;
				previewURL=null;
				material=null;
				drawr=null;
				thread=null;
				tweet=null;
				twitter=null;
				character=null;
				devianID=null;
				devianCreator=null;
				pixivID=null;
				member=null;
				//replyMessage+="Нашёл: "+picUrl;
				//System.out.println(picUrl);
				accuracy = result.getElementsByClass("resultsimilarityinfo").get(0).text();
				replyMessage+="#"+(resCount+1)+" [Совпадение "+accuracy+"]";
				try {
					previewURL = result.getElementsByClass("resultimage").get(0).getElementsByTag("img").attr("src");
					if(previewURL!=null&&previewURL.length()>1) {
						previewURLs.add(previewURL);
					}
				} catch (Exception e) {
					//
				}
				Elements title = result.getElementsByClass("resulttitle");
				if(title.size()>0) {
					creator=title.get(0).text();
					creator = (RegexpTools.checkRegexp("Creator: ", creator)) ?
							creator.replace("Creator: ","Автор: ") :
								"Арт: "+creator;
					replyMessage+="\n"+creator;
				}
				Elements contents = result.getElementsByClass("resultcontentcolumn");
				for(Element content : contents) {
					String htmlCont=content.getAllElements().html();
					Document sourceUrlDoc = getParsedHtml("Source",htmlCont);
					if(sourceUrlDoc!=null) {
						sourceUrl="\n"+"Источник: ";
						for(Element ss : sourceUrlDoc.getElementsByTag("a")) {
							sourceUrl+=ss.attr("href");
						}
						replyMessage+=sourceUrl;
					}
					Document tweetDoc = getParsedHtml("Tweet ID",htmlCont);
					if(tweetDoc!=null) {
						try {
							tweet="\n"+String.format("Tweet: %s",tweetDoc.getElementsByTag("a").get(0).attr("href"));
							replyMessage+=tweet;
						} catch (Exception ignored) {}
					}	
					Document twitterDoc = getParsedHtml("Twitter",htmlCont);
					if(twitterDoc!=null) {
						try {
							Element a = twitterDoc.getElementsByTag("a").get(0);
							twitter="\n"+String.format("Twitter(%s): %s",a.text(),a.attr("href"));
							replyMessage+=twitter;
						} catch (Exception ignored) {}
					}
					Document drawrDoc = getParsedHtml("Drawr ID",htmlCont);
					if((drawrDoc!=null&&drawrDoc.getElementsByTag("a").size()>0)) {
						try {
							drawr="\n"+String.format("Drawr(%s): ",drawrDoc.getElementsByTag("a").get(0).text()+"")+" "+
									drawrDoc.getElementsByTag("a").get(0).attr("href");
							replyMessage+=drawr;
						} catch (Exception ignored) {}
					}
					Document threadDoc = getParsedHtml("Thread",htmlCont);
					if(threadDoc!=null) {
						try {
							thread="\n"+String.format("Thread(%s): ",threadDoc.getElementsByTag("a").get(0).text()+"")+" "+
									threadDoc.getElementsByTag("a").get(0).attr("href");
							replyMessage+=thread;
						} catch (Exception ignored) {}
					}		
					Document materialDoc = getParsedHtml("Material",htmlCont);
					if(materialDoc!=null) {
						material="\n"+"Откуда: ";
						material+=materialDoc.text().replace("Material: ","").trim();
						replyMessage+=material;
					}					
					Document characterDoc = getParsedHtml("Characters",htmlCont);
					if(characterDoc!=null) {
						character="\n"+"Персонаж: ";
						character+=characterDoc.text().replace("Characters: ","").trim();
						replyMessage+=character;
					}					
					Document devianIDDoc = getParsedHtml("dA ID",htmlCont);
					if(devianIDDoc!=null) {
						try {
							devianID="\n"+"DevianArt: ";
							devianID+=devianIDDoc.getElementsByTag("a").get(0).attr("href");
							replyMessage+=devianID;
						} catch (Exception ignored) {}
					}						
					Document devianCreatorDoc = getParsedHtml("Author",htmlCont);
					if(devianCreatorDoc!=null) {
						devianCreator="\n"+"Автор";
						Elements links=devianCreatorDoc.getElementsByTag("a");
						if(links.size()>0) {
							devianCreator+="("+links.get(0).text()+"): ";
							devianCreator+=links.get(0).attr("href");
						} else {
							devianCreator+=": ";
							devianCreator+=devianCreatorDoc.text();
						}
						replyMessage+=devianCreator;
					}	
					Document pixivIDDoc = getParsedHtml("Pixiv ID",htmlCont);
					if(pixivIDDoc!=null) {
						try {
							pixivID="\n"+"Pixiv: ";
							pixivID+=pixivIDDoc.getElementsByTag("a").get(0).attr("href");
							replyMessage+=pixivID;
						} catch (Exception ignored) {}
					}		
					Document memberDoc = getParsedHtml("Member",htmlCont);
					if(memberDoc!=null) {
						try {
							member="\n"+"Автор("+memberDoc.getElementsByTag("a").get(0).text()+"): ";
							member+=memberDoc.getElementsByTag("a").get(0).attr("href");
							replyMessage+=member;
						} catch (Exception ignored) {}
					}							
				}
				if(replyMessage.length()>1) {
					replyMessage+="\n\n";
				}
			}
			ResponseGen<Integer> intStore = new ResponseGen<Integer>();
			ResponseGen<MessageInfo> miS = new ResponseGen<MessageInfo>(); 
			miS.setResponse(mi1);
			intStore.setResponse(0);
			String[] attachs = previewURLs.stream()
				.map(x->{
					try {
						intStore.setResponse(intStore.getResponse()+1);
						miS.setResponse(miS.getResponse().editMessageOrDeleteAndReply("Загрузка превью ["+(intStore.getResponse())+"/"+previewURLs.size()+"]"));
						return VK.getUploadedPhoto(vk.getVK().getPeer_id(), new URL(x)).toStringAttachment();
					} catch (Exception e) {
						return null;
					}
				})
				.filter(x->x!=null)
				.limit(10)
				.toArray(String[]::new);
			if(attachs.length>0) {
				replyMessage+="[Превью]";
			}
			mi1 = miS.getResponse();
			if(replyMessage.length()>1) {
				//mi1.deleteMessage();
				//vk.reply(replyMessage,attachs.length>0?attachs:null);
				mi1.editMessageOrDeleteAndReply(replyMessage,attachs.length>0?attachs:null);
			} else {
				mi1.editMessageOrDeleteAndReply("Арт не найден");
			}
		} 
	}
	private Document getParsedHtml(String string, String htmlCont) {
		htmlCont=htmlCont.replaceAll("></d", ">ъ</d");
		htmlCont=htmlCont.replaceAll("><st", ">ъ<st");		
		Pattern patternS = Pattern.compile("(<strong>"+string+": <\\/strong>)[^ъ]+(<br>)");
		Matcher matcherS = patternS.matcher(htmlCont);
		String result = matcherS.find()?matcherS.group():null;
		Document html = null;
		if(result!=null) {
			result=result.replaceAll(">ъ</d", "></d");
			result=result.replaceAll(">ъ<st", "><st");
			html = Jsoup.parse(result);
			//System.out.println(html.text());
		}
		return html;
	}
	private Document loadURL(String imgUrl, Integer dbNum,VK vk) throws Exception {
		int retryCount = 0;
		Document htmlResponse = null;
		if (dbNum == null) {
			dbNum = 29;
		}
		String urlHost = "https://saucenao.com/search.php";
		Exception lastException = null;
		while (++retryCount < 7) {
			try {
				String urlR = urlHost;
				Connection conJsoup = Jsoup.connect(urlR);
				conJsoup.cookie("token", SAUCENAO_TOKEN);
				conJsoup.cookie("user", SAUCENAO_USER);
				conJsoup.cookie("auth", SAUCENAO_AUTH);
				conJsoup.data("url", imgUrl);
				conJsoup.data("frame", "1");
				conJsoup.data("hide", "0");
				conJsoup.data("database", "999");
				htmlResponse = conJsoup.get();
				return htmlResponse;
			} catch (HttpStatusException e) {
				try {
					Thread.sleep(REQUEST_TIMEOUT * (retryCount));
				} catch (Exception ignored) {
				}
				lastException = e;
			} catch (IOException e) {
				throw e;
			}
		}
		throw lastException;
	}	
}
