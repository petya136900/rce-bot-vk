package com.petya136900.rcebot.rce.timetable;

import java.util.Arrays;
import java.util.HashMap;

import com.petya136900.rcebot.vk.VK;

public class TimetableException extends Exception {
	private ExceptionCode code;
	public enum ExceptionCode {
		BAD_GROUP,
		BAD_DAY,
		SET_GROUP,
		SQL_CONNECT_ERROR,
		SUNDAY,
		SQL_ERROR_CREATING_TABLE,
		SQL_CHECK_ERROR,
		SQL_UNKWN_ERROR, 
		UNKWN_ERROR_CODE,
		UNKWN_ERROR,
		ERROR_CREATING_DIRECTORY, 
		PARSING_EXCEPTION,
		RCE_UNAVAILABLE, 
		TIMETABLE_FOR_GROUP_NOT_FOUND, 
		BAD_CAB, 
		EMPTY_CAB
	}
	private static final HashMap<ExceptionCode, String> descriptions = new HashMap<ExceptionCode, String>(){
		private static final long serialVersionUID = 560431342273821838L;
		{
			put(ExceptionCode.BAD_GROUP, 
				"Группа %s не найдена");
			put(ExceptionCode.BAD_DAY, 
				"ERROR | Неверно указан день"+"\n"
				+"INFO | Формат - dd.MM.yyyy"+"\n"
				+"Например: Пары на 05.06.2019"+"\n"
				+"Или: Пары на завтра");
			put(ExceptionCode.SET_GROUP, 
				"ERROR | Вы не указали для какой группы"+"\n"
				+"INFO | Пример: Бот, пары для ССА-301 на завтра"+"\n"
				+"Бот запоминает введенную группу!"+"\n"
				+"В дальнейшем её указывать не обязательно"+"\n"
				+"Не забывайте о предлоге 'для', без него бот не поймет запрос");
			put(ExceptionCode.SQL_CONNECT_ERROR, 
				"[SQL]: Ошибка подключения к SQL-серверу");
			put(ExceptionCode.SUNDAY, 
				"Воскресенье - выходной");			
			put(ExceptionCode.SQL_ERROR_CREATING_TABLE, 
				"[SQL]: Ошибка создания таблицы SQL(%s)");
			put(ExceptionCode.SQL_CHECK_ERROR, 
				"[SQL]: Непредвиденная ошибка на %s этапе");
			put(ExceptionCode.SQL_UNKWN_ERROR, 
				"[SQL]: Неизвестная ошибка");		
			put(ExceptionCode.UNKWN_ERROR_CODE, 
				"[ERROR] | Неизвестная ошибка, код#%s");
			put(ExceptionCode.UNKWN_ERROR, 
				"[ERROR] | Неизвестная ошибка");				
			put(ExceptionCode.ERROR_CREATING_DIRECTORY, 
				"[ERROR] | Не удалось создать директорию(%s)");
			put(ExceptionCode.PARSING_EXCEPTION, 
				"[ERROR] | Не удалось проверить расписание\n Отчет отправлен администратору");
			put(ExceptionCode.RCE_UNAVAILABLE, 
				"[ERROR] | Сайт колледжа недоступен для Бота");
			put(ExceptionCode.TIMETABLE_FOR_GROUP_NOT_FOUND, 			
				"[ERROR] | Расписание для этой группы не найдено");
			put(ExceptionCode.BAD_CAB,
				"[ERROR] | Кабинет не указан или некорректен");
			put(ExceptionCode.EMPTY_CAB,
				"[ERROR] | В указанный день нет пар в этом кабинете");
	}};
	private static final long serialVersionUID = 5628085242170699675L;
	private String message;
	private String causeMethod;
	private String causeString;
	private Exception causedException;
	public String getCauseMethod() {
		if(causeMethod!=null) {
			return causeMethod;
		} else {
			return "null";
		}		
	}
	public String getCauseString() {
		if(causeString!=null) {
			return causeString;
		} else {
			return "null";
		}
	}
	public TimetableException(ExceptionCode code, String ...  additionValues) {
		this.code=code;
		Object[] addInfo = new Object[additionValues.length];
		for(int i=0;i<additionValues.length;i++) {
			addInfo[i]=additionValues[i];
		}
		this.message = String.format(descriptions.get(code),addInfo); 
	}
	public TimetableException(ExceptionCode code) {
		this.code=code;
		this.message = descriptions.get(code);
	}	
	public TimetableException(ExceptionCode code, String methodName, String localizedMessage,
			Exception exc) {
		this.code=code;
		this.message = descriptions.get(code);
		this.causeMethod=methodName;
		this.causeString=localizedMessage;
		this.setCausedException(exc);
	}
	public TimetableException(ExceptionCode code, String methodName, String localizedMessage,
			Exception exc, String ...  additionValues) {
		this.code=code;
		Object[] addInfo = new Object[additionValues.length];
		for(int i=0;i<additionValues.length;i++) {
			addInfo[i]=additionValues[i];
		}
		this.message = String.format(descriptions.get(code),addInfo); 
		this.causeMethod=methodName;
		this.causeString=localizedMessage;
		this.setCausedException(exc);		
	}	
	@Override
	public StackTraceElement[] getStackTrace() {
		return super.getStackTrace();
	}
	public String getStringStackTrace() {
		return toString(getStackTrace());
	}
	private String toString(StackTraceElement[] stackTrace) {
		StringBuilder sb = new StringBuilder("");
		for(StackTraceElement stack : stackTrace) {
			sb.append(stack.toString()+"\n");
		}
		return sb.toString();
	}
	@Override
	public String getMessage() {
		return message;
	}
	public Exception getCausedException() {
		return causedException;
	}
	public void setCausedException(Exception causedException) {
		this.causedException = causedException;
	}
	public String messageForAdmin(VK vkContent) {
		return "Произошла ошибка у пользователя: vk.com/id"
				  +vkContent.getVK().getFrom_id()
				  +" ("+vkContent.getVK().getPeer_id()+")"+"\n"
				  +"Запрос: "+vkContent.getVK().getText()+"\n"
				  +getMessage()+"\n"
				  +getStringStackTrace()
				  +(getCauseString()!=null?"\n"+getCauseString():"")
				  +(getCausedException()!=null?("\n"+Arrays.toString(getCausedException().getStackTrace()).replaceAll(",", ",\n")):"");
	}
	public String messageErrorNotify() {
		return "Произошла ошибка рассылки уведомлений:\n"
				  +getMessage()+"\n"
				  +getStringStackTrace()
				  +(getCauseString()!=null?"\n"+getCauseString():"")
				  +(getCausedException()!=null?("\n"+Arrays.toString(getCausedException().getStackTrace()).replaceAll(",", ",\n")):"");
	}	
	public ExceptionCode getCode() {
		return code;
	}
	public void setCode(ExceptionCode code) {
		this.code = code;
	}
}
