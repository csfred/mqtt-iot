package com.lot.mapper;

import com.lot.entity.Device;
import com.lot.entity.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TODO
 *
 * @author cs
 * @since 2020/11/17 15:56
 */
@Mapper
@Repository
public interface DeviceMapper {


    /**
     * 保存设备信息
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

    void updateSameDeviceCounter(@Param("endReceiveTime") String endReceiveTime,
                                 @Param("stationNo") String stationNo,
                                 @Param("varListFieldsMd5") String varListFieldsMd5);


    /**
     * 检查VarList是否相同
     *
     * @param varList4FieldsMd5 参数，值 MD5
     * @param stationNo         站点编号
     * @return
     */
    Long checkDeviceExist(@Param("stationNo") String stationNo,
                          @Param("varList4FieldsMd5") String varList4FieldsMd5);

    /**
     * 获取站点下所有设备种类信息
     *
     * @param stationNo 站点
     * @return List
     */
    List<DeviceInfo> getDeviceInfoAll(@Param("stationNo") String stationNo);

    /**
     * 获取扩展表数据
     *
     * @return list
     */
    List<Device> getDeviceAll();
}
