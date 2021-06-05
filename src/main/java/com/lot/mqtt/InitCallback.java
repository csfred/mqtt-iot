package com.lot.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lot.entity.Constants;
import com.lot.entity.Device;
import com.lot.entity.DeviceExt;
import com.lot.entity.DeviceExt2;
import com.lot.service.DeviceService;
import com.lot.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

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
        try {
            String msg = new String(message.getPayload());
            boolean isWrongTopic = !topic.startsWith(Constants.TOPIC_START_SYS_STR) ||
                    !topic.endsWith(Constants.TOPIC_END_UP_STR);
            if (isWrongTopic) {
                return;
            }
            String stationNo = topic.substring(Constants.TOPIC_START_SYS_STR.length(),
                    topic.indexOf(Constants.TOPIC_END_UP_STR));
            JSONObject jsonObject = JSON.parseObject(msg);
            if (null == jsonObject || jsonObject.isEmpty()) {
                return;
            }
            jsonObject.put(Constants.STATION_NO_KEY, stationNo);
            if (jsonObject.containsKey(Constants.MSG_GATEWAY_SN_KEY)) {
                //站点信息
            }
            if (jsonObject.containsKey(Constants.MSG_DEV_LIST)) {
                //站点设备列表
            }
            if (jsonObject.containsKey(Constants.MSG_VAR_LIST)) {
                //设备信息
                saveDevice(jsonObject);
            }
            String clientId = String.valueOf(jsonObject.get(Constants.MSG_CLIENT_ID));
            if (!StringUtils.isEmpty(clientId) && !Constants.NULL_STR.equals(clientId)) {
                if (topic.endsWith(Constants.TOPIC_END_DISCONNECT_STR)) {
                    log.info("客户端已掉线：{}", clientId);
                } else {
                    log.info("客户端已上线：{}", clientId);
                }
            }

        } catch (JSONException e) {
            log.error("JSON Format Parsing Exception : {}", e.getMessage());
        }
    }

    private void saveDevice(JSONObject jsonObject) {
        Device device = new Device();
        String stationNo = jsonObject.getString(Constants.STATION_NO_KEY);
        device.setCmdId(jsonObject.getInteger(Constants.MSG_CMD_ID));
        device.setDevId(jsonObject.getInteger(Constants.MSG_DEV_ID));
        device.setDevNo(jsonObject.getInteger(Constants.MSG_DEV_NO));
        device.setType(jsonObject.getInteger(Constants.MSG_TYPE));
        device.setVersion(jsonObject.getString(Constants.MSG_VERSION));
        device.setStationNo(stationNo);

        String originStringBuilder = String.valueOf(device.getCmdId()) +
                device.getDevId() + device.getDevNo() +
                device.getType() + device.getVersion();
        String deviceMd5 = Md5Utils.md5(originStringBuilder);
        device.setMd5(deviceMd5);

        String varList = jsonObject.getString(Constants.MSG_VAR_LIST);
        DeviceExt deviceExt = new DeviceExt();
        String varListMd5 = Md5Utils.md5(varList);
        List<String> fieldList = new ArrayList<>(8);
        JSONObject varListObject = JSON.parseObject(varList);
        if (null != varListObject && !varListObject.isEmpty()) {
            for (Map.Entry entry : varListObject.entrySet()) {
                if (null != entry.getKey()) {
                    String field = entry.getKey().toString();
                    if (!fieldList.contains(field)) {
                        fieldList.add(field);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(fieldList)) {
                Collections.sort(fieldList);
            }
        }
        String varFieldList = JSON.toJSONString(fieldList);
        deviceExt.setVarFieldsMd5(Md5Utils.md5(varFieldList));
        deviceExt.setDeviceMd5(deviceMd5);
        deviceExt.setVarListMd5(varListMd5);
        deviceExt.setVarList(varList);
        deviceExt.setVarFields(varFieldList);

        DeviceExt2 deviceExt2 = new DeviceExt2();
        deviceExt2.setVarListMd5(varListMd5);
        deviceExt2.setReceiveTime(Timestamp.valueOf(
                jsonObject.getString(Constants.MSG_TIME)));

        if (deviceService.checkDeviceMd5Exist(deviceMd5, stationNo) == 0L) {
            deviceService.saveDevice(device);
        }
        if (deviceService.checkVarListMd5Exist(varListMd5) == 0L) {
            deviceService.saveDeviceExt(deviceExt);
        } else {
            deviceService.updateDeviceExt(deviceExt);
        }

        deviceService.saveDeviceExt2(deviceExt2);
    }
}
