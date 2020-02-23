package com.gaocy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSONObject;

public class CommonUtils {
	
	public static final String TARGET_URL = "https://report.tingyun.com/server/application/selectFilter/webAction.json";
	
	public static Map<String,String> getResultMap(HttpServletRequest req) throws IOException {
		Map<String,String> resultMap=new HashMap<String,String>();
		String applicationId = req.getParameter("applicationId");
		String instanceId = req.getParameter("instanceId");
		String timeType = req.getParameter("timeType");
		String endTime = req.getParameter("endTime");
		String timePeriod = req.getParameter("timePeriod");
		String baseonIdListStr = req.getParameter("baseonIdList");
		String cookie = req.getParameter("cookie");
	
		List<String> baseonIdList = JSONObject.parseArray(baseonIdListStr,String.class);
	
		if(baseonIdList!=null&&baseonIdList.size()>0) {
			for(int i=0;i<baseonIdList.size();i++) {
				String baseonId=baseonIdList.get(i);
				
				URL url = new URL(TARGET_URL);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setDoOutput(true);// 允许写出
				urlConnection.setDoInput(true);// 允许读入
				urlConnection.setRequestMethod("POST");
				// 设置超时
				urlConnection.setConnectTimeout(5000);
				urlConnection.setReadTimeout(5000);
				// 设置请求需要返回的数据类型和字符集类型
				urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				urlConnection.setRequestProperty("Connection", "close");
				urlConnection.setRequestProperty("Cookie", cookie);

				StringBuffer params = new StringBuffer();
				// 表单参数与get形式一样
				params.append("applicationId").append("=").append(applicationId).append("&").append("instanceId")
						.append("=").append(instanceId).append("&").append("timeType").append("=").append(timeType)
						.append("&").append("endTime").append("=").append(endTime).append("&").append("timePeriod")
						.append("=").append(timePeriod).append("&").append("baseonId").append("=").append(baseonId);
				byte[] bypes = params.toString().getBytes();
				urlConnection.getOutputStream().write(bypes);// 输入参数
				urlConnection.getOutputStream().flush();
				urlConnection.getOutputStream().close();
				// 获取返回结果
				InputStream inputStream = urlConnection.getInputStream();
				String bodyInfo = IOUtils.toString(inputStream, "utf-8");
				resultMap.put(baseonId, bodyInfo);
				// 关闭资源
				inputStream.close();
				urlConnection.disconnect();
			}
		}
		return resultMap;
	}

}
