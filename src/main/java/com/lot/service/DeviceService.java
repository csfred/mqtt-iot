package com.lot.service;

import com.lot.entity.Device;
import com.lot.entity.DeviceInfo;

import java.util.List;

/**
 * TODO
 *
 * @author cs
 * @since 2020/11/17 15:35
 */
public interface DeviceService {


    /**
     * 存储设备主信息
     *
     * @param param 参数
     */
    void saveDevice(Device param);


    /**
     * 更新相同站点相同设备参数计数
     *
     * @param endReceiveTime   最后接收时间
     * @param stationNo        站点编号
     * @param varListFieldsMd5 参数变量值MD5
     */
    void updateSameDeviceCounter(String endReceiveTime,
                                 String stationNo,
                                 String varListFieldsMd5);


    /**
     * 检查varListMd5是否存在
     *
     * @param stationNo
     * @param varListFieldsMd5
     * @return total
     */
    Long checkDeviceExist(String stationNo, String varListFieldsMd5);


    /**
     * 获取站点下所有设备种类信息
     *
     * @param stationNo 站点
     * @return List
     */
    List<DeviceInfo> getDeviceInfoAll(String stationNo);

    /**
     * 获取扩展表数据
     *
     * @return list
     */
    List<Device> getDeviceAll();

}
