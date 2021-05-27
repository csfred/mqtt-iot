package com.lot.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lot.device.entity.Device;
import com.lot.device.entity.DeviceExt;
import com.lot.device.entity.DeviceExt2;
import com.lot.device.service.DeviceService;
import com.lot.utils.Md5Utils;
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

        String originStringBuilder = String.valueOf(device.getCmdId()) + device.getDevId() +
                device.getDevNo() + device.getType() +
                device.getVersion();
        String deviceMd5 = Md5Utils.md5(originStringBuilder);
        device.setMd5(deviceMd5);

        String varList = jsonObject.getString("varList");
        DeviceExt deviceExt = new DeviceExt();
        String varListMd5 = Md5Utils.md5(varList);
        deviceExt.setDeviceMd5(deviceMd5);
        deviceExt.setVarListMd5(varListMd5);
        deviceExt.setVarList(varList);

        DeviceExt2 deviceExt2 = new DeviceExt2();
        deviceExt2.setVarListMd5(varListMd5);
        deviceExt2.setReceiveTime(Timestamp.valueOf(jsonObject.getString("time")));

        log.info("DEVICE MSG={}", jsonObject.toJSONString());

        if (deviceService.checkDeviceMd5Exist(deviceMd5) == 0L) {
            deviceService.saveDevice(device);
        }
        if (deviceService.checkVarListMd5Exist(varListMd5) == 0L) {
            deviceService.saveDeviceExt(deviceExt);
        }

        deviceService.saveDeviceExt2(deviceExt2);
    }
}
