package com.iot.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.iot.device.entity.Device;
import com.iot.device.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * MQTT回调函数
 *
 * @author Mr.Qu
 * @since 2020/11/18
 */
@Slf4j
@Component
public class InitCallback implements MqttCallback {

    @Resource
    private DeviceService deviceService;

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
        //log.info("[{}] : {}", topic, new String(message.getPayload()));
        try {
            String msg = new String(message.getPayload());
            JSONObject jsonObject = JSON.parseObject(msg);
            if (null == jsonObject || jsonObject.isEmpty()) {
                return;
            }
            if (jsonObject.containsKey("gwSn")) {
                //站点信息
            }
            if (jsonObject.containsKey("devList")) {
                //站点设备列表
            }
            if (jsonObject.containsKey("varList")) {
                //设备信息
                saveDevice(jsonObject);
            }
            String clientId = String.valueOf(jsonObject.get("clientid"));
            if (topic.endsWith("/disconnected")) {
                log.info("客户端已掉线：{}", clientId);
            } else {
                log.info("客户端已上线：{}", clientId);
            }
        } catch (JSONException e) {
            log.error("JSON Format Parsing Exception : {}", e.getMessage());
        }
    }

    private void saveDevice(JSONObject jsonObject) {
        Device device = new Device();
        device.setCmdId(jsonObject.getInteger("cmdId"));
        device.setDevId(jsonObject.getInteger("devId"));
        device.setDevNo(jsonObject.getInteger("devNo"));
        device.setType(jsonObject.getInteger("type"));
        device.setVersion(jsonObject.getString("ver"));
        device.setVarList(jsonObject.getString("varList"));
        device.setTime(Timestamp.valueOf(jsonObject.getString("time")));

        log.info("DEVICE MSG={}", jsonObject.toJSONString());

        deviceService.saveDevice(device);
    }
}
