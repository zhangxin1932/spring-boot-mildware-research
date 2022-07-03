package com.zy.spring.mildware.misc.config;

import com.zy.spring.mildware.misc.controller.UserController;
import org.springframework.context.ApplicationListener;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.stereotype.Component;

@Component
public class SessionDeleteEventListener implements ApplicationListener<SessionDeletedEvent> {
    @Override
    public void onApplicationEvent(SessionDeletedEvent event) {
        System.out.println("--------------------------------");
        Object user = event.getSession().getAttribute(UserController.USER_ATTRIBUTE_NAME);
        System.out.println("SessionDeletedEvent, user is: " + user);
        System.out.println("--------------------------------");
    }
}
