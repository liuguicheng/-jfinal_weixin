package com.javen.demo.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.qy.weixin.sdk.api.ApiConfig;
import com.jfinal.qy.weixin.sdk.api.ApiResult;
import com.jfinal.qy.weixin.sdk.api.OAuthApi;
import com.jfinal.qy.weixin.sdk.jfinal.ApiController;
public class QyOAuthController extends ApiController {
	private Log logger=Log. getLog(QyOAuthController.class);
	/**
	 * 如果要支持多公众账号，只需要在此返回各个公众号对应的  ApiConfig 对象即可
	 * 可以通过在请求 url 中挂参数来动态从数据库中获取 ApiConfig 属性值
	 */
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();
		
		// 配置微信 API 相关常量
		ac.setToken(PropKit.get("token"));
		ac.setCorpId(PropKit.get("corpId"));
		ac.setCorpSecret(PropKit.get("secret"));
				
		
		/**
		 *  是否对消息进行加密，对应于微信平台的消息加解密方式：
		 *  1：true进行加密且必须配置 encodingAesKey
		 *  2：false采用明文模式，同时也支持混合模式
		 */
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
		return ac;
	}
	public void index(){
		try {
			String redirect_uri=URLEncoder.encode("http://wlweixin.com.ngrok.cc/jfinal_qyweixin/qyoauth2/code", "utf-8");
			String codeUrl = OAuthApi.getCodeUrl(redirect_uri, "123");
			redirect(codeUrl);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void code(){
		String openid=null;
		String nickname=null;
		String headimgurl=null;
		String webaccesstoken=null;
		if (!isParaBlank("code")) {
			String code = getPara("code");
			System.out.println(">>>code:"+code);
			logger.info("code:"+code);
			if (!isParaBlank("state")) {
				String state = getPara("state");
				logger.info(" state:"+state);
				System.out.println(" state:"+state);
			}
			ApiResult userInfoApiResult = OAuthApi.getUserAccessTokenByCode(code);
			if (userInfoApiResult.isSucceed()) {
				String userInfoJson=userInfoApiResult.getJson();
				JSONObject object = JSON.parseObject(userInfoJson);
				try {
					openid = object.getString("openid");
					webaccesstoken=object.getString("access_token");
					System.out.println(">>>webaccesstoken:"+webaccesstoken);
					System.out.println(">>>openid:"+openid);
//					//如果获取userId为空 说明没有关注
//						openid = object.getString("openid");
//						//如果未关注 openid无法转化为userid
//						ApiResult toUserIdApiResult = OAuthApi.ToUserId(openid,webaccesstoken);
//						System.out.println(">>>toUserIdApiResult:"+toUserIdApiResult.getJson());
//						if (toUserIdApiResult.isSucceed()) {
//							nickname=JSON.parseObject(toUserIdApiResult.getJson()).getString("nickname");
//							headimgurl=JSON.parseObject(toUserIdApiResult.getJson()).getString("headimgurl");
//						}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			renderText(userInfoApiResult.getJson()+">>>openid:"+openid+" nickname:"+nickname+" headimgurl:"+headimgurl);
		}
	}
}
