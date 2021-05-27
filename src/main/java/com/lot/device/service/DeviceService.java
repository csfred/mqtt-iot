package com.lot.device.service;

import com.lot.device.entity.Device;
import com.lot.device.entity.DeviceExt;
import com.lot.device.entity.DeviceExt2;

/**
 * TODO
 *
 * @author Mr.Qu
 * @title: DeviceService
 * @since 2020/11/17 15:35
 */
public interface DeviceService {

    /**
     * 更新主设备
     *
     * @param param 参数
     * @return
     */
    boolean updateDevice(Device param);

    /**
     * 存储设备主信息
     *
     * @param param 参数
     */
    void saveDevice(Device param);

    /**
     * 检测是否存在
     *
     * @param md5 参数
     * @return total
     */
    Long checkDeviceMd5Exist(String md5);

    /**
     * 存储设备扩展信息
     *
     * @param param 参数
     */
    void saveDeviceExt(DeviceExt param);

    /**
     * 检查varListMd5是否存在
     *
     * @param varListMd5
     * @return total
     */
    Long checkVarListMd5Exist(String varListMd5);

    /**
     * 存储扩展信息2
     * @param param 参数
     */
    void saveDeviceExt2(DeviceExt2 param);

}
