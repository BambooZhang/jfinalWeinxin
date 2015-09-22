package cn.bamboo.weixin.interceptor;

import cn.bamboo.weixin.util.SignUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

/************
 * *
*Title: jfinal+微信处理拦截器
*Description: 微信拦截器验证请求来源
*Copyright: Copyright (c) 2015
*Company: 
*Makedate:2015年9月21日 上午9:56:08
* @author zjcjava@163.com 
* @version 1.0 
* @since 1.0 
*
 */
public class CoreFilterInterceptor implements Interceptor {
 
    // 验证请求来源拦截器
    public void intercept(ActionInvocation ai) {
        Controller c = ai.getController();
        String signature = c.getPara("signature");
        // 时间戳
        String timestamp = c.getPara("timestamp");
        // 随机数
        String nonce = c.getPara("nonce");
        // 随机字符串
        String echostr = c.getPara("echostr");
 
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            c.renderText(echostr);
            ai.invoke();
        }
    }
 
}
