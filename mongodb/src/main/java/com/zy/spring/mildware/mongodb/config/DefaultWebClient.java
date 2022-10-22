package com.zy.spring.mildware.mongodb.config;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.core.eventbus.EventBus;
import io.vertx.rxjava3.core.eventbus.MessageConsumer;
import io.vertx.rxjava3.ext.web.client.WebClient;

public final class DefaultWebClient {
    private static final int CONNECTION_TIMEOUT_IN_MILLS = 60_000;
    private static final int IDLE_TIMEOUT_MILLS = 60_000;
    private static final int MAX_TOTAL_CONNECTION = 200;
    private static final WebClient WEB_CLIENT;
    private static final WebClientOptions OPTIONS;
    private static final EventBus EVENT_BUS;

    static {
        OPTIONS = new WebClientOptions()
                .setMaxPoolSize(MAX_TOTAL_CONNECTION)
                .setConnectTimeout(CONNECTION_TIMEOUT_IN_MILLS)
                //.setSsl(false)
                // FIXME 这里信任所有主机
                .setTrustAll(true)
                .setIdleTimeout(IDLE_TIMEOUT_MILLS)
                .setKeepAlive(true);
        Vertx vertx = Vertx.vertx();
        WEB_CLIENT = WebClient.create(vertx, OPTIONS);
        EVENT_BUS = vertx.eventBus();
    }

    public static WebClient getInstance() {
        return WEB_CLIENT;
    }

    public EventBus publish(String address, Object message, DeliveryOptions options) {
        return EVENT_BUS.publish(address, message, options);
    }

    public <T> MessageConsumer<T> consumer(String address) {
        return EVENT_BUS.consumer(address);
    }

}
