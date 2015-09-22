package cn.bamboo.websocket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.JsonKit;


/**
 * 聊天服务器类
 * 
 * @author shiyanlou *
 */
@ServerEndpoint("/websocket")
public class WebSocketServer  {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd  HH:mm:ss"); // 日期格式

	private List<Map> messageList=new ArrayList<Map>();
	
	@OnOpen
	public void open(Session session) {
	}

	/**
	 * 接受客户端的消息，并把消息发送给连接的会
	 *@param message 客户端发来的消息 * 
	 *@param session 客户端的会话
	 */
	@OnMessage
	public void getMessage(String message, Session session) {

		for (Session openSession : session.getOpenSessions()) {
			///System.out.println(message);
			 System.out.println(message.split(":")[0]);
	        //利用键主键的方式获取到消息体为昵称:内容  
			//message=message.split(":")[0]+ DATE_FORMAT.format(new Date())+"</br>"+message.split(":")[1];
			Map result=new HashMap<String, Object>();
			result.put("nickName", message.split(":")[0]);
			result.put("time",  DATE_FORMAT.format(new Date()));
			result.put("message", message.split(":")[1]);
			messageList.add(result);
			if(!openSession.equals(session)){
				openSession.getAsyncRemote().sendText(JsonKit.toJson(result, 2));
			}
			
		}
	}

	@OnClose
	public void close() {
		System.out.println("close....");
	}

	@OnError
	public void error(Throwable t) {
	}
	
	
	//"{nickName:"+nickName.value + ',message:' + textMessage.value+"}"
	 public static void main(String[] args) {  
		 String s = "{nickName:\"hello\",message:\"nihao\"}";  
		 JSONObject object = JSON.parseObject(s);  
        //利用键�?对的方式获取到�?  
        System.out.println(object.get("nickName"));  
    }

	
}