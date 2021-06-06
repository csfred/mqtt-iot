package com.lot.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lot.entity.*;
import com.lot.service.DeviceService;
import com.lot.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;
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

    private List<DeviceInfo> deviceInfoList = null;

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
            deviceInfoList = deviceService.getDeviceInfoAll(stationNo);
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

    private List<String> getAllVarFieldList(String varListJson) {
        List<String> allFieldList = new ArrayList<>(8);
        JSONObject varListObject = JSON.parseObject(varListJson);
        if (null != varListObject && !varListObject.isEmpty()) {
            for (Map.Entry entry : varListObject.entrySet()) {
                if (null != entry.getKey()) {
                    String field = entry.getKey().toString();
                    if (!allFieldList.contains(field)) {
                        allFieldList.add(field);
                    }
                }
            }
        }
        return allFieldList;
    }

    private void saveDevice(JSONObject jsonObject) {
        Device device = new Device();
        String stationNo = jsonObject.getString(Constants.STATION_NO_KEY);
        device.setStationNo(stationNo);

        String varListJson = jsonObject.getString(Constants.MSG_VAR_LIST);
        JSONObject varListObject = JSON.parseObject(varListJson);
        if (null == varListObject || varListObject.isEmpty()) {
            return;
        }
        List<String> allFieldList = getAllVarFieldList(varListJson);
        for (DeviceInfo deviceInfo : deviceInfoList) {
            List<String> deviceInfoFieldList = JSON.parseArray(deviceInfo.getDevVarFields(), String.class);
            if (allFieldList.containsAll(deviceInfoFieldList)) {
                JSONObject varList4FieldObject = new JSONObject(8);
                for (String key : deviceInfoFieldList) {
                    if (null != varListObject.get(key)) {
                        varList4FieldObject.put(key, varListObject.get(key));
                    }
                }
                String receiveTime = jsonObject.getString(Constants.MSG_TIME);
                String varList4Fields = varList4FieldObject.toJSONString();
                String varListFieldsMd5 = Md5Utils.md5(varList4FieldObject.toJSONString());
                device.setDevNo(deviceInfo.getDevNo());
                device.setVarListFields(varList4Fields);
                device.setEndReceiveTime(Timestamp.valueOf(receiveTime));
                device.setStartReceiveTime(Timestamp.valueOf(receiveTime));
                boolean saveAble = deviceService.checkDeviceExist(stationNo, varListFieldsMd5) == 0L;
                if (saveAble) {
                    deviceService.saveDevice(device);
                } else {
                    deviceService.updateSameDeviceCounter(receiveTime, stationNo, varListFieldsMd5);
                }
            }
        }
    }

    public static void main(String[] args) {
        String testStr = "[\"调节池液位\"]";
        List<String> testList = JSON.parseArray(testStr, String.class);
        Collections.sort(testList);
        System.out.println(JSON.toJSONString(testList));
        System.out.println(Md5Utils.md5(JSON.toJSONString(testList)));
    }
}
