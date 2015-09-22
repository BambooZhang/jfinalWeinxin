package cn.bamboo.view.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.bamboo.websocket.WebSocketServer;
import cn.bamboo.weixin.interceptor.CoreFilterInterceptor;
import cn.bamboo.weixin.service.CoreService;

import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;

public class ViewController extends Controller {

	public void index(){///首页根目录默认执行方法
		this.render("/index.html");
	}

	public void chart(){//访问其他路径或者方法名称
		String userName = this.getAttr("userName");
		String sayHello = "Hello " + userName + "，welcome to JFinal world.";
		this.setAttr("sayHello", sayHello);
		this.render("/chat.html");
	}
	
	@ActionKey("/add")
	public void add(){//测试调用方法
		String userName =getPara("userName");
		String sayHello = "Hello " + userName + "，welcome to JFinal world.";
		this.setAttr("sayHello", sayHello);
		this.render("/weChat.jsp");//forwardAction("/hancity");
	}
	
	//微信核心-通过拦截器处理请求
    @Before(CoreFilterInterceptor.class)
    public void coreFilter() {
        // 调用核心业务类接收消息、处理消息 // 处理微信服务器发来的消息  
        String respMessage=CoreService.processRequest(getRequest());  
           
        // 响应消息  
        renderText(respMessage);  
    }
	
    
    /** 
     * 解析微信发来的请求（XML） 
     *  
     * @param request 
     * @return 
     * @throws Exception 
     */  
    @SuppressWarnings("unchecked")  
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {  
        // 将解析结果存储在HashMap中  
        Map<String, String> map = new HashMap<String, String>();  
      
        // 从request中取得输入流  
        InputStream inputStream = request.getInputStream();  
        // 读取输入流  
        SAXReader reader = new SAXReader();  
        Document document = reader.read(inputStream);  
        // 得到xml根元素  
        Element root = document.getRootElement();  
        // 得到根元素的所有子节点  
        List<Element> elementList = root.elements();  
      
        // 遍历所有子节点  
        for (Element e : elementList)  
            map.put(e.getName(), e.getText());  
      
        // 释放资源  
        inputStream.close();  
        inputStream = null;  
      
        return map;  
    }   
   
}
