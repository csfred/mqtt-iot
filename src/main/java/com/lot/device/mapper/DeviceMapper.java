package com.lot.device.mapper;

import com.lot.device.entity.Device;
import com.lot.device.entity.DeviceExt;
import com.lot.device.entity.DeviceExt2;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * TODO
 *
 * @author Mr.Qu
 * @title: DeviceMapper
 * @since 2020/11/17 15:56
 */
@Repository
public interface DeviceMapper {

    /**
     * 更新主设备信息
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
     * @param md5
     * @return
     */
    Long checkDeviceMd5Exist(@Param("md5") String md5);

    /**
     * 存储设备扩展信息
     * @param param 参数
     */
    void saveDeviceExt(DeviceExt param);

    /**
     * 检查VarList是否相同
     * @param varListMd5
     * @return
     */
    Long checkVarListMd5Exist(@Param("varListMd5") String varListMd5);

    /**
     * 存储扩展信息2
     * @param param 参数
     */
    void saveDeviceExt2(DeviceExt2 param);


}
