package com.tencent.xinge;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Test;

public class XingeAppAndroidUnitTest extends XingeApp {
	
	public XingeAppAndroidUnitTest()
	{
		super(2100001932, "ebaf10ce868fec205efc0706e8bc5312");
	}

	@Test
	public void testCallRestful() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("key1", 2);
		JSONObject json = this.callRestful("http://10.1.152.139/v1/push/single_device", params);
		assertEquals("{\"err_msg\":\"common param error!\",\"ret_code\":-1}", json.toString());
		json = this.callRestful("http://10.1.152.139/v1/push/single_", params);
		assertEquals("{\"err_msg\":\"call restful error\",\"ret_code\":-1}", json.toString());
		json = this.callRestful("abc", params);
		assertEquals("{\"err_msg\":\"generateSign error\",\"ret_code\":-1}", json.toString());
	}

	@Test
    public void testQueryApi(){
        JSONObject response = queryTags();
        System.out.println(response);
        assertEquals(0, response.getInt("ret_code"));

        response = queryTagTokenNum("456");
        System.out.println(response);
        assertEquals(0, response.getInt("ret_code"));

        response = queryDeviceCount();
        System.out.println(response);
        assertEquals(0, response.getInt("ret_code"));
    }

	@Test
	public void testPushApi() {
        JSONObject response;
        Message message = new Message();
        message.setTitle("title");
        message.setContent("content");
        message.setType(Message.TYPE_MESSAGE);
        message.setExpireTime(86400);

        response = pushSingleDevice("2a060d364fd921ec4fd8a1aaf4ea25f638f5f55f", message);
        System.out.println(response);
        assertEquals(40, response.getInt("ret_code"));

        List<String> tags = new ArrayList<String>();
        tags.add("123");
        response = pushTags(0, tags, "OR", message);
        System.out.println(response);
        assertEquals(0, response.getInt("ret_code"));
        Long pushTagsId = response.getJSONObject("result").getLong("push_id");

        response = pushAllDevice(0, message);
        System.out.println(response);
        assertEquals(0, response.getInt("ret_code"));
        Long pushAllId = response.getJSONObject("result").getLong("push_id");

        List<String> pushIdList = new ArrayList<String>();
        pushIdList.add(pushTagsId.toString());
        pushIdList.add(pushAllId.toString());
        System.out.println(queryPushStatus(pushIdList));
        assertEquals(0, response.getInt("ret_code"));
	}

}
