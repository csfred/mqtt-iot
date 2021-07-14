package com.iot.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.apache.commons.io.FileUtils;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 项目启动 监听主题
 *
 * @author Mr.Qu
 * @since 2020/11/18 0018
 */
@Slf4j
@Component
public class MQTTListener implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${mqtt.username}")
    private String username;
    @Value("${mqtt.password}")
    private String password;
    @Value("${mqtt.resource.topic}")
    private String mqttTopicIni;

    private final MQTTConnect server;
    private final InitCallback initCallback;

    @Autowired
    public MQTTListener(MQTTConnect server, InitCallback initCallback) {
        this.server = server;
        this.initCallback = initCallback;
        this.initCallback._setMqttLinsrener(this);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            server.setMqttClient(username, password, initCallback);
            List<String> topicLines = FileUtils.readLines(new File(mqttTopicIni));
            if (!CollectionUtils.isEmpty(topicLines)) {
                for (String topic : topicLines) {
                    server.sub(topic);
                }
            }
        } catch (MqttException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void pubTopic(String topic) {
        try {
            server.sub(topic);
        } catch (MqttException e) {
            log.error(e.getMessage(), e);
        }
    }
}


