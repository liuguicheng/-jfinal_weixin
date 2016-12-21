
package com.jfinal.qy.weixin.sdk.api;

import com.jfinal.qy.weixin.sdk.utils.HttpUtils;

public class OAuthApi {
//	private static String getCodeUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
	private static String getCodeUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
//	private static String getUserInfoUrl="https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE";
	private static String getUserInfoUrl="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	
//	private static String getUserAccessToken="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	/**
	 * userid转换成openid接口
	 */
	private static String toOpenIdUrl="https://qyapi.weixin.qq.com/cgi-bin/user/convert_to_openid?access_token=ACCESS_TOKEN";
	/**
	 * openid转换成userid接口
	 */
//	private static String toUserIdUrl="https://qyapi.weixin.qq.com/cgi-bin/user/convert_to_userid?access_token=ACCESS_TOKEN";
	private static String toUserIdUrl="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	/**
	 * 获取用户授权codeUrl
	 * @param redirectUri
	 * @param state
	 * @return
	 */
	public static String getCodeUrl(String redirectUri,String state){
		getCodeUrl=getCodeUrl.replace("APPID", ApiConfigKit.getApiConfig().getCorpId())
				.replace("REDIRECT_URI", redirectUri).replace("SCOPE", "snsapi_base");
		if (state!=null && !state.equals("")) {
			getCodeUrl=getCodeUrl.replace("STATE", state);
		}else {
			getCodeUrl=getCodeUrl.replace("&state=STATE", "");
		}
				
		return getCodeUrl;
	}
	/**
	 * 获取code
	 */
	public static ApiResult getUserCodeBy(String url){
		String jsonResult = HttpUtils.get(url);
		System.out.println(">>>>getUserCodeBy==json=>>"+jsonResult);
		return new ApiResult(jsonResult);
	}
	/**
	 * 获取网页授权access_token
	 * @param code
	 * @return
	 */
	public static ApiResult getUserAccessTokenByCode(String code){
		getUserInfoUrl=getUserInfoUrl.replace("APPID",  ApiConfigKit.getApiConfig().getCorpId()).replace("SECRET", ApiConfigKit.getApiConfig().getCorpSecret())
				.replace("CODE", code);
		String jsonResult = HttpUtils.get(getUserInfoUrl);
		System.out.println(">>>>getUserAccessTokenByCode==json=>>"+jsonResult);
		return new ApiResult(jsonResult);
	}
	/**
	 * 根据code获取成员信息
	 * @param code
	 * @return
	 */
	public static ApiResult getUserInfoByCode(String code){
		getUserInfoUrl=getUserInfoUrl.replace("ACCESS_TOKEN", AccessTokenApi.getAccessTokenStr())
				.replace("CODE", code);
		String jsonResult = HttpUtils.get(getUserInfoUrl);
		return new ApiResult(jsonResult);
	}
	/**
	 * userid转换成openid接口
	 * @param data
	 *  {<br/>
		   "userid": "zhangsan",<br/>
		   "agentid": 1<br/>
		}<br/>
	 * @return
	 */
	public static ApiResult ToOpenId(String data){
		toOpenIdUrl=toOpenIdUrl.replace("ACCESS_TOKEN", AccessTokenApi.getAccessTokenStr());
		String jsonResult=HttpUtils.post(toOpenIdUrl, data);
		return new ApiResult(jsonResult);
	}
	/**
	 * 
	 * openid转换成userid接口
	 * @param data
	 {
   		"openid": "oDOGms-6yCnGrRovBj2yHij5JL6E"
	 }
	 * @param webaccesstoken 
	 * @return
	 */
	public static ApiResult ToUserId(String data, String webaccesstoken){
		toUserIdUrl=toUserIdUrl.replace("ACCESS_TOKEN", webaccesstoken);
		String jsonResult=HttpUtils.post(toUserIdUrl, data);
		return new ApiResult(jsonResult);
	}
}
