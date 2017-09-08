package com.hxgy.pushload;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hxgy.pushload.entity.Message;
import com.hxgy.pushload.entity.MessageInfo;
import com.hxgy.pushload.service.PushService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HxgyPushLoadApplicationTests {

	@Autowired
	private PushService pushService;
	
	@Test
	public void contextLoads() {
    	Message message = new Message();
    	message.setFrom("系统消息");
    	message.setTo("所有人");
    	message.setPushTime(System.currentTimeMillis());
    	message.setType(Message.Type.SYSTEM_MSG);
    	MessageInfo msg = new MessageInfo();
    	msg.setSubject("333333");
    	msg.setContent("33333");
    	msg.setTime(System.currentTimeMillis()+"");
    	message.setMessageInfo(msg);
    	
    	pushService.pushBroadcastMsg(message);
	}

}
