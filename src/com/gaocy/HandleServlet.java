package com.gaocy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HandleServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4568412322810303448L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//保留的事务
		String actionListStr = req.getParameter("actionList");
		String[] actionList = actionListStr.split("\\n|\\r");
		
		
		Set<String> actionSet=new HashSet<String>();
		for(String s:actionList) {
			actionSet.add(s);
		}
		//
		Map<String, String> map = CommonUtils.getResultMap(req);

		//解析之后的返回结果Map
		Map<String, Map<String,String>> parseMap = new HashMap<String, Map<String,String>>();
		for(String s:actionList) {
			parseMap.put(s, new HashMap<String,String>());
		}
		

		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			//获取key-value
			String key = (String) it.next();
			if(key.trim().length()==0) {
				continue;
			}
			String value = map.get(key);
			
			JSONObject jsonObject = JSON.parseObject(value);
			List<Map> list = JSONArray.parseArray(jsonObject.getString("list"), Map.class);
			for(int i=0;i<list.size();i++) {
				String name=(String) list.get(i).get("name");
				if(actionSet.contains(name)) {
					String formatValue=(String) list.get(i).get("formatValue");
					parseMap.get(name).put(key, formatValue);
				}
			}
		}
		
		
		String returnJson=JSON.toJSONString(parseMap);
		System.out.println(returnJson);
		IOUtils.write(returnJson, resp.getOutputStream(), "utf-8");

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

}
