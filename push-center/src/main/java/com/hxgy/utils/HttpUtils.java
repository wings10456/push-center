package com.hxgy.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * HTTP 工具类
 * @author ljf
 *
 */
public final class HttpUtils {

	private static int DEFAULT_CONNECTIONTIME=2000;//默认连接超时时间
	private static int DEFAULT_READTIME=5000;//默认读取超时时间

	/**
	 * @param serviceUrl http地址
	 * @return
	 */
	public static String doPost(String serviceUrl,String body){
		
		 try {
				URL url = new URL(serviceUrl);
				//打开restful链接
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				// 提交模式
				conn.setRequestMethod("POST");//POST GET PUT DELETE
			 	conn.setRequestProperty("Content-Type","application/json");
				//设置访问提交模式，表单提交
				conn.setConnectTimeout(DEFAULT_CONNECTIONTIME);//连接超时 单位毫秒
				conn.setReadTimeout(DEFAULT_READTIME);//读取超时 单位毫秒
				conn.setDoOutput(true);// 是否输入参数
			 	conn.getOutputStream().write(body.getBytes("UTF-8"));
				//读取请求返回值
				InputStream inStream=conn.getInputStream();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int len = 0;
				byte[] buf = new byte[1024];
				while((len=inStream.read(buf))!=-1){
					out.write(buf, 0, len);
				}
			 return out.toString("UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
	}
}
