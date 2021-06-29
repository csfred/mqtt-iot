package com.iot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.iot.entity.*;
import com.iot.mapper.DeviceMapper;
import com.iot.service.DeviceService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO
 *
 * @author cs
 * @since 2020/11/17 15:55
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;


    @Override
    public void saveStationInfo(StationInfo stationInfo) {
        deviceMapper.saveStationInfo(stationInfo);
    }

    @Override
    public void updateStationInfo(StationInfo stationInfo) {
        deviceMapper.updateStationInfo(stationInfo);
    }

    @Override
    public JSONObject getPageAllStationInfo(@RequestParam("page") Integer page,
                                            Integer pageSize,
                                            String stationName) {
        String stationNameLike = "%" + stationName + "%";
        Long total = deviceMapper.getPageAllStationInfoTotal(stationNameLike);
        if (page > 0) {
            page = page - 1;
        }
        List<StationInfo> retList = deviceMapper.getPageAllStationInfo(page, pageSize, stationNameLike);
        JSONObject retObject = new JSONObject(8);
        retObject.put("total", total);
        retObject.put("dataList", retList);
        return retObject;
    }

    @Override
    public void deleteStationInfo(String stationNo) {
        deviceMapper.deleteStationInfo(stationNo);
    }

    @Override
    public void saveDevice(Device param) {
        deviceMapper.saveDevice(param);
    }

    @Override
    public void saveDeviceInfo(DeviceInfo deviceInfo) {
        deviceMapper.saveDeviceInfo(deviceInfo);
    }

    @Override
    public List<DeviceInfo> getDeviceInfoByStationNo(String stationNo) {
        return deviceMapper.getDeviceInfoByStationNo(stationNo);
    }

    @Override
    public void updateDeviceInfo(DeviceInfo deviceInfo) {
        deviceMapper.updateDeviceInfo(deviceInfo);
    }


    @Override
    public void updateSameDeviceCounter(String endReceiveTime,
                                        String stationNo,
                                        String varListFieldsMd5) {

        deviceMapper.updateSameDeviceCounter(endReceiveTime, stationNo, varListFieldsMd5);
    }

    @Override
    public Long checkDeviceExist(String stationNo, String varListFieldsMd5) {
        return deviceMapper.checkDeviceExist(stationNo, varListFieldsMd5);
    }


    @Override
    public List<DeviceInfo> getDeviceInfoAll(@Param("stationNo") String stationNo) {
        return deviceMapper.getDeviceInfoAll(stationNo);
    }

    @Override
    public List<DeviceType> getAllDeviceType() {
        return deviceMapper.getAllDeviceType();
    }

    @Override
    public Device getDeviceLiveData(String stationNo, Long devNo) {
        DeviceLiveData deviceLiveData = new DeviceLiveData();
        deviceLiveData.setDevNo(devNo);
        deviceLiveData.setStationNo(stationNo);
        LocalDateTime queryTime = LocalDateTime.now().minusSeconds(20);
        deviceLiveData.setQueryTime(Timestamp.valueOf(queryTime));

        return deviceMapper.getDeviceLiveData(deviceLiveData);
    }
}
