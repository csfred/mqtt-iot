package com.iot.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iot.entity.Constants;
import com.iot.entity.Device;
import com.iot.entity.DeviceInfo;
import com.iot.service.DeviceService;
import com.iot.utils.Md5Utils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
@Component
//spring 多例
@Scope("prototype")
public class BusinessThread implements Runnable {

    private DeviceService deviceService;

    private List<DeviceInfo> deviceInfoList = null;

    private String stationNo;

    private String msg;

    private Boolean isDisconnected;

    @Override
    public void run() {
        //业务操作
        JSONObject jsonObject = JSON.parseObject(msg);
        if (null == jsonObject || jsonObject.isEmpty()) {
            return;
        }
        //这里只检测下线，还有一种情况是没数据
        if (!isDisconnected) {
            long ret = deviceService.updateStationOnline(stationNo, false);
            //站点不存在或者修改失败
            if (ret <= 0) {
                return;
            }
        }
        jsonObject.put(Constants.STATION_NO_KEY, stationNo);
        if (jsonObject.containsKey(Constants.MSG_GATEWAY_SN_KEY)) {
            //站点信息
        }
        if (jsonObject.containsKey(Constants.MSG_DEV_LIST)) {
            //站点设备列表
        }
        if (jsonObject.containsKey(Constants.MSG_VAR_LIST)) {
            try {
                long ret = deviceService.updateStationOnline(stationNo, true);
                if (ret <= 0) {
                    return;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return;
            }

            deviceInfoList = deviceService.getDeviceInfoAll(stationNo);
            //设备信息
            saveDevice(jsonObject);
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
                boolean saveAble = deviceService.checkDeviceExist(stationNo, deviceInfo.getDevNo(), varListFieldsMd5) == 0L;
                if (saveAble) {
                    deviceService.saveDevice(device);
                } else {
                    deviceService.updateSameDeviceCounter(receiveTime, stationNo, varListFieldsMd5);
                }
            }
        }
    }
}
