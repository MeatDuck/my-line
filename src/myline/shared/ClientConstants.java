package myline.shared;


//oauth_token=aR9sowYtyAmMZaNtwKOdLZEdTuSRv6IxvFyDVKCut8
//http://127.0.0.1:8888/My_line.html?gwt.codesvr=127.0.0.1:9997
final public class ClientConstants {
	private ClientConstants(){
		
	}
	
	//lang
	public static final String SERVER_ERROR = "An error occurred while "
		+ "attempting to contact the server. Please check your network "
		+ "connection and try again.";
	public static final String LINK_NAME = "Перейдите по ссылке для авторизации";
	public static final String SEND_LABEL = "Отправить";
	public static final String CLEAR_LABEL = "Очистить";
	public static final String DELETE_TEXT = "Удалить";
	public static final String SENT_CTRL_ENTER = "Отправить (Ctrl + Enter)";
	public static final String SHORT_LINKS_LABEL = "Короткие ссылки";	
	public static final String INSERT_URL_LABEL = "Вставьте URL в поле ввода и нажмите ОК";
	public static final String OK_LABEL = "OK";
	public static final String GET_SHORT_URL = "Получить короткую ссылку";
	public static final String SYN_LABEL = "Синхронизация";
	public static final String SETTINGS_LABEL = "Настройки";
	public static final String STATUS_SETTINGS_LABEL = "Настройки статусов";
	public static final String SEND_STATUS_TOWALL = "Отправлять статус на стену";
	public static final String TURN_ON_COOKIES = "Включите cookies в вашем браузере";
	public static final String UPDATE_LINE_ERROR_MESSAGE = "Ошибка загрузки твитов";
	public static final String ADD_LINE_ERROR_MESSAGE = "Ошибка добавления твитов";
	public static final String ADD_LINK_ERROR_MESSAGE = "Ошибка получения короткой ссылки";
	public static final String DELETE_TWIT_ERROR_MESSAGE = "Ошибка удаления твита";
	public static final String LOGOUT_ERROR_MESSAGE = "Ошибка выхода";
	public static final String WAIT_LABEL = "Подождите";
	public static final String TWI_LABEL = "Twit-light";
	public static final String LOGOUT_LABEL = "Зайти под другим twitter аккаунтом";
	public static final String TOKEN_ERROR = "Токен не валиден";
	
	//const for def
	public static final String REQUEST_TOKEN = "rt";
	public static final String REQUEST_HASH = "sec";
	public static final String ACC_TOKEN = "acctoken";
	public static final String ACC_SECRET = "acchash";
	public static final String SCREENNAME = "SCNAME";
	
	//temp
	public static final int MAX_MESSAGE_LENGTH = 140;
	public static final String LOCAL_ADDRESS_HOST = "127.0.0.1";
	public static final Integer TIMER_CALL_TIME = 60 * 1000;
	public static final Integer TIMER_ERROR_SHOW_TIME = 3 * 1000;
	public static final Integer TIMER_REFESH_SENDER_TIME = 3 * 1000;
	
	
	
	
	
	
	
	
}
