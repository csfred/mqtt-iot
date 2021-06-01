package com.lot.device.service.impl;

import com.lot.device.entity.Device;
import com.lot.device.entity.DeviceExt;
import com.lot.device.entity.DeviceExt2;
import com.lot.device.mapper.DeviceMapper;
import com.lot.device.service.DeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author Mr.Qu
 * @title: DeviceServiceImpl
 * @since 2020/11/17 15:55
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public boolean updateDevice(Device param) {
        return deviceMapper.updateDevice(param);
    }

    @Override
    public void saveDevice(Device param) {
        deviceMapper.saveDevice(param);
    }

    @Override
    public Long checkDeviceMd5Exist(String md5, String stationNo) {
        return deviceMapper.checkDeviceMd5Exist(md5,stationNo);
    }

    @Override
    public void saveDeviceExt(DeviceExt param) {
        deviceMapper.saveDeviceExt(param);
    }

    @Override
    public Long checkVarListMd5Exist(String varListMd5) {
        return deviceMapper.checkVarListMd5Exist(varListMd5);
    }

    @Override
    public void saveDeviceExt2(DeviceExt2 param) {
        deviceMapper.saveDeviceExt2(param);
    }
}
