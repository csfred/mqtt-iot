package com.iot.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.iot.entity.Constants;
import com.iot.entity.Device;
import com.iot.entity.DeviceInfo;
import com.iot.service.DeviceService;
import com.iot.utils.Md5Utils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

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

    private List<DeviceInfo> deviceInfoUpdateList = null;

    @Override
    public void run() {
        if (!CollectionUtils.isEmpty(deviceInfoUpdateList) && null != deviceService) {
            String updateErrorNo = deviceService.batchUpdateDeviceInfoThread(deviceInfoUpdateList);
            if (!StringUtils.isEmpty(updateErrorNo)) {
                updateErrorNo = updateErrorNo.substring(0, updateErrorNo.length() - 1);
                log.error("部分更新失败 失败编号={}", updateErrorNo);
            }
            return;
        }
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
            String varListJson = jsonObject.getString(Constants.MSG_VAR_LIST);
            Map<String, Boolean> paraMap = new HashMap<>(4);
            List<String> allFieldList = getAllVarFieldList(varListJson, paraMap);
            boolean isAllNullVarList;
            if (CollectionUtils.isEmpty(allFieldList)) {
                isAllNullVarList = true;
            } else {
                isAllNullVarList = paraMap.get("IS_ALL_NULL_VALUE");
            }
            if (isAllNullVarList) {
                deviceService.updateStationOnline(stationNo, false);
            } else {
                deviceInfoList = deviceService.getDeviceInfoAll(stationNo);
                //设备信息
                saveDevice(jsonObject);
            }
        }
    }

    private List<String> getAllVarFieldList(String varListJson, Map<String, Boolean> retPramMap) {
        List<String> allFieldList = new ArrayList<>(8);
        JSONObject varListObject = JSON.parseObject(varListJson);
        boolean isAllValueIsNull = true;
        if (null != varListObject && !varListObject.isEmpty()) {
            for (Map.Entry entry : varListObject.entrySet()) {
                if (null != entry.getKey()) {
                    String field = entry.getKey().toString().trim();
                    if (!allFieldList.contains(field)) {
                        allFieldList.add(field);
                    }
                }
                if (isAllValueIsNull) {
                    isAllValueIsNull = null == entry.getValue() || Constants.NULL_STR.equalsIgnoreCase(entry.getValue().toString());
                }
            }
        }
        if (null != retPramMap) {
            retPramMap.put("IS_ALL_NULL_VALUE", isAllValueIsNull);
        }
        return allFieldList;
    }

    public static void main(String[] args) {
//        List<String> deviceAllVarKeys = Arrays.asList("变量1", "变量4", "var1", "var6");
//        Collections.sort(deviceAllVarKeys);
//        List<String> lastDeviceAllVarKeys = Arrays.asList("var1", "变量4", "变量10", "var6");
//        Collections.sort(lastDeviceAllVarKeys);
//        if (deviceAllVarKeys.equals(lastDeviceAllVarKeys)) {
//            System.out.println("yes");
//        } else {
//            System.out.println("no");
//        }
        Map<String, Object> testMap = new HashMap<>(8);
        testMap.put("aaa", 110);
        testMap.put("bbb", 112);
        testMap.put("ccc", null);
        testMap.put("ddd", 114);
        System.out.println(JSON.toJSONString(testMap, SerializerFeature.WriteMapNullValue));
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
        JSONObject varListObjectInit = new JSONObject(8);
        for (Map.Entry<String, Object> entry : varListObject.entrySet()) {
            varListObjectInit.put(entry.getKey().trim(), entry.getValue());
        }
        List<String> allFieldList = getAllVarFieldList(varListJson, null);
        if (CollectionUtils.isEmpty(allFieldList)) {
            return;
        }
        if (!allFieldList.contains(Constants.EMPTY_STR)) {
            allFieldList.add(Constants.EMPTY_STR);
        }
        for (DeviceInfo deviceInfo : deviceInfoList) {
            List<String> deviceAllVarKeys = new ArrayList<>(16);
            List<String> deviceInfoFieldList = JSON.parseArray(deviceInfo.getDevVarFields(), String.class);
            Map<String, Object> varList4FieldObject = new HashMap<>(8);
            boolean isAllNullData = true;
            for (String key : deviceInfoFieldList) {
                Object filedVal = varListObjectInit.get(key);
                if (isAllNullData) {
                    isAllNullData = (null == filedVal || Constants.NULL_STR.equalsIgnoreCase(filedVal.toString()));
                }
                if (null != filedVal) {
                    varList4FieldObject.put(key, filedVal);
                } else {
                    varList4FieldObject.put(key, null);
                }
                deviceAllVarKeys.add(key);
            }
            if (isAllNullData) {
                continue;
            }
            String receiveTime = jsonObject.getString(Constants.MSG_TIME);
            String varList4Fields = JSON.toJSONString(varList4FieldObject, SerializerFeature.WriteMapNullValue);
            String varListFieldsMd5 = Md5Utils.md5(varList4Fields);
            device.setDevNo(deviceInfo.getDevNo());
            device.setVarListFields(varList4Fields);
            device.setVarListFieldsMd5(varListFieldsMd5);
            device.setEndReceiveTime(receiveTime);
            device.setStartReceiveTime(receiveTime);

            if (Constants.EMPTY_JSON_OBJECT.equalsIgnoreCase(varList4Fields) ||
                    StringUtils.isEmpty(varListFieldsMd5) ||
                    Constants.NULL_STR.equalsIgnoreCase(varListFieldsMd5)) {
                continue;
            }
            boolean notSaveAble = deviceService.checkDeviceExist(stationNo, deviceInfo.getDevNo(), varListFieldsMd5) > 0L;
            if (notSaveAble) {
                deviceService.updateSameDeviceCounter(receiveTime, stationNo, varListFieldsMd5);
            } else {
                Device lastDevice = deviceService.getDeviceLiveData(stationNo, deviceInfo.getDevNo(), true);
                if (null == lastDevice) {
                    deviceService.saveDevice(device);
                } else {
                    String lastVarList4Fields = lastDevice.getVarListFields();
                    JSONObject lastVarList4FieldsObject = JSON.parseObject(lastVarList4Fields);
                    Collections.sort(deviceAllVarKeys);
                    List<String> lastDeviceAllVarKeys = new ArrayList<String>(lastVarList4FieldsObject.keySet());
                    Collections.sort(lastDeviceAllVarKeys);
                    boolean isEqual = deviceAllVarKeys.equals(lastDeviceAllVarKeys);
                    if (isEqual) {
                        deviceService.saveDevice(device);
                    } else {
                        deviceService.updateToHistory(deviceInfo.getDevNo());
                    }
                }
            }
        }
    }
}
