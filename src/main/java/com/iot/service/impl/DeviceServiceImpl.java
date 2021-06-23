package com.iot.service.impl;

import com.iot.entity.Device;
import com.iot.entity.DeviceInfo;
import com.iot.entity.StationInfo;
import com.iot.mapper.DeviceMapper;
import com.iot.service.DeviceService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public List<StationInfo> getStationInfoByLonLat(Double stationLon, Double stationLat) {
        return deviceMapper.getStationInfoByLonLat(stationLon, stationLat);
    }

    @Override
    public void saveDevice(Device param) {
        deviceMapper.saveDevice(param);
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
    public List<Device> getDeviceAll() {
        return deviceMapper.getDeviceAll();
    }
}
