package com.iot.mqtt;

import com.alibaba.fastjson.JSONObject;
import com.iot.common.ThreadPoolManager;
import com.iot.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * MQTT回调函数
 *
 * @author Mr.cs
 * @since 2020/11/18
 */
@Slf4j
@Component
public class InitCallback implements MqttCallback {

    @Resource
    private ThreadPoolManager threadPoolManager;


    public void _setMqttListener(MQTTListener mqttListener) {
        threadPoolManager.setMqttListener(mqttListener);
    }

    /**
     * MQTT 断开连接会执行此方法
     */
    @Override
    public void connectionLost(Throwable cause) {
        log.error(cause.getMessage(), cause);
    }

    /**
     * publish发布成功后会执行到这里
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    /**
     * subscribe订阅后得到的消息会执行到这里
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String msg = new String(message.getPayload());
        //log.error("topic = {}, msg={}", topic, msg);
        boolean isWrongTopic = !topic.startsWith(Constants.TOPIC_START_SYS_STR) ||
                !topic.endsWith(Constants.TOPIC_END_UP_STR);
        if (isWrongTopic) {
            return;
        }
        String stationNo = topic.substring(Constants.TOPIC_START_SYS_STR.length(),
                topic.indexOf(Constants.TOPIC_END_UP_STR));
        JSONObject jsonObject = JSONObject.parseObject(msg);
        if (null == jsonObject || jsonObject.isEmpty()) {
            return;
        }
        boolean isDisConnected = 2 == jsonObject.getInteger(Constants.MSG_CMD_ID);
        boolean isConnected = 1 == jsonObject.getInteger(Constants.MSG_CMD_ID);
        if (isDisConnected) {
            log.info("客户端已掉线：{}", stationNo);
            threadPoolManager.addDisConnectTask(stationNo);
        }
        if (isConnected) {
            log.info("客户端已上线：{}", stationNo);
        }
        //发送了真正的数据包，包含VARLIST
        if (!StringUtils.isEmpty(msg) && msg.contains(Constants.MSG_VAR_LIST)) {
            threadPoolManager.addSaveDbTask(stationNo, msg);
        }

    }
}
