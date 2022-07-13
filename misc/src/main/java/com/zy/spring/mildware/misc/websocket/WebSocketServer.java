package com.zy.spring.mildware.misc.websocket;

import com.alibaba.fastjson.JSON;
import com.zy.spring.mildware.misc.threadpool.dto.ThreadPoolExecutorDTO;
import com.zy.spring.mildware.misc.threadpool.service.ThreadPoolExecutorServiceImpl;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws/getAllThreadPoolExecutors")
@Component
@Slf4j
public class WebSocketServer {

    /**
     * 集群环境下, 可存放至 redis 中
     */
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    /**
     * 用来存放每个客户端对应的Session对象
     * 集群环境下, 可存放至 redis 中
     */
    private static final CopyOnWriteArraySet<Session> SESSION_SET = new CopyOnWriteArraySet<>();

    @Autowired
    private ThreadPoolExecutorServiceImpl threadPoolExecutorService;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        SESSION_SET.add(session);
        // 在线数加1
        int cnt = ONLINE_COUNT.incrementAndGet();
        log.info("有连接加入，当前连接数为：{}", cnt);
        sendMessage(session, "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SESSION_SET.remove(session);
        int cnt = ONLINE_COUNT.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息：{}",message);
        sendMessage(session, "收到消息，消息内容："+message);
    }

    /**
     * 出现错误
     * @param session 会话
     * @param error 异常
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
        error.printStackTrace();
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     * @param session 会话
     * @param message 消息
     */
    public static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     * @param message 消息
     * @throws IOException 异常
     */
    public static void broadCastInfo(String message) throws IOException {
        for (Session session : SESSION_SET) {
            if(session.isOpen()){
                sendMessage(session, message);
            }
        }
    }

    /**
     * 指定Session发送消息
     * @param sessionId 会话id
     * @param message 消息
     * @throws IOException 异常
     */
    public static void sendMessage(String message, String sessionId) throws IOException {
        Session session = null;
        for (Session s : SESSION_SET) {
            if(s.getId().equals(sessionId)){
                session = s;
                break;
            }
        }
        if(session!=null){
            sendMessage(session, message);
        }
        else{
            log.warn("没有找到你指定ID的会话：{}",sessionId);
        }
    }

    @PostConstruct
    public void init() {
        /*System.out.println("websocket 加载");

        // 这里启动定时任务, 主动向客户端推送消息
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("webSocketServer"));
        executor.scheduleWithFixedDelay(() -> {
            List<ThreadPoolExecutorDTO> list = threadPoolExecutorService.getAllThreadPoolExecutors();
            try {
                broadCastInfo(JSON.toJSONString(list));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 5L, 1L, TimeUnit.SECONDS);*/
    }

}
