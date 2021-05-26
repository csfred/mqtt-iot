package com.iot.device.service.serviceImpl;

import com.iot.device.entity.Device;
import com.iot.device.mapper.DeviceMapper;
import com.iot.device.service.DeviceService;
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
  public boolean updateDeviceStatus(Device param) {
    return deviceMapper.updateDeviceStatus(param);
  }
}
