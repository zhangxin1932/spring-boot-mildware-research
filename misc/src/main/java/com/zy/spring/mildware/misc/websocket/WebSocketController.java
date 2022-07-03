package com.zy.spring.mildware.misc.websocket;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * https://www.cnblogs.com/dand/p/10319031.html
 * https://blog.csdn.net/moshowgame/article/details/80275084
 * https://www.cnblogs.com/xuwenjin/p/12664650.html
 * https://www.jianshu.com/p/2c9be4641d43
 * http://www.mydlq.club/article/86/
 */
@RestController
@RequestMapping("/api/ws")
public class WebSocketController {

    /**
     * 群发消息内容
     * @param message
     * @return
     */
    @RequestMapping(value="/sendAll")
    public String sendAllMessage(@RequestParam String message){
        try {
            WebSocketServer.broadCastInfo(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    /**
     * 指定会话ID发消息
     * @param message 消息内容
     * @param id 连接会话ID
     * @return
     */
    @RequestMapping(value="/sendOne")
    public String sendOneMessage(@RequestParam String message,@RequestParam String id){
        try {
            WebSocketServer.sendMessage(message,id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }

}
